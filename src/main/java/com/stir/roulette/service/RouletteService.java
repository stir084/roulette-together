package com.stir.roulette.service;

import com.stir.roulette.config.ConfigBean;
import com.stir.roulette.domain.*;
import com.stir.roulette.repository.RouletteRepository;
import com.stir.roulette.repository.UserRepository;
import com.stir.roulette.web.dto.RouletteResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class RouletteService {

    private final UserService userService;
    private final UserRepository userRepository;
    private final RouletteRepository rouletteRepository;
    private final ConfigBean configBean;

    @Transactional
    public RouletteResponseDto findLastGame(String userIp) {

        // 회원 정보 없을 시 회원 생성
        if(userRepository.findByUserIp(userIp).isEmpty()){
            Roulette roulette = Roulette.createRoulette(configBean.getGameRandomCode());
            User user = User.createUser(userIp, roulette);
            userRepository.save(user);
        }

        // 게임 조회
        List<Roulette> rouletteList = rouletteRepository.findLastGameByUserIp(userIp);

        return new RouletteResponseDto(rouletteList.get(0));
    }

    @Transactional
    public RouletteResponseDto startRoulette(String rouletteCode) {
        // 현재 룰렛 조회
        Roulette roulette = rouletteRepository.findByRouletteCode(rouletteCode);

        // 게임 시작 - 랜덤 prize 선정
        int prizeNum;
        if(roulette.getStatus() == RouletteStatus.READY) {
            int rouletteSegmentSize = roulette.getRouletteSegments().size();
            prizeNum = (int) (Math.random() * (rouletteSegmentSize - 1)) + 1;
            roulette.setPrize(prizeNum);
            roulette.setStatus(RouletteStatus.FINISH);
        }else{
            throw new IllegalArgumentException("이미 완료된 게임 입니다.");
        }
        return new RouletteResponseDto(roulette);
    }
}

