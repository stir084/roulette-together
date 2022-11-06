package com.stir.roulette.repository;


import com.stir.roulette.domain.Roulette;

import java.util.List;


public interface RouletteRepositoryCustom {
    List<Roulette> findLastGame(String userIp);
}
