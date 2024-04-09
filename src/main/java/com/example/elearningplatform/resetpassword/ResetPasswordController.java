package com.example.elearningplatform.resetpassword;

import java.io.IOException;
import java.sql.SQLException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.example.elearningplatform.Response;
import com.example.elearningplatform.Validator;
import com.example.elearningplatform.user.User;
import com.example.elearningplatform.user.UserRepository;
import com.example.elearningplatform.verficationtoken.VerficationTokenService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ResetPasswordController {

    private final ResetPasswordService resetPasswordService;
    private final VerficationTokenService verficationTokenService;
    private final UserRepository userRepository;

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
        System.out.println(user);
        if (user == null)
            return new Response(HttpStatus.BAD_REQUEST, "User not found", null);
        if (!user.isEnabled()) {
            return verficationTokenService.sendRegistrationVerificationCode(data.getEmail(), request,
                    verficationTokenService.generateToken(data.getEmail()));
        }
        return resetPasswordService.sendResetpasswordEmail(data.getEmail(), request,
                verficationTokenService.generateToken(data.getEmail()));

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

    /***************************************************************************************************************/

}
