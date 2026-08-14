package com.akashf.springv4.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.akashf.springv4.demo.model.UserToken;

public interface UserTokenRepo extends JpaRepository<UserToken, Long> {
    Optional<UserToken> findTopByOrderByIdDesc();
}