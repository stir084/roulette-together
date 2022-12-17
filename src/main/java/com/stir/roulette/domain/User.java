package com.stir.roulette.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column
    private String userIp;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Roulette> roulettes = new ArrayList<>();

    private int FavoriteCount;
  /*  @Builder
    public User(String userIp) {
        this.userIp = userIp;
    }*/

    public void addRoulette(Roulette roulette) {
        this.roulettes.add(roulette);
        roulette.setUser(this);
    }

    public static User createUser(String userIp, Roulette roulette){
        User user = new User();
        user.setUserIp(userIp);
        user.addRoulette(roulette);
        user.setFavoriteCount(0);

        return user;
    }
}
