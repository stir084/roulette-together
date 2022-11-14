package com.stir.roulette.repository;

import com.stir.roulette.domain.Roulette;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface RouletteRepository extends JpaRepository<Roulette, Long>, RouletteRepositoryCustom {
    Optional<Roulette> findByRouletteCode(String rouletteCode);


}
