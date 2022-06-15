package com.stir.roulette.web.dto;

import com.stir.roulette.domain.Game;
import lombok.Getter;

@Getter
public class GamesResponseDto {

    private Long id;
    private String gameCode;
    private String userIp;
    private String author;

    public GamesResponseDto(Game entity) {
        this.id = entity.getId();
        this.gameCode = entity.getGameCode();
        this.userIp = entity.getUserIp();
    }
}
