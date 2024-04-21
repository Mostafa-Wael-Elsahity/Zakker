package com.example.elearningplatform.signup;

import java.io.IOException;
import java.sql.SQLException;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.elearningplatform.response.Response;
import com.example.elearningplatform.security.TokenUtil;
import com.example.elearningplatform.user.User;
import com.example.elearningplatform.user.UserRepository;
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

    /******************************************************************************************************************/

    @PostMapping(value = "/signup/post-signup", consumes = { "multipart/form-data" })

    public Response signUp(@Valid SignUpRequest signUpRequest, BindingResult result,
            HttpServletRequest request) throws MessagingException, IOException, SQLException {
        // return new Response(HttpStatus.OK, "ok", signUpRequest.getEmail());
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

    @GetMapping("/verifyEmail")

    public Response verifyEmail(@RequestParam("token") String verficationToken) throws SQLException, IOException {
        // System.out.println(verficationToken);

        return signUpService.verifyEmail(verficationToken);
    }

}