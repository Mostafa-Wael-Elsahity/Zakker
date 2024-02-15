package com.example.elearningplatform.entity.user;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "verification_token")
public class VerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String token;
    @OneToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_id")
    private User user;
    private LocalDateTime expiryDate;

    public VerificationToken(User user) {
        expiryDate = LocalDateTime.now();
        this.token = UUID.randomUUID().toString();
        this.user = user;
    }

    // standard constructors, getters and setters
}