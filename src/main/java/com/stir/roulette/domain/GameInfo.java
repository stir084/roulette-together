package com.stir.roulette.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Getter
@NoArgsConstructor
@Entity
public class GameInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String element;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "game_id")
    private Game game;

    @Builder
    public GameInfo(String element) {
        this.element = element;
    }

    //==연관관계 메서드==//
    public void setGame(Game game) {
        this.game = game;
        game.getGameInfos().add(this);
    }

    public void updateGame(Game game){
        this.game = game;
    }


}
