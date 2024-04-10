package com.example.elearningplatform.resetpassword;

import java.io.IOException;
import java.sql.SQLException;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.elearningplatform.Response;
import com.example.elearningplatform.email.EmailService;
import com.example.elearningplatform.security.TokenUtil;
import com.example.elearningplatform.user.User;
import com.example.elearningplatform.user.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ResetPasswordService {

    private final TokenUtil tokenUtil;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    /******************************************************************************************************************/

    public String resetPassword(String email) throws MessagingException, IOException, SQLException {

        String token = tokenUtil.generateToken(email, 1000L);
        return token;
    }

    /********************************************************************************************************************/
    public Response savePassword(String email, String password)
            throws SQLException, IOException, MessagingException {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            return new Response(HttpStatus.BAD_REQUEST, "User not found", null);
        }

        user.setPassword(passwordEncoder.encode(password)); // encoded password);
        userRepository.save(user);
        return new Response(HttpStatus.OK, "Password Changed Successfully ! , Now you can login", null);

    }

    /********************************************************************************************************************/

    public Response sendResetpasswordEmail(String email, HttpServletRequest request, String token) {

        try {
            String url = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath()
                    + "/check-token?token=" + token;
            System.out.println("url : " + url);
            String subject = "Reset Password Verification";
            String senderName = "User Registration Portal Service";
            String content = "<p> Hi, " + email + ", </p>" +
                    "<p>Thank you for registering with us," + "" +
                    "Please, follow the link below to complete your registration.</p>" +
                    "<a href=\"" + url + "\">Reset password</a>" +
                    "<p> Thank you <br> Reset Password Portal Service";
            emailService.sendEmail(email, content, subject, senderName);

            return new Response(HttpStatus.OK, "Please, check your email to reset your password", null);
        } catch (Exception e) {

            return new Response(HttpStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }
    /********************************************************************************************************************/

}
