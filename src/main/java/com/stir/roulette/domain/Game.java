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

    @Column(nullable = false)
    private String userIp;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;
/*
    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL)
    private List<GameInfo> gameInfos = new ArrayList<>();*/

    @Builder
    public Game(String gameCode, String userIp) {
        this.gameCode = gameCode;
        this.userIp = userIp;
    }

    //==연관관계 메서드==//
    public void setUser(User user) {
        this.user = user;
        user.getGames().add(this);
    }
}
