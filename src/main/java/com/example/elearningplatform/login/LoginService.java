package com.example.elearningplatform.login;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Map;

import javax.sql.rowset.serial.SerialException;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.elearningplatform.exception.CustomException;
import com.example.elearningplatform.login.oAuth2.OAuth2UserDetails;
import com.example.elearningplatform.login.oAuth2.OAuth2UserGitHub;
import com.example.elearningplatform.login.oAuth2.OAuth2UserGoogle;
import com.example.elearningplatform.response.Response;
import com.example.elearningplatform.security.TokenUtil;
import com.example.elearningplatform.signup.SignUpService;
import com.example.elearningplatform.user.user.User;
import com.example.elearningplatform.user.user.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoginService {
    private final SignUpService signUpService;
    private final PasswordEncoder passwordEncoder;
    private final TokenUtil tokenUtil;
    private final UserRepository userRepository;

    /***************************************************************************************************************/
    public Response verifyLogin(LoginRequest loginRequest, HttpServletRequest request)
            throws SQLException, IOException {
        
        try{       
         User user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(() -> new IllegalArgumentException("User not found!"));
      
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new CustomException("Wrong Password!", HttpStatus.BAD_REQUEST);
        }
        if (!user.isEnabled()) {
            String token = tokenUtil.generateToken(loginRequest.getEmail(),1000, 1000L);
            return signUpService.sendRegistrationVerificationCode(loginRequest.getEmail(), request,
                    token);

        }

        String token = tokenUtil.generateToken(loginRequest.getEmail(),user.getId(), 3000000L);
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);
        return new Response(HttpStatus.OK, "Success!", token);
    } catch (Exception e) {
        return new Response(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", e.getMessage());
    }
    }

    /****************************************************************************************************************/

    public Response loginOuth2(Map<String, Object> principal, String registrationId)
            throws IOException, SerialException, SQLException {
        OAuth2UserDetails oAuth2UserDetails;
        try {
            if (registrationId.equals("google")) {
                oAuth2UserDetails = new OAuth2UserGoogle(principal);
            } else if (registrationId.equals("github")) {
                oAuth2UserDetails = new OAuth2UserGitHub(principal);
            } else {
                throw new CustomException("Provider not supported!", HttpStatus.BAD_REQUEST);
            }

            User user = userRepository.findByEmail(oAuth2UserDetails.getName()).orElse(null);
            if (user == null) {
                user = signUpService.registerOuth2(oAuth2UserDetails);
            }
            user.setEnabled(true);
            user.setLastLogin(LocalDateTime.now());
            userRepository.save(user);
            String token = tokenUtil.generateToken(user.getEmail(), user.getId(), 3000000L);

            return new Response(HttpStatus.OK, "Success!", token);
        } catch (CustomException e) {
            return new Response(e.getStatus(), e.getMessage(), null);
        } catch (Exception e) {
            return new Response(HttpStatus.BAD_REQUEST, "Provider not supported!", null);
        }
    }
}
