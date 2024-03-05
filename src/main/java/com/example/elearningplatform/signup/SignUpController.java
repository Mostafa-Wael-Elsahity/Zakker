package com.example.elearningplatform.signup;

import java.io.IOException;
import java.sql.SQLException;
import jakarta.mail.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.example.elearningplatform.Response;
import com.example.elearningplatform.email.EmailService;
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
public class SignUpController {

    @Autowired
    private SignUpService signUpService;

    @Autowired
    private VerficationTokenService verficationTokenService;

    @Autowired
    private EmailService emailService;
    @Autowired
    private UserService userService;

    /******************************************************************************************************************/

    @GetMapping("/signup/get-signup")
    public ModelAndView signup() {
        // model.addAttribute("registrationRequest", new RegistrationRequest());
        ModelAndView mv = new ModelAndView("signup");
        return mv;
    }

    /******************************************************************************************************************/

    //
    @PostMapping(value = "/signup/post-signup", consumes = { "multipart/form-data" })

    public Response signUp(SignUpRequest signUpRequest,
            HttpServletRequest request) throws MessagingException, IOException, SQLException {
        // return new Response(HttpStatus.OK, "ok", signUpRequest.getEmail());

        User user = userService.findByEmail(signUpRequest.getEmail());

        if (user != null) {
            return new Response(HttpStatus.BAD_REQUEST, "Email already exists , Please login", null);

        }

        Response response = signUpService.registerUser(signUpRequest);

        if (response.getStatus() != HttpStatus.OK) {
            return response;
        }
        user = (User) response.getData();

        String verficationToken = verficationTokenService.create(user);

        response = emailService.sendVerificationCode(user, request,
                verficationToken);
        if (response.getStatus() != HttpStatus.OK) {
            return response;
        }
        response.setMessage("Registration successful, please check your email to verify your account");
        return response;

    }

    /******************************************************************************************************************/

    @GetMapping("/verifyEmail")

    public Response verifyEmail(@RequestParam("token") String verficationToken) throws SQLException, IOException {

        // System.out.println(verficationToken + " verifyemail1");

        return signUpService.verifyEmail(verficationToken);
    }

}