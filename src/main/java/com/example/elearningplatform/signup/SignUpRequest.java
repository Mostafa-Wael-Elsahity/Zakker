package com.example.elearningplatform.signup;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class SignUpRequest {

    @Pattern(regexp = "^\\S*$", message = "Spaces are not allowed")
    @NotEmpty(message = "First name cannot be empty")
    private String firstName;
    @Pattern(regexp = "^\\S*$", message = "Spaces are not allowed")
    private String lastName;

    @Email(message = "Invalid email address")
    @NotEmpty(message = "Email cannot be empty")
    private String email;

    @NotEmpty(message = "Password cannot be empty")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$", message = "Password must contain at least one number, one uppercase letter, one lowercase letter, one special character, and be at least 8 characters long")
    private String password;
    @Pattern(regexp = "^01\\d{9}$", message = "Invalid phone number")
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
