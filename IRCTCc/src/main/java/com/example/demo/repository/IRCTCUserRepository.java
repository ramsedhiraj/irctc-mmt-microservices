package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.entity.User;

public interface IRCTCUserRepository extends JpaRepository<User, Integer> {
    boolean existsByEmail(String email);
}
