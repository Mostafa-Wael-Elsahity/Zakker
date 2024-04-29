package com.example.elearningplatform.course.reply;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.example.elearningplatform.course.comment.Comment;
import com.example.elearningplatform.course.reply.dto.CreateReplyRequest;
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

public class Reply {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    private String content;
    private LocalDateTime creationDate;
    private LocalDateTime modificationDate;
    @ManyToOne(fetch = jakarta.persistence.FetchType.EAGER)
    @ToString.Exclude
    @JoinColumn(name = "user_id")
    User user;

    @ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @ManyToMany(fetch = jakarta.persistence.FetchType.LAZY)
    @ToString.Exclude
    @JoinTable(name = "reply_likes", joinColumns = @JoinColumn(name = "reply_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    List<User> likes = new ArrayList<>();

    private Integer numberOfLikes = 0;

    public void incrementNumberOfLikes() {
        this.numberOfLikes++;
    }

    public void decrementNumberOfLikes() {
        this.numberOfLikes--;
    }

    public  Reply(CreateReplyRequest createReply, Comment comment, User user) {
        this.comment = comment;
        this.content = createReply.getContent();

        this.creationDate = LocalDateTime.now();
        this.modificationDate = LocalDateTime.now();
        this.user = user;
        this.content = createReply.getContent();

    }



}
