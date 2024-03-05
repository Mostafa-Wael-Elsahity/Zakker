package com.example.elearningplatform.verficationtoken;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.elearningplatform.user.User;

public interface VerficationTokenRepository extends JpaRepository<VerificationToken, Integer> {

   VerificationToken findByToken(String verificationToken);

   VerificationToken findByUser(User user);

}
