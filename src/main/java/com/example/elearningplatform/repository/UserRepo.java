package com.example.elearningplatform.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.elearningplatform.entity.user.User;

public interface UserRepo extends JpaRepository<User, Long> {
    public User findByEmail(String email);
}
