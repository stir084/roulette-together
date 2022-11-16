package com.stir.roulette.service;

import com.stir.roulette.config.ConfigBean;
import com.stir.roulette.domain.*;
import com.stir.roulette.repository.RouletteRepository;
import com.stir.roulette.repository.RouletteSegmentRepository;
import com.stir.roulette.repository.UserRepository;
import com.stir.roulette.web.dto.RouletteResponseDto;
import com.stir.roulette.web.dto.RouletteSegmentSettingRequestDto;
import com.stir.roulette.web.dto.RouletteSettingRequestDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Service
public class RouletteService {

    private final UserService userService;
    private final UserRepository userRepository;
    private final RouletteRepository rouletteRepository;
    private final RouletteSegmentRepository rouletteSegmentRepository;
    private final ConfigBean configBean;

    @Transactional
    public RouletteResponseDto findLastGame(String userIp) {

        // 회원 정보 없을 시 초기 생성
        if(userRepository.findByUserIp(userIp).isEmpty()){
           //RouletteSegment rouletteSegment = RouletteSegment.createRouletteSegment("짜장면");
            List<RouletteSegment> rouletteSegmentList = new ArrayList<>();

            rouletteSegmentList.add(RouletteSegment.createRouletteSegment("짜장면"));
            rouletteSegmentList.add(RouletteSegment.createRouletteSegment("짬뽕"));
            rouletteSegmentList.add(RouletteSegment.createRouletteSegment("탕수육"));
            rouletteSegmentList.add(RouletteSegment.createRouletteSegment("취두부"));

            Roulette roulette = Roulette.createInitRoulette("점심 뭐 먹지?",
                    rouletteSegmentList.stream().toArray(RouletteSegment[]::new));
            User user = User.createUser(userIp, roulette);
            userRepository.save(user);
        }

        // 게임 조회
        List<Roulette> rouletteList = rouletteRepository.findLastGameByUserIp(userIp);

        return new RouletteResponseDto(rouletteList.get(0));
    }

    @Transactional
    public RouletteResponseDto startRoulette(UUID rouletteUID, String userIp) {

        User user = userRepository.findByUserIp(userIp).get();

        // 현재 룰렛 조회
        Roulette roulette = rouletteRepository.findByRouletteUID(rouletteUID)
                .orElseThrow(() -> new IllegalArgumentException("조회된 내역이 없습니다"));

        if(roulette.getStatus() == RouletteStatus.FINISH) {
            throw new IllegalArgumentException("이미 완료된 게임 입니다.");
        }
        if(user != roulette.getUser()){
            throw new IllegalArgumentException("해당 룰렛의 권한이 없습니다.");
        }

        // 게임 시작 - 랜덤 prize 선정
        int prizeNum;
        int rouletteSegmentSize = roulette.getRouletteSegments().size();
        prizeNum = (int) (Math.random() * (rouletteSegmentSize - 1)) + 1;
        roulette.setPrize(prizeNum);
        roulette.setStatus(RouletteStatus.FINISH);

        return new RouletteResponseDto(roulette);
    }

    @Transactional
    public RouletteResponseDto createNewRoulette(String userIp) {

        //가장 최근 게임 조회
        List<Roulette> rouletteList = rouletteRepository.findLastGameByUserIp(userIp);
        User user = userRepository.findByUserIp(userIp).get();
        Roulette lastRoulette = rouletteList.get(0);
        Roulette newRoulette = new Roulette();

        //최근 게임 기반으로 룰렛 재생성
        //newRoulette.setrouletteUID(configBean.getGameRandomCode());

        newRoulette.setTitle(lastRoulette.getTitle());
        newRoulette.setStatus(RouletteStatus.READY);
        newRoulette.setCreateDate(LocalDateTime.now());
        newRoulette.addUser(user);

       // Roulette.createInitRoulette()

        List<RouletteSegment> rouletteSegmentList = new ArrayList<>();

        for (RouletteSegment segment : lastRoulette.getRouletteSegments()) {
            RouletteSegment rouletteSegment = new RouletteSegment();
            rouletteSegment.setElement(segment.getElement());
            rouletteSegment.setCreateDate(LocalDateTime.now());
            rouletteSegment.addRoulette(newRoulette);
            rouletteSegmentList.add(rouletteSegment);
        }

        newRoulette.setRouletteSegments(rouletteSegmentList);

        //새 룰렛 저장
        rouletteRepository.save(newRoulette);

        return new RouletteResponseDto(newRoulette);
    }

    @Transactional
    public void saveRouletteSegment(String element, UUID rouletteUID) {
        // 현재 룰렛 조회
        Roulette roulette = rouletteRepository.findByRouletteUID(rouletteUID)
                .orElseThrow(() -> new IllegalArgumentException("조회된 내역이 없습니다"));

        if(roulette.getStatus() == RouletteStatus.FINISH) {
            throw new IllegalArgumentException("이미 완료된 게임 입니다.");
        }

        // 세그먼트 생성
        RouletteSegment rouletteSegment = RouletteSegment.createRouletteSegment(element);
        roulette.addRouletteSegment(rouletteSegment);

        // 세그먼트 저장
        rouletteSegmentRepository.save(rouletteSegment);
    }

    @Transactional
    public RouletteResponseDto findSharedRoulette(UUID rouletteUID) {
        // 룰렛 조회
        Roulette roulette = rouletteRepository.findByRouletteUID(rouletteUID)
                .orElseThrow(() -> new IllegalArgumentException("조회된 내역이 없습니다"));

        return new RouletteResponseDto(roulette);
    }

    @Transactional
    public void updateRoulette(RouletteSettingRequestDto rouletteRequestDto) {
        // Id와 rouletteUID로 같이 조회하기
        System.out.println();
        Roulette roulette = Optional.of(rouletteRepository.findByIdAndRouletteUID(rouletteRequestDto.getId(), rouletteRequestDto.getRouletteUID()))
                .orElseThrow(() -> new IllegalArgumentException("조회된 내역이 없습니다"));


        roulette.setTitle(rouletteRequestDto.getTitle());

        //기존에 불러온 값이랑 가져온 값이랑 체크 / 조작된 값이 있는지 체크


        // DB값 미리 넣기
        Map<SegmentUpdateChkVO, List<SegmentUpdateChkVO>> classifiedPayment = new LinkedHashMap<>();
        List<RouletteSegment> rouletteSegmentList = roulette.getRouletteSegments(); // 지연로딩 List 하나라 1개만 가져옴 띠용 그럼 N+1인 언제 생기는거였지?
        for (RouletteSegment rouletteSegment : rouletteSegmentList) {
            RouletteSegmentSettingRequestDto rouletteSegmentSettingRequestDto = new RouletteSegmentSettingRequestDto(rouletteSegment);

            SegmentUpdateChkVO segmentUpdateChkVO = new SegmentUpdateChkVO(rouletteSegmentSettingRequestDto);
            classifiedPayment.put(segmentUpdateChkVO, Collections.singletonList(segmentUpdateChkVO));
        }

        //수정된거 있는지 출력하기
        /*List<RouletteSegmentSettingRequestDto> rouletteSegmentList1 = rouletteRequestDto.getRouletteSegmentList();
        for (RouletteSegmentSettingRequestDto rouletteSegmentSettingRequestDto : rouletteSegmentList1) {
            SegmentUpdateChkVO.classify(rouletteSegmentSettingRequestDto, classifiedPayment);
        }*/

        //수정된게 있는지 체크
        // 가져온 Segment 데이터 중 수정된 내역이 있으면 Save
        HashMap<Long, String> hhh = SegmentUpdateChkVO.classify(rouletteRequestDto.getRouletteSegmentList(), classifiedPayment);
        for (Long aLong : hhh.keySet()) {
            RouletteSegment byId = rouletteSegmentRepository.findById(aLong)
                    .orElseThrow(() -> new IllegalArgumentException("조회된 내역이 없습니다"));;
            byId.setElement(hhh.get(aLong));
        }

    }

    @Data
    @EqualsAndHashCode
    static class SegmentUpdateChkVO{
        private Long id;
        private String element;

        public SegmentUpdateChkVO(RouletteSegmentSettingRequestDto rouletteSegment) {
            this.id = rouletteSegment.getId();
            this.element = rouletteSegment.getElement();
        }
        public static HashMap<Long, String> classify(List<RouletteSegmentSettingRequestDto> rouletteSegmentList
                , Map<SegmentUpdateChkVO, List<SegmentUpdateChkVO>> testPayment){
            Map<SegmentUpdateChkVO, List<SegmentUpdateChkVO>> classifiedPayment = testPayment;//new LinkedHashMap<>();


            HashMap<Long, String> test = new HashMap<>();

            for(RouletteSegmentSettingRequestDto rouletteSegment : rouletteSegmentList){
                SegmentUpdateChkVO dto = new SegmentUpdateChkVO(rouletteSegment);
                List<SegmentUpdateChkVO> list = classifiedPayment.get(dto);

                if(list == null){ // 비교 값이 없으면
                    test.put(dto.getId(), dto.getElement());
               /* if(dto.getId()==null){ // 새로 추가된 Segment 용도

                }*/

                    //classifiedPayment.put(dto, new ArrayList<>(Collections.singletonList(dto))); // 기존에 없으면 save하거나 throw
                }
            }

           // System.out.println("하하"+test);
            /*return classifiedPayment.entrySet().stream()
                    .map(Map.Entry::getValue);*/
            return test;
        }
    }

}

