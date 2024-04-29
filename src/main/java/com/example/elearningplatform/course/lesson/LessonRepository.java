package com.example.elearningplatform.course.lesson;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.elearningplatform.course.comment.Comment;
import com.example.elearningplatform.course.course.Course;

public interface LessonRepository extends JpaRepository<Lesson, Integer> {
    List<Lesson> findBySectionId(Integer sectionId);

    /**************************************************************************************** */
    @Query("""
            SELECT l.comments FROM Lesson l WHERE l.id = :lessonId
            """)
    List<Comment> findLessonComments(@Param("lessonId") Integer lessonId, Pageable pageable);

    /**************************************************************************************** */
    @Query("""
                SELECT l.section.course FROM Lesson l WHERE l.id = :lessonId
            """)
    Optional<Course> findCourseByLessonId(@Param("lessonId") Integer lessonId);
    /**************************************************************************************** */
}
