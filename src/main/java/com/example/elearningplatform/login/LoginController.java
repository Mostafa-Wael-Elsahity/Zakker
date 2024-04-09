package com.example.elearningplatform.login;

import java.io.IOException;
import java.sql.SQLException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.elearningplatform.Response;
import com.example.elearningplatform.Validator;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/login")
public class LoginController {

    private final LoginService loginService;

    /*****************************************************************************************************************/

    /***************************************************************************************************************/

    @PostMapping("/post-login")
    public Response login(@RequestBody @Valid LoginRequest loginRequest, BindingResult result,
            HttpServletRequest request)
            throws MessagingException, SQLException, IOException {

        if (result.hasErrors()) {

            return Validator.validate(result);
        }

        return loginService.verifyLogin(loginRequest, request);
    }

    /*****************************************************************************************************************/

}
