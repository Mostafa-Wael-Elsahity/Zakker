package com.example.elearningplatform.course;

import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.elearningplatform.Response;
import com.example.elearningplatform.course.dto.CourseSubscripeDto;
import com.example.elearningplatform.course.dto.CourseUnsubscripeDto;
import com.example.elearningplatform.course.dto.InstructorDto;
import com.example.elearningplatform.course.dto.SerchCourseDto;
import com.example.elearningplatform.course.lesson.Lesson;
import com.example.elearningplatform.course.lesson.LessonDto;
import com.example.elearningplatform.course.lesson.LessonVideoDto;
import com.example.elearningplatform.course.section.Section;
import com.example.elearningplatform.course.section.SectionDto;
import com.example.elearningplatform.course.section.SectionLessonDto;
import com.example.elearningplatform.security.TokenUtil;
import com.example.elearningplatform.user.User;
import com.example.elearningplatform.user.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@SuppressWarnings({ "null" })
@RequiredArgsConstructor
@Transactional
public class CourseService {
        private final CourseRepository courseRepository;
        private final TokenUtil tokenUtil;
        private final UserRepository userRepository;

        /***************************************************************************************************
        */
        public List<SerchCourseDto> findByInstructorsName(String searchKey, Integer pageNumber, Integer pageSize) {

                List<SerchCourseDto> courses = courseRepository.findByInstructorsName(searchKey).stream()
                                .map(course -> {
                                        SerchCourseDto serchCourseDto = null;
                                        try {
                                                serchCourseDto = new SerchCourseDto(course);
                                        } catch (SQLException e) {
                                                e.printStackTrace();
                                        }

                                        for (User user : course.getInstructors()) {
                                                try {
                                                        InstructorDto instr = new InstructorDto(user);
                                                        serchCourseDto.addInstructor(instr);
                                                } catch (SQLException e) {
                                                        e.printStackTrace();
                                                }
                                        }
                                        return serchCourseDto;
                                }).toList();
                return courses;

                // ...
        }

        /****************************************************************************************/
        public List<SerchCourseDto> findByTitle(String searchKey, Integer pageNumber, Integer pageSize)
                        throws SQLException {
                List<SerchCourseDto> courses = courseRepository.findByTitle(searchKey).stream().map(course -> {
                        SerchCourseDto serchCourseDto = null;
                        try {
                                serchCourseDto = new SerchCourseDto(course);
                        } catch (SQLException e) {
                                e.printStackTrace();
                        }

                        for (User user : course.getInstructors()) {
                                try {
                                        InstructorDto instr = new InstructorDto(user);
                                        serchCourseDto.addInstructor(instr);
                                } catch (SQLException e) {
                                        e.printStackTrace();
                                }
                        }
                        return serchCourseDto;
                }).toList();
                return courses;
        }

        public CourseSubscripeDto getSubScripeCourse(Integer id) throws SQLException {
                Course course = courseRepository.findById(id).orElse(null);
                if (course == null)
                        return null;
                CourseSubscripeDto courseSubscripeDto = new CourseSubscripeDto(course);

                for (User user : course.getInstructors()) {
                        InstructorDto instr = new InstructorDto(user);

                        courseSubscripeDto.addInstructor(instr);

                }
                for (Section section : course.getSections()) {
                        SectionLessonDto sectionLessonDto = new SectionLessonDto(section);
                        for (Lesson lesson : section.getLessons()) {
                                LessonVideoDto lessoncDto = new LessonVideoDto();
                                lessoncDto.setTitle(lesson.getTitle());
                                lessoncDto.setDuration(lesson.getDuration());
                                sectionLessonDto.addLesson(lessoncDto);
                        }
                        courseSubscripeDto.addSection(sectionLessonDto);
                }

                return courseSubscripeDto;
        }

        public CourseUnsubscripeDto getUnSubScripeCourse(Integer id) throws SQLException {
                Course course = courseRepository.findById(id).orElse(null);
                if (course == null)
                        return null;
                CourseUnsubscripeDto courseUnsubscripeDto = new CourseUnsubscripeDto(course);
                for (User user : course.getInstructors()) {
                        InstructorDto instr = new InstructorDto(user);
                        courseUnsubscripeDto.addInstructor(instr);
                }
                for (Section section : course.getSections()) {
                        SectionDto sectionDto = new SectionDto(section);
                        for (Lesson lesson : section.getLessons()) {
                                LessonDto lessoncDto = new LessonDto();
                                lessoncDto.setTitle(lesson.getTitle());
                                lessoncDto.setDuration(lesson.getDuration());

                                sectionDto.addLesson(lessoncDto);
                        }
                        courseUnsubscripeDto.addSection(sectionDto);
                }

                return courseUnsubscripeDto;
        }

        static public String imageConverter(Blob blob) throws SQLException {

                byte[] imageBytes = null;

                if (blob == null) {
                        return null;
                }

                imageBytes = blob.getBytes(1, (int) blob.length());

                String image = new String(imageBytes);
                return image;
        }

        public String getUserNameFromToken(String token) throws SQLException {
                String userName = tokenUtil.getUserNameFromToken(token);
                return userName;
        }

        public Boolean ckeckCourseSubscribe(String token, Integer id) throws SQLException {
                String userName = getUserNameFromToken(token.substring(7, token.length()));
                // System.out.println(userName);
                User user = userRepository.findByEmail(userName).orElse(null);
                // System.out.println(user.getCourses());
                for (Course c : user.getCourses()) {
                        if (c.getId() == id)
                                return true;
                }
                return false;

        }

        /****************************************************************************************/

        public Response getsubCourse(@RequestParam Integer id)
                        throws SQLException {
                CourseSubscripeDto course = getSubScripeCourse(id);

                if (course == null) {
                        return new Response(HttpStatus.NOT_FOUND, "Course not found", null);

                }
                return new Response(HttpStatus.OK, "Success", course);

        }

        /*******************************************************************************************/

        public Response getunsubCourse(@RequestParam Integer id)
                        throws SQLException {
                CourseUnsubscripeDto course = getUnSubScripeCourse(id);

                if (course == null) {
                        return new Response(HttpStatus.NOT_FOUND, "Course not found", null);
                }
                return new Response(HttpStatus.OK, "Success", course);

        }

}
