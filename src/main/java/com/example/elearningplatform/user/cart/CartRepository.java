package com.example.elearningplatform.user.cart;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface CartRepository extends JpaRepository<Cart, Integer> {
    @Query("""
        SELECT c FROM Cart c JOIN FETCH c.user u WHERE u.email = :userName
        """)
    Optional<Cart> findByUserName(@Param("userName") String userName);

    Optional<Cart> findByUserId(Integer userId);
}
