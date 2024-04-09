package com.example.elearningplatform.course;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.elearningplatform.Response;
import com.example.elearningplatform.course.dto.SerchCourseDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Transactional
public class CourseController {

    private final CourseService courseService;

    @GetMapping("/course/title")

    public Response searchCourseWithTitle(@RequestParam(defaultValue = "0") Integer pageNumber,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "") String searchKey) throws SQLException {
        searchKey = "%" + searchKey + "%";
        // System.out.println("searchKey1 : " + searchKey1);
        List<SerchCourseDto> courses = courseService.findByTitle(searchKey, pageSize,
                pageSize);
        // System.out.println(courses);

        return new Response(HttpStatus.OK, "Success", courses);
    }

    /*******************************************************************************************/
    @GetMapping("/course/instructor")
    public Response searchCourseWithInstructor(@RequestParam(defaultValue = "0") Integer pageNumber,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "") String searchKey) {
        searchKey = "%" + searchKey + "%";

        List<SerchCourseDto> courses = courseService.findByInstructorsName(searchKey, pageNumber, pageSize);

        // System.out.println(courses);
        return new Response(HttpStatus.OK, "Success", courses);
    }

    /*****************************************************************************************/
    @GetMapping("/check-course-subscription")
    public Response checkCourse(@RequestParam Integer id, @RequestHeader Map<String, String> headers)
            throws SQLException {
        Boolean check = courseService.ckeckCourseSubscribe(headers.get("authorization"), id);
        // System.out.println(check);
        if (check)
            return new Response(HttpStatus.OK, "Success", courseService.getsubCourse(id));
        return new Response(HttpStatus.OK, "Success", courseService.getunsubCourse(id));

    }

}
