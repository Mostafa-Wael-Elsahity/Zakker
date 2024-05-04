package com.example.elearningplatform.user.user.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class UpdateProfileRequest {

    @NotEmpty(message = "Email cannot be empty")
    private String firstName;

    @NotEmpty(message = "Email cannot be empty")
    private String lastName;

    private String paypalEmail;

    private String phoneNumber;

    private String about;

    private Integer age;
    private String street;

    private String city;

    private String state;

    private String country;

}
