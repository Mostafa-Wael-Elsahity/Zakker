package com.example.elearningplatform.course.section.lesson.question.comment;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface CommentRepository extends JpaRepository<Comment, Integer> {

    List<Comment> findByLessonId(Integer lessonId);

    List<Comment> findByUserId(Integer userId);

    @Query("""
            SELECT c FROM Comment c
            JOIN FETCH  c.likes l WHERE l.id = :id
                """)
    List<Comment> findByLikes(@Param("id") Integer id);

}
