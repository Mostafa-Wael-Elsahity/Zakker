package com.example.elearningplatform.resetpassword;

import java.io.IOException;
import java.sql.SQLException;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    /**
     * Handles the request to enter the email for password reset. Validates the request body, checks if the user exists,
     * and sends the appropriate response based on the user's status. If the user is disabled, sends a verification
     * code for registration. Otherwise, sends a password reset email.
     *
     * @param  email          the email address entered by the user
     * @param  result         the binding result of the request body validation
     * @param  request        the HTTP servlet request
     * @return                the response indicating the success or failure of the password reset process
     * @throws MessagingException   if there is an error sending the email
     * @throws SQLException        if there is an error accessing the database
     * @throws IOException          if there is an error generating the token
     */
    @PostMapping("/forget-password")
    public Response enterEmail(@RequestBody @Valid EnterEmailRequest email, BindingResult result,
            HttpServletRequest request)
            throws MessagingException, SQLException, IOException {

               if (result.hasErrors()) {
                   return Validator.validate(result);
               }

                    User user = userRepository.findByEmail(email.getEmail()).orElse(null);
        // System.out.println(user);
        if (user == null)
            return new Response(HttpStatus.BAD_REQUEST, "User not found", null);
        if (!user.isEnabled()) {
            return signUpService.sendRegistrationVerificationCode(email.getEmail(), request,
                    tokenUtil.generateToken(email.getEmail(), 1000, 1000L));
        }
        return resetPasswordService.sendResetpasswordEmail(email.getEmail(), request,
                tokenUtil.generateToken(email.getEmail(), 1000, 1000L));
    }

    /***************************************************************************************************************/

    /**
     * Handles the request to change the password. Validates the request body, and saves the new password.
     *
     * @param  data          the ChangePasswordRequest object containing email and new password
     * @param  result        the binding result of the request body validation
     * @param  request       the HTTP servlet request
     * @return               the response indicating the success or failure of the password change process
     * @throws SQLException        if there is an error accessing the database
     * @throws IOException         if there is an error during I/O operations
     * @throws MessagingException  if there is an error sending an email
     */
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
    
    /**
     * Handles the request to save the password based on the provided token. Checks if the token is expired
     * and returns an appropriate response.
     *
     * @param  token   the token extracted from the request URL
     * @param  model   the model object
     * @return         the response containing the result of the password save operation
     */
    @GetMapping("/check-token/{token}")
    public Response savePassword(@PathVariable("token") String token, Model model)
            throws SQLException, IOException {

        if (tokenUtil.isTokenExpired(token)) {
            return new Response(HttpStatus.BAD_REQUEST, "invalid token", null);
        }
        return new Response(HttpStatus.OK, "valid token",
                tokenUtil.getUserName(token));
    }
    /***************************************************************************************************************/

}
