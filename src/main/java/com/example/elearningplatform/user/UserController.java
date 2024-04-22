package com.example.elearningplatform.user;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.example.elearningplatform.response.Response;
import com.example.elearningplatform.security.TokenUtil;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController

@RequiredArgsConstructor

public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final TokenUtil tokenUtil;
    @Autowired  HttpServletRequest  request;

    /**************************************************************************************************************/

    @GetMapping("/user/get-all-users")

    public ModelAndView getAllUsers() throws SQLException {

        ModelAndView mv = new ModelAndView("showAllUsers");
        List<User> users = userRepository.findAll();
        mv.addObject("users", users);
        return mv;

    }

    /**************************************************************************************************************/

    @GetMapping("/user/get-user-by-email/{email}")
    public Response getUserById(@PathVariable("email") String email) {

        return new Response(HttpStatus.OK, "success", null);

    }

    /**************************************************************************************************************/

    @GetMapping("/user/get-current-user")
    // public Response getUser(@RequestHeader Map<String, String> headers) {
    public Response getUser(@RequestHeader Map<String, String> headers) {
  
        return new Response(HttpStatus.OK, "success",tokenUtil.getUserId());
    }

    /**************************************************************************************************************/
    @GetMapping("/user/display-image/{id}")
    public ResponseEntity<byte[]> displayImage(@PathVariable("id") int id) throws IOException,
            SQLException {
        // Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // User user = userRepository.findByEmail(authentication.getName()).orElse(null);
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(user.getProfilePicture());
    }

    @GetMapping("/user/search/{key}/{PageNumber}")
    public ResponseEntity<?> search(@PathVariable("key") String key,@PathVariable("PageNumber") int pageNumber) {

        List<UserDto>instructors = userService.findBySearchKey(key,pageNumber);
      
        return ResponseEntity.ok(instructors);
    }
/**************************************************************************************************************/
}
