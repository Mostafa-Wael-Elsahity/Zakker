package com.example.elearningplatform.user;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.example.elearningplatform.Response;

import lombok.RequiredArgsConstructor;

@RestController

@RequiredArgsConstructor

public class UserController {

    private final UserRepository userRepository;

    /**************************************************************************************************************/

    @GetMapping("/user/get-all-users")

    public ModelAndView getAllUsers(@RequestHeader Map<String, String> headers) throws SQLException {
        System.out.println(headers);
        ModelAndView mv = new ModelAndView("showAllUsers");
        List<User> users = userRepository.findAll();

        mv.addObject("users", users);

        return mv;

    }

    @GetMapping("/user/get-user-by-email/{email}")
    // @GetMapping("/get-user-by-email")
    public Response getUserById(@PathVariable("email") String email) {

        return new Response(HttpStatus.OK, "success", null);

    }

    @GetMapping("/user/get-current-user")
    // @GetMapping("/get-user-by-email")
    public Response getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // System.out.println(authentication.getName());
        return new Response(HttpStatus.OK, "success", authentication.getName());
    }

    @SuppressWarnings("null")
    @GetMapping("/user/display")
    public ResponseEntity<String> displayImage(@RequestParam("email") String email) throws IOException,
            SQLException {
        // System.out.println("test: " + email);

        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        byte[] imageBytes = null;
        if (user.getProfilePicture() == null) {
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body("");
        }
        imageBytes = user.getProfilePicture().getBytes(1, (int) user.getProfilePicture().length());
        String image = new String(imageBytes);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);
    }

}
