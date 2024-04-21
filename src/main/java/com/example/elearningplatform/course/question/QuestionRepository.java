package com.example.elearningplatform.course.question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Integer> {

    List<Question> findByLessonId(Integer lessonId);

    List<Question> findByUserId(Integer userId);
    @Query("""
        SELECT q FROM Question q
        JOIN FETCH  q.votes v WHERE v.id = :id
            """)
List<Question> findByVote(@Param("id") Integer id);

}
