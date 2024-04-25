package com.example.elearningplatform.payment.transaction;

import java.io.Serializable;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class TransactionId implements Serializable {
    
    private Integer courseId;
    private Integer userId;

    // Getters and setters
}
