package com.example.elearningplatform.course.course;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.elearningplatform.course.course.dto.CourseDto;
import com.example.elearningplatform.course.course.dto.CreateCourseRequest;
import com.example.elearningplatform.course.course.dto.SearchCourseDto;
import com.example.elearningplatform.course.course.dto.UpdateCourseRequest;
import com.example.elearningplatform.course.lesson.dto.LessonDto;
import com.example.elearningplatform.course.section.SectionRepository;
import com.example.elearningplatform.course.section.dto.SectionDto;
import com.example.elearningplatform.exception.CustomException;
import com.example.elearningplatform.response.Response;
import com.example.elearningplatform.security.TokenUtil;
import com.example.elearningplatform.user.user.User;
import com.example.elearningplatform.user.user.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

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
        @Autowired
        private UserRepository userRepository;
        @Value("${APIKey}")
        private String ApiKey;

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
                                                                .map(
                                                                                lesson -> new LessonDto(lesson)

                                                                )
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
                if (userId == null)
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

        /************************************************************************************************************** */
        public Response createCourse(CreateCourseRequest createCourseRequest)
                        throws IOException, InterruptedException {

                try {

                        User user = userRepository.findById(tokenUtil.getUserId())
                                        .orElseThrow(() -> new CustomException("Please login", HttpStatus.NOT_FOUND));

                        HttpRequest request = HttpRequest.newBuilder()
                                        .uri(URI.create("https://api.bunny.net/videolibrary"))
                                        .header("accept", "application/json")
                                        .header("content-type", "application/json")
                                        .header("AccessKey", ApiKey)
                                        .method("POST", HttpRequest.BodyPublishers
                                                        .ofString("{\"Name\":\"" + createCourseRequest.getTitle()
                                                                        + "\"}"))
                                        .build();
                        HttpResponse<String> response = HttpClient.newHttpClient().send(request,
                                        HttpResponse.BodyHandlers.ofString());
                        ObjectMapper mapper = new ObjectMapper();
                        Map<String, Object> responseMap = mapper.readValue(response.body(),
                                        new TypeReference<Map<String, Object>>() {
                                        });
                        if (responseMap.get("error") != null) {
                                throw new CustomException(responseMap.get("error").toString(), HttpStatus.BAD_REQUEST);
                        }
                        Course course = new Course(createCourseRequest);
                        course.setOwner(user);
                        course.setGuid(Integer.parseInt(responseMap.get("Id").toString()));
                        course.setApiKey(responseMap.get("ApiKey").toString());

                        courseRepository.save(course);

                        return new Response(HttpStatus.OK, "Course created successfully",
                                        new SearchCourseDto(course,
                                                        courseRepository.findCourseInstructors(course.getId()),
                                                        courseRepository.findCourseCategory(course.getId()),
                                                        courseRepository.findCourseTags(course.getId())));

                } catch (Exception e) {
                        return new Response(HttpStatus.NOT_FOUND, e.getMessage(), null);
                }
        }

        /***************************************************************************************************************/

        public Response updateCourse(UpdateCourseRequest updateCourseRequest) {
                try {
                        Course course = courseRepository.findById(updateCourseRequest.getCourseId())
                                        .orElseThrow(() -> new CustomException("Course not found",
                                                        HttpStatus.NOT_FOUND));
                        course.setTitle(updateCourseRequest.getTitle());
                        course.setDescription(updateCourseRequest.getDescription());
                        course.setWhatYouWillLearn(updateCourseRequest.getWhatYouWillLearn());
                        course.setPrerequisite(updateCourseRequest.getPrerequisite());
                        course.setLanguage(updateCourseRequest.getLanguage());
                        course.setLevel(updateCourseRequest.getLevel());

                        courseRepository.save(course);
                        return new Response(HttpStatus.OK, "Course updated successfully",
                                        new SearchCourseDto(course,
                                                        courseRepository.findCourseInstructors(course.getId()),
                                                        courseRepository.findCourseCategory(course.getId()),
                                                        courseRepository.findCourseTags(course.getId())));
                } catch (CustomException e) {
                        return new Response(e.getStatus(), e.getMessage(), null);
                } catch (Exception e) {
                        return new Response(HttpStatus.NOT_FOUND, e.getMessage(), null);
                }
        }

        /***************************************************************************************************************/

        public Response deleteCourse(Integer courseId) {
                try {
                        Course course = courseRepository.findById(courseId)
                                        .orElseThrow(() -> new CustomException("Course not found",
                                                        HttpStatus.NOT_FOUND));
                        System.out.println(course.getGuid());
                        HttpRequest request = HttpRequest.newBuilder()
                                        .uri(URI.create(
                                                        String.format(
                                                                        "https://api.bunny.net/videolibrary/%s",
                                                                        course.getGuid())))
                                        .header("accept", "application/json")
                                        .header("AccessKey", ApiKey)
                                        .method("DELETE", HttpRequest.BodyPublishers.noBody())
                                        .build();
                        HttpResponse<String> response = HttpClient.newHttpClient().send(request,
                                        HttpResponse.BodyHandlers.ofString());
                        System.out.println(response);
                        if (response.statusCode() >=200 && response.statusCode() < 300)
                                courseRepository.delete(course);

                        else
                                throw new CustomException(response.body(), HttpStatus.INTERNAL_SERVER_ERROR);


                        return new Response(HttpStatus.OK, "Course deleted successfully", null);
                } catch (CustomException e) {
                        return new Response(e.getStatus(), e.getMessage(), null);
                } catch (Exception e) {
                        return new Response(HttpStatus.NOT_FOUND, e.getMessage(), null);
                }
        }
        /***************************************************************************************************************/

}