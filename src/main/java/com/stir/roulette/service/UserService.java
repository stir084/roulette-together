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
public class UserService {
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public String findByidentifiedGame() {
        User user = new User();

        InetAddress local;
        String ip = "";
        try {
            local = InetAddress.getLocalHost();
            ip = local.getHostAddress();
        } catch (UnknownHostException e1) {
            e1.printStackTrace();
        }


        System.out.println(userRepository.save(user.builder().ip(ip).build()).getId());

        return "dddd";
    }
}
