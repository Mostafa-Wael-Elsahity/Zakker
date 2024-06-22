package com.example.elearningplatform.user.lists;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.elearningplatform.course.course.Course;

public interface UserListRepository extends JpaRepository<UserList, Integer>{
    List<UserList> findByUserId(Integer userId);
    
    /*********************************************************************************************** */
       @Query("""
            SELECT ul.courses FROM UserList ul  join ul.courses c WHERE c.id = :courseId And ul.id = :listId
            """)
    Optional<Course> findCourse(@Param("courseId") Integer courseId, @Param("listId") Integer listId);
    /*********************************************************************************************** */
    @Query("""
            SELECT u.courses FROM UserList u
            WHERE u.id = :id
            """)
    List<Course> findCourses(@Param("id") Integer id);

    /*********************************************************************************************** */
    @Modifying
    @Query(value = """
                DELETE FROM lists_courses
                WHERE user_id = :userId AND course_id = :courseId
            """, nativeQuery = true)
    void removeFromUserList(@Param("userId") Integer userId, @Param("courseId") Integer courseId);

    /*********************************************************************************************** */
    @Modifying
    @Query(value = """
                INSERT INTO lists_courses (list_id, course_id)
                VALUES (:listId, :courseId)
            """, nativeQuery = true)
    void addToUserList(@Param("listId") Integer listId, @Param("courseId") Integer courseId);
}
