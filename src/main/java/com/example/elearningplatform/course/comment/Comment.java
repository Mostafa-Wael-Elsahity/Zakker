package com.example.elearningplatform.course.comment;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.example.elearningplatform.course.lesson.Lesson;
import com.example.elearningplatform.user.User;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.ToString;

@Data
@Entity
@Table(name = "Comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotEmpty(message = "Content cannot be empty")
    private String content;
    private LocalDate creationDate;
    private LocalDate modificationDate;
    private Integer numberOfVotes = 0;
    private Integer numberOfSubComments = 0;
    @ManyToOne(fetch = jakarta.persistence.FetchType.EAGER)
    @JoinColumn(name = "parent_id")
    private Comment parentComment;

    @ManyToMany(fetch = jakarta.persistence.FetchType.EAGER)
    @ToString.Exclude
    @JoinTable(name = "Comment_votes", joinColumns = @JoinColumn(name = "Comment_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> votes = new ArrayList<>();

    @ManyToOne(fetch = jakarta.persistence.FetchType.EAGER)
    @ToString.Exclude
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = jakarta.persistence.FetchType.EAGER)
    @ToString.Exclude
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;

    public void incrementNumberOfReplies() {

        this.numberOfSubComments++;
    }

    public void decrementNumberOfReplies() {
        this.numberOfSubComments--;
    }

    public void addVote(User user) {
        this.numberOfVotes++;
        this.votes.add(user);
    }

    public void removeVote(User user) {
        this.numberOfVotes--;
        this.votes.remove(user);
    }

    public Comment() {
        this.creationDate = LocalDate.now();
        this.modificationDate = LocalDate.now();
    }

}
