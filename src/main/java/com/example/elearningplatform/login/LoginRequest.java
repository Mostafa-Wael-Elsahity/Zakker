package com.example.elearningplatform.login;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    @Email(message = "Invalid email address")
    @NotEmpty(message = "Email cannot be empty")
    public String email;
    // @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$", message = "Password must contain at least one number, one uppercase letter, one lowercase letter, one special character, and be at least 8 characters long")
    // @NotEmpty(message = "Password cannot be empty")
    public String password;

}
