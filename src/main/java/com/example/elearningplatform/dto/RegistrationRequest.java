package com.example.elearningplatform.dto;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import com.example.elearningplatform.entity.user.Address;
import com.example.elearningplatform.entity.user.User;

import jakarta.persistence.Column;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class RegistrationRequest {
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
    private int age;

    public User toUser() throws IOException, SQLException {
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPassword(password);
        user.setAge(age);
        user.setBio(bio);
        user.setPhoneNumber(phoneNumber);
        byte[] bytes = profilePicture.getBytes();
        Blob blob = new javax.sql.rowset.serial.SerialBlob(bytes);
        user.setProfilePicture(blob);
        return user;
    }
}
