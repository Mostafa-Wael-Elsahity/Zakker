package com.example.elearningplatform.resetpassword;

import java.io.IOException;
import java.sql.SQLException;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.elearningplatform.response.Response;
import com.example.elearningplatform.security.TokenUtil;
import com.example.elearningplatform.signup.SignUpService;
import com.example.elearningplatform.user.user.User;
import com.example.elearningplatform.user.user.UserRepository;
import com.example.elearningplatform.validator.Validator;

import ch.qos.logback.core.model.Model;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ResetPasswordController {

    private final ResetPasswordService resetPasswordService;
    private final SignUpService signUpService;
    private final UserRepository userRepository;
    private final TokenUtil tokenUtil;

    /*************************************************************************************************************/

    @PostMapping("/forget-password")

    public Response sendEmail(@RequestParam("email") String email, HttpServletRequest request,
            BindingResult result)
            throws MessagingException, SQLException, IOException {
        if (result.hasErrors()) {
            return Validator.validate(result);
        }

        User user = userRepository.findByEmail(email).orElse(null);
        // System.out.println(user);
        if (user == null)
            return new Response(HttpStatus.BAD_REQUEST, "User not found", null);
        if (!user.isEnabled()) {
            return signUpService.sendRegistrationVerificationCode(email, request,
                    tokenUtil.generateToken(email, 1000, 1000L));
        }
        return resetPasswordService.sendResetpasswordEmail(email, request,
                tokenUtil.generateToken(email, 1000, 1000L));
    }

    /***************************************************************************************************************/

    @PostMapping("/change-password")
    public Response changePassword(@RequestBody @Valid ChangePasswordRequest data, BindingResult result,
            HttpServletRequest request)
            throws SQLException, IOException, MessagingException {
        if (result.hasErrors()) {
            return Validator.validate(result);
        }
        return resetPasswordService.savePassword(data.getEmail(), data.getPassword());
    }

    /***************************************************************************************************************/
    @GetMapping("/check-token/{token}")
    public Object savePassword(@PathVariable("token") String token, Model model)
            throws SQLException, IOException {

        if (tokenUtil.isTokenExpired(token)) {
            return new Response(HttpStatus.BAD_REQUEST, "invalid token", null);
        }
        return new Response(HttpStatus.OK, "valid token",
                tokenUtil.getUserName(token));
    }
    /***************************************************************************************************************/

}
