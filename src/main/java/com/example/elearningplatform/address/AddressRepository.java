package com.example.elearningplatform.address;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.elearningplatform.user.User;

public interface AddressRepository extends JpaRepository<Address, Long> {
    public Address findByUser(User user);
}
