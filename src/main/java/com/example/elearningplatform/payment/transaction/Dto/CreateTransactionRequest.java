package com.example.elearningplatform.payment.transaction.dto;

import lombok.Data;
@Data
public class CreateTransactionRequest {
    private Integer courseId;
    private String paymentMethod;
    private Integer price;
}

