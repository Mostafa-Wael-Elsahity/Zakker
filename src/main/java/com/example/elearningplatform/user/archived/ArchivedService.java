package com.example.elearningplatform.user.archived;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.elearningplatform.course.course.CourseRepository;
import com.example.elearningplatform.course.course.dto.SearchCourseDto;
import com.example.elearningplatform.exception.CustomException;
import com.example.elearningplatform.response.Response;
import com.example.elearningplatform.security.TokenUtil;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ArchivedService {
    @Autowired
    private ArchivedRepository archivedRepository;
    @Autowired
    private TokenUtil tokenUtil;
    @Autowired
    private CourseRepository courseRepository;

    /*********************************************************************************************************** */
    /*************************************************************************************************************** */

    public Response getArchived() {
        try {

            List<SearchCourseDto> courses = archivedRepository.findArchivedCourses(tokenUtil.getUserId()).stream()
                    .map(course -> new SearchCourseDto(
                            course, courseRepository.findCourseInstructors(course.getId()),
                            courseRepository.findCourseCategory(course.getId()),
                            courseRepository.findCourseTags(course.getId())))
                    .toList();

            return new Response(HttpStatus.OK, "Success", courses);
        } catch (Exception e) {
            return new Response(HttpStatus.NOT_FOUND, e.getMessage(), null);

        }
    }

    /********************************************************************************************************* */

    public Response addToArchived(Integer courseId) {
        try {

            if (archivedRepository.findCourseInArchived(courseId, tokenUtil.getUserId()).isPresent()) {
                throw new CustomException("Course already in archived", HttpStatus.BAD_REQUEST);
            }

            archivedRepository.addToArchivedCourses(tokenUtil.getUserId(), courseId);
            return new Response(HttpStatus.OK, "Course added to archived", null);
        } catch (CustomException e) {
            return new Response(e.getStatus(), e.getMessage(), null);
        } catch (Exception e) {
            return new Response(HttpStatus.NOT_FOUND, e.getMessage(), null);

        }

    }

    /********************************************************************************************************* */
    public Response deleteFromArchived(Integer courseId) {

        try {

            if (!archivedRepository.findCourseInArchived(courseId, tokenUtil.getUserId()).isPresent()) {
                throw new CustomException("Course is not in archived", HttpStatus.BAD_REQUEST);
            }

            archivedRepository.removeFromArchivedCourses(tokenUtil.getUserId(), courseId);
            return new Response(HttpStatus.OK, "Course deleted from archived", null);
        } catch (CustomException e) {
            return new Response(e.getStatus(), e.getMessage(), null);
        } catch (Exception e) {
            return new Response(HttpStatus.NOT_FOUND, e.getMessage(), null);
        }

    }
}
