package com.stir.roulette.service;

import com.stir.roulette.domain.Game;
import com.stir.roulette.domain.GameRepository;
import com.stir.roulette.domain.User;
import com.stir.roulette.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class GameService {
    private final UserRepository userRepository;
    private final GameRepository gameRepository;

    @Transactional(readOnly = true)
    public String findMyGame() {



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

        Game game = gameRepository.findByIdentifiedGameCode("dd")
                .orElseThrow(() -> new IllegalArgumentException("해당 게임이 없습니다."));
        user2.getUserIp()
       // user2 = user2.builder().userIp(ip).build();
       // System.out.println(userRepository.findByUserIp(user.getUserIp())+"ttttttttttttttt");



        return "dddd";
    }


}
