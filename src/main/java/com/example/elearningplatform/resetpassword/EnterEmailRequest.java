package com.example.elearningplatform.resetpassword;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
@Setter
@Getter
public class EnterEmailRequest {
    @Email(message = "Invalid email address")
    @NotEmpty(message = "Email cannot be empty")
    public String email;
    
}
