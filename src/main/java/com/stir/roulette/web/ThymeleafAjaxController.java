package com.stir.roulette.web;

import com.stir.roulette.config.ConfigBean;
import com.stir.roulette.service.RouletteService;
import com.stir.roulette.web.dto.PageDTO;
import com.stir.roulette.web.dto.RouletteHistoryResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class ThymeleafAjaxController {

    private final RouletteService rouletteService;
    private final ConfigBean configBean;


    @GetMapping("/api/v1/roulette/history")
    public String getRouletteHistory(@CookieValue String userUUID, HttpServletRequest request, Pageable pageable, Model model) {
        PageDTO<RouletteHistoryResponseDto> rouletteHistory = rouletteService.findRouletteHistory(userUUID, pageable);
        model.addAttribute("roulette", rouletteHistory.getContent());
        return "/roulette-history :: #rouletteHistoryTable";
    }
}
