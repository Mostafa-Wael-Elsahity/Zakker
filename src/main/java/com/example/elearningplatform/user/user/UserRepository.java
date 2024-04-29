package com.example.elearningplatform.user.user;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.elearningplatform.course.course.Course;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);

    Optional<User> findByFirstName(String firstName);

    Optional<User> findByLastName(String lastName);

    /************************************************************************************************/
    @Query("""
                                SELECT new com.example.elearningplatform.user.user.User(u) FROM User u
                WHERE lower(u.email) LIKE lower(concat('%', :searchKey, '%'))
                OR lower(u.firstName) LIKE lower(concat('%', :searchKey, '%'))
                OR lower(u.lastName) LIKE lower(concat('%', :searchKey, '%'))
            """)
    Page<User> findBySearchKey(@Param("searchKey") String searchKey, Pageable pageable);

    /************************************************************************************************/

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
    @Modifying
    @Query(value = """
                DELETE FROM user_archived_courses
                WHERE user_id = :userId AND course_id = :courseId
            """, nativeQuery = true)
    void removeFromArchivedCourses(@Param("userId") Integer userId, @Param("courseId") Integer courseId);

    /************************************************************************************************/
    @Modifying
    @Query(value = """
                INSERT INTO user_archived_courses (user_id, course_id)
                VALUES (:userId, :courseId)
            """, nativeQuery = true)
    void addToArchivedCourses(@Param("userId") Integer userId, @Param("courseId") Integer courseId);

    /************************************************************************************************/

    @Query("""
            SELECT u.archivedCourses FROM User u
            WHERE u.id = :id
            """)
    List<Course> findArchivedCourses(Integer id);

    /************************************************************************************************/

    @Query("""
            SELECT u.enrolledCourses FROM User u
            WHERE u.id = :id
            """)
    List<Course> findEnrolledCourses(Integer id);

    /************************************************************************************************/

    @Query("""
            SELECT u.instructoredCourses FROM User u
            WHERE u.id = :id
            """)
    List<Course> findInstructedCourses(Integer id);

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
    @Query("""
            SELECT u.enrolledCourses FROM User u join u.enrolledCourses e
            WHERE u.id = :userId And e.id = :id
            """)
    Optional<Course> findCourseInEnrolled(@Param("id") Integer id, @Param("userId") Integer userId);

    /*************************************************************************************************** */
//     @Query("""
//             SELECT u.instructoredCourses FROM User u
//             WHERE u.id = :id And u.instructoredCourses = :userId
//             """)
//     Optional<Course> findCourseInInstructed(@Param("id") Integer id, @Param("userId") Integer userId);

    /*************************************************************************************************** */
    @Query("""
            SELECT u.whishlist FROM User u join u.whishlist w
            WHERE u.id = :userId And w.id = :id
            """)
    Optional<Course> findCourseInWhishList(@Param("id") Integer id, @Param("userId") Integer userId);

    /*************************************************************************************************** */
    @Query("""
            SELECT u.archivedCourses FROM User u join u.archivedCourses a
            WHERE u.id = :userId And a.id = :id
            """)
    Optional<Course> findCourseInArchived(@Param("id") Integer id, @Param("userId") Integer userId);
    /*************************************************************************************************** */

}
