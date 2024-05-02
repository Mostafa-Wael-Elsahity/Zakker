package com.example.elearningplatform.user.cart;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.elearningplatform.course.course.Course;
import com.example.elearningplatform.user.user.User;

public interface CartRepository extends JpaRepository<User, Integer> {
 /************************************************************************************************/
    @Query("""
            SELECT u.cart FROM User u
            WHERE u.id = :id
            """)
    List<Course> findCartCourses(Integer id);

    /************************************************************************************************/
    @Modifying
    @Query(value = """
                DELETE FROM user_cart_courses
                WHERE user_id = :userId AND course_id = :courseId
            """, nativeQuery = true)
    void removeFromCart(@Param("userId") Integer userId, @Param("courseId") Integer courseId);

    /************************************************************************************************/
    @Modifying
    @Query(value = """
                INSERT INTO user_cart_courses (user_id, course_id)
                VALUES (:userId, :courseId)
            """, nativeQuery = true)
    void addToCart(@Param("userId") Integer userId, @Param("courseId") Integer courseId);

    /*****************************************************************************************************/
    @Query("""
            SELECT u.cart FROM User u join u.cart c
            WHERE u.id = :userId And c.id = :id
            """)
    Optional<Course> findCourseInCart(@Param("id") Integer id, @Param("userId") Integer userId);

    /*************************************************************************************************** */
}
