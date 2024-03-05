package com.example.elearningplatform.course.comment;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.example.elearningplatform.base.BaseRepository;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@Service
public class CommentService extends BaseRepository {
    @Autowired
    JdbcTemplate jdbcTemplate;

    /******************************************************************************************************* */
    // public List<Comment> getAllComments() {
    // String sql = "SELECT * FROM comments";
    // return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Comment.class));
    // }

    /******************************************************************************************************* */

    // public Comment getCommentById(Integer id) {
    // String sql = "SELECT * FROM comments WHERE id = " + id;
    // List<Comment> comments = jdbcTemplate.query(sql, new
    // BeanPropertyRowMapper<>(Comment.class));
    // if (comments.isEmpty()) {
    // return null;
    // }
    // return comments.get(0);
    // }

    /******************************************************************************************************* */

    public List<Comment> getCommentsByLessonId(Integer id) {
        String sql = "SELECT * FROM comments WHERE lesson_id = " + id;
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Comment.class));
    }

    /******************************************************************************************************* */

    public List<Comment> getCommentsByUserId(Integer id) {
        String sql = "SELECT * FROM comments WHERE user_id = " + id;
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Comment.class));
    }

    /******************************************************************************************************* */

    public List<Comment> getCommentsByParentId(Integer id) {
        String sql = "SELECT * FROM comments WHERE parent_id = " + id;
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Comment.class));
    }

    /******************************************************************************************************* */

    public List<Comment> getCommentsByCourseId(Integer id) {
        String sql = "SELECT * FROM comments WHERE course_id = " + id;
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Comment.class));
    }

    /******************************************************************************************************* */
    // @Transactional
    // public void deleteComment(Integer id) {
    // String sql = "DELETE FROM comments WHERE id = " + id;
    // jdbcTemplate.update(sql);
    // }

    // /*******************************************************************************************************
    // */
    // @Transactional
    // public void updateComment(Comment comment) {
    // String sql = "UPDATE comments SET content = '" + comment.getContent() + "'
    // WHERE id = " + comment.getId();
    // jdbcTemplate.update(sql);
    // }

    /******************************************************************************************************* */
    // @Transactional
    // public void saveComment(Comment comment) {
    // entityManager.persist(comment);
    // }

    /******************************************************************************************************* */
    // @Transactional
    // public void addComment(Comment comment) {
    // String sql = "INSERT INTO comments (content, parent_id, user_id, lesson_id)
    // VALUES (?, ?, ?, ?)";
    // jdbcTemplate.update(sql, comment.getContent(), comment.getParentId(),
    // comment.getUser().getId(),
    // comment.getLesson().getId());
    // }
    /******************************************************************************************************* */

    /******************************************************************************************************* */

}
