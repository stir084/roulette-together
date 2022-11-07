package com.stir.roulette.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class RouletteSegment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String element;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "roulette_id")
    private Roulette roulette;

    //==연관관계 메서드==//
    public void addRoulette(Roulette roulette) {
        this.roulette = roulette;
        roulette.getRouletteSegments().add(this);
    }

    public static RouletteSegment createRouletteSegment(Roulette roulette, String element){
        RouletteSegment rouletteSegment = new RouletteSegment();
        rouletteSegment.addRoulette(roulette);
        rouletteSegment.setElement(element);
        return rouletteSegment;
    }
   /* public void updateGame(Roulette roulette){
        this.roulette = roulette;
    }*/


}
