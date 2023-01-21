package com.stir.roulette.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static javax.persistence.FetchType.LAZY;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Roulette {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "roulette_id")
    private Long id;

    @Type(type = "uuid-char")
    private UUID rouletteUID;

    @PrePersist
    private void setUUID(){
        this.setRouletteUID(UUID.randomUUID());
    }

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private FavoriteStatus favoriteStatus;

    @JsonIgnore
    @OneToMany(mappedBy = "roulette", cascade = CascadeType.ALL)
    private List<RouletteSegment> rouletteSegments = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private RouletteStatus status;

    private int prize;

    private String title;

    private int maxCount;

    private LocalDateTime createDate;



    // N쪽에 써주는 연관관계 메소드 //
    public void addUser(User user) {
        this.user = user;
        user.getRoulettes().add(this);
    }

    public void addRouletteSegment(RouletteSegment rouletteSegment){
        this.rouletteSegments.add(rouletteSegment);
        rouletteSegment.setRoulette(this);
    }

    public static Roulette createInitRoulette(String title, RouletteSegment... rouletteSegments){
        Roulette roulette = new Roulette();

        roulette.setStatus(RouletteStatus.READY);
        for (RouletteSegment rouletteSegment : rouletteSegments) {
            roulette.addRouletteSegment(rouletteSegment);
        }
        roulette.setMaxCount(8); // 초기 최대 갯수 8개
        roulette.setTitle(title);
        roulette.setFavoriteStatus(FavoriteStatus.UNFAVORED);
        roulette.setCreateDate(LocalDateTime.now());

        return roulette;
    }
}
