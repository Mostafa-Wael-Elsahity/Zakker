package com.example.elearningplatform.entity.user;

import jakarta.persistence.*;
import lombok.Data;

import javax.validation.constraints.NotBlank;

import java.sql.Blob;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank
    @Column(name = "first_name")
    private String firstName;

    @NotBlank
    @Column(name = "last_name")
    private String lastName;

    @NotBlank
    @Column(name = "email")
    private String email;

    @NotBlank
    @Column(name = "password")
    private String password;

    @NotBlank
    @Column(name = "role")
    private String role;

    @NotBlank
    @Column(name = "phone_number")
    private String phoneNumber;

    @NotBlank
    @Column(name = "profile_picture")
    @Lob
    private Blob profilePicture;

//    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
//    private Address address;

    @Column(name = "is_enabled")
    private boolean isEnabled;

    @Column(name = " registration_date")
    private LocalDateTime registrationDate;

    @NotBlank
    @Column(name = "bio")
    private String bio;

    @Column(name = "age")
    private int age;

    @Column(name = "last_login")
    private LocalDate lastLogin;
}
