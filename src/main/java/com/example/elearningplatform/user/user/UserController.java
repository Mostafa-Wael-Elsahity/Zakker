package com.example.elearningplatform.user.user;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.elearningplatform.response.Response;
import com.example.elearningplatform.user.user.dto.InstructorDto;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;


    /***************************************************************************************************/

    @GetMapping("/get-user/{id}")
    public Response getUser(@PathVariable("id") Integer id) throws SQLException {
        return userService.getUser(id);

    }

    /***********************************************************************************************/
    @GetMapping("/get-all-users")

    public Response getUsers() {

        return new Response(HttpStatus.OK, "success",
                userRepository.findAll().stream().map(user -> new InstructorDto(user)).toList());

    }

    /**********************************************************************************************/

    @GetMapping("/profile")
    public Response getProfile() {

        return new Response(HttpStatus.OK, "success", userService.getProfile());
    }

    /************************************************************************************************/

    @GetMapping("/my-learning")
    public Response myLearning() {
        return new Response(HttpStatus.OK, "success", userService.getEnrolledCourses());

    }

    /***************************************************************************************************/

    /*****************************************************************************************************/

    @GetMapping("/get-wishlist")
    public Response getWishlist() {
        return userService.getWishlist();
    }

    /*****************************************************************************************************/

    @GetMapping("/get-archived")
    public Response getarchived() {
        return userService.getArchived();
    }

    /************************************************************************************************* */
    @GetMapping("/get-cart")
    public Response getCart() throws SQLException {
        // return new Response(HttpStatus.OK, "Success",
        // cartService.getCart(headers.get("authorization")));
        return userService.getCart();
    }

    /****************************************************************************************************/

    @GetMapping("/add-to-wishlist/{courseId}")
    public Response addCourseToWishlist(@PathVariable("courseId") Integer courseId) throws SQLException {

        return userService.addToWishlist(courseId);
    }

    /****************************************************************************************************/
    @GetMapping("/add-to-archived/{courseId}")
    public Response addToArchived(@PathVariable("courseId") Integer courseId) throws SQLException {

        return userService.addToArchived(courseId);
    }

    /***************************************************************************************************/

    @GetMapping("/add-to-cart/{courseId}")
    public Response addToCart(@PathVariable("courseId") Integer courseId) throws SQLException {
        return userService.addToCart(courseId);
    }

    /***************************************************************************************************/

    @DeleteMapping("/delete-from-wishlist/{courseId}")
    public Response deleteFromWishlist(@PathVariable("courseId") Integer courseId) throws SQLException {

        return userService.deleteFromoWishlist(courseId);
    }




    /****************************************************************************************************/
    @DeleteMapping("/delete-from-archived/{courseId}")
    public Response deleteFromArchived(@PathVariable("courseId") Integer courseId) throws SQLException {

        return userService.deleteFromArchived(courseId);
    }

    /****************************************************************************************************/

    /****************************************************************************************************/

    /****************************************************************************************************/
    @DeleteMapping("/delete-from-cart/{courseId}")
    public Response deleteFromCart(@PathVariable("courseId") Integer courseId) throws SQLException {
        return userService.deleteFromCart(courseId);
    }

    /*****************************************************************************************************/


}