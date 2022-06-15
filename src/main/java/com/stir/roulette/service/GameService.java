package com.stir.roulette.service;

import com.stir.roulette.domain.Game;
import com.stir.roulette.domain.GameRepository;
import com.stir.roulette.domain.User;
import com.stir.roulette.domain.UserRepository;
import com.stir.roulette.web.dto.GamesResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Optional;
import java.util.Random;

@RequiredArgsConstructor
@Service
public class GameService {
    private final UserRepository userRepository;
    private final GameRepository gameRepository;

    @Transactional(readOnly = true)
    public GamesResponseDto findMyGame() {
        InetAddress local;
        String ip = "";
        try {
            local = InetAddress.getLocalHost();
            ip = local.getHostAddress();
        } catch (UnknownHostException e1) {
            e1.printStackTrace();
        }

        Optional<User> user = userRepository.findByUserIp(ip);
        if(!user.isPresent()){
            userRepository.save(new User().builder().userIp(ip).build());
        }

        User user2 = userRepository.findByUserIp(ip)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다. ip="));

        //user만드는건 다른 함수로 만들기..
        //다시 get해오기는 어떻게하는게 좋을까..

        Optional<Game> game = gameRepository.findTopByUserIpOrderByIdDesc(ip);
        if(!game.isPresent()){
            int leftLimit = 97; // letter 'a'
            int rightLimit = 122; // letter 'z'
            int targetStringLength = 10;
            Random random = new Random();
            StringBuilder buffer = new StringBuilder(targetStringLength);
            for (int i = 0; i < targetStringLength; i++) {
                int randomLimitedInt = leftLimit + (int)
                        (random.nextFloat() * (rightLimit - leftLimit + 1));
                buffer.append((char) randomLimitedInt);
            }
            String generatedString = buffer.toString();
            gameRepository.save(new Game().builder().gameCode(generatedString).userIp(ip).build());
        }
        Game game2 = gameRepository.findTopByUserIpOrderByIdDesc(ip)
                .orElseThrow(() -> new IllegalArgumentException("해당 게임 없습니다."));
        System.out.println(game2.getGameCode());




        return new GamesResponseDto(game2);
    }

    @Transactional(readOnly = true)
    public String findByGameCode(Long gameCode) {





        return "dddd";
    }


}
