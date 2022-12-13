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
    public RouletteResponseDto getRoulette(HttpServletRequest request) {
        String userIp = configBean.getUserIp(request);
        RouletteResponseDto rouletteResponseDto = rouletteService.findLastGame(userIp);
        return rouletteResponseDto; //responseEntity로 수정하기
    }

    @GetMapping("/api/v1/roulette/{rouletteUID}")
    public RouletteResponseDto getSpecificRoulette(@PathVariable UUID rouletteUID, HttpServletRequest request) {
        String userIp = configBean.getUserIp(request);
        RouletteResponseDto rouletteResponseDto = rouletteService.getSpecificRoulette(userIp, rouletteUID);
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
    public RouletteResponseDto startRoulette(@RequestParam UUID rouletteUID, HttpServletRequest request){
        String userIp = configBean.getUserIp(request);
        return rouletteService.startRoulette(rouletteUID, userIp);
    }

    @PutMapping("/api/v1/roulette")
    public UUID updateRoulette(@RequestBody RouletteSettingRequestDto rouletteRequestDto){
        System.out.println("머야"+rouletteRequestDto.getRouletteSegmentList().get(0).getSegmentUID());
        rouletteService.updateRoulette(rouletteRequestDto);
        return rouletteRequestDto.getRouletteUID();
    }

    @PostMapping("/api/v1/roulette/new") //RestApi에 안맞는 이름임.
    public RouletteResponseDto createNewRoulette(Model model, HttpServletRequest request){
        String userIp = configBean.getUserIp(request);
        return rouletteService.createNewRoulette(userIp);
    }

    @PostMapping("/api/v1/roulette/segment")
    public UUID saveRouletteSegment(@RequestParam("element") String element, @RequestParam("rouletteUID") UUID rouletteUID) {
        rouletteService.saveRouletteSegment(element, rouletteUID);
        return rouletteUID;
    }
   /* @GetMapping("/api/v1/roulette/favorite")
    public List<RouletteResponseDto> getRouletteFavorite(HttpServletRequest request){
        String userIp = configBean.getUserIp(request);
        //RouletteResponseDto rouletteResponseDto = rouletteService.findLastGame(userIp);
        List<RouletteResponseDto> rouletteResponseDtoList = rouletteService.getRouletteFavorite(userIp);
        return rouletteResponseDtoList;
    }*/
    @PutMapping("/api/v1/roulette/favorite")
    public UUID changeRouletteFavoriteStatus(@RequestParam("rouletteUID") UUID rouletteUID){

        rouletteService.changeRouletteFavoriteStatus(rouletteUID);
        return rouletteUID;
    }




    /*PostMapping("/login")
    public ResponseEntity<?> login(HttpSession session){
        UUID uid = Optional.ofNullable(UUID.class.cast(session.getAttribute("uid")))
                .orElse(UUID.randomUUID());
        session.setAttribute("uid", uid);
        return new ResponseEntity<>("로그인 성공", HttpStatus.OK);
    }*/
}
