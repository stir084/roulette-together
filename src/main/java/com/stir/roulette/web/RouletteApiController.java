package com.stir.roulette.web;

import com.stir.roulette.config.ConfigBean;
import com.stir.roulette.service.RouletteService;
import com.stir.roulette.web.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
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
    public RouletteResponseDto startRoulette(String rouletteCode, HttpServletRequest request){
        String userIp = configBean.getUserIp(request);
        return rouletteService.startRoulette(rouletteCode, userIp);
    }




    @PutMapping("/api/v1/roulette")
    public Long updateRoulette(@RequestBody RouletteSettingRequestDto rouletteRequestDto){
        rouletteService.updateRoulette(rouletteRequestDto);
      //  System.out.println(rouletteRequestDto.getRouletteCode());
       // System.out.println(rouletteRequestDto.getRouletteSegment().get(0).getElement());
        return rouletteRequestDto.getId();
    }

    @PostMapping("/api/v1/roulette/new") //RestApi에 안맞는 이름임.
    public RouletteResponseDto createNewRoulette(Model model, HttpServletRequest request){
        String userIp = configBean.getUserIp(request);
        return rouletteService.createNewRoulette(userIp);
    }

    @PostMapping("/api/v1/roulette/segment")
    public String saveRouletteSegment(@RequestParam("element") String element, @RequestParam("rouletteCode") String rouletteCode) {
        rouletteService.saveRouletteSegment(element, rouletteCode);
        return rouletteCode;
    }



    /*PostMapping("/login")
    public ResponseEntity<?> login(HttpSession session){
        UUID uid = Optional.ofNullable(UUID.class.cast(session.getAttribute("uid")))
                .orElse(UUID.randomUUID());
        session.setAttribute("uid", uid);
        return new ResponseEntity<>("로그인 성공", HttpStatus.OK);
    }*/
}
