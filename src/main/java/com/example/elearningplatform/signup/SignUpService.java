package com.example.elearningplatform.signup;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import javax.sql.rowset.serial.SerialException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.elearningplatform.email.EmailService;
import com.example.elearningplatform.login.oAuth2.OAuth2UserDetails;
import com.example.elearningplatform.response.Response;
import com.example.elearningplatform.security.TokenUtil;
import com.example.elearningplatform.user.address.Address;
import com.example.elearningplatform.user.address.AddressRepository;
import com.example.elearningplatform.user.role.Role;
import com.example.elearningplatform.user.role.RoleRepository;
import com.example.elearningplatform.user.user.User;
import com.example.elearningplatform.user.user.UserRepository;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SignUpService {

    private final UserRepository userRepository;
    private final TokenUtil tokenUtil;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final AddressRepository addressRepository;
    private final RoleRepository roleRepository;

    /******************************************************************************************************************/

    public Response registerUser(SignUpRequest request) throws MessagingException, IOException, SQLException {
        try {

            User user = User.builder().email(request.getEmail()).password(passwordEncoder.encode(request.getPassword()))
                    .age(request.getAge())
                    .about(request.getBio()).firstName(request.getFirstName()).lastName(request.getLastName())
                    .enabled(false)
                    .phoneNumber(request.getPhoneNumber()).registrationDate(LocalDateTime.now()).build();
            userRepository.save(user);
            Role role = roleRepository.findByRole("ROLE_USER").orElse(null);
            user.setRoles(List.of(role));
            userRepository.save(user);
            Address address = Address.builder().user(user).city(request.getCity()).country(request.getCountry())
                    .street(request.getStreet()).state(request.getState()).zipCode(request.getZipCode()).build();
            addressRepository.save(address);

            return new Response(HttpStatus.OK, null, user);

        } catch (Exception e) {

            return new Response(HttpStatus.BAD_REQUEST, "Error while sending email", e.getMessage());
        }

    }

    /******************************************************************************************************************/

    public Response verifyEmail(String token) throws SQLException, IOException {

        if (tokenUtil.isTokenExpired(token)) {
            return new Response(HttpStatus.BAD_REQUEST, "Expired token!", null);
        }

        User user = userRepository.findByEmail(tokenUtil.getUserName(token)).orElse(null);
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
                    + "/verifyEmail/" + verficationToken;

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

            return new Response(HttpStatus.BAD_REQUEST, "Error while sending email", e.getMessage());

        }
    }

    /**************************************************************************************************************/
    public User registerOuth2(OAuth2UserDetails oAuth2UserDetails) throws IOException, SerialException, SQLException {
        User user = new User();

        user.setEmail(oAuth2UserDetails.getEmail());
        user.setFirstName(oAuth2UserDetails.getFirstName());
        user.setLastName(oAuth2UserDetails.getLastName());
        user.setEnabled(true);
        Role role = roleRepository.findByRole("ROLE_USER").orElse(null);
            user.setRoles(List.of(role));
        user.setLastLogin(LocalDateTime.now());
        user.setPassword(passwordEncoder.encode("password@M.reda.49"));
        user.setRegistrationDate(LocalDateTime.now());
        // user.setProfilePicture(downloadImage(oAuth2UserDetails.getPicture()));

        return user;
    }

    /******************************************************************************************************************/
    public byte[] downloadImage(String imageUrl) throws IOException, SerialException, SQLException {
        RestTemplate restTemplate = new RestTemplate();

        // Make a request to the image URL
        ResponseEntity<byte[]> response = restTemplate.getForEntity(imageUrl, byte[].class);
        
       return response.getBody();

    }
}

/******************************************************************************************************************/
