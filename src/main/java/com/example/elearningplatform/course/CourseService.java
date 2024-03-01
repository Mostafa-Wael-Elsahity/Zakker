package com.example.elearningplatform.course;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.example.elearningplatform.user.User;

@Service
@SuppressWarnings({ "deprecation", "rawtypes", "unchecked", "null" })
public class CourseService {

        @Autowired
        private CourseRepository courseRepository;
        @Autowired
        private JdbcTemplate jdbcTemplate;

        public List<CourseDao> getAllCoursesByTitle(String searchKey, Integer pageNumber, Integer pageSize) {
                String sql = "SELECT course.id, course.title, course.description,course.image_url," +
                                " GROUP_CONCAT(users.email) as instructors FROM course " +
                                " JOIN instructed_courses ON course.id=instructed_courses.course_id" +
                                " Join users ON users.id=instructed_courses.user_id WHERE course.title like ?" +
                                "Group by course.id  limit ? offset ?";
                List<CourseDao> coursesDao = jdbcTemplate.queryForList(sql, new Object[] { searchKey,
                                pageSize, pageNumber * pageSize }).stream().map(
                                                object -> new CourseDao(object))
                                .toList();

                return coursesDao;
        }

        /**************************************************************************************** */

        public List<CourseDao> getAllCoursesByInstructorName(String searchKey, Integer pageNumber, Integer pageSize) {
                String sql = "SELECT course.id, course.title, course.description,course.image_url," +
                                " GROUP_CONCAT(users.email) as instructors FROM course " +
                                " JOIN instructed_courses ON course.id=instructed_courses.course_id" +
                                " Join users ON users.id=instructed_courses.user_id WHERE users.email like ?" +
                                "Group by course.id  limit ? offset ?";
                List<CourseDao> coursesDao = jdbcTemplate.queryForList(sql, new Object[] { searchKey,
                                pageSize, pageNumber * pageSize }).stream().map(
                                                object -> new CourseDao(object))
                                .toList();
                return coursesDao;
        }

        /**************************************************************************************** */

        public Course getCourseById(Long id) {
                String sql = "SELECT * FROM course WHERE id=?";
                List<Course> courses = jdbcTemplate.query(
                                sql, new Object[] { id },
                                new BeanPropertyRowMapper(Course.class));

                return courses.get(0);
        }

}
