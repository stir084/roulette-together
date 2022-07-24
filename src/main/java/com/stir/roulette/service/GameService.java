package com.stir.roulette.service;

import com.stir.roulette.config.ConfigBean;
import com.stir.roulette.domain.*;
import com.stir.roulette.web.dto.GamesResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@RequiredArgsConstructor
@Service
public class GameService {
    private final UserRepository userRepository;
    private final GameRepository gameRepository;
    private final ConfigBean configBean;

    @Transactional(readOnly = true)
    public GamesResponseDto findMyGame(String MyIp) {

        // 회원 조회
        List<User> findUsers = userRepository.findByUserIp(MyIp);

        // 회원 없을 시 저장
        /*User user = new User().builder()
                .userIp();*/
        User user;
        Game game = new Game();
        if (findUsers.isEmpty()) {
            createUser(MyIp);

        } else {
            user = findUsers.get(0);
        }


        return new GamesResponseDto(game);
    }

    @Transactional(readOnly = true)
    public void createUser(String MyIp) {
        String gameRandomCode = configBean.getGameRandomCode();
        User user = new User().builder()
                .userIp(MyIp)
                .build();

        Game game = new Game().builder()
                .gameCode(gameRandomCode)
                .userIp(MyIp)
                .build();
        game.setUser(user);
        System.out.println("하하");
        userRepository.save(user);
       // List<Game> gameList = gameRepository.findByUser(user);

       /* Game game;
        if (gameList.isEmpty()) {
            List<GameInfo> gameInfoList = new ArrayList<GameInfo>();
            for(int i=0; i<8;i++){
                GameInfo gameInfos = new GameInfo().builder()
                        .gameCode(configBean.getGameRandomCode())
                        .element("짜라라")
                        .build();
                gameInfoList.add(gameInfos);
            }

            game = new Game().builder()
                    .gameCode(configBean.getGameRandomCode())
                    .userIp(MyIp)
                    .gameInfos(gameInfoList)
                    .build();
            System.out.println(game.getId()+"하하");
            gameRepository.save(game);
        } else {
            game = gameList.get(0);
        }*/
        //userRepository.save(user);
    }
}

