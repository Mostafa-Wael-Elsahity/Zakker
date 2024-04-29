package com.example.elearningplatform.course.comment;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.elearningplatform.course.course.Course;
import com.example.elearningplatform.course.lesson.Lesson;
import com.example.elearningplatform.user.user.User;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

    List<Comment> findByLessonId(Integer lessonId, Pageable pageable);

    List<Comment> findByUserId(Integer userId);

    /**************************************************************************** */

    @Query("""
            SELECT c.likes FROM Comment c
            WHERE c.id = :id
            """)
    List<User> findLikes(@Param("id") Integer id);

    /**************************************************************************** */
    @Modifying
    @Query(value = """
                DELETE FROM comment_likes
                WHERE user_id = :userId AND comment_id = :commentId
            """, nativeQuery = true)
    void removeLikeFromComment(@Param("userId") Integer userId, @Param("commentId") Integer commentId);

    /**************************************************************************** */
    @Modifying
    @Query(value = """
                INSERT INTO comment_likes (user_id, comment_id)
                VALUES (:userId, :commentId)
            """, nativeQuery = true)
    void likeComment(@Param("userId") Integer userId, @Param("commentId") Integer commentId);

    /**************************************************************************** */
    // @Query("""
    // SELECT c.likes FROM Comment c
    // join c.likes l WHERE l.id = :userId AND c.id=:commentId
    // """)
    // Optional<User> findLikeByUserIdAndCommentId(@Param("commentId") Integer
    // commentId, @Param("userId") Integer userId);
    @Query("""
                SELECT c FROM Comment c
                JOIN c.likes l
                WHERE l.id = :userId AND c.lesson.id = :lessonId
            """)
    List<Comment> findLikedCommentsByUserIdAndLesson(@Param("userId") Integer userId,
            @Param("lessonId") Integer lessonId);

    /**************************************************************************** */
    @Query("""
            SELECT c.lesson.section.course FROM Comment c
            WHERE c.id = :id
            """)
    Optional<Course> findCourseByCommentId(@Param("id") Integer id);

    /**************************************************************************** */
    @Query("""
            SELECT c.lesson FROM Comment c
            WHERE c.id = :id
            """)
    Optional<Lesson> findLesson(@Param("id") Integer id);

    /**************************************************************************** */

}
