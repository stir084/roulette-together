package com.stir.roulette.service;

import com.stir.roulette.config.ConfigBean;
import com.stir.roulette.domain.*;
import com.stir.roulette.web.dto.GamesResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class GameService {
    private final UserRepository userRepository;
    private final GameRepository gameRepository;
    private final ConfigBean configBean;

    @Transactional(readOnly = true)
    public Game findMyGame(String MyIp) {

        // 회원 조회
        List<User> findUsers = userRepository.findByUserIp(MyIp);

        System.out.println(findUsers.stream().collect(Collectors.toList()));
                //.map(PostsListResponseDto::new)

        // 회원 없을 시 저장
        /*User user = new User().builder()
                .userIp();*/
        User user;
        Game game = new Game();
        if (findUsers.isEmpty()) {
            user = createUser(MyIp);
        } else {
            user = findUsers.get(0);
        }
        game = gameRepository.findByTopGame(PageRequest.of(0,1)).get(0);

        //return new GamesResponseDto(game);
        return game;
    }

    @Transactional(readOnly = true)
    public User createUser(String MyIp) {
        String gameRandomCode = configBean.getGameRandomCode();
        User user = new User().builder()
                .userIp(MyIp)
                .build();

        Game game = new Game().builder()
                .gameCode(gameRandomCode)
                .build();
        game.setUser(user);

        game.addGameInfo(new GameInfo().builder().element("짜장면").build());
        game.addGameInfo(new GameInfo().builder().element("짬뽕").build());
        game.addGameInfo(new GameInfo().builder().element("볶음밥").build());
        game.addGameInfo(new GameInfo().builder().element("탕수육").build());

        userRepository.save(user);
        //game.setUser로 연관관계 메소드 세팅을 해주더라도 gameRepository save는 안된다.
        //무조건 1:N중 1만 세이브하도록

        return user;
    }
}

