package com.example.elearningplatform.course;

import java.sql.SQLException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.elearningplatform.response.Response;
import com.example.elearningplatform.user.UserDto;
import com.example.elearningplatform.user.UserService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController
@RequiredArgsConstructor
@Transactional
@RequestMapping("/course")
public class CourseController {

    private final CourseService courseService;
    private final CourseRepository courseRepository;
    private final UserService userService;

    @GetMapping("/search/{searchKey}/{pageNumber}")
    public Response searchCourse(@PathVariable("searchKey") String searchKey,
            @PathVariable("pageNumber") Integer pageNumber) throws SQLException {

        List<UserDto>instructors=userService.findBySearchKey(searchKey, pageNumber);
        if(instructors.isEmpty())
        {
            List<SearchCourseDto> courses = courseService.findByTitle(searchKey, pageNumber);
            return new Response(HttpStatus.OK, "Success", courses);
        }
      
        return new Response(HttpStatus.OK, "Success", instructors);
    }

    /*******************************************************************************************/
    // @GetMapping("/course/instructor/{searchKey}/{pageNumber}")
    // public Response searchCourseWithInstructor(@PathVariable("searchKey") String searchKey,
    //         @PathVariable("pageNumber") Integer pageNumber) throws SQLException {

    //     List<SerchCourseDto> courses = courseService.findByInstructorsName(searchKey, pageNumber);

    //     return new Response(HttpStatus.OK, "Success", courses);
    // }

    /*****************************************************************************************/
    @GetMapping("/retrieve-course/{id}")
    // public Response checkCourse(@RequestParam Integer id, @RequestHeader
    // Map<String, String> headers)
    public Response getCourse(@PathVariable("id") Integer id)
            throws SQLException {

        return courseService.getCourse(id);
    }

    /*****************************************************************************************/
    @GetMapping("/display-image/{id}")
    public ResponseEntity<?> displayImage(@PathVariable("id") Integer id) throws SQLException {
        Course course = courseRepository.findById(id).orElse(null);
        if (course == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(course.getImage());

    }


}
