package com.example.elearningplatform.course;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.elearningplatform.Response;
import com.example.elearningplatform.course.section.SectionService;

@RestController
public class CourseController {
    @Autowired
    SectionService sectionService;

    @Autowired
    private CourseService courseService;

    /***************************************************************************************** */

    @GetMapping("/course/title")
    public Response searchCourseWithTitle(@RequestParam(defaultValue = "0") Integer pageNumber,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "") String searchKey) {
        String searchKey1 = "%" + searchKey + "%";
        // System.out.println("searchKey1 : " + searchKey1);
        List<CourseDao> courses = courseService.findByTitle(searchKey1, pageNumber, pageSize);
        return new Response(HttpStatus.OK, "Success", courses);
    }

    /*******************************************************************************************/
    @GetMapping("/course/instructor")
    public Response searchCourseWithInstructor(@RequestParam(defaultValue = "0") Integer pageNumber,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "") String searchKey) {
        String searchKey1 = "%" + searchKey + "%";
        // System.out.println("searchKey1 : " + searchKey1);

        List<CourseDao> courses = courseService.findByInstructorName(searchKey1, pageNumber,
                pageSize);
        return new Response(HttpStatus.OK, "Success", courses);
    }

    /*******************************************************************************************/

    @GetMapping("/get-course")
    public Response searchCourse(@RequestParam Integer id) {

        Course course = courseService.getCourse(id);
        // System.out.println(course);
        if (course == null) {
            return new Response(HttpStatus.NOT_FOUND, "Course not found", null);

        }
        return new Response(HttpStatus.OK, "Success", course);

    }
    /*******************************************************************************************/

}
