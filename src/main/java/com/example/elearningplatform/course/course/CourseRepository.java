package com.example.elearningplatform.course.course;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.elearningplatform.course.category.Category;
import com.example.elearningplatform.course.review.Review;
import com.example.elearningplatform.course.section.Section;
import com.example.elearningplatform.course.tag.CourseTag;
import com.example.elearningplatform.user.user.User;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {

    @Query("""
            SELECT c FROM Course c
             FULL JOIN  c.instructors i WHERE i.id = :id And c.isPublished = true
                """)
    Page<Course> findByInstructorId(@Param("id") Integer id, Pageable pageable);

    /******************************************************************************************* */
    @Query("""
            SELECT c FROM Course c
            WHERE c.id = :id And c.isPublished = true
                """)
    Optional<Course> findByCourseId(@Param("id") Integer id);

    /******************************************************************************************* */
    // @Query("""
    //         Insert INTO user_enrolled_courses (user_id, course_id)
    //         VALUES (:userId, :courseId)
    //             """)
    // void enrollCourse(@Param("userId") Integer userId, @Param("courseId") Integer courseId);
    /******************************************************************************************* */

    @Query("""
            SELECT c FROM Course c
            WHERE c.isPublished = true
                """)
    Page<Course> findAllPublished(Pageable pageable);

    /******************************************************************************************* */
    List<Course> findByOwnerId(Integer ownerId);

    /******************************************************************************************* */

    @Query("""
            SELECT c.owner FROM Course c
             WHERE c.id = :courseId
                """)
    Optional<User> findOwner(@Param("courseId") Integer courseId);

    /******************************************************************************************* */

    @Query("""
            SELECT c FROM Course c WHERE lower(c.title) LIKE lower(concat('%', :title, '%')) AND c.isPublished = true
                                    """)
    Page<Course> findByTitle(@Param("title") String title, Pageable pageable);

    /******************************************************************************************* */
    @Query("""
            SELECT c FROM Course c
            LEFT JOIN c.instructors i
            WHERE
            (
            lower(c.title) LIKE lower(concat('%', :searchKy, '%'))
            OR lower(i.firstName) LIKE lower(concat('%', :searchKy, '%'))
            OR lower(i.lastName) LIKE lower(concat('%', :searchKy, '%'))
            )
            AND c.isPublished = true
            """)
    Page<Course> findBySearchKey(@Param("searchKy") String searchKy, Pageable pageable);

    /******************************************************************************************* */
   
    @Query("""
                            SELECT c FROM Course c
                        LEFT JOIN c.categories cat WHERE cat.id = :categoryId and c.isPublished = true
                """)
    Page<Course> findByCategoryId(Integer categoryId, Pageable pageable);
     
    @Query("""
                            SELECT c FROM Course c
                          LEFT JOIN c.categories cat WHERE lower(cat.name) LIKE lower(concat('%', :categoryName, '%')) and c.isPublished = true
                """)
    Page<Course> findByCategoryName(String categoryName, Pageable pageable);

    /******************************************************************************************* */
    // @Query("""
    //                 SELECT c FROM Course c
    //         JOIN FETCH c.tags t WHERE t.id = :tagId and c.isPublished = true
    //                         """)
    // Page<Course> findByTagId(Integer tagId, Pageable pageable);
    /******************************************************************************************* */
    @Query("""
                    SELECT c FROM Course c
             LEFT JOIN c.tags t WHERE lower(t.Tag) LIKE lower(concat('%', :tagName, '%')) and c.isPublished = true
                            """)
    Page<Course> findByTagName(String tagName, Pageable pageable);

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
    List<CourseTag> findCourseTags(@Param("id") Integer id);

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

    /******* **************************************************************** */
    @Modifying
    @Query(value = """
                INSERT INTO user_enrolled_courses (user_id, course_id)
                VALUES (:userId, :courseId)
            """, nativeQuery = true)
    void enrollCourse(@Param("userId") Integer userId, @Param("courseId") Integer courseId);

    /************************************************************************* */
    @Modifying
    @Query(value = """
                DELETE FROM user_enrolled_courses
                WHERE user_id = :userId AND course_id = :courseId
            """, nativeQuery = true)
    void unEnrollCourse(@Param("userId") Integer userId, @Param("courseId") Integer courseId);
    /************************************************************************* */
    @Modifying
    @Query(value = """
                        INSERT INTO course_instructors (user_id, course_id)
                        VALUES (:userId, :courseId)
                    """, nativeQuery = true)
    void addInstructor(@Param("userId") Integer userId, @Param("courseId") Integer courseId);

    /************************************************************************ */
    @Modifying
    @Query(value = """
                    DELETE FROM course_instructors
                    WHERE user_id = :userId AND course_id = :courseId
                        """, nativeQuery = true)
    void deleteInstructor(@Param("userId") Integer userId, @Param("courseId") Integer courseId);
}