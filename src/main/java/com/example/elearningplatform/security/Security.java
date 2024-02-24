//package com.example.elearningplatform.security;
//
//import com.example.elearningplatform.user.User;
//import jakarta.annotation.PostConstruct;
//import lombok.AllArgsConstructor;
//import lombok.NoArgsConstructor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.security.crypto.bcrypt.BCrypt;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//@Service
//@NoArgsConstructor
//@AllArgsConstructor
//public class Security {
//
//    public boolean checkAuthority(User user, String role) {
//        return user.getRole().equals(role);
//    }
//
//    private int saltRounds = 10;
//    private String salt;
//
//    @PostConstruct
//    public void postConstruct() {
//        salt = BCrypt.gensalt(saltRounds);
//    }
//
//    public String encryptPassword(String password) {
//        return BCrypt.hashpw(password, salt);
//
//    }
//
//    public boolean verifyPassword(String password, String encrypedPassword) {
//        return BCrypt.checkpw(password, encrypedPassword);
//    }
//}
