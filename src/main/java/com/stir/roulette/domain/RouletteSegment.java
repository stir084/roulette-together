package com.stir.roulette.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static javax.persistence.FetchType.LAZY;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class RouletteSegment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Type(type = "uuid-char")
    private UUID rouletteSegmentUID;

    @PrePersist
    private void setUUID(){
        this.setRouletteSegmentUID(UUID.randomUUID());
    }

    @Column(nullable = false)
    private String element;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "roulette_id")
    private Roulette roulette;

    private LocalDateTime createDate;

    public void addRoulette(Roulette roulette) {
        this.roulette = roulette;
        roulette.getRouletteSegments().add(this);
    }

    public static RouletteSegment createRouletteSegment(String element){
        RouletteSegment rouletteSegment = new RouletteSegment();
        //rouletteSegment.addRoulette(roulette); //룰렛에서 해줭하하
       rouletteSegment.setElement(element);
        rouletteSegment.setCreateDate(LocalDateTime.now());
        return rouletteSegment;
    }
   /* public void updateGame(Roulette roulette){
        this.roulette = roulette;
    }*/


}
