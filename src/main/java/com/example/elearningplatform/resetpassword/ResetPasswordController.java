package com.example.elearningplatform.resetpassword;

import java.io.IOException;
import java.sql.SQLException;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
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

    /***************************************************************************************************************/
    /*************************************************************************************************************/

    @PostMapping("/forget-password/send-email")

    public Response sendEmail(@RequestBody @Valid ResetPaswordRequest data, HttpServletRequest request,
            BindingResult result)
            throws MessagingException, SQLException, IOException {
        if (result.hasErrors()) {
            return Validator.validate(result);
        }

        User user = userRepository.findByEmail(data.getEmail()).orElse(null);
        // System.out.println(user);
        if (user == null)
            return new Response(HttpStatus.BAD_REQUEST, "User not found", null);
        if (!user.isEnabled()) {
            return signUpService.sendRegistrationVerificationCode(data.getEmail(), request,
                    tokenUtil.generateToken(data.getEmail(), 1000, 1000L));
        }
        return resetPasswordService.sendResetpasswordEmail(data.getEmail(), request,
                tokenUtil.generateToken(data.getEmail(), 1000, 1000L));
    }

    /***************************************************************************************************************/

    @PostMapping("/forget-password/save-password")
    public Response changePassword(@RequestBody @Valid ChangePasswordRequest data, BindingResult result,
            HttpServletRequest request)
            throws SQLException, IOException, MessagingException {
        if (result.hasErrors()) {
            return Validator.validate(result);
        }
        return resetPasswordService.savePassword(data.getEmail(), data.getPassword());
    }

    /***************************************************************************************************************/
    @GetMapping("/check-token")
    public Object savePassword(@RequestParam("token") String token, Model model)
            throws SQLException, IOException {

        if (tokenUtil.isTokenExpired(token)) {
            return new Response(HttpStatus.BAD_REQUEST, "invalid token", null);
        }
        return new Response(HttpStatus.OK, "valid token",
                tokenUtil.getUserName(token));
    }
    /***************************************************************************************************************/

}
