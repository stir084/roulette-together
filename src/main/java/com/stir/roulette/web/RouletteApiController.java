package com.stir.roulette.web;

import com.stir.roulette.config.ConfigBean;
import com.stir.roulette.service.RouletteSegmentService;
import com.stir.roulette.service.RouletteService;
import com.stir.roulette.web.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
public class RouletteApiController {

    private final RouletteService rouletteService;
    private final RouletteSegmentService rouletteSegmentService;
    private final ConfigBean configBean;

    @GetMapping("/api/v1/roulette/last")
    public RouletteResponseDto getRoulette(@CookieValue String userUUID, HttpServletRequest request) {
        RouletteResponseDto rouletteResponseDto = rouletteService.findLastGame(userUUID);
        return rouletteResponseDto; //responseEntity로 수정하기
    }

    @GetMapping("/api/v1/roulette/{rouletteUID}")
    public RouletteResponseDto getSpecificRoulette(@CookieValue String userUUID, @PathVariable UUID rouletteUID, HttpServletRequest request) {
        RouletteResponseDto rouletteResponseDto = rouletteService.getSpecificRoulette(userUUID, rouletteUID);
        return rouletteResponseDto;
    }

    @GetMapping("/api/v1/roulette/share/{rouletteUID}")
    public RouletteResponseDto getSharedRoulette(@PathVariable UUID rouletteUID, HttpServletRequest request) {
        RouletteResponseDto rouletteResponseDto = rouletteService.getSharedRoulette(rouletteUID);
        return rouletteResponseDto;
    }

    @DeleteMapping("/api/v1/roulette/segment/{segmentUID}")
    public UUID deleteSegment(@PathVariable UUID segmentUID, HttpServletRequest request) {
        rouletteSegmentService.deleteSegment(segmentUID);
        return segmentUID;
    }

    @PostMapping("/api/v1/roulette")
    public RouletteResponseDto startRoulette(@CookieValue String userUUID, @RequestParam UUID rouletteUID, HttpServletRequest request){
        return rouletteService.startRoulette(rouletteUID, userUUID);
    }

    @PutMapping("/api/v1/roulette") //멱등성 유지 안됨
    public UUID updateRoulette(@RequestBody RouletteSettingRequestDto rouletteRequestDto){
        rouletteService.updateRoulette(rouletteRequestDto);

        return rouletteRequestDto.getRouletteUID();
    }

    @PostMapping("/api/v1/roulette/new")
    public RouletteResponseDto createNewRoulette(@CookieValue String userUUID, Model model, HttpServletRequest request){
        return rouletteService.createNewRoulette(userUUID);
    }

    @PostMapping("/api/v1/roulette/segment")
    public UUID saveRouletteSegment(@RequestParam("element") String element, @RequestParam("rouletteUID") UUID rouletteUID) {
        rouletteService.saveRouletteSegment(element, rouletteUID);
        return rouletteUID;
    }
    @PutMapping("/api/v1/roulette/favorite")
    public UUID changeRouletteFavoriteStatus(@CookieValue String userUUID, @RequestParam("rouletteUID") UUID rouletteUID, HttpServletRequest request){
        //String userIp = configBean.getUserIp(request);
        rouletteService.changeRouletteFavoriteStatus(rouletteUID, userUUID);
        return rouletteUID;
    }
}
