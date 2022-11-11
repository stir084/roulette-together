package com.stir.roulette.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import java.time.LocalDateTime;
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
    @Column(name = "roulette_id")
    private Long id;

    private String rouletteCode;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @JsonIgnore
    @OneToMany(mappedBy = "roulette", cascade = CascadeType.ALL)
    private List<RouletteSegment> rouletteSegments = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private RouletteStatus status; //주문상태 [ORDER, CANCEL]

    private int prize;

    private String title;

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

    // 1쪽에 써주는 연관관계 메소드(updateGame같이 메소드가 하나 더 필요하다) //
    /*public void addGameInfo(RouletteSegment rouletteSegment){
        this.getRouletteSegments().add(rouletteSegment);
        rouletteSegment.updateGame(this);
    }*/

   /* public static Roulette createInitRoulette2(String rouletteCode){
        Roulette roulette = new Roulette();

        roulette.setRouletteCode(rouletteCode);
        roulette.setStatus(RouletteStatus.READY);

        List<RouletteSegment> rouletteSegmentList = new ArrayList<>();
        rouletteSegmentList.add(RouletteSegment.createRouletteSegment(roulette, "짜장면"));
        rouletteSegmentList.add(RouletteSegment.createRouletteSegment(roulette, "짬뽕"));
        rouletteSegmentList.add(RouletteSegment.createRouletteSegment(roulette, "탕수육"));
        rouletteSegmentList.add(RouletteSegment.createRouletteSegment(roulette, "깐풍기"));

        roulette.setRouletteSegments(rouletteSegmentList);
        roulette.setTitle("오늘의 점심은?");
        roulette.setCreateDate(LocalDateTime.now());

        return roulette;
    }*/

    public static Roulette createInitRoulette(String rouletteCode, String title, RouletteSegment... rouletteSegments){
        Roulette roulette = new Roulette();

        roulette.setRouletteCode(rouletteCode);
        roulette.setStatus(RouletteStatus.READY);

        for (RouletteSegment rouletteSegment : rouletteSegments) {
            roulette.addRouletteSegment(rouletteSegment);
        }
        //roulette.setRouletteSegments(rouletteSegmentList);
        roulette.setTitle(title);
        roulette.setCreateDate(LocalDateTime.now());

        return roulette;
    }
}
