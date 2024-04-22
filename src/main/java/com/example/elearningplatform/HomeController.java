package com.example.elearningplatform;

import java.util.Map;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    @GetMapping
    public String home(@AuthenticationPrincipal Map<String, Object> principal) {
        System.out.println(principal);

        return "home";
    }

    @GetMapping("/home")
    public String getMethodName(@AuthenticationPrincipal OAuth2User user) {
        System.out.println(user);
        return "mohamed";
    }

}
