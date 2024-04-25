package com.example.elearningplatform.course;

import java.sql.SQLException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.elearningplatform.response.Response;
import com.example.elearningplatform.user.UserDto;
import com.example.elearningplatform.user.UserService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
@Transactional
@RequestMapping("/course")
public class CourseController {

    private final CourseService courseService;
    private final CourseRepository courseRepository;
    private final UserService userService;

    /*******************************************************************************************/
    @GetMapping("/search/{searchKey}/{pageNumber}")
    public Response searchCourse(@PathVariable("searchKey") String searchKey,
            @PathVariable("pageNumber") Integer pageNumber) throws SQLException {

        List<UserDto> instructors = userService.findBySearchKey(searchKey, pageNumber);
        if (instructors.isEmpty()) {
            List<SearchCourseDto> courses = courseService.findByTitle(searchKey, pageNumber);
            return new Response(HttpStatus.OK, "Success", courses);
        }

        return new Response(HttpStatus.OK, "Success", instructors);
    }

    /*******************************************************************************************/
    @GetMapping("/course/instructor/{id}/{pageNumber}")
    public Response searchCourseWithInstructor(@PathVariable("id") Integer id,
            @PathVariable("pageNumber") Integer pageNumber) throws SQLException {

        List<SearchCourseDto> courses = courseService.findByInstructorName(id,
                pageNumber);

        return new Response(HttpStatus.OK, "Success", courses);
    }

    /*****************************************************************************************/
    // @PostMapping("/create-course")
    // public Response createCourse() {

    // }
    /*******************************************************************************************/

    @GetMapping("/retrieve-course/{id}")
    // public Response checkCourse(@RequestParam Integer id, @RequestHeader
    // Map<String, String> headers)
    public Response getCourse(@PathVariable("id") Integer id)
            throws SQLException {
        return new Response(HttpStatus.OK, "Success", courseService.getCourse(id));
    }

    /***************************************************************************************** */
    @GetMapping("/")
    public Response getAllCourses() {
        return new Response(HttpStatus.OK, "Success", courseService.getAllCourses());
    }

    /*****************************************************************************************/
    @GetMapping("/display-image/{id}")
    public Response displayImage(@PathVariable("id") Integer id) throws SQLException {
        Course course = courseRepository.findById(id).orElse(null);
        if (course == null) {
            return new Response(HttpStatus.NOT_FOUND, "Course not found", null);
        }
        return new Response(HttpStatus.OK, "Success", course.getImage());

    }



    

}
