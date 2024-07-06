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
import com.example.elearningplatform.course.course.dto.AddInstructorRequest;
import com.example.elearningplatform.course.course.dto.CreateCourseRequest;
import com.example.elearningplatform.course.course.dto.UpdateCourseRequest;
import com.example.elearningplatform.exception.CustomException;
import com.example.elearningplatform.response.CoursesResponse;
import com.example.elearningplatform.response.Response;
import com.example.elearningplatform.validator.Validator;

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


    /*******************************************************************************************/
    
    /**
     * Searches for courses based on a search key and returns a paginated response of CoursesResponse.
     *
     * @param  searchKey   the search key to filter courses by
     * @param  pageNumber  the page number of the results to retrieve
     * @return             a paginated response of CoursesResponse containing the search results
     * @throws SQLException if an error occurs while querying the database
     */
    @GetMapping("/public/search/{searchKey}/{pageNumber}")
    public CoursesResponse searchCourse(@PathVariable("searchKey") String searchKey,
            @PathVariable("pageNumber") Integer pageNumber) throws SQLException {

        return courseService.findBysearchkey(searchKey, pageNumber);
    }


    /*******************************************************************************************/
    
    
    /**
     * Retrieves a paginated list of courses based on the specified category name and page number.
     *
     * @param  categoryName  the name of the category to filter courses by
     * @param  pageNumber    the page number of the results to retrieve
     * @return               a paginated response of CoursesResponse containing the search results
     * @throws SQLException if an error occurs while querying the database
     */
    @GetMapping("/public/get-by-category/{categoryName}/{pageNumber}")
    public CoursesResponse searchCourseWithCategory(@PathVariable("categoryName") String categoryName,
            @PathVariable("pageNumber") Integer pageNumber) throws SQLException {
        return courseService.getCoursesByCategoryName(categoryName, pageNumber);
    }

    /*******************************************************************************************/
    
    
    /**
     * Retrieves a paginated list of courses based on the specified tag name and page number.
     *
     * @param  tagName  the name of the tag to filter courses by
     * @param  pageNumber  the page number of the results to retrieve
     * @return  a CoursesResponse object containing the paginated list of courses
     * @throws SQLException if an error occurs while querying the database
     */
    @GetMapping("/public/get-by-tag/{tagName}/{pageNumber}")
    public CoursesResponse searchCourseWithTag(@PathVariable("tagName") String tagName,
            @PathVariable("pageNumber") Integer pageNumber)
            throws SQLException {
        return courseService.getCoursesByTagName(tagName, pageNumber);
    }

    /*******************************************************************************************/

    /**
     * Retrieves a course by its ID.
     *
     * @param  id     the ID of the course to retrieve
     * @return        a Response object containing the course information
     * @throws SQLException if an error occurs while querying the database
     */
    @GetMapping("/public/get-course/{id}")
    @SecurityRequirement(name = "bearerAuth")
    public Response getCourse(@PathVariable("id") Integer id)
            throws SQLException {
        return courseService.getCourse(id);
    }

    /***************************************************************************************** */
    
    
    /**
     * Retrieves a paginated list of all courses based on the specified page number.
     *
     * @param  pageNumber  the page number of the results to retrieve
     * @return             a CoursesResponse object containing the paginated list of courses
     */
    @GetMapping("/public/get-courses/{pageNumber}")
    public CoursesResponse getAllCourses(@PathVariable("pageNumber") Integer pageNumber) {
        return courseService.getAllCourses(pageNumber);
    }

    
    /**
     * A description of the entire Java function.
     *
     * @param  course   the CreateCourseRequest object containing the course details
     * @param  result   the BindingResult object for validation
     * @return          the Response object indicating the result of the course creation
     */
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/create-course")
    public Response createCourse(@RequestBody @Valid CreateCourseRequest course, BindingResult result)
            throws IOException, InterruptedException {

      if (result.hasErrors()) {
            return Validator.validate(result);
        }
        return courseService.createCourse(course);
    }

    /***********************************************************************************************************/
    
    /**
     * A description of the entire Java function.
     *
     * @param  course   the UpdateCourseRequest object containing the course details
     * @param  result   the BindingResult object for validation
     * @return          the Response object indicating the result of the course update
     */
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/update-course")
    public Response updateCourse(@RequestBody @Valid UpdateCourseRequest course, BindingResult result)
            throws IOException, InterruptedException {
                if (result.hasErrors()) {
                    return Validator.validate(result);
                }
        return courseService.updateCourse(course);
    }

    /*********************************************************************************************************** */
    
    /**
     * A description of the entire Java function.
     *
     * @param  id   description of parameter
     * @return      description of return value
     */
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/delete-course")
    public Response unPublishCourse(@RequestParam("id") Integer id) throws SQLException {

        return courseService.unPublishCourse(id);
    }
    /*************************************************************************************************** */


    /**
     * Uploads an image file for a course.
     *
     * @param  image      the image file to upload
     * @param  courseId   the ID of the course to associate the image with
     * @return            a ResponseEntity with a success message or an error message
     * @throws IOException if there is an error reading the image file
     */
    @SecurityRequirement(name = "bearerAuth")
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

    
    /**
     * Deletes the image associated with a course.
     *
     * @param  courseId   the ID of the course whose image will be deleted
     * @return            a ResponseEntity with a success message or an error message
     * @throws CustomException if the course is not found or there is an error deleting the image from Cloudinary
     */
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/delete-image")
    public ResponseEntity<String> deleteImage(@RequestParam("courseId") int courseId) {
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
    
    /**
     * A description of the entire Java function.
     *
     * @param  id   description of parameter
     * @return      description of return value
     */
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("publish-course/{id}")
    public Response publishCourse(@PathVariable("id") Integer id) throws SQLException {
        return courseService.publishCourse(id);
    }

    /******************************************************************************************** */
    
    /**
     * Adds an instructor to a course.
     *
     * @param  request  the request object containing the instructor details
     * @return          the response object indicating the result of the operation
     */
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/add-instructor")
    public Response addInstructor(@RequestBody AddInstructorRequest request) {

        return courseService.addInstructor(request);
    }

    /******************************************************************************************** */
    
    /**
     * Deletes an instructor from a course.
     *
     * @param  request  the request object containing the instructor details
     * @return          the response object indicating the result of the operation
     */
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/delete-instructor")
    public Response deleteInstructor(@RequestBody AddInstructorRequest request) {

        return courseService.deleteInstructor(request);
    }
}
