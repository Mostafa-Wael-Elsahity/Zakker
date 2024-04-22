package com.example.elearningplatform.resetpassword;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class ResetPaswordRequest {

    @NotEmpty(message = "Email cannot be empty")
    @Email(message = "Invalid email address")
    private String email;
}
