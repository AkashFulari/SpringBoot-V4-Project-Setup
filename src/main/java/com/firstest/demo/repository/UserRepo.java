package com.firstest.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.firstest.demo.model.User;

public interface UserRepo extends JpaRepository<User, Long> {
}