package com.stir.roulette.repository;

import com.stir.roulette.domain.FavoriteStatus;
import com.stir.roulette.domain.Roulette;
import com.stir.roulette.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface RouletteRepository extends JpaRepository<Roulette, Long>, RouletteRepositoryCustom {
    Optional<Roulette> findByRouletteUID(UUID rouletteUID);

    List<Roulette> findByUserAndFavoriteStatus(User user, FavoriteStatus favoriteStatus);

    //Page<Roulette> findByUserAndStatus(User user, RouletteStatus rouletteStatus, Pageable pageable);


}
        
