package com.stir.roulette.service;

import com.stir.roulette.config.ConfigBean;
import com.stir.roulette.domain.*;
import com.stir.roulette.exception.RouletteException;
import com.stir.roulette.repository.RouletteRepository;
import com.stir.roulette.repository.RouletteSegmentRepository;
import com.stir.roulette.repository.UserRepository;
import com.stir.roulette.web.dto.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class RouletteService {

    private final UserService userService;
    private final UserRepository userRepository;
    private final RouletteRepository rouletteRepository;
    private final RouletteSegmentRepository rouletteSegmentRepository;
    private final ConfigBean configBean;

    @Transactional
    public RouletteResponseDto findLastGame(String userUUID) {

        // 회원 정보 없을 시 초기 생성
        if(userRepository.findByUserUUID(userUUID).isEmpty()){
            List<RouletteSegment> rouletteSegmentList = new ArrayList<>();

            rouletteSegmentList.add(RouletteSegment.createRouletteSegment("치킨🥠"));
            rouletteSegmentList.add(RouletteSegment.createRouletteSegment("삼겹살🥩"));
            rouletteSegmentList.add(RouletteSegment.createRouletteSegment("피자🍕"));
            rouletteSegmentList.add(RouletteSegment.createRouletteSegment("초밥🍣"));


            Roulette roulette = Roulette.createInitRoulette("점심 뭐 먹지?",
                    rouletteSegmentList.stream().toArray(RouletteSegment[]::new));
            User user = User.createUser(userUUID, roulette);
            userRepository.save(user);
        }

        // 게임 조회
        List<Roulette> rouletteList = rouletteRepository.findLastGameByUserUUID(userUUID);

        return new RouletteResponseDto(rouletteList.get(0));
    }


    public RouletteResponseDto getSpecificRoulette(String userIp, UUID rouletteUID) {
        Roulette roulette = rouletteRepository.findByRouletteUID(rouletteUID)
                .orElseThrow(() -> new RouletteException("조회된 내역이 없습니다"));
        if(!roulette.getUser().getUserUUID().equals(userIp)){
            throw new RouletteException("올바른 접근이 아닙니다.");
        }
        return new RouletteResponseDto(roulette);
    }


    @Transactional
    public RouletteResponseDto startRoulette(UUID rouletteUID, String userUUID) {

        User user = userRepository.findByUserUUID(userUUID).get();

        // 현재 룰렛 조회
        Roulette roulette = rouletteRepository.findByRouletteUID(rouletteUID)
                .orElseThrow(() -> new RouletteException("조회된 내역이 없습니다"));

        if(roulette.getStatus() == RouletteStatus.FINISH) {
            throw new RouletteException("이미 완료된 게임 입니다.");
        }
        if(user != roulette.getUser()){
            throw new RouletteException("해당 룰렛의 권한이 없습니다.");
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
    public RouletteResponseDto createNewRoulette(String userUUID) {

        //가장 최근 게임 조회
        List<Roulette> rouletteList = rouletteRepository.findLastGameByUserUUID(userUUID);
        User user = userRepository.findByUserUUID(userUUID).get();
        Roulette lastRoulette = rouletteList.get(0);
        Roulette newRoulette = new Roulette();

        //최근 게임 기반으로 룰렛 재생성
        newRoulette.setTitle(lastRoulette.getTitle());
        newRoulette.setStatus(RouletteStatus.READY);
        newRoulette.setFavoriteStatus(FavoriteStatus.UNFAVORED);
        newRoulette.setCreateDate(LocalDateTime.now());
        newRoulette.setMaxCount(lastRoulette.getMaxCount());
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
                .orElseThrow(() -> new RouletteException("조회된 내역이 없습니다"));

        if(roulette.getMaxCount() == roulette.getRouletteSegments().size()){
            throw new RouletteException("최대 아이템 개수는 " + roulette.getMaxCount() + "개 입니다.");
        }
        if(roulette.getStatus() == RouletteStatus.FINISH) {
            throw new RouletteException("이미 완료된 게임 입니다.");
        }

        // 세그먼트 생성
        RouletteSegment rouletteSegment = RouletteSegment.createRouletteSegment(element);
        roulette.addRouletteSegment(rouletteSegment);

        // 세그먼트 저장
        rouletteSegmentRepository.save(rouletteSegment);
    }


    @Transactional
    public RouletteResponseDto getSharedRoulette(UUID rouletteUID) {
        // 룰렛 조회
        Roulette roulette = rouletteRepository.findByRouletteUID(rouletteUID)
                .orElseThrow(() -> new RouletteException("조회된 내역이 없습니다"));

        return new RouletteResponseDto(roulette);
    }

    @Transactional
    public void updateRoulette(RouletteSettingRequestDto rouletteRequestDto) {
        Roulette roulette = rouletteRepository.findByRouletteUID(rouletteRequestDto.getRouletteUID())
                .orElseThrow(() -> new RouletteException("조회된 내역이 없습니다"));

        int totalSegmentSize = roulette.getRouletteSegments().size() + rouletteRequestDto.getNewRouletteSegmentList().size();
        if(rouletteRequestDto.getMaxCount() < totalSegmentSize){
            throw new RouletteException("등록 아이템의 개수는 최대 아이템 개수보다 많아야 합니다.");
        }
        if(rouletteRequestDto.getMaxCount()>100){
            throw new RouletteException("최대 아이템 개수는 100개 입니다.");
        }
        if(roulette.getStatus()==RouletteStatus.FINISH){
            throw new RouletteException("이미 종료된 게임입니다.");
        }
        roulette.setTitle(rouletteRequestDto.getTitle());
        roulette.setMaxCount(rouletteRequestDto.getMaxCount());

        // new Roulette은 새로 저장
        for(int i=0; i<rouletteRequestDto.getNewRouletteSegmentList().size(); i++) {
            RouletteSegment rouletteSegment = new RouletteSegment();
            rouletteSegment.addRoulette(roulette);
            rouletteSegment.setCreateDate(LocalDateTime.now());
            rouletteSegment.setElement(rouletteRequestDto.getNewRouletteSegmentList().get(i).getElement());
            rouletteSegmentRepository.save(rouletteSegment);
        }




        //기존에 불러온 값이랑 가져온 값이랑 체크 / 조작된 값이 있는지 체크 / 정리해야함 필수
        // DB값 미리 넣기
        Map<SegmentUpdateChkVO, List<SegmentUpdateChkVO>> classifiedRequestRoulette = new LinkedHashMap<>();
        List<RouletteSegment> rouletteSegmentList = roulette.getRouletteSegments(); // 지연로딩 List 하나라 1개만 가져옴 띠용 그럼 N+1인 언제 생기는거였지?
        for (RouletteSegment rouletteSegment : rouletteSegmentList) {
            RouletteSegmentSettingRequestDto rouletteSegmentSettingRequestDto = new RouletteSegmentSettingRequestDto(rouletteSegment);

            SegmentUpdateChkVO segmentUpdateChkVO = new SegmentUpdateChkVO(rouletteSegmentSettingRequestDto);
            classifiedRequestRoulette.put(segmentUpdateChkVO, Collections.singletonList(segmentUpdateChkVO));
        }

        //수정된거 있는지 출력하기
        /*List<RouletteSegmentSettingRequestDto> rouletteSegmentList1 = rouletteRequestDto.getRouletteSegmentList();
        for (RouletteSegmentSettingRequestDto rouletteSegmentSettingRequestDto : rouletteSegmentList1) {
            SegmentUpdateChkVO.classify(rouletteSegmentSettingRequestDto, classifiedPayment);
        }*/

        //수정된게 있는지 체크
        // 가져온 Segment 데이터 중 수정된 내역이 있으면 Save
        HashMap<UUID, String> hhh = SegmentUpdateChkVO.classify(rouletteRequestDto.getRouletteSegmentList(), classifiedRequestRoulette);

        for (UUID aLong : hhh.keySet()) {
            RouletteSegment rouletteSegment = rouletteSegmentRepository.findBySegmentUID(aLong)
                    .orElseThrow(() -> new RouletteException("조회된 내역이 없습니다"));
            rouletteSegment.setElement(hhh.get(aLong));
        }

    }

    @Transactional
    public PageDTO<RouletteHistoryResponseDto> findRouletteHistory(String userUUID, Pageable pageable) {
        User user = userRepository.findByUserUUID(userUUID).get();
        //PageRequest pageRequest = PageRequest.of(0, 5);

      /*  return rouletteRepository.findByUserAndStatus(user, RouletteStatus.FINISH, pageable)
                .map(RouletteHistoryResponseDto::new);*/
       // Page<Roulette> byUserAndStatus = rouletteRepository.findByUserAndStatus(user, RouletteStatus.FINISH, pageable);
        Page<Roulette> byUserAndStatus = rouletteRepository.findByUserAndStatus(user, RouletteStatus.FINISH, pageable);
        Page<RouletteHistoryResponseDto> map = byUserAndStatus.map(RouletteHistoryResponseDto::new);
        PageDTO<RouletteHistoryResponseDto> pageDto = new PageDTO<RouletteHistoryResponseDto>(map);

        return new PageDTO<RouletteHistoryResponseDto>(map);
    }

    @Transactional
    public void changeRouletteFavoriteStatus(UUID rouletteUID, String userUUID) {
        Roulette roulette = rouletteRepository.findByRouletteUID(rouletteUID)
                .orElseThrow(() -> new RouletteException("조회된 내역이 없습니다"));

        User user = userRepository.findByUserUUID(userUUID)
                .orElseThrow(() -> new RouletteException("조회된 내역이 없습니다"));
        if(roulette.getFavoriteStatus() == FavoriteStatus.UNFAVORED){

            if(user.getFavoriteCount() >= 5){
                throw new RouletteException("즐겨찾기 개수는 최대 5개 입니다.");
            }
            roulette.setFavoriteStatus(FavoriteStatus.FAVORED);
            user.setFavoriteCount(user.getFavoriteCount()+1);
        }else{
            roulette.setFavoriteStatus(FavoriteStatus.UNFAVORED);
            user.setFavoriteCount(user.getFavoriteCount()-1);
        }
    }

    @Transactional
    public List<RouletteFavoriteResponseDto> getRouletteFavorite(String userUUID) {
        Optional<User> user = userRepository.findByUserUUID(userUUID);
        if(user.isEmpty()){
            return new ArrayList<>(); //조회 내역 없음
        }
        List<Roulette> rouletteList = rouletteRepository.findByUserAndFavoriteStatus(user.get(), FavoriteStatus.FAVORED);

        List<RouletteFavoriteResponseDto> rouletteResponseDtoList = new ArrayList<>();
        for (Roulette roulette : rouletteList) {
            rouletteResponseDtoList.add(new RouletteFavoriteResponseDto(roulette));
        }


        return rouletteResponseDtoList;
    }



    @Data
    @EqualsAndHashCode
    static class SegmentUpdateChkVO{
        private UUID segmentUID;
        private String element;

        public SegmentUpdateChkVO(RouletteSegmentSettingRequestDto rouletteSegment) {
            this.segmentUID = rouletteSegment.getSegmentUID();
            this.element = rouletteSegment.getElement();
        }
        public static HashMap<UUID, String> classify(List<RouletteSegmentSettingRequestDto> rouletteSegmentList
                , Map<SegmentUpdateChkVO, List<SegmentUpdateChkVO>> classifiedRequest){
            Map<SegmentUpdateChkVO, List<SegmentUpdateChkVO>> classifiedRequestRoulette = classifiedRequest;//new LinkedHashMap<>();
            HashMap<UUID, String> classifiedSegmentMap = new HashMap<>();

            for(RouletteSegmentSettingRequestDto rouletteSegment : rouletteSegmentList){
                SegmentUpdateChkVO dto = new SegmentUpdateChkVO(rouletteSegment);
                List<SegmentUpdateChkVO> list = classifiedRequestRoulette.get(dto);

                if(list == null){ // 비교 값이 없으면
                    classifiedSegmentMap.put(dto.getSegmentUID(), dto.getElement());
                    //classifiedPayment.put(dto, new ArrayList<>(Collections.singletonList(dto))); // 기존에 없으면 save하거나 throw
                }
            }
            /*return classifiedPayment.entrySet().stream()
                    .map(Map.Entry::getValue);*/
            return classifiedSegmentMap;
        }
    }

}

