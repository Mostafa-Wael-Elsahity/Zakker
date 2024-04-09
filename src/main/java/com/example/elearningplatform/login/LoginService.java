package com.example.elearningplatform.login;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import com.example.elearningplatform.Response;
import com.example.elearningplatform.security.TokenUtil;
import com.example.elearningplatform.user.User;
import com.example.elearningplatform.user.UserRepository;
import com.example.elearningplatform.verficationtoken.VerficationTokenService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoginService {
    private final VerficationTokenService verficationTokenService;
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
            return new Response(HttpStatus.BAD_REQUEST, "Wrong Password!",
                    List.of(passwordEncoder.encode(loginRequest.getPassword()),
                            user.getPassword()));
        }

        if (!user.isEnabled()) {
            String verficationToken = verficationTokenService.generateToken(loginRequest.getEmail());
            return verficationTokenService.sendRegistrationVerificationCode(loginRequest.getEmail(), request,
                    verficationToken);

        }

        // final Authentication authentication = authenticationManager.authenticate(
        // new UsernamePasswordAuthenticationToken(loginRequest.get("email"),
        // loginRequest.get("password")));

        // SecurityContextHolder.getContext().setAuthentication(authentication);

        // System.out.println(loginRequest);
        // System.out.println(user);
        String token = tokenUtil.generateToken(user);
        return new Response(HttpStatus.OK, "Success!", token);
    }
}
