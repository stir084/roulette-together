package com.stir.roulette.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static javax.persistence.FetchType.LAZY;

@Getter
@NoArgsConstructor
@Entity
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "game_id")
    private Long id;

    @Column(nullable = false)
    private String gameCode;



    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL)
    private List<GameInfo> gameInfos = new ArrayList<>();

    @Builder
    public Game(String gameCode) {
        this.gameCode = gameCode;
    }

    // N쪽에 써주는 연관관계 메소드 //
    public void setUser(User user) {
        this.user = user;
        user.getGames().add(this);
    }

    // 1쪽에 써주는 연관관계 메소드(updateGame같이 메소드가 하나 더 필요하다) //
    public void addGameInfo(GameInfo gameInfo){
        this.getGameInfos().add(gameInfo);
        gameInfo.updateGame(this);
    }
}
