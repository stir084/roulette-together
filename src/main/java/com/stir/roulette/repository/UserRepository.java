package com.stir.roulette.repository;

import com.stir.roulette.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    <T> Optional<T> findById(String ip);

    Optional<User> findByUserIp(String userIp);
}