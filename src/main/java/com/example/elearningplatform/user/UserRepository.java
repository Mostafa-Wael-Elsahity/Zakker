package com.example.elearningplatform.user;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);

    Optional<User> findByFirstName(String firstName);

    Optional<User> findByLastName(String lastName);

    @Query("""
                SELECT u FROM User u
                WHERE lower(u.email) LIKE lower(concat('%', :searchKey, '%'))
                OR lower(u.firstName) LIKE lower(concat('%', :searchKey, '%'))
                OR lower(u.lastName) LIKE lower(concat('%', :searchKey, '%'))
            """)
    Page<User> findBySearchKey(@Param("searchKey") String searchKey, Pageable pageable);

}