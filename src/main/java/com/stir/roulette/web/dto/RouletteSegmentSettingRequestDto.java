package com.stir.roulette.web.dto;

import com.stir.roulette.domain.Roulette;
import com.stir.roulette.domain.RouletteSegment;
import com.stir.roulette.domain.RouletteStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class RouletteSegmentSettingRequestDto {

    //private Long id;
    private UUID segmentUID;
    private String element;

    public RouletteSegmentSettingRequestDto(RouletteSegment entity) {
        //this.id = entity.getId();
        this.segmentUID = entity.getSegmentUID();
        this.element = entity.getElement();
    }
}