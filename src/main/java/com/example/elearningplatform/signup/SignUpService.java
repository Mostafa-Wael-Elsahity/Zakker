package com.example.elearningplatform.signup;

import java.io.IOException;
import java.sql.SQLException;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.elearningplatform.Response;
import com.example.elearningplatform.user.User;
import com.example.elearningplatform.user.UserRepository;
import com.example.elearningplatform.user.UserService;
import com.example.elearningplatform.verficationtoken.VerficationTokenService;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SignUpService {

    private final UserRepository userRepository;
    private final UserService userService;
    private final VerficationTokenService verficationTokenService;

    /******************************************************************************************************************/

    public Response registerUser(SignUpRequest request) throws MessagingException, IOException, SQLException {
        try {

            User user = userService.saveUser(request);

            return new Response(HttpStatus.OK, null, user);

        } catch (Exception e) {

            return new Response(HttpStatus.BAD_REQUEST, e.getMessage(), null);
        }

    }

    /******************************************************************************************************************/

    public Response verifyEmail(String token) throws SQLException, IOException {
        // System.out.println(token);

        if (!verficationTokenService.checkTokenValidation(token)) {
            return new Response(HttpStatus.BAD_REQUEST, "Expired token!", null);
        }

        User user = userRepository.findByEmail(token.split(",")[2]).orElse(null);
        if (user == null) {
            return new Response(HttpStatus.NOT_FOUND, "User not found!", null);
        }

        if (user.isEnabled()) {
            return new Response(HttpStatus.BAD_REQUEST, "Email is Already verified!", null);
        }

        user.setEnabled(true);
        userRepository.save(user);

        return new Response(HttpStatus.OK, "Email verified successfully! Now you can login", null);
    }
}

/******************************************************************************************************************/

/******************************************************************************************************************/
