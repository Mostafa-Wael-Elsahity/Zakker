package com.example.elearningplatform.email;

import com.example.elearningplatform.Response;
import com.example.elearningplatform.user.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
@NoArgsConstructor
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    /******************************************************************************************************************/

    @SuppressWarnings("null")
    public void sendEmail(User user, String content, String subject, String senderName)
            throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom("mohammedzahw49@outlook.com", senderName);
        helper.setTo(user.getEmail());
        helper.setSubject(subject);
        helper.setText(content, true);
        mailSender.send(message);
    }

    /**************************************************************************************************************/
    public Response sendResetpasswordEmail(User user, HttpServletRequest request, String verficationToken) {

        try {
            String url = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath()
                    + "/check-token?token=" + verficationToken;
            System.out.println("url : " + url);
            String subject = "Reset Password Verification";
            String senderName = "User Registration Portal Service";
            String content = "<p> Hi, " + user.getFirstName() + ", </p>" +
                    "<p>Thank you for registering with us," + "" +
                    "Please, follow the link below to complete your registration.</p>" +
                    "<a href=\"" + url + "\">Reset password</a>" +
                    "<p> Thank you <br> Reset Password Portal Service";
            sendEmail(user, content, subject, senderName);

            return new Response(HttpStatus.OK, "Please, check your email to reset your password", null);
        } catch (Exception e) {

            return new Response(HttpStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }

    /*********************************************************************************************************************/

    public Response sendVerificationCode(User user, HttpServletRequest request, String verficationToken) {

        try {
            String url = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath()
                    + "/verifyEmail?token=" + verficationToken;

            System.out.println(user);
            System.out.println("url : " + url);
            String subject = "Email Verification";
            String senderName = "User Registration Portal Service";
            String content = "<p> Hi, " + user.getFirstName() + ", </p>" +
                    "<p>Thank you for registering with us," + "" +
                    "Please, follow the link below to complete your registration.</p>" +
                    "<a href=\"" + url + "\">Verify your email to activate your account</a>" +
                    "<p> Thank you <br> Users Registration Portal Service";
            sendEmail(user, content, subject, senderName);

            return new Response(HttpStatus.OK, "Email is not verfied ,please check your email to verfy it!", null);
        } catch (Exception e) {

            return new Response(HttpStatus.BAD_REQUEST, "Error while sending email", null);

        }
    }
}
