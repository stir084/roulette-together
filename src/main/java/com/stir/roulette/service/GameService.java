package com.stir.roulette.service;

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

    @Transactional(readOnly = true)
    public String findMyGame() {


        User user2 = new User();

        InetAddress local;
        String ip = "";
        try {
            local = InetAddress.getLocalHost();
            ip = local.getHostAddress();
        } catch (UnknownHostException e1) {
            e1.printStackTrace();
        }
        user2 = user2.builder().userIp(ip).build();
       // System.out.println(userRepository.findByUserIp(user.getUserIp())+"ttttttttttttttt");


       /* Optional<User> user = userRepository.findByUserIp(ip);
        user.ifPresent(selectUser->{
            userRepository.delete(selectUser);
        });

        if(!user.isPresent()){
            userRepository.save(user2.builder().userIp(ip).build());
        }
        String name2 = Optional.of("baeldung").orElseGet(() -> "test");

        user.orElseGet(() -> userRepository.save(user2.builder().userIp(ip).build()));

        user = user.orElseGet(() -> user2);


        User result1;
        result1 = Optional.ofNullable(user).orElseGet("dd");


        User entity = userRepository.findById(ip)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다. ip=" );
        User user2 = userRepository.findById(ip)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다. ip=" );


        User user2 = userRepository.getByUserIp(user.getUserIp()).orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다."));;
        System.out.println("하하"+user2);*/
        return "dddd";
    }
}
