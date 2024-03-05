package com.example.elearningplatform.resetpassword;

import com.example.elearningplatform.Response;
import com.example.elearningplatform.email.EmailService;
import com.example.elearningplatform.user.User;
import com.example.elearningplatform.user.UserService;
import com.example.elearningplatform.verficationtoken.VerficationTokenRepository;
import com.example.elearningplatform.verficationtoken.VerficationTokenService;
import com.example.elearningplatform.verficationtoken.VerificationToken;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.Map;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@AllArgsConstructor
@NoArgsConstructor
public class ResetPasswordController {

    @Autowired
    private ResetPasswordService resetPasswordService;
    @Autowired
    private VerficationTokenService verficationTokenService;
    @Autowired
    private VerficationTokenRepository verficationTokenRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private EmailService emailService;

    /***************************************************************************************************************/
    @GetMapping("/forget-password/get-email")
    public ModelAndView getEmail() {
        return new ModelAndView("resetpageenteremail");
    }

    /***************************************************************************************************************/

    @PostMapping("/forget-password/send-email")

    public Response sendEmail(@RequestBody Map<String, String> data, HttpServletRequest request)
            throws MessagingException, UnsupportedEncodingException {

        User user = userService.findByEmail(data.get("email"));
        System.out.println(user);
        if (user == null)
            return new Response(HttpStatus.BAD_REQUEST, "User not found", null);
        if (!user.isEnabled()) {
            return emailService.sendVerificationCode(user, request, verficationTokenService.create(user));
        }
        return resetPasswordService.sendResetpasswordEmail(user, request, verficationTokenService.create(user));

    }

    /***************************************************************************************************************/

    @PostMapping("/forget-password/save-password")
    public Response changePassword(@RequestBody Map<String, String> data, HttpServletRequest request)
            throws SQLException, IOException, MessagingException {

        try {
            VerificationToken token = verficationTokenRepository.findByToken(data.get("token"));
            User user = token.getUser();
            verficationTokenRepository.delete(token);
            if (user == null) {
                return new Response(HttpStatus.BAD_REQUEST, "User not found", null);
            }
            return resetPasswordService.savePassword(user, data.get("password"));
        } catch (Exception e) {
            return new Response(HttpStatus.BAD_REQUEST, e.getMessage(), null);
        }

    }

    /***************************************************************************************************************/

    @GetMapping("/check-token")
    public Object savePassword(@RequestParam("token") String token, Model model) throws SQLException, IOException {
        VerificationToken token1 = verficationTokenRepository.findByToken(token);

        if (token1 == null) {
            return new Response(HttpStatus.BAD_REQUEST, "invalid token", null);
        }
        if (!verficationTokenService.checkTokenValidation(token1)) {
            verficationTokenRepository.delete(token1);
            return new Response(HttpStatus.BAD_REQUEST, "invalid token", null);
        }
        // User user=token1.getUser();
        // model.addAttribute("token", token);
        // verficationTokenRepo.delete(token1);
        // ModelAndView mv = new ModelAndView("changepassword", "model", model);
        // System.out.println(mv);
        // return mv;
        return new Response(HttpStatus.OK, "valid token", token1.getToken());
    }
    /***************************************************************************************************************/

}
