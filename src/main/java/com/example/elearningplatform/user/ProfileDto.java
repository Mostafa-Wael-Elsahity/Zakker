package com.example.elearningplatform.user;

import java.time.LocalDateTime;

import com.example.elearningplatform.user.address.AddressDto;

import lombok.Data;

@Data
public class ProfileDto {

    private Integer id;

    private String email;

    private String firstName;

    private String lastName;

    private String phoneNumber;

    private Boolean enabled;

    private LocalDateTime registrationDate;
    private String bio;

    private LocalDateTime lastLogin;

    private Integer age;

    private AddressDto address;

}
