package com.stir.roulette.config;

import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Random;

@Component
public class ConfigBean {
    public String getUserIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");

        if (ip == null) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null) {
            ip = request.getRemoteAddr();
        }
        if(ip.equals("0:0:0:0:0:0:0:1")){ // 혹은 help - vm option에 -Djava.net.preferIPv4Stack=true 설정
            ip = "127.0.0.1";
        }
        if (ip == null){
            throw new IllegalArgumentException("올바르지 않은 조회입니다.");
        }
        return ip;
    }
}
