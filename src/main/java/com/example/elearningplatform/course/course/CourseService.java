package com.example.elearningplatform.course.course;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.elearningplatform.course.course.dto.CourseDto;
import com.example.elearningplatform.course.course.dto.SearchCourseDto;
import com.example.elearningplatform.course.lesson.dto.LessonDto;
import com.example.elearningplatform.course.section.SectionRepository;
import com.example.elearningplatform.course.section.dto.SectionDto;
import com.example.elearningplatform.exception.CustomException;
import com.example.elearningplatform.response.Response;
import com.example.elearningplatform.security.TokenUtil;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CourseService {
        @Autowired 
        private CourseRepository courseRepository;

        @Autowired
        private TokenUtil tokenUtil;
        @Autowired
        private SectionRepository sectionRepository;

        /***************************************************************************************** */
        public Response getCoursesByCategoryId(Integer categoryId, Integer pageNumber) {
                try {
                        List<SearchCourseDto> courses = courseRepository
                                        .findByCategoryId(categoryId, PageRequest.of(pageNumber, 8))
                                        .stream()
                                        .map(course -> new SearchCourseDto(
                                                        course, courseRepository.findCourseInstructors(course.getId()),
                                                        courseRepository.findCourseCategory(course.getId()),
                                                        courseRepository.findCourseTags(course.getId())))
                                        .toList();
                        return new Response(HttpStatus.OK, "Courses fetched successfully", courses);
                } catch (Exception e) {
                        return new Response(HttpStatus.NOT_FOUND, e.getMessage(), null);
                }
        }

        /***************************************************************************************** */
        public List<SearchCourseDto> getAllCourses() {

                List<SearchCourseDto> courses = courseRepository.findAll().stream()
                                .map(course -> new SearchCourseDto(
                                                course, courseRepository.findCourseInstructors(course.getId()),
                                                courseRepository.findCourseCategory(course.getId()),
                                                courseRepository.findCourseTags(course.getId())))
                                .toList();
                ;
                return courses;
        }

        /****************************************************************************************/
        public List<SearchCourseDto> findByCategory(Integer categoryId, Integer pageNumber) {

                Pageable pageable = PageRequest.of(pageNumber, 8);

                List<SearchCourseDto> courses = courseRepository.findByCategoryId(categoryId, pageable).stream()
                                .map(course -> new SearchCourseDto(
                                                course, courseRepository.findCourseInstructors(course.getId()),
                                                courseRepository.findCourseCategory(course.getId()),
                                                courseRepository.findCourseTags(course.getId())))
                                .toList();
                return courses;
        }

        /****************************************************************************************/
        public List<SearchCourseDto> findBysearchkey(String searchKey, Integer pageNumber) {
                if (searchKey == null) {
                        return new ArrayList<>();
                }

                Pageable pageable = PageRequest.of(pageNumber, 8);

                List<SearchCourseDto> courses = courseRepository.findBySearchKey(searchKey, pageable).stream()
                                .map(course -> new SearchCourseDto(
                                                course, courseRepository.findCourseInstructors(course.getId()),
                                                courseRepository.findCourseCategory(course.getId()),
                                                courseRepository.findCourseTags(course.getId())))
                                .toList();

                return courses;

        }

        /****************************************************************************************/
        public List<SearchCourseDto> findByTitle(String title, Integer pageNumber) {

                Pageable pageable = PageRequest.of(pageNumber, 8);

                List<SearchCourseDto> courses = courseRepository.findByTitle(title, pageable).stream()
                                .map(course -> new SearchCourseDto(
                                                course, courseRepository.findCourseInstructors(course.getId()),
                                                courseRepository.findCourseCategory(course.getId()),
                                                courseRepository.findCourseTags(course.getId())))
                                .toList();

                return courses;
        }

        /**************************************************************************************** */
        public List<SearchCourseDto> findByInstructorName(Integer instructorId, Integer pageNumber) {

                Pageable pageable = PageRequest.of(pageNumber, 8);

                List<SearchCourseDto> courses = courseRepository.findByInstructorId(instructorId, pageable).stream()
                                .map(course -> new SearchCourseDto(
                                                course, courseRepository.findCourseInstructors(course.getId()),
                                                courseRepository.findCourseCategory(course.getId()),
                                                courseRepository.findCourseTags(course.getId())))
                                .toList();
                return courses;
        }

        /**************************************************************************************** */
        public Response getCourse(Integer courseId) {
                try {
                        Course course = courseRepository.findById(courseId)
                                        .orElseThrow(() -> new CustomException("Course not found", HttpStatus.NOT_FOUND));
                        CourseDto courseDto = new CourseDto(
                                        course, ckeckCourseSubscribe(courseId),
                                        courseRepository.findCourseInstructors(courseId),
                                        courseRepository.findCourseCategory(courseId),
                                        courseRepository.findCourseTags(courseId));
                        List<SectionDto> sections = courseRepository.findCourseSections(courseId).stream()
                                        .map(section -> new SectionDto(section)).toList();
                        sections.forEach(section -> {
                                section.setLessons(
                                                sectionRepository.findSectionLessons(section.getId())
                                                                .stream()
                                                                .map(lesson -> new LessonDto(lesson))
                                                                .toList());
                        });
                        courseDto.setSections(sections);

                        return new Response(HttpStatus.OK, "Course fetched successfully", courseDto);

                }
                catch(CustomException e) {
                        return new Response(e.getStatus(), e.getMessage(), null);
                }
                 catch (Exception e) {
                        return new Response(HttpStatus.NOT_FOUND, e.getMessage(), null);
                }
        }

        /**************************************************************************************** */

        public Boolean ckeckCourseSubscribe(Integer courseId) {
                Integer userId = tokenUtil.getUserId();
                if (userId.equals(null))
                        return false;
                return courseRepository.findEnrolledCourseByUserIdAndCourseId(userId, courseId).isPresent();
        }

        /**************************************************************************************** */
        public Response getCoursesByTagId(Integer tagId, Integer pageNumber) {
                try {
                        List<SearchCourseDto> courses = courseRepository
                                        .findByTagId(tagId, PageRequest.of(pageNumber, 8))
                                        .stream()
                                        .map(course -> new SearchCourseDto(
                                                        course, courseRepository.findCourseInstructors(course.getId()),
                                                        courseRepository.findCourseCategory(course.getId()),
                                                        courseRepository.findCourseTags(course.getId())))
                                        .toList();
                        return new Response(HttpStatus.OK, "Courses fetched successfully", courses);
                } catch (Exception e) {
                        return new Response(HttpStatus.NOT_FOUND, e.getMessage(), null);
                }
        }
}