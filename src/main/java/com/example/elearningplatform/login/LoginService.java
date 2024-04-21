package com.example.elearningplatform.login;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;
import java.time.LocalDateTime;
import javax.sql.rowset.serial.SerialException;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.elearningplatform.login.oAuth2.OAuth2UserDetails;
import com.example.elearningplatform.login.oAuth2.OAuth2UserGitHub;
import com.example.elearningplatform.login.oAuth2.OAuth2UserGoogle;
import com.example.elearningplatform.response.Response;
import com.example.elearningplatform.security.TokenUtil;
import com.example.elearningplatform.signup.SignUpService;
import com.example.elearningplatform.user.User;
import com.example.elearningplatform.user.UserRepository;

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
        User user = userRepository.findByEmail(loginRequest.getEmail()).orElse(null);
        if (user == null) {
            return new Response(HttpStatus.NOT_FOUND, "User not found!", null);
        }
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            return new Response(HttpStatus.BAD_REQUEST, "Wrong Password!", null);
        }
        if (!user.isEnabled()) {
            String token = tokenUtil.generateToken(loginRequest.getEmail(),1000, 1000L);
            return signUpService.sendRegistrationVerificationCode(loginRequest.getEmail(), request,
                    token);

        }

        // final Authentication authentication = authenticationManager.authenticate(
        // new UsernamePasswordAuthenticationToken(loginRequest.get("email"),
        // loginRequest.get("password")));
        // SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = tokenUtil.generateToken(loginRequest.getEmail(),user.getId(), 3000000L);
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);
        return new Response(HttpStatus.OK, "Success!", token);
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
                return new Response(HttpStatus.BAD_REQUEST, "Provider not supported!", null);
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
        } catch (Exception e) {
            return new Response(HttpStatus.BAD_REQUEST, "Provider not supported!", null);
        }
    }
}
