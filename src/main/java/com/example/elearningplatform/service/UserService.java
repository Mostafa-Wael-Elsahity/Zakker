package com.example.elearningplatform.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import com.example.elearningplatform.entity.user.Address;
import com.example.elearningplatform.repository.AddressRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.datasource.AbstractDriverBasedDataSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.example.elearningplatform.dto.RegistrationRequest;
import com.example.elearningplatform.entity.user.User;
import com.example.elearningplatform.entity.user.VerificationToken;
import com.example.elearningplatform.exception.UserAlreadyExistException;
import com.example.elearningplatform.repository.TokenRepo;
import com.example.elearningplatform.repository.UserRepo;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private AddressRepo addressRepo;

    @Autowired
    private TokenRepo tokenRepo;

    @Autowired
    private JavaMailSender mailSender;

    /******************************************************************************************************************/
    @SuppressWarnings("null")
    public void sendEmail(User user, String url)
            throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        String subject = "Email Verification";
        String senderName = "User Registration Portal Service";
        String content = "<p> Hi, " + user.getFirstName() + ", </p>" +
                "<p>Thank you for registering with us," + "" +
                "Please, follow the link below to complete your registration.</p>" +
                "<a href=\"" + url + "\">Verify your email to activate your account</a>" +
                "<p> Thank you <br> Users Registration Portal Service";
        System.out.println("url : " + url+" sendemail");
        helper.setFrom("mohammedzahw49@outlook.com", senderName);
        helper.setTo(user.getEmail());
        helper.setSubject(subject);
        helper.setText(content, true);
        mailSender.send(message);
    }

    /******************************************************************************************************************/

    public String createVerficationToken(User user) {
        VerificationToken verificationToken = new VerificationToken(user);
        tokenRepo.save(verificationToken);
        return verificationToken.getToken();
    }

    /******************************************************************************************************************/

    @SuppressWarnings("null")
    public User registerUser(RegistrationRequest request) throws MessagingException, IOException, SQLException {

        if (userRepo.findByEmail(request.getEmail()) != null) {
             throw new UserAlreadyExistException( "Error: Email: " + request.getEmail() + " is already in use!");
        }
        
        User user = request.toUser();
        user.setRegistrationDate(LocalDateTime.now());
        user=userRepo.save(user);
        Address address = new Address();
        address.setUser(user);
        address.setCity(request.getCity());
        address.setCountry(request.getCountry());
        address.setStreet(request.getStreet());
        address.setState(request.getState());
        address.setZipCode(request.getZipCode());
        addressRepo.save(address);

        return  user;
    }

    /******************************************************************************************************************/

    @SuppressWarnings("null")
    public ResponseEntity<?> verifyEmail(String verficationToken) {
        VerificationToken token = tokenRepo.findByToken(verficationToken);

        if (token.getUser().isEnabled()) {
            return new ResponseEntity<>("Email is Already verified!", HttpStatus.BAD_REQUEST);
        } else if (token != null && LocalDateTime.now().isBefore(token.getExpiryDate().plus(1, ChronoUnit.MINUTES))) {
            User user = userRepo.findByEmail(token.getUser().getEmail());
            user.setEnabled(true);
            userRepo.save(user);
            tokenRepo.delete(token);
            return new ResponseEntity<>("Email verified successfully! Now you can login", HttpStatus.OK);
        }
        tokenRepo.delete(token);
        userRepo.delete(token.getUser());
        return new ResponseEntity<>("Error: Couldn't verify email", HttpStatus.BAD_REQUEST);
    }

    /******************************************************************************************************************/
}