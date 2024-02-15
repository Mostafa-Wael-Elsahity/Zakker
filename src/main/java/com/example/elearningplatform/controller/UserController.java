package com.example.elearningplatform.controller;

import java.awt.*;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Blob;
import java.sql.SQLException;

import com.fasterxml.jackson.core.JsonToken;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.elearningplatform.dto.RegistrationRequest;
import com.example.elearningplatform.entity.user.User;

import com.example.elearningplatform.service.UserService;

import jakarta.mail.MessagingException;


@RestController
public class UserController {

    @Autowired
    private UserService userService;

    /******************************************************************************************************************/
    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> registerUser(@ModelAttribute RegistrationRequest registrationRequest,
                                          final HttpServletRequest request) throws MessagingException, IOException, SQLException {
        User user = userService.registerUser(registrationRequest);
        String verficationToken = userService.createVerficationToken(user);
        System.out.println(verficationToken+" controller");
        String url = applicationUrl(request) + "/register/verifyEmail?token=" + verficationToken;
        System.out.println("url : " + url);
        userService.sendEmail(user, url);
        return new ResponseEntity<>("Success!  Please, check your email for to complete your registration",
                HttpStatus.OK);
    }

    public String applicationUrl(final HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }

    /******************************************************************************************************************/

    @RequestMapping(value = "/register/verifyEmail", method = RequestMethod.GET)
    public ResponseEntity<?> verifyEmail(@RequestParam("token") String verficationToken) {
        System.out.println(verficationToken + " verifyemail1");
        return userService.verifyEmail(verficationToken);
    }

}