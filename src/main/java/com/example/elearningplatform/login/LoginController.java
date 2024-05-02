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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import com.example.elearningplatform.exception.CustomException;
import com.example.elearningplatform.response.Response;
import com.example.elearningplatform.validator.Validator;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor

public class LoginController implements ErrorController {

    private final LoginService loginService;


    /*****************************************************************************************************************/
    // @GetMapping("/")
    // public Response login() {
    // return new Response( HttpStatus.OK, "ok", null);
    // }

    /***************************************************************************************************************/
    @PostMapping("/login/custom")
    public Response loginCustom(@RequestBody @Valid LoginRequest loginRequest, BindingResult result,
            HttpServletRequest request)
            throws MessagingException, SQLException, IOException {

        if (result.hasErrors()) {

            return Validator.validate(result);
        }

        return loginService.verifyLogin(loginRequest, request);
    }

    /*****************************************************************************************************************/
    @GetMapping("/login/google")
    public RedirectView loginWithGoogle() {
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl("http://localhost:8080/oauth2/authorization/google");

        return redirectView;
    }

    /*****************************************************************************************************************/
    /*****************************************************************************************************************/
    @GetMapping("/login/github")
    public RedirectView loginWithGithub() {

        RedirectView redirectView = new RedirectView();
        redirectView.setUrl("http://localhost:8080/oauth2/authorization/github");
        return redirectView;
       
    }

    /*****************************************************************************************************************/

    @GetMapping("/login/oauth2/success")
    public Response loginOuth2(@AuthenticationPrincipal OAuth2User oAuth2User)
            throws SerialException, IOException, SQLException {
        System.out.println("mohamed");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // System.out.println(authentication);
        try {
            if (authentication instanceof OAuth2AuthenticationToken) {

                OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;

                String registrationId = oauthToken.getAuthorizedClientRegistrationId();

                return loginService.loginOuth2(oAuth2User.getAttributes(), registrationId);

            } else {

                throw new CustomException("User is not authenticated with OAuth2", HttpStatus.UNAUTHORIZED);
            }
        } catch (CustomException e) {
            return new Response(e.getStatus(), e.getMessage(), null);
        } catch (Exception e) {
            return new Response(HttpStatus.BAD_REQUEST, "login failed ! : " + e.getMessage(), null);
        }
    }

    // @GetMapping("/login/oauth2/code/{provider}")
    // public String loginSuccess(@PathVariable String provider,
    // OAuth2AuthenticationToken authenticationToken) {

    // OAuth2AuthorizedClient client = clientService.loadAuthorizedClient(
    // authenticationToken.getAuthorizedClientRegistrationId(),
    // authenticationToken.getName());
    // System.out.println(client);

    // return "/login-success";
    // }

    // @GetMapping("/login-error")
    // public String loginSuccess() {

    // return "error";
    // }
}


/*****************************************************************************************************************/
