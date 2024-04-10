package com.example.elearningplatform.login;

import java.io.IOException;
import java.sql.SQLException;

import javax.sql.rowset.serial.SerialException;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.example.elearningplatform.Response;
import com.example.elearningplatform.validator.Validator;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/login")
public class LoginController implements ErrorController {

    private final LoginService loginService;

    /*****************************************************************************************************************/
    @GetMapping
    public ModelAndView login() {
        return new ModelAndView("login");
    }

    /***************************************************************************************************************/

    @PostMapping("/custom")
    public Response loginCustom(@RequestBody @Valid LoginRequest loginRequest, BindingResult result,
            HttpServletRequest request)
            throws MessagingException, SQLException, IOException {

        if (result.hasErrors()) {

            return Validator.validate(result);
        }

        return loginService.verifyLogin(loginRequest, request);
    }

    /*****************************************************************************************************************/

    @GetMapping("/outh2")
    public Response loginOuth2(@AuthenticationPrincipal OAuth2User oAuth2User)
            throws SerialException, IOException, SQLException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try {
            if (authentication instanceof OAuth2AuthenticationToken) {

                OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;

                String registrationId = oauthToken.getAuthorizedClientRegistrationId();

                return loginService.loginOuth2(oAuth2User.getAttributes(), registrationId);

            } else {

                return new Response(HttpStatus.UNAUTHORIZED, "User is not authenticated with OAuth2", null);
            }
        } catch (Exception e) {
            return new Response(HttpStatus.BAD_REQUEST, "login failed ! : " + e.getMessage(), null);
        }
    }
}

/*****************************************************************************************************************/
