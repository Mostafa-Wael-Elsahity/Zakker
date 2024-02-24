package com.example.elearningplatform.signup;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class SignUpRequest {

    @NotBlank
    @NotEmpty
    private String firstName;
    @NotBlank
    @NotEmpty
    private String lastName;

    @NotBlank
    @NotEmpty
    @Email
    private String email;

    @NotBlank
    @NotEmpty
    @Pattern(regexp = "^(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2})$")
    private String password;

    private String phoneNumber;
    private MultipartFile profilePicture;
    private String street;
    private String city;
    private String state;
    private String country;
    private String zipCode;
    private String bio;
    private Integer age;

}
