package com.example.elearningplatform.login;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.apache.tomcat.util.net.openssl.ciphers.Encryption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.example.elearningplatform.Response;
import com.example.elearningplatform.email.EmailService;

import com.example.elearningplatform.user.User;
import com.example.elearningplatform.user.UserRepository;
import com.example.elearningplatform.verficationtoken.VerficationTokenService;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@AllArgsConstructor
@NoArgsConstructor
@RequestMapping("/login")
public class LoginController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private VerficationTokenService verficationTokenService;
    @Autowired
    private EmailService emailService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    /*****************************************************************************************************************/

    @GetMapping("/get-login")
    public ModelAndView login(Model model, HttpServletRequest request) {
       
        model.addAttribute("loginRequest", new LoginRequest());
        ModelAndView mv = new ModelAndView("login", "loginRequest", model);
        return mv;
    }

    /*****************************************************************************************************************/

    @PostMapping("/post-login")
    public Response login(@RequestBody Map<String, String> loginRequest, HttpServletRequest request)
            throws MessagingException, UnsupportedEncodingException {
        System.out.println(loginRequest);
        User user = userRepository.findByEmail(loginRequest.get("email"));
        if (user == null) {
            return new Response(HttpStatus.NOT_FOUND, "User not found!", null);
        }

        String savedPassword = user.getPassword();
        String loginPassword = passwordEncoder.encode(loginRequest.get("password"));
        // String loginPassword =
        // encryption.encryptPassword(loginRequest.get("password"));
        // System.out.println(user);
        System.out.println("savedPassword : " + savedPassword);
        System.out.println("loginPassword : " + loginPassword);

        if (!passwordEncoder.matches(loginRequest.get("password"), savedPassword)) {
            return new Response(HttpStatus.BAD_REQUEST, "Wrong Password!",
                    List.of(passwordEncoder.encode(loginRequest.get("password")),
                            user.getPassword()));
        }

        // if (!encryption.verifyPassword(savedPassword, loginPassword)) {
        //
        // return new Response(HttpStatus.BAD_REQUEST, "Wrong Password!",
        // List.of(encryption.encryptPassword(loginRequest.get("password")),
        // user.getPassword()));
        // }
        if (!user.isEnabled()) {
            String verficationToken = verficationTokenService.create(user);
            return emailService.sendVerificationCode(user, request, verficationToken);

        }
        System.out.println(loginRequest);

        return new Response(HttpStatus.OK, "\"Success!\"", user);
    }

    @GetMapping("/home")
    public ModelAndView home() {
        return new ModelAndView("home");
    }
    /*****************************************************************************************************************/

}
