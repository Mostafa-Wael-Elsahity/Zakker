package com.example.elearningplatform.login;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.example.elearningplatform.Response;
import com.example.elearningplatform.email.EmailService;
import com.example.elearningplatform.security.TokenUtil;
import com.example.elearningplatform.user.User;
import com.example.elearningplatform.user.UserService;
import com.example.elearningplatform.verficationtoken.VerficationTokenService;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@RestController
@AllArgsConstructor
@NoArgsConstructor
@RequestMapping("/login")
public class LoginController {
    @Autowired
    private VerficationTokenService verficationTokenService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenUtil tokenUtil;

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

        User user = userService.findByEmail(loginRequest.get("email"));
        // System.out.println(user);
        if (user == null) {
            return new Response(HttpStatus.NOT_FOUND, "User not found!", null);
        }

        // System.out.println("savedPassword : " + savedPassword);
        // System.out.println("loginPassword : " + loginPassword);

        if (!passwordEncoder.matches(loginRequest.get("password"), user.getPassword())) {
            return new Response(HttpStatus.BAD_REQUEST, "Wrong Password!",
                    List.of(passwordEncoder.encode(loginRequest.get("password")),
                            user.getPassword()));
        }

        if (!user.isEnabled()) {
            String verficationToken = verficationTokenService.create(user);
            return emailService.sendVerificationCode(user, request, verficationToken);

        }

        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.get("email"), loginRequest.get("password")));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // System.out.println(loginRequest);
        // System.out.println(user);
        String token = tokenUtil.generateToken(user);
        return new Response(HttpStatus.OK, "Success!", token);
    }

    @GetMapping("/home")
    public ModelAndView home() {
        return new ModelAndView("home");
    }
    /*****************************************************************************************************************/

}
