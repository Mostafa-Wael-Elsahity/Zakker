package com.example.elearningplatform.course.section.lesson.question.comment;

import java.util.ArrayList;
import java.util.List;

import com.example.elearningplatform.course.section.lesson.Lesson;
import com.example.elearningplatform.course.section.lesson.question.Question;
import com.example.elearningplatform.user.User;

import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;

import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity

@Table(name = "Comment", indexes = {

        @Index(name = "lesson_comment_id_index", columnList = "lesson_id", unique = false),
        @Index(name = "user_comment_id_index", columnList = "user_id", unique = false)
})
public class Comment extends Question {

    @ManyToOne(fetch = jakarta.persistence.FetchType.EAGER)
    @ToString.Exclude
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;

    @ManyToMany(fetch = jakarta.persistence.FetchType.EAGER)
    @ToString.Exclude
    @JoinTable(name = "comment_likes", joinColumns = @JoinColumn(name = "comment_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    List<User> likes = new ArrayList<>();

    private Integer numberOfReplies = 0;

    private Integer numberOfLikes = 0;

    public void addLike(User user) {
        numberOfLikes++;
        likes.add(user);
    }

    public void removeLike(User user) {
        numberOfLikes--;
        likes.remove(user);
    }

    public void incrementNumberOfReplies() {

        this.numberOfReplies++;
    }

    public void decrementNumberOfReplies() {
        this.numberOfReplies--;
    }

    public Comment(CreateComment createComment, User user, Lesson lesson) {
        super(createComment, user);
        this.lesson = lesson;

    }

    public Comment() {
        super();
    }

}
