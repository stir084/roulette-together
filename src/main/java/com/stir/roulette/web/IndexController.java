package com.stir.roulette.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {
    /*@GetMapping("/hello")
    public String hello() {
        return "hello";
    }*/

    @GetMapping("/")
    public String index(ModelMap model) {
        model.addAttribute("data", "Hello Spring!");
        model.addAttribute("msg", 11);
        return "index";
    }

}
