package com.example.elearningplatform.course.course;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.elearningplatform.course.category.Category;
import com.example.elearningplatform.course.review.Review;
import com.example.elearningplatform.course.section.Section;
import com.example.elearningplatform.course.tag.Tag;
import com.example.elearningplatform.user.user.User;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {

    @Query("""
            SELECT c FROM Course c
            JOIN FETCH  c.instructors i WHERE i.id = :id
                """)
    Page<Course> findByInstructorId(@Param("id") Integer id, Pageable pageable);

    /******************************************************************************************* */

    @Query("""
            SELECT c FROM Course c WHERE lower(c.title) LIKE lower(concat('%', :title, '%'))
                                    """)
    Page<Course> findByTitle(@Param("title") String title, Pageable pageable);

    /******************************************************************************************* */
    @Query("""
            SELECT c FROM Course c
            JOIN FETCH c.instructors i
            WHERE lower(c.title) LIKE lower(concat('%', :searchKy, '%'))
            OR lower(i.firstName) LIKE lower(concat('%', :searchKy, '%')) 
            OR lower(i.lastName) LIKE lower(concat('%', :searchKy, '%'))
            """)
    Page<Course> findBySearchKey(@Param("searchKy") String searchKy, Pageable pageable);

    /******************************************************************************************* */

    @Query("""
                            SELECT c FROM Course c
                            JOIN FETCH c.categories cat WHERE cat.id = :categoryId
                """)
    Page<Course> findByCategoryId(Integer categoryId, Pageable pageable);

    /******************************************************************************************* */
    @Query("""
                    SELECT c FROM Course c
                    JOIN FETCH c.tags t WHERE t.id = :tagId
                            """)
    Page<Course> findByTagId(Integer tagId, Pageable pageable);

    /******************************************************************************************* */
    @Query("""
                    SELECT c.instructors FROM Course c
                    WHERE c.id = :id
                    """)
    List<User> findCourseInstructors(@Param("id") Integer id);

    /******************************************************************************************* */
    @Query(value = """
                        SELECT course_id FROM user_enrolled_courses
                        WHERE user_id = :userId AND course_id = :courseId
                    """, nativeQuery = true)
    Optional<Integer> findEnrolledCourseByUserIdAndCourseId(@Param("userId") Integer userId,
                    @Param("courseId") Integer courseId);

    /******************************************************************************************* */
    @Query("""
                    SELECT c.categories FROM Course c
                    WHERE c.id = :id
                    """)
    List<Category> findCourseCategory(@Param("id") Integer id);

    /******************************************************************************************* */
    @Query("""
                    SELECT c.tags FROM Course c
                    WHERE c.id = :id
                    """)
    List<Tag> findCourseTags(@Param("id") Integer id);

    /******************************************************************************************* */
    @Query("""
                    SELECT c.reviews FROM Course c
                    WHERE c.id = :id
                    """)
    List<Review> findCourseReviews(@Param("id") Integer id);

    /******************************************************************************************* */
    @Query("""
                    SELECT c.sections FROM Course c
                    WHERE c.id = :id
                    """)
    List<Section> findCourseSections(@Param("id") Integer id);
}