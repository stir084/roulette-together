package com.stir.roulette.domain;

import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

import static javax.persistence.FetchType.LAZY;


@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class RouletteSegment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Type(type = "uuid-char")
    private UUID segmentUID;

    @PrePersist
    private void setUUID(){
        this.setSegmentUID(UUID.randomUUID());
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
        rouletteSegment.setElement(element);
        rouletteSegment.setCreateDate(LocalDateTime.now());
        return rouletteSegment;
    }
}
