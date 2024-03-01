package com.example.elearningplatform.course;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.elearningplatform.Response;

@RestController
public class CourseController {
    @Autowired
    private CourseService courseService;

    @GetMapping("/course/title")
    public Response searchCourseWithTitle(@RequestParam(defaultValue = "0") Integer pageNumber,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "") String searchKey) {
        String searchKey1 = "%" + searchKey + "%";
        // System.out.println("searchKey1 : " + searchKey1);
        List<CourseDao> courses = courseService.getAllCoursesByTitle(searchKey1, pageNumber, pageSize);
        return new Response(HttpStatus.OK, "Success", courses);
    }

    @GetMapping("/course/instructor")
    public Response searchCourseWithInstructor(@RequestParam(defaultValue = "0") Integer pageNumber,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "") String searchKey) {
        String searchKey1 = "%" + searchKey + "%";
        // System.out.println("searchKey1 : " + searchKey1);

        List<CourseDao> courses = courseService.getAllCoursesByInstructorName(searchKey1, pageNumber,
                pageSize);
        return new Response(HttpStatus.OK, "Success", courses);
    }

    @GetMapping("/course")
    public Response searchCourse(@RequestParam Long id) {

        Course course = courseService.getCourseById(id);

        return new Response(HttpStatus.OK, "Success", course);
    }
}
