package com.example.elearningplatform.user;

import java.io.IOException;
import java.sql.SQLException;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.example.elearningplatform.response.Response;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;


    /**************************************************************************************************************/

    @GetMapping("/get-user/{id}")

    public ModelAndView getUser(@PathVariable("id") Integer id) throws SQLException {

        ModelAndView mv = new ModelAndView("showuser");
        User user = userRepository.findById(id).orElse(null);
        mv.addObject("user", user);
        return mv;

    }
    @GetMapping("/get-all-users")

    public Response getUsers() {

        return new Response(HttpStatus.OK, "success",
                userRepository.findAll().stream().map(user -> new UserDto(user)).toList());

    }

    /**************************************************************************************************************/
    // @GetMapping("/get-wishlist")
    // public Response getWishlist() {
    //     return new Response(HttpStatus.OK, "success", userService.getWishlist());
    // }

    // /**************************************************************************************************************/
    // @GetMapping("/get-archived")
    // public Response getarchived() {
    //     return new Response(HttpStatus.OK, "success", userService.getArchived());
    // }
    /**************************************************************************************************************/
    @GetMapping("/display-image/{id}")
    public ResponseEntity<byte[]> displayImage(@PathVariable("id") int id) throws IOException,
            SQLException {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(user.getProfilePicture());
    }

    /**************************************************************************************************************/
    // @GetMapping("/search/{key}/{PageNumber}")
    // public ResponseEntity<?> search(@PathVariable("key") String key,
    // @PathVariable("PageNumber") int pageNumber) {

    // List<UserDto> instructors = userService.findBySearchKey(key, pageNumber);

    // return ResponseEntity.ok(instructors);
    // }

    /**************************************************************************************************************/
    @GetMapping("/profile")
    public Response getProfile() {

        return new Response(HttpStatus.OK, "success", userService.getProfile());
    }


    @GetMapping("/my-learning")
    public Response myLearning() {
        return new Response(HttpStatus.OK, "success", userService.getEnrolledCourses());

    }

}
