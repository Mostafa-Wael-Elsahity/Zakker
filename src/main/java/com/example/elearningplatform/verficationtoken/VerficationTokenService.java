package com.example.elearningplatform.verficationtoken;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.elearningplatform.Response;
import com.example.elearningplatform.email.EmailService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VerficationTokenService {

    private final EmailService emailService;

    /*******************************************************************************************************************/
    public String generateToken(String email) throws SQLException, IOException {
        // VerificationToken token = verficationTokenRepository.findByUser(user);
        // if (token != null) {
        // verficationTokenRepository.delete(token);
        // }
        String token = LocalDateTime.now() + "," + UUID.randomUUID().toString() + "," + email + ","
                + UUID.randomUUID().toString();
        return token;
    }

    /*******************************************************************************************************************/

    public Response sendRegistrationVerificationCode(String email, HttpServletRequest request,
            String verficationToken) {

        try {
            String url = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath()
                    + "/verifyEmail?token=" + verficationToken;

            System.out.println("url : " + url);
            String subject = "Email Verification";
            String senderName = "User Registration Portal Service";
            String content = "<p> Hi, " + email + ", </p>" +
                    "<p>Thank you for registering with us," + "" +
                    "Please, follow the link below to complete your registration.</p>" +
                    "<a href=\"" + url + "\">Verify your email to activate your account</a>" +
                    "<p> Thank you <br> Users Registration Portal Service";
            emailService.sendEmail(email, content, subject, senderName);

            return new Response(HttpStatus.OK, "Email is not verfied ,please check your email to verfy it!", null);
        } catch (Exception e) {

            return new Response(HttpStatus.BAD_REQUEST, "Error while sending email", null);

        }
    }

    /*******************************************************************************************************************/

    public boolean checkTokenValidation(String token) throws SQLException, IOException {
        // DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd
        // HH:mm:ss");
        // System.out.println(token);
        LocalDateTime tokenCreationTime = LocalDateTime.parse(token.split(",")[0]);
        System.out.println(tokenCreationTime);
        if (LocalDateTime.now().isBefore(tokenCreationTime.plus(20, ChronoUnit.MINUTES))) {
            return true;
        }
        return false;
    }
}
