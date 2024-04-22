package com.example.elearningplatform.resetpassword;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ChangePasswordRequest {
    private String email;
    @NotEmpty(message = "Password cannot be empty")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$", message = "Password must contain at least one number, one uppercase letter, one lowercase letter, one special character, and be at least 8 characters long")
    private String password;
    @NotEmpty(message = "Confirm password cannot be empty")
    private String confirmPassword;

    @AssertTrue(message = "Passwords must match")
    private boolean isPasswordMatching() {
        if (password == null || confirmPassword == null) {
            return false;
        } else {
            return password.equals(confirmPassword);
        }
    }
}
