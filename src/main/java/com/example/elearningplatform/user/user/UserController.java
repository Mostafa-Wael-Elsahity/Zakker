package com.example.elearningplatform.user.user;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.elearningplatform.cloudinary.CloudinaryService;
import com.example.elearningplatform.exception.CustomException;
import com.example.elearningplatform.response.Response;
import com.example.elearningplatform.security.TokenUtil;
import com.example.elearningplatform.user.user.dto.InstructorDto;
import com.example.elearningplatform.user.user.dto.UpdateProfileRequest;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    CloudinaryService cloudinaryService;

    @Autowired
    private TokenUtil tokenUtil;

    /***************************************************************************************************/

    /**
     * Retrieves a user by their ID.
     *
     * @param  id  the ID of the user to retrieve
     * @return     the Response object containing the user details
     * @throws SQLException if an error occurs while retrieving the user from the database
     */
    @GetMapping("/get-user/{id}")
    public Response getUser(@PathVariable("id") Integer id) throws SQLException {
        return userService.getUser(id);

    }

    /***********************************************************************************************/

    /**
     * Retrieves all users.
     *
     * @return          the Response object containing the list of all users
     */
    @GetMapping("/get-all-users")

    public Response getUsers() {

        return new Response(HttpStatus.OK, "success",
                userRepository.findAll().stream().map(user -> new InstructorDto(user)).toList());

    }

    /**********************************************************************************************/

    /**
     * Retrieves the profile of the authenticated user.
     *
     * @return          the Response object containing the user's profile details
     */
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/profile")
    public Response getProfile() {

        return userService.getProfile();
    }

    /************************************************************************************************/
    
    /**
     * Retrieves the enrolled courses for the authenticated user.
     *
     * @return          the Response object containing the list of enrolled courses
     */
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/my-learning")
    public Response myLearning() {
        return  userService.getEnrolledCourses();

    }

    /***************************************************************************************************/
    
    /**
     * Deletes the authenticated user.
     *
     * @return          the Response object indicating the result of the deletion
     * @throws SQLException if an error occurs during the deletion process
     */
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/delete-user")
    public Response deleteUser() throws SQLException {

        return userService.deleteUser();  
    }
    
    /***************************************************************************************************/
    
    /**
     * Deletes a user by their ID.
     *
     * @param  id    the ID of the user to be deleted
     * @return       the Response object indicating the result of the deletion
     * @throws SQLException if an error occurs during the deletion process
     */
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/delete-user-by-id")
    public Response deleteUserById(@RequestParam ("id") Integer id) throws SQLException {
        try {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new CustomException("User not found", HttpStatus.NOT_FOUND));
                    
            // List<Course> courses = userRepository.findInstructedCourses(tokenUtil.getUserId());
            userRepository.delete(user);
            return new Response(HttpStatus.OK, "User deleted successfully", null);
        } catch (CustomException e) {
            return new Response(e.getStatus(), e.getMessage(), null);
        } catch (Exception e) {
            return new Response(HttpStatus.NOT_FOUND, e.getMessage(), null);
        }
    }


    /***************************************************************************************************/
    
    /**
     * Updates the user profile with the provided request.
     *
     * @param  updateProfileRequest  the request containing the updated profile information
     * @return                      the response indicating the result of the update operation
     */
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/update-profile")
    public Response updateprofile(@RequestBody UpdateProfileRequest updateProfileRequest) {

        return userService.updateprofile(updateProfileRequest);
    }

    /************************************************************************************************************* */
    
    /**
     * A description of the entire Java function.
     *
     * @param  paramName    description of parameter
     * @return             description of return value
     */
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/get-instructor-courses")
    public Response getIstructedCourses() {

        return new Response(HttpStatus.OK, "Success", userService.getInstructorCourses());
    }

    /************************************************************************************************************* */
    
    /**
     * Uploads an image file.
     *
     * @param  image    the image file to upload
     * @return          a ResponseEntity with a success message or an error message
     */
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/upload-image")
    @ResponseBody
    public ResponseEntity<String> upload(@RequestParam MultipartFile image) throws IOException {
        try {
            BufferedImage bi = ImageIO.read(image.getInputStream());
            if (bi == null) {
                throw new CustomException("Invalid image file", HttpStatus.BAD_REQUEST);
            }
            User user = userRepository.findById(tokenUtil.getUserId())
                    .orElseThrow(() -> new CustomException("User not found", HttpStatus.NOT_FOUND));
            if (user.getImageId() != null) {
                cloudinaryService.delete(user.getImageId());
            }
            @SuppressWarnings("rawtypes")
            Map result = cloudinaryService.upload(image);
            user.setImageId((String) result.get("public_id"));
            user.setImageUrl((String) result.get("url"));
            userRepository.save(user);

            return new ResponseEntity<>("image uploaded  ! ", HttpStatus.OK);
        } catch (CustomException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to upload image to Cloudinary", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /******************************************************************************************** */

    /**
     * Deletes the image associated with a user.
     *
     * @return          a ResponseEntity with a success message or an error message
     */
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/delete-image")
    public ResponseEntity<String> delete() {
        try {

            User user = userRepository.findById(tokenUtil.getUserId())
                    .orElseThrow(() -> new CustomException("User not found", HttpStatus.NOT_FOUND));
            String cloudinaryImageId = user.getImageId();
            try {
                cloudinaryService.delete(cloudinaryImageId);
            } catch (IOException e) {
                throw new CustomException("Failed to delete image from Cloudinary", HttpStatus.INTERNAL_SERVER_ERROR);
            }
            user.setImageId(null);
            userRepository.save(user);
            return new ResponseEntity<>("image deleted !", HttpStatus.OK);
        } catch (CustomException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to delete image from Cloudinary", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


}