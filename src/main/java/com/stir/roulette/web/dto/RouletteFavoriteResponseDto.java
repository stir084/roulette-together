package com.stir.roulette.web.dto;

import com.stir.roulette.domain.FavoriteStatus;
import com.stir.roulette.domain.Roulette;
import com.stir.roulette.domain.RouletteStatus;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
public class RouletteFavoriteResponseDto {

    private UUID rouletteUID;
    private List<RouletteSegmentResponseDto> rouletteSegment;
    private int prize;
    private String prizeName;
    private String title;
    private RouletteStatus rouletteStatus;
    private FavoriteStatus favoriteStatus;
    private String createDate;
    public RouletteFavoriteResponseDto(Roulette entity) {
        this.rouletteUID = entity.getRouletteUID();
        this.rouletteSegment = entity.getRouletteSegments().stream()
                .map(o -> new RouletteSegmentResponseDto(o)).collect(Collectors.toList());
        this.prize = entity.getPrize();
        this.title = entity.getTitle();
        this.rouletteStatus = entity.getStatus();
        this.favoriteStatus = entity.getFavoriteStatus();
        if(entity.getPrize() != 0){
            this.prizeName = entity.getRouletteSegments().get(entity.getPrize()-1).getElement();
        }else{
            this.prizeName = "";
        }
        this.createDate = entity.getCreateDate().format(DateTimeFormatter.ofPattern("yy.MM.dd HH:mm"));
    }
}