package com.example.elearningplatform.course.reply;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.elearningplatform.course.comment.Comment;
import com.example.elearningplatform.course.course.Course;

public interface ReplyRepository extends JpaRepository<Reply, Integer> {
    List<Reply> findByCommentId(Integer parentCommentId, Pageable pageable);

    @Query("""
                SELECT r FROM Reply r
                WHERE r.comment.id = :commentId
            """)
    List<Reply> findByComment(@Param("commentId") Integer commentId);

    @Query("""
                SELECT r FROM Reply r
                JOIN r.likes l
                WHERE l.id = :userId AND r.comment.id = :commentId
            """)
    List<Reply> findLikedRepliesByUserIdAndCommentId(@Param("userId") Integer userId,
            @Param("commentId") Integer commentId);

    /************************************************************************************************* */
    @Query("""
                SELECT r.comment FROM Reply r

                WHERE  r.id = :replyId
            """)
    Optional<Comment> findComment(@Param("replyId") Integer replyId);

    /************************************************************************************************* */
    @Query("""
                SELECT r.comment.lesson.section.course FROM Reply r
                WHERE r.id = :replyId
            """)
    Optional<Course> findCourseByReplyId(@Param("replyId") Integer replyId);

    /************************************************************************************************* */
    @Query("""
                SELECT r FROM Reply r
                join r.likes l
                WHERE r.id = :replyId
                And l.id = :userId

            """)
    Optional<Reply> findLikedRepliesByUserId(@Param("replyId") Integer replyId, @Param("userId") Integer userId);

    /************************************************************************************************* */
    @Modifying
    @Query(value = """
                INSERT INTO reply_likes (reply_id, user_id)
                VALUES (:replyId, :userId)
            """, nativeQuery = true)
    void likeReply(@Param("userId") Integer userId, @Param("replyId") Integer replyId);

    /**************************************************************************************************** */
    @Modifying
    @Query(value = """
                DELETE FROM reply_likes
                WHERE user_id = :userId AND reply_id = :replyId
            """, nativeQuery = true)
    void removeLikeFromReply(@Param("userId") Integer userId, @Param("replyId") Integer replyId);

    /**************************************************************************************************** */
   

}
