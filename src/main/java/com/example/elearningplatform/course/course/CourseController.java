package com.example.elearningplatform.course.course;

import java.sql.SQLException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.elearningplatform.response.Response;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Transactional
@RequestMapping("/course")
public class CourseController {

    private final CourseService courseService;

    /*******************************************************************************************/
    @GetMapping("/search/{searchKey}/{pageNumber}")
    public Response searchCourse(@PathVariable("searchKey") String searchKey,
            @PathVariable("pageNumber") Integer pageNumber) throws SQLException {

        return new Response(HttpStatus.OK, "Success", courseService.findBysearchkey(searchKey, pageNumber));
    }


    /*******************************************************************************************/
    @GetMapping("/get-by-category/{categoryId}/{pageNumber}")
    public Response searchCourseWithCategory(@PathVariable("categoryId") Integer categoryId,
            @PathVariable("pageNumber") Integer pageNumber) throws SQLException {
        return courseService.getCoursesByCategoryId(categoryId, pageNumber);
    }

    /*******************************************************************************************/
    @GetMapping("/get-by-tag/{tagId}/{pageNumber}")
    public Response searchCourseWithTag(@PathVariable("tagId") Integer tagId,
            @PathVariable("pageNumber") Integer pageNumber)
            throws SQLException {
        return courseService.getCoursesByTagId(tagId, pageNumber);
    }

    /*******************************************************************************************/

    @GetMapping("/get-course/{id}")

    public Response getCourse(@PathVariable("id") Integer id)
            throws SQLException {
        return new Response(HttpStatus.OK, "Success", courseService.getCourse(id));
    }

    /***************************************************************************************** */
    @GetMapping("/get-courses")
    public Response getAllCourses() {
        return new Response(HttpStatus.OK, "Success", courseService.getAllCourses());
    }

    /*****************************************************************************************/
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
