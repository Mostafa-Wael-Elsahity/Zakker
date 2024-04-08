package com.example.elearningplatform.course.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

    List<Comment> findByLessonId(Integer lessonId);

    List<Comment> findByUserId(Integer userId);

    List<Comment> findByParentId(Integer parentId);
}
