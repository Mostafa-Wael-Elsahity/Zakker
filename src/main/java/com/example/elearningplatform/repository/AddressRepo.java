package com.example.elearningplatform.repository;

import com.example.elearningplatform.entity.user.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepo extends JpaRepository<Address, Long> {
}
