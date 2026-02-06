package com.example.SpringSecurity_WithDB.repo;

import com.example.SpringSecurity_WithDB.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User,Long> {
    Optional<User>findByUsername(String username);
}
