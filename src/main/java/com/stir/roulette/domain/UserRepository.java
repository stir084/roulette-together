package com.stir.roulette.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserIp(String ip);
    Optional<User> getByUserIp(String ip);

    <T> Optional<T> findById(String ip);
}