package com.example.elearningplatform.login;

import java.io.IOException;
import java.sql.SQLException;

import java.util.Map;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
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



    /***************************************************************************************************************/
    
    /**
     * Handles the custom login request.
     *
     * @param  loginRequest  the login request containing the user's credentials
     * @param  result        the result of the validation of the login request
     * @param  request       the HTTP servlet request
     * @return               the response indicating the result of the login process
     * @throws MessagingException    if there is an error sending an email
     * @throws SQLException         if there is an error with the database
     * @throws IOException           if there is an error with the input/output
     */
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
    
    /**
     * Retrieves the Google login URL and redirects the user to it.
     *
     * @param  request   the HttpServletRequest object
     * @return           a RedirectView object for the Google OAuth2 authorization URL
     */
    @GetMapping("/login/google")
    public RedirectView loginWithGoogle(HttpServletRequest request) {
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
    RedirectView redirectView = new RedirectView();
    redirectView.setUrl(baseUrl + "/oauth2/authorization/google");

        return redirectView;
    }

    /*****************************************************************************************************************/
    
    /**
     * Handles the OAuth2 login with Github.
     *
     * @param  request   the HttpServletRequest object
     * @return           a RedirectView object for the Github OAuth2 authorization URL
     */
    @GetMapping("/login/github")
    public RedirectView loginWithGithub(HttpServletRequest request) {
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
    RedirectView redirectView = new RedirectView();
        redirectView.setUrl(baseUrl + "/oauth2/authorization/github");
        return redirectView;
       
    }
    /*****************************************************************************************************************/

    /**
     * Handles the OAuth2 login success response.
     *
     * @param  oAuth2User   the OAuth2 user object containing the user's attributes
     * @return              a Response object containing the status, message, and data
     * @throws SerialException    if a serialization error occurs
     * @throws IOException        if an I/O error occurs
     * @throws SQLException       if a database error occurs
     */
    @GetMapping("/login/oauth2/success")
    public Response loginOAuth2(@AuthenticationPrincipal OAuth2User oAuth2User)
            throws IOException, SQLException {
        OAuth2AuthenticationToken authentication = (OAuth2AuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        try {
            if (authentication != null) {
                String registrationId = authentication.getAuthorizedClientRegistrationId();
                Map<String, Object> attributes = oAuth2User.getAttributes();
                return loginService.loginOuth2(attributes, registrationId);
            } else {
                throw new CustomException("User is not authenticated with OAuth2", HttpStatus.UNAUTHORIZED);
            }
        } catch (CustomException e) {
            return new Response(e.getStatus(), e.getMessage(), null);
        } catch (Exception e) {
            return new Response(HttpStatus.BAD_REQUEST, "login failed ! : " + e.getMessage(), null);
        }
    }

}


/*****************************************************************************************************************/
