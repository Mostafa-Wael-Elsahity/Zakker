package com.example.elearningplatform.user.wishlist;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.elearningplatform.response.Response;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/user")
@SecurityRequirement(name = "bearerAuth")
public class WishListController {
    @Autowired
    private WishListService userService;

    /*****************************************************************************************************/
    @GetMapping("/get-wishlist")
    public Response getWishlist() {
        return userService.getWishlist();
    }

    /****************************************************************************************************/

    @PostMapping("/add-to-wishlist")
    public Response addCourseToWishlist(@RequestParam("courseId") Integer courseId)  {

        return userService.addToWishlist(courseId);
    }

    /****************************************************************************************************/
    @DeleteMapping("/delete-from-wishlist")
    public Response deleteFromWishlist(@RequestParam("courseId") Integer courseId)  {

        return userService.deleteFromoWishlist(courseId);
    }
}
