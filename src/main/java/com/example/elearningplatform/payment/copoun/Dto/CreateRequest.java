package com.example.elearningplatform.payment.copoun.Dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class CreateRequest {
    @NotEmpty(message = "Code cannot be empty")
    private String code;
    @NotEmpty(message = "Expiration date cannot be empty")
    private Integer expirationDate;
    @NotEmpty(message = "Number of copouns cannot be empty")
    private Integer numberOfCopouns;
    @NotEmpty(message = "Discount cannot be empty")
    private Double discount;
    private Integer courseId;
}
