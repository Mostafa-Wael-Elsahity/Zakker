package com.example.elearningplatform.course;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.elearningplatform.base.BaseRepository;
import com.example.elearningplatform.course.section.Section;

@Service
@SuppressWarnings({ "deprecation", "rawtypes", "unchecked", "null" })
public class CourseService extends BaseRepository {

        /*************************************************************************************************** */

        public List<CourseDao> findByTitle(String searchKey, Integer pageNumber, Integer pageSize) {
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

        public List<CourseDao> findByInstructorName(String searchKey, Integer pageNumber, Integer pageSize) {
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
        // public Course getCourseId(Integer id) {
        // String sql = "SELECT * FROM course WHERE id = " + id;
        // List<Course> courses = jdbcTemplate.query(sql, new
        // BeanPropertyRowMapper<>(Course.class));
        // if (courses.isEmpty()) {
        // return null;
        // }
        // return courses.get(0);
        // }

        /**************************************************************************************** */

        // public List<CourseDao> getAllCourses() {
        // String sql = "SELECT course.id, course.title,
        // course.description,course.image_url," +
        // " GROUP_CONCAT(users.email) as instructors FROM course " +
        // " JOIN instructed_courses ON course.id=instructed_courses.course_id" +
        // " Join users ON users.id=instructed_courses.user_id" +
        // "Group by course.id";

        // List<CourseDao> coursesDao = jdbcTemplate.queryForList(sql).stream().map(
        // object -> new CourseDao(object))
        // .toList();
        // return coursesDao;
        // }

        /*********************************************************************************************** */
        public Course getCourse(Integer id) {
                Course course = findById(Course.class, id);
                if (course != null) {

                        List<Section> sections = sectionService.findByCourseId(id);
                        for (Section section : sections) {
                                section.setLessons(lessonService.findBySectionId(section.getId()));
                        }
                        course.setSections(sections);
                        course.setInstructors(userService.findCourseInstructors(id));
                        course.setTags(tagService.findByCourseId(id));
                        course.setReviews(reviewService.findByCourseId(id));
                        course.setCategories(categoryService.findByCourseId(id));
                        return course;
                }

                return null;
        }

        /**************************************************************************************** */
        // @Transactional
        // public void saveCourse(Course course) {
        // entityManager.persist(course);
        // }

        // /****************************************************************************************
        // */
        // @Transactional
        // public void updateCourse(Course course) {
        // entityManager.merge(course);
        // }
}
