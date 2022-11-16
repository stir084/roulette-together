package com.stir.roulette.repository;


import com.stir.roulette.domain.Roulette;

import java.util.List;
import java.util.UUID;


public interface RouletteRepositoryCustom {
    List<Roulette> findLastGameByUserIp(String userIp);

    Roulette findByIdAndRouletteUID(Long id, UUID rouletteUID);


}
