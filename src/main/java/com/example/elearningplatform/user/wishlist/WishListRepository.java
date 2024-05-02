package com.example.elearningplatform.user.wishlist;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.elearningplatform.course.course.Course;
import com.example.elearningplatform.user.user.User;

@Repository
public interface WishListRepository extends JpaRepository<User, Integer> {

    /*************************************************************************************************** */
    /*************************************************************************************************** */
    @Query("""
            SELECT u.whishlist FROM User u join u.whishlist w
            WHERE u.id = :userId And w.id = :id
            """)
    Optional<Course> findCourseInWhishList(@Param("id") Integer id, @Param("userId") Integer userId);

    /*************************************************************************************************** */


    @Query("""
            SELECT u.whishlist FROM User u
            WHERE u.id = :id
            """)
    List<Course> findWhishListCourses(Integer id);

    /************************************************************************************************/
    @Modifying
    @Query(value = """
                DELETE FROM user_whishlist_courses
                WHERE user_id = :userId AND course_id = :courseId
            """, nativeQuery = true)
    void removeFromWishlist(@Param("userId") Integer userId, @Param("courseId") Integer courseId);

    /************************************************************************************************/
    @Modifying
    @Query(value = """
                INSERT INTO user_whishlist_courses (user_id, course_id)
                VALUES (:userId, :courseId)
            """, nativeQuery = true)
    void addToWishlist(@Param("userId") Integer userId, @Param("courseId") Integer courseId);

    /************************************************************************************************/
    /*************************************************************************************************** */
}
