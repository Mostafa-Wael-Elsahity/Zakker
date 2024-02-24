package com.example.elearningplatform.signup;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.elearningplatform.Response;
import com.example.elearningplatform.address.Address;
import com.example.elearningplatform.address.AddressRepository;

import com.example.elearningplatform.user.User;
import com.example.elearningplatform.user.UserRepository;
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
    @Autowired  private UserRepository userRepository;
    @Autowired private VerficationTokenRepository verficationTokenRepository;
    @Autowired private VerficationTokenService verficationTokenService;
    @Autowired private BCryptPasswordEncoder passwordEncoder;
    @Autowired private AddressRepository addressRepository;

    /******************************************************************************************************************/

    // public Response sendEmail(User user, HttpServletRequest request, String
    // verficationToken)
    // throws MessagingException, UnsupportedEncodingException {
    //
    // try {
    // String url = "http://" + request.getServerName() + ":" +
    // request.getServerPort() + request.getContextPath()
    // + "/register/verifyEmail?token=" + verficationToken;
    //
    // System.out.println("url : " + url);
    // String subject = "Email Verification";
    // String senderName = "User Registration Portal Service";
    // String content = "<p> Hi, " + user.getFirstName() + ", </p>" +
    // "<p>Thank you for registering with us," + "" +
    // "Please, follow the link below to complete your registration.</p>" +
    // "<a href=\"" + url + "\">Verify your email to activate your account</a>" +
    // "<p> Thank you <br> Users Registration Portal Service";
    // emailService.sendEmail(user, content, subject, senderName);
    //
    // return new Response(HttpStatus.OK, "\"Success! Please, check your email to
    // Verify your account\"", null);
    // } catch (Exception e) {
    //
    // return new Response(HttpStatus.BAD_REQUEST, e.getMessage(), null);
    // }
    // }

    /******************************************************************************************************************/

    public Response registerUser(SignUpRequest request) throws MessagingException, IOException, SQLException {
        try {

            User user = convertRequestToUser(request);

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
                userRepository.save(user);
                verficationTokenRepository.delete(token);
                System.out.println(user);
                verficationTokenRepository.delete(token);
                return new Response(HttpStatus.OK, "Email verified successfully! Now you can login", null);
            }
        } catch (Exception e) {
            return new Response(HttpStatus.BAD_REQUEST, " Couldn't verify email", null);
        }
    }

    /******************************************************************************************************************/

    public User convertRequestToUser(SignUpRequest request) throws IOException, SQLException {

        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRegistrationDate(LocalDateTime.now());
        user.setAge(request.getAge());
        user.setBio(request.getBio());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setRole("ROLE_USER");

        if (request.getProfilePicture() == null) {
            user.setProfilePicture(null);
        } else {
            byte[] bytes = request.getProfilePicture().getBytes();
            Blob blob = new javax.sql.rowset.serial.SerialBlob(bytes);
            user.setProfilePicture(blob);
        }

        userRepository.save(user);
        // System.out.println("dvdfg");
        Address address = new Address();
        address.setUser(user);
        address.setCity(request.getCity());
        address.setCountry(request.getCountry());
        address.setStreet(request.getStreet());
        address.setState(request.getState());
        address.setZipCode(request.getZipCode());
        addressRepository.save(address);
        return user;
    }

    /******************************************************************************************************************/

}