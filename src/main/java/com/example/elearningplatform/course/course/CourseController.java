package com.example.elearningplatform.course.course;

import java.io.IOException;
import java.sql.SQLException;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.elearningplatform.course.course.dto.CreateCourseRequest;
import com.example.elearningplatform.course.course.dto.UpdateCourseRequest;
import com.example.elearningplatform.response.Response;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Transactional
@RequestMapping("/course")
public class CourseController {

    private final CourseService courseService;

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
    @GetMapping("/public/get-courses")
    public Response getAllCourses() {
        return new Response(HttpStatus.OK, "Success", courseService.getAllCourses());
    }

    /**
     * @throws InterruptedException
     * @throws IOException
     ***************************************************************************************/
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/create-course")
    public Response createCourse(@RequestBody @Valid CreateCourseRequest course,BindingResult bindingResult) throws IOException, InterruptedException {
      if(bindingResult.hasErrors()){
        return new Response(HttpStatus.BAD_REQUEST, "Validation Error", bindingResult.getAllErrors());
      }
        return courseService.createCourse(course);
    }

    /***********************************************************************************************************/
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/update-course")
    public Response updateCourse(@RequestBody @Valid UpdateCourseRequest course,BindingResult bindingResult) throws IOException, InterruptedException {
if(bindingResult.hasErrors()){
    return new Response(HttpStatus.BAD_REQUEST, "Validation Error", bindingResult.getAllErrors());
}
        return courseService.updateCourse(course);
    }

    /*********************************************************************************************************** */
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/delete-course/{id}")
    public Response deleteCourse(@PathVariable("id") Integer id) throws SQLException {

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

}
