package com.stir.roulette.repository;

import com.stir.roulette.domain.Roulette;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RouletteRepository extends JpaRepository<Roulette, Long>, RouletteRepositoryCustom {
    Roulette findByRouletteCode(String rouletteCode);

}
