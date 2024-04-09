package com.example.elearningplatform.verficationtoken;

import java.io.IOException;
import java.sql.SQLException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.elearningplatform.Response;

import ch.qos.logback.core.model.Model;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class VerificationTokenController {
    private final VerficationTokenService verficationTokenService;

    @GetMapping("/check-token")
    public Object savePassword(@RequestParam("token") String token, Model model) throws SQLException, IOException {

        if (!verficationTokenService.checkTokenValidation(token)) {
            return new Response(HttpStatus.BAD_REQUEST, "invalid token", null);
        }
        return new Response(HttpStatus.OK, "valid token", token.split(",")[2]);
    }
}
