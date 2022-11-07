package com.stir.roulette.web.dto;

import com.stir.roulette.domain.Roulette;
import com.stir.roulette.domain.RouletteStatus;
import lombok.Getter;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class RouletteResponseDto {

    private Long id;
    private String rouletteCode;
    private List<RouletteSegmentResponseDto> rouletteSegmentResponseDtos;
    private int prize;
    private RouletteStatus rouletteStatus;

    public RouletteResponseDto(Roulette entity) {
        this.id = entity.getId();
        this.rouletteCode = entity.getRouletteCode();
        this.rouletteSegmentResponseDtos = entity.getRouletteSegments().stream()
                .map(o -> new RouletteSegmentResponseDto(o)).collect(Collectors.toList());
        this.prize = entity.getPrize();
        this.rouletteStatus = entity.getStatus();
    }
}