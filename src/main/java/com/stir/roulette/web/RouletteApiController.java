package com.stir.roulette.web;

import com.stir.roulette.config.ConfigBean;
import com.stir.roulette.service.RouletteService;
import com.stir.roulette.web.dto.RouletteResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RestController
public class RouletteApiController {

    private final RouletteService rouletteService;
    private final ConfigBean configBean;

    @GetMapping("/api/v1/roulette")
    public RouletteResponseDto getRoulette(HttpServletRequest request) {
        String userIp = configBean.getUserIp(request);
        RouletteResponseDto rouletteResponseDto = rouletteService.findLastGame(userIp);
        return rouletteResponseDto;
    }

    @GetMapping("/api/v1/roulette/{rouletteCode}")
    public RouletteResponseDto getSharedRoulette(@PathVariable String rouletteCode, HttpServletRequest request) {
        RouletteResponseDto rouletteResponseDto = rouletteService.findSharedRoulette(rouletteCode);
        return rouletteResponseDto;
    }

    @PostMapping("/api/v1/roulette")
    public RouletteResponseDto startRoulette(String segmentLength, String rouletteCode){
        return rouletteService.startRoulette(rouletteCode);
    }

    @PostMapping("/api/v1/roulette/segment")
    public String saveRouletteSegment(@RequestParam("element") String element, @RequestParam("rouletteCode") String rouletteCode) {
        rouletteService.saveRouletteSegment(element, rouletteCode);
        return rouletteCode;
    }
}
