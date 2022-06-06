package com.stir.roulette.web;

import com.stir.roulette.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
public class IndexController {

    private final UserService userService;
    @GetMapping("/")
    public String index(ModelMap model) {

        model.addAttribute("posts", userService.findByidentifiedGame());
        model.addAttribute("data", "Hello Spring!");
        model.addAttribute("msg", 11);
        return "index";
    }

}
