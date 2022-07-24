package com.stir.roulette.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column
    private String userIp;

    @Builder
    public User(String userIp) {
        this.userIp = userIp;
    }

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Game> games = new ArrayList<>();


}
