package com.example.elearningplatform.user.address;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Integer> {
    Optional<Address> findByUserId(Integer id);
}
