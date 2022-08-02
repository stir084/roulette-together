package com.stir.roulette.web.dto;

import com.stir.roulette.domain.Game;
import com.stir.roulette.domain.GameInfo;
import lombok.Getter;

import java.util.List;

@Getter
public class GamesResponseDto {

    private Long id;
    private String gameCode;
    public GamesResponseDto(Game entity) {
        this.id = entity.getId();
        this.gameCode = entity.getGameCode();
    }
}
