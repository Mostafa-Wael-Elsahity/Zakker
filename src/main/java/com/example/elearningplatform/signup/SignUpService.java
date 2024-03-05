package com.example.elearningplatform.signup;

import java.io.IOException;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.elearningplatform.Response;
import com.example.elearningplatform.user.User;
import com.example.elearningplatform.user.UserService;
import com.example.elearningplatform.verficationtoken.VerficationTokenRepository;
import com.example.elearningplatform.verficationtoken.VerficationTokenService;
import com.example.elearningplatform.verficationtoken.VerificationToken;

import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class SignUpService {
    @Autowired
    UserService userService;
    @Autowired
    private VerficationTokenRepository verficationTokenRepository;
    @Autowired
    private VerficationTokenService verficationTokenService;

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

    public Response verifyEmail(String verficationToken) throws SQLException, IOException {
        try {
            VerificationToken token = verficationTokenRepository.findByToken(verficationToken);
            if (!verficationTokenService.checkTokenValidation(token))
                return new Response(HttpStatus.BAD_REQUEST, "Expired token!", null);

            User user = token.getUser();
            if (user.isEnabled()) {
                return new Response(HttpStatus.BAD_REQUEST, "Email is Already verified!", null);
            }

            else {
                user.setEnabled(true);
                userService.update(user);
                verficationTokenRepository.delete(token);
                verficationTokenRepository.delete(token);
                return new Response(HttpStatus.OK, "Email verified successfully! Now you can login", null);
            }
        } catch (Exception e) {
            return new Response(HttpStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }

    /******************************************************************************************************************/

    /******************************************************************************************************************/

}