package com.stir.roulette.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String gameCode;

    @Column(nullable = false)
    private String userIp;

    @Builder
    public Game(String gameCode, String userIp) {
        this.gameCode = gameCode;
        this.userIp = userIp;
    }
}
