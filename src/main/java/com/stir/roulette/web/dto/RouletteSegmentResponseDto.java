package com.stir.roulette.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.stir.roulette.domain.Roulette;
import com.stir.roulette.domain.RouletteSegment;
import com.stir.roulette.domain.User;
import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static javax.persistence.FetchType.LAZY;

@Getter
public class RouletteSegmentResponseDto {

    private Long id;
    private String element;

    public RouletteSegmentResponseDto(RouletteSegment entity) {
        this.id = entity.getId();
        this.element = entity.getElement();
    }
}
