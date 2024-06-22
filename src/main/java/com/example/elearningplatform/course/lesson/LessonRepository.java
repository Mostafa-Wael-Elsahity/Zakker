package com.example.elearningplatform.course.lesson;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.elearningplatform.course.comment.Comment;
import com.example.elearningplatform.course.course.Course;
import com.example.elearningplatform.course.section.Section;

public interface LessonRepository extends JpaRepository<Lesson, Integer> {
    List<Lesson> findBySectionId(Integer sectionId);

    // /****************************************************************************************
    // */
    // @Query("""
    // SELECT l.notes FROM Lesson l
    // JOIN l.notes n
    // WHERE n.user.id = :userId And l.id = :lessonId
    // """)
    // Optional<Note> findLessonNoteWithUserId(@Param("lessonId") Integer lessonId,
    // @Param("userId") Integer userId);

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
    @Query("""
                    SELECT l FROM Lesson l WHERE l.section.course.id = :courseId
                    """)
    List<Lesson> findLessonsByCourseId(@Param("courseId") Integer courseId);

/**************************************************************************************** */
    @Query("""
            SELECT l.section FROM Lesson l WHERE l.id = :lessonId
            """)
    Optional<Section> findSection(@Param("lessonId") Integer lessonId);
}
