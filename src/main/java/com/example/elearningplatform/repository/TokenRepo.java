package com.example.elearningplatform.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.elearningplatform.entity.user.VerificationToken;

public interface TokenRepo extends JpaRepository<VerificationToken, Long> {
   VerificationToken findByToken(String verificationToken);
}
