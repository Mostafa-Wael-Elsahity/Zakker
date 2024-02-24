package com.example.elearningplatform.user;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.elearningplatform.Response;

import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.ModelAndView;

import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RestController
@AllArgsConstructor
@NoArgsConstructor

public class UserController {
    @Autowired
    private UserRepository userRepository;

    /**************************************************************************************************************/

    @GetMapping("/user/get-all-users")

    public ModelAndView getAllUsers() throws SQLException {
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

    @GetMapping("/user/display")
    public ResponseEntity<byte[]> displayImage(@RequestParam("email") String email) throws IOException,
            SQLException {
//        System.out.println("test: " + email);

        User user = userRepository.findByEmail(email);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        byte[] imageBytes = null;
        if(user.getProfilePicture() == null) {
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imageBytes);
        }
        imageBytes = user.getProfilePicture().getBytes(1, (int) user.getProfilePicture().length());
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imageBytes);
    }


}
