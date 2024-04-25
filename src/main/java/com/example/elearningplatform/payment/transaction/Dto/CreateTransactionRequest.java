package com.example.elearningplatform.payment.transaction.Dto;

import lombok.Data;
@Data
public class CreateTransactionRequest {
    private Integer courseId;
    private String paymentMethod;
    private Integer price;
}
