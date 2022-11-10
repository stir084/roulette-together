package com.stir.roulette.service;

import com.stir.roulette.config.ConfigBean;
import com.stir.roulette.domain.*;
import com.stir.roulette.repository.RouletteRepository;
import com.stir.roulette.repository.RouletteSegmentRepository;
import com.stir.roulette.repository.UserRepository;
import com.stir.roulette.web.dto.RouletteResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
    public RouletteResponseDto findLastGame(String userIp) {

        // 회원 정보 없을 시 회원 생성
        if(userRepository.findByUserIp(userIp).isEmpty()){
            Roulette roulette = Roulette.createInitRoulette(configBean.getGameRandomCode());
            User user = User.createUser(userIp, roulette);
            userRepository.save(user);
        }

        // 게임 조회
        List<Roulette> rouletteList = rouletteRepository.findLastGameByUserIp(userIp);

        return new RouletteResponseDto(rouletteList.get(0));
    }

    @Transactional
    public RouletteResponseDto startRoulette(String rouletteCode, String userIp) {

        User user = userRepository.findByUserIp(userIp).get();

        // 현재 룰렛 조회
        Roulette roulette = rouletteRepository.findByRouletteCode(rouletteCode)
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
        newRoulette.setRouletteCode(configBean.getGameRandomCode());
        newRoulette.setTitle(lastRoulette.getTitle());
        newRoulette.setStatus(RouletteStatus.READY);
        newRoulette.addUser(user);


        List<RouletteSegment> rouletteSegmentList = new ArrayList<>();

        for (RouletteSegment segment : lastRoulette.getRouletteSegments()) {
            RouletteSegment rouletteSegment = new RouletteSegment();
            rouletteSegment.setElement(segment.getElement());
            rouletteSegment.addRoulette(newRoulette);
            rouletteSegmentList.add(rouletteSegment);
        }

        newRoulette.setRouletteSegments(rouletteSegmentList);

        //새 룰렛 저장
        rouletteRepository.save(newRoulette);

        return new RouletteResponseDto(newRoulette);
    }

    @Transactional
    public void saveRouletteSegment(String element, String rouletteCode) {
        // 현재 룰렛 조회
        Roulette roulette = rouletteRepository.findByRouletteCode(rouletteCode)
                .orElseThrow(() -> new IllegalArgumentException("조회된 내역이 없습니다"));

        // 세그먼트 생성
        RouletteSegment rouletteSegment = RouletteSegment.createRouletteSegment(roulette, element);

        // 세그먼트 저장
        rouletteSegmentRepository.save(rouletteSegment);
    }

    @Transactional
    public RouletteResponseDto findSharedRoulette(String rouletteCode) {
        // 룰렛 조회
        Roulette roulette = rouletteRepository.findByRouletteCode(rouletteCode)
                .orElseThrow(() -> new IllegalArgumentException("조회된 내역이 없습니다"));

        return new RouletteResponseDto(roulette);
    }
}

