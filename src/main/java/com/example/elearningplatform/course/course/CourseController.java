package com.example.elearningplatform.course.course;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
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
import com.example.elearningplatform.course.course.dto.CreateCourseRequest;
import com.example.elearningplatform.course.course.dto.UpdateCourseRequest;
import com.example.elearningplatform.exception.CustomException;
import com.example.elearningplatform.response.Response;
import com.example.elearningplatform.security.TokenUtil;
import com.example.elearningplatform.user.user.User;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Transactional
@RequestMapping("/course")
public class CourseController {

    @Autowired
    private CourseService courseService;
    @Autowired
    private CloudinaryService cloudinaryService;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private TokenUtil tokenUtil;

    /*******************************************************************************************/
    @GetMapping("/get-instructor-courses")
    public Response getInstructorCourses() throws SQLException {

        return new Response(HttpStatus.OK, "Success", courseService.getInstructedCourses());
    }

    /*******************************************************************************************/
    @GetMapping("/public/search/{searchKey}/{pageNumber}")
    public Response searchCourse(@PathVariable("searchKey") String searchKey,
            @PathVariable("pageNumber") Integer pageNumber) throws SQLException {

        return new Response(HttpStatus.OK, "Success", courseService.findBysearchkey(searchKey, pageNumber));
    }


    /*******************************************************************************************/
    @GetMapping("/public/get-by-category/{categoryId}/{pageNumber}")
    public Response searchCourseWithCategory(@PathVariable("categoryId") Integer categoryId,
            @PathVariable("pageNumber") Integer pageNumber) throws SQLException {
        return courseService.getCoursesByCategoryId(categoryId, pageNumber);
    }

    /*******************************************************************************************/
    @GetMapping("/public/get-by-tag/{tagId}/{pageNumber}")
    public Response searchCourseWithTag(@PathVariable("tagId") Integer tagId,
            @PathVariable("pageNumber") Integer pageNumber)
            throws SQLException {
        return courseService.getCoursesByTagId(tagId, pageNumber);
    }

    /*******************************************************************************************/

    @GetMapping("/public/get-course/{id}")

    public Response getCourse(@PathVariable("id") Integer id)
            throws SQLException {
        return new Response(HttpStatus.OK, "Success", courseService.getCourse(id));
    }

    /***************************************************************************************** */
    @GetMapping("/public/get-courses/{pageNumber}")
    public Response getAllCourses(@PathVariable("pageNumber") Integer pageNumber) {
        return new Response(HttpStatus.OK, "Success", courseService.getAllCourses(pageNumber));
    }

    /**
     * @throws InterruptedException
     * @throws IOException
     ***************************************************************************************/
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/create-course")
    public Response createCourse(@RequestBody @Valid CreateCourseRequest course, BindingResult bindingResult)
            throws IOException, InterruptedException {
        User user = tokenUtil.getUser();
        if (user == null || user.getPaypalEmail() == null) {
            return new Response(HttpStatus.BAD_REQUEST, "Please Enter your paypal email", null);

        }
        if (bindingResult.hasErrors()) {
            return new Response(HttpStatus.BAD_REQUEST, "Validation Error", bindingResult.getAllErrors());
        }
        return courseService.createCourse(course);
    }

    /***********************************************************************************************************/
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/update-course")
    public Response updateCourse(@RequestBody @Valid UpdateCourseRequest course, BindingResult bindingResult)
            throws IOException, InterruptedException {
        if (bindingResult.hasErrors()) {
            return new Response(HttpStatus.BAD_REQUEST, "Validation Error", bindingResult.getAllErrors());
        }
        return courseService.updateCourse(course);
    }

    /*********************************************************************************************************** */
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/delete-course")
    public Response deleteCourse(@RequestParam("id") Integer id) throws SQLException {

        return courseService.deleteCourse(id);
    }
    /*************************************************************************************************** */

    // @GetMapping("/display-image/{id}")
    // public Response displayImage(@PathVariable("id") Integer id) throws
    // SQLException {
    // Course course = courseRepository.findById(id).orElse(null);
    // if (course == null) {
    // return new Response(HttpStatus.NOT_FOUND, "Course not found", null);
    // }
    // return new Response(HttpStatus.OK, "Success", course.getImage());

    // }

    /*************************************************************************************************** */
    @PostMapping("/upload-image")
    @ResponseBody
    public ResponseEntity<String> upload(@RequestParam MultipartFile image, @RequestParam("courseId") int courseId)
            throws IOException {
        try {
            BufferedImage bi = ImageIO.read(image.getInputStream());
            if (bi == null) {
                throw new CustomException("Invalid image file", HttpStatus.BAD_REQUEST);
            }
            Course course = courseRepository.findById(courseId)
                    .orElseThrow(() -> new CustomException("Course not found", HttpStatus.NOT_FOUND));
            if (course.getImageId() != null) {
                cloudinaryService.delete(course.getImageId());
            }
            @SuppressWarnings("rawtypes")
            Map result = cloudinaryService.upload(image);
            course.setImageId((String) result.get("public_id"));
            course.setImageUrl((String) result.get("url"));
            courseRepository.save(course);

            return new ResponseEntity<>("image uploaded  ! ", HttpStatus.OK);
        } catch (CustomException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to upload image to Cloudinary", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /******************************************************************************************** */

    @DeleteMapping("/delete-image")
    public ResponseEntity<String> delete(@RequestParam("courseId") int courseId) {
        try {
            Course course = courseRepository.findById(courseId)
                    .orElseThrow(() -> new CustomException("Course not found", HttpStatus.NOT_FOUND));
            String cloudinaryImageId = course.getImageId();
            try {
                cloudinaryService.delete(cloudinaryImageId);
            } catch (IOException e) {
                throw new CustomException("Failed to delete image from Cloudinary", HttpStatus.INTERNAL_SERVER_ERROR);
            }
            course.setImageId(null);
            courseRepository.save(course);
            return new ResponseEntity<>("image deleted !", HttpStatus.OK);
        } catch (CustomException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to delete image from Cloudinary", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /******************************************************************************************** */
    @GetMapping("publish-course/{id}")
    public Response publishCourse(@PathVariable("id") Integer id) throws SQLException {
        return courseService.publishCourse(id);
    }
    /******************************************************************************************** */
}
