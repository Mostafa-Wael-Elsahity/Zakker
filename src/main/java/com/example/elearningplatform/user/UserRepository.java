package com.example.elearningplatform.user;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);

    List<User> findByEmailContainingIgnoreCase(
            String email);

    @Query(value = "select id from users where email like ?", nativeQuery = true)
    List<Object> findUserId(String email);
}
