package com.stir.roulette.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Roulette {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "game_id")
    private Long id;

    private String rouletteCode;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @JsonIgnore
    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL)
    private List<RouletteSegment> rouletteSegments = new ArrayList<>();


    // N쪽에 써주는 연관관계 메소드 //
    public void setUser(User user) {
        this.user = user;
        user.getRoulettes().add(this);
    }

    // 1쪽에 써주는 연관관계 메소드(updateGame같이 메소드가 하나 더 필요하다) //
    public void addGameInfo(RouletteSegment rouletteSegment){
        this.getRouletteSegments().add(rouletteSegment);
        rouletteSegment.updateGame(this);
    }

    public static Roulette createRoulette(String rouletteCode){
        Roulette roulette = new Roulette();

        roulette.setRouletteCode(rouletteCode);
        roulette.addGameInfo(new RouletteSegment().builder().element("짜장면").build());
        roulette.addGameInfo(new RouletteSegment().builder().element("짬뽕").build());
        roulette.addGameInfo(new RouletteSegment().builder().element("볶음밥").build());
        roulette.addGameInfo(new RouletteSegment().builder().element("탕수육").build());

        return roulette;
    }
}
