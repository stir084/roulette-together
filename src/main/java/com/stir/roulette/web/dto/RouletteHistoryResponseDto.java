package com.stir.roulette.web.dto;

import com.stir.roulette.domain.Roulette;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Getter
@NoArgsConstructor
public class RouletteHistoryResponseDto {
    private String title;
    private UUID rouletteUID;
    private String createDate;
    private String prize;

    public RouletteHistoryResponseDto(Roulette entity) {
        this.title = entity.getTitle();
        this.rouletteUID = entity.getRouletteUID();
        this.createDate = entity.getCreateDate().format(DateTimeFormatter.ofPattern("yy.MM.dd HH:mm"));
        this.prize = entity.getRouletteSegments().get(entity.getPrize()-1).getElement();
    }
}