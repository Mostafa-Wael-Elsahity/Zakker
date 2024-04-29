package com.example.elearningplatform.course.comment;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.example.elearningplatform.course.lesson.Lesson;
import com.example.elearningplatform.course.reply.Reply;
import com.example.elearningplatform.user.user.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
public class Comment  {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    private String content;
    private LocalDateTime creationDate;
    private LocalDateTime modificationDate;
    private Integer numberOfLikes = 0;
    private Integer numberOfReplies = 0;

    @ManyToOne(fetch = jakarta.persistence.FetchType.EAGER)
    @ToString.Exclude
    @JoinColumn(name = "user_id")
    User user;


    @ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;

    @ManyToMany(fetch = jakarta.persistence.FetchType.LAZY)
    @ToString.Exclude
    @JoinTable(name = "comment_likes", joinColumns = @JoinColumn(name = "comment_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    List<User> likes = new ArrayList<>();

    @OneToMany(mappedBy = "comment",cascade = CascadeType.REMOVE)
    @ToString.Exclude
    List<Reply> replies = new ArrayList<>();


    public void incrementNumberOfReplies() {

        this.numberOfReplies++;
    }
    public void decrementNumberOfReplies() {
        this.numberOfReplies--;
    }
    public void incrementNumberOfLikes() {

        this.numberOfLikes++;
    }
    public void decrementNumberOfLikes() {
        this.numberOfLikes--;
    }


    public Comment() {

        this.creationDate = LocalDateTime.now();
        this.modificationDate = LocalDateTime.now();

    }

    // public Comment() {
    //     super();
    // }

}
