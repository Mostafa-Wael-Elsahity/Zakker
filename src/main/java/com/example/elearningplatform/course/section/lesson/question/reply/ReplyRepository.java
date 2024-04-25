package com.example.elearningplatform.course.section.lesson.question.reply;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReplyRepository extends JpaRepository<Reply, Integer> {
    List<Reply> findByCommentId(Integer parentCommentId);

    @Query("""
            SELECT r FROM Reply r
            JOIN FETCH  r.likes l WHERE l.id = :id
                """)
    List<Reply> findByLikes(@Param("id") Integer id);
}
