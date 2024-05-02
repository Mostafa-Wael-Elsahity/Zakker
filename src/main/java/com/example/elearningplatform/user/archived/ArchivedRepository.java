package com.example.elearningplatform.user.archived;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.elearningplatform.course.course.Course;
import com.example.elearningplatform.user.user.User;

public interface ArchivedRepository extends JpaRepository<User, Integer> {

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
            SELECT u.archivedCourses FROM User u join u.archivedCourses a
            WHERE u.id = :userId And a.id = :id
            """)
    Optional<Course> findCourseInArchived(@Param("id") Integer id, @Param("userId") Integer userId);
    /*************************************************************************************************** */

}
