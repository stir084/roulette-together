package com.stir.roulette.web.dto;

import com.stir.roulette.domain.RouletteSegment;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor
public class RouletteSegmentSettingRequestDto {

    private UUID segmentUID;
    private String element;

    public RouletteSegmentSettingRequestDto(RouletteSegment entity) {
        this.segmentUID = entity.getSegmentUID();
        this.element = entity.getElement();
    }

    public RouletteSegment toEntity(){
        return RouletteSegment.builder()
                .segmentUID(segmentUID)
                .element(element)
                .createDate(LocalDateTime.now())
                .build();
    }
}