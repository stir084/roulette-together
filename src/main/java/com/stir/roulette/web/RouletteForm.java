package com.stir.roulette.web;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter @Setter
public class RouletteForm {
    @NotEmpty(message = "아이템 이름은 필수입니다")
    private String rouletteItem;
}
