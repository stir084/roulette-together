package com.stir.roulette.web.dto;

import com.stir.roulette.domain.RouletteSegment;
import lombok.Getter;

import java.util.UUID;

@Getter
public class RouletteSegmentResponseDto {

    private UUID segmentUID;
    private String element;

    public RouletteSegmentResponseDto(RouletteSegment entity) {
        this.segmentUID = entity.getSegmentUID();
        this.element = entity.getElement();
    }
}
