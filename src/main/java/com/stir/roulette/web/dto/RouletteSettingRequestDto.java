package com.stir.roulette.web.dto;

import com.stir.roulette.domain.Roulette;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class RouletteSettingRequestDto {

    private String title;
    private UUID rouletteUID;
    private List<RouletteSegmentSettingRequestDto> rouletteSegmentList;

    public RouletteSettingRequestDto(Roulette entity) {
        this.title = entity.getTitle();
        this.rouletteUID = entity.getRouletteUID();
        this.rouletteSegmentList = entity.getRouletteSegments().stream()
                .map(o -> new RouletteSegmentSettingRequestDto(o)).collect(Collectors.toList());
    }
}