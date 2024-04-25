package com.example.elearningplatform.course.section.lesson.question.reply;

import java.util.ArrayList;
import java.util.List;

import com.example.elearningplatform.course.section.lesson.question.Question;
import com.example.elearningplatform.course.section.lesson.question.comment.Comment;
import com.example.elearningplatform.user.User;

import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@Table(name = "reply", indexes = {

        @Index(name = "comment_reply_id_index", columnList = "comment_id", unique = false),
        @Index(name = "user_reply_id_index", columnList = "user_id", unique = false)
})

public class Reply extends Question {
    @ManyToOne(fetch = jakarta.persistence.FetchType.EAGER)
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @ManyToMany(fetch = jakarta.persistence.FetchType.EAGER, cascade = jakarta.persistence.CascadeType.ALL)
    @ToString.Exclude
    @JoinTable(name = "reply_likes", joinColumns = @JoinColumn(name = "reply_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    List<User> likes = new ArrayList<>();

    private Integer numberOfLikes = 0;

    public void addLike(User user) {
        numberOfLikes++;
        likes.add(user);
    }

    public void removeLike(User user) {
        numberOfLikes--;
        likes.remove(user);
    }

    public Reply(CreateReply createReply, User user, Comment comment) {
        super(createReply, user);
        this.comment = comment;

    }
}
