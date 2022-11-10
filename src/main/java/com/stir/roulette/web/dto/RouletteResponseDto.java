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
    private List<RouletteSegmentResponseDto> rouletteSegment;
    private int prize;
    private String title;
    private RouletteStatus rouletteStatus;

    public RouletteResponseDto(Roulette entity) {
        this.id = entity.getId();
        this.rouletteCode = entity.getRouletteCode();
        this.rouletteSegment = entity.getRouletteSegments().stream()
                .map(o -> new RouletteSegmentResponseDto(o)).collect(Collectors.toList());
        this.prize = entity.getPrize();
        this.title = entity.getTitle();
        this.rouletteStatus = entity.getStatus();
    }
}