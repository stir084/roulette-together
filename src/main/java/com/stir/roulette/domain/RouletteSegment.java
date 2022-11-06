package com.stir.roulette.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Getter
@NoArgsConstructor
@Entity
public class RouletteSegment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String element;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "game_id")
    private Roulette roulette;

    @Builder
    public RouletteSegment(String element) {
        this.element = element;
    }

    //==연관관계 메서드==//
    public void setRoulette(Roulette roulette) {
        this.roulette = roulette;
        roulette.getRouletteSegments().add(this);
    }

    public void updateGame(Roulette roulette){
        this.roulette = roulette;
    }


}
