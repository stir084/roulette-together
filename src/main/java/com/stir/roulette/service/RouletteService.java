package com.stir.roulette.service;

import com.stir.roulette.config.ConfigBean;
import com.stir.roulette.domain.*;
import com.stir.roulette.repository.RouletteRepository;
import com.stir.roulette.repository.UserRepository;
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
    public void findUserGame(String userIp) {

        // 회원 정보 없을 시 회원 생성
        if(userRepository.findByUserIp(userIp).isEmpty()){

            Roulette roulette = Roulette.createRoulette(configBean.getGameRandomCode());
            User newUser = User.createUser(userIp, roulette);
            userRepository.save(newUser);
        }

        // 회원 조회
        User user = userRepository.findByUserIp(userIp).get();


/*        rouletteRepository.findByTopGame(PageRequest.of(0,1))
        user.getRoulettes().get(0);*/
        //user에서 lazy 로딩하거나..

               // .orElseGet(() -> new User().createUser(userIp, new Roulette().createRoulette()));

      /*  if(!user.isPresent()){

        }else{

        }*/


        // 내 룰렛 조회

        // 룰렛 없을시 룰렛 생성

        /*User user = new User().builder()
                .userIp();*/
        /*User user;
        Roulette roulette = new Roulette();
        if (findUsers.isEmpty()) {
            user = createUser(userIp);
        } else {
            user = findUsers.get(0);
        }*/
        //roulette = rouletteRepository.findByTopGame(PageRequest.of(0,1)).get(0);

        //return new GamesResponseDto(game);
       // return roulette;
    }

   /* @Transactional
    public User createUser(String MyIp) {
        String gameRandomCode = configBean.getGameRandomCode();
        User user = new User().builder()
                .userIp(MyIp)
                .build();

        Roulette roulette = new Roulette().builder()
                .gameCode(gameRandomCode)
                .build();
        roulette.setUser(user);

        roulette.addGameInfo(new RouletteSegment().builder().element("짜장면").build());
        roulette.addGameInfo(new RouletteSegment().builder().element("짬뽕").build());
        roulette.addGameInfo(new RouletteSegment().builder().element("볶음밥").build());
        roulette.addGameInfo(new RouletteSegment().builder().element("탕수육").build());

        userRepository.save(user);
        //game.setUser로 연관관계 메소드 세팅을 해주더라도 gameRepository save는 안된다.
        //무조건 1:N중 1만 세이브하도록

        return user;
    }*/
}

