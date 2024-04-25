package com.example.elearningplatform.payment.transaction;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "transaction_id")
    private Integer id;
    private Integer courseId;
    private Integer userId;
    private LocalDateTime paymentDate;
    private String paymentMethod;
    private Integer price;
}
