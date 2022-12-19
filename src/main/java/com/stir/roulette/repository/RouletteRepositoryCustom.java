package com.stir.roulette.repository;


import com.stir.roulette.domain.FavoriteStatus;
import com.stir.roulette.domain.Roulette;
import com.stir.roulette.domain.RouletteStatus;
import com.stir.roulette.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface RouletteRepositoryCustom {
    List<Roulette> findLastGameByUserUUID(String userUUID);

    //Roulette findByRouletteUID(Long id, UUID rouletteUID);

    Page<Roulette> findByUserAndStatus(User user, RouletteStatus rouletteStatus, Pageable pageable);


}
