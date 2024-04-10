package com.example.elearningplatform.signup;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import javax.sql.rowset.serial.SerialException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.elearningplatform.Response;
import com.example.elearningplatform.email.EmailService;
import com.example.elearningplatform.login.oAuth2.OAuth2UserDetails;
import com.example.elearningplatform.security.TokenUtil;
import com.example.elearningplatform.user.Role;
import com.example.elearningplatform.user.User;
import com.example.elearningplatform.user.UserRepository;
import com.example.elearningplatform.user.UserService;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SignUpService {

    private final UserRepository userRepository;
    private final UserService userService;
    private final TokenUtil tokenUtil;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

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

        if (tokenUtil.isTokenExpired(token)) {
            return new Response(HttpStatus.BAD_REQUEST, "Expired token!", null);
        }

        User user = userRepository.findByEmail(tokenUtil.getUserNameFromToken(token)).orElse(null);
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

    /******************************************************************************************************************/
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

    /**************************************************************************************************************/
    public User registerOuth2(OAuth2UserDetails oAuth2UserDetails) throws IOException, SerialException, SQLException {
        User user = new User();

        user.setEmail(oAuth2UserDetails.getEmail());
        user.setFirstName(oAuth2UserDetails.getFirstName());
        user.setLastName(oAuth2UserDetails.getLastName());
        user.setEnabled(true);
        user.setRoles(List.of(Role.ROLE_USER));
        user.setLastLogin(LocalDateTime.now());
        user.setPassword(passwordEncoder.encode("123456"));
        user.setRegistrationDate(LocalDateTime.now());
        user.setProfilePicture(downloadImage(oAuth2UserDetails.getPicture()));

        return user;
    }

    /******************************************************************************************************************/
    public Blob downloadImage(String imageUrl) throws IOException, SerialException, SQLException {
        RestTemplate restTemplate = new RestTemplate();

        // Make a request to the image URL
        ResponseEntity<byte[]> response = restTemplate.getForEntity(imageUrl, byte[].class);
        if (response.getStatusCode().is2xxSuccessful()) {

            Blob blob = new javax.sql.rowset.serial.SerialBlob(response.getBody());

            return blob;
        } else {
            return null;
        }

    }
}

/******************************************************************************************************************/
