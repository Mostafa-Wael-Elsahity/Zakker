package com.example.elearningplatform.course.comment;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface CommentRepository extends JpaRepository<Comment, Integer> {

    List<Comment> findByLessonId(Integer lessonId);

    List<Comment> findByUserId(Integer userId);
    List<Comment> findByParentCommentId(Integer parentCommentId);

    @Query("""
            SELECT q FROM Comment q
            JOIN FETCH  q.votes v WHERE v.id = :id
                """)
    List<Comment> findByVote(@Param("id") Integer id);

}
