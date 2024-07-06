package com.example.elearningplatform.signup;

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
import com.example.elearningplatform.user.user.User;
import com.example.elearningplatform.user.user.UserRepository;
import com.example.elearningplatform.validator.Validator;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class SignUpController {

    private final SignUpService signUpService;

    private final UserRepository userRepository;
    private final TokenUtil tokenUtil;


    /******************************************************************************************************************/

    /**
     * Handles the sign-up request by validating the request, checking if the email already exists,
     * registering the user, generating a registration verification code, and sending the code via email.
     *
     * @param signUpRequest the sign-up request containing user information
     * @param result        the binding result containing validation errors
     * @param request       the HTTP servlet request
     * @return              the response indicating the status of the sign-up process
     * @throws MessagingException    if there is an error while sending the verification code via email
     * @throws IOException           if there is an error while processing the request
     * @throws SQLException          if there is an error while accessing the database
     */
    @PostMapping(value = "/signup")
    public Response signUp(@Valid @RequestBody SignUpRequest signUpRequest, BindingResult result,
            HttpServletRequest request) throws MessagingException, IOException, SQLException {
        if (result.hasErrors()) {
            return Validator.validate(result);
        }

        User user = userRepository.findByEmail(signUpRequest.getEmail()).orElse(null);
        if (user != null) {
            return new Response(HttpStatus.BAD_REQUEST, "Email already exists , Please login", null);

        }

        Response response = signUpService.registerUser(signUpRequest);

        if (response.getStatus() != HttpStatus.OK) {
            return response;
        }
        user = (User) response.getData();

        String token = tokenUtil.generateToken(signUpRequest.getEmail(),1000, 1000L);

        response = signUpService.sendRegistrationVerificationCode(signUpRequest.getEmail(), request,
                token);
        if (response.getStatus() != HttpStatus.OK) {
            return response;
        }
        response.setMessage("Registration successful, please check your email to verify your account");
        return response;

    }

    /******************************************************************************************************************/

    /**
     * Verifies the email using the provided token.
     *
     * @param  verficationToken   the token used to verify the email
     * @return                     the response indicating the result of the verification
     * @throws SQLException       if there is an error accessing the database
     * @throws IOException        if there is an error reading or writing to the database
     */
    @GetMapping("/verifyEmail/{token}")

    public Response verifyEmail(@PathVariable("token") String verficationToken) throws SQLException, IOException {
        return signUpService.verifyEmail(verficationToken);
    }

}