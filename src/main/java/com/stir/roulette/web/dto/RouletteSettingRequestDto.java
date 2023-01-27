package com.stir.roulette.web.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
public class RouletteSettingRequestDto {

    private String title;
    private UUID rouletteUID;
    private List<RouletteSegmentSettingRequestDto> rouletteSegmentList;
    private List<RouletteSegmentSettingRequestDto> newRouletteSegmentList;

    private int maxCount;

}