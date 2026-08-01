package com.akashf.springv4.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.akashf.springv4.demo.model.User;

public interface UserRepo extends JpaRepository<User, Long> {
}