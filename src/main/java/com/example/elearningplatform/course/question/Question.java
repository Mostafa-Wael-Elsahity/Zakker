package com.example.elearningplatform.course.question;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.example.elearningplatform.course.lesson.Lesson;
import com.example.elearningplatform.course.question.reply.Reply;
import com.example.elearningplatform.user.User;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.ToString;

@Data
@Entity
@Table(name = "question")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotEmpty(message = "Content cannot be empty")
    private String content;
    private LocalDate creationDate;

    private LocalDate modificationDate;
    @ManyToMany(fetch = jakarta.persistence.FetchType.EAGER)
    @ToString.Exclude
    @JoinTable(name = "question_votes", joinColumns = @JoinColumn(name = "question_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> votes = new ArrayList<>();

    @OneToMany
    @ToString.Exclude
    private List<Reply> replys = new ArrayList<>();
    
    @ManyToOne
    @ToString.Exclude
    @JoinColumn(name = "user_id")
    private User user;
    
    @ManyToOne
    @ToString.Exclude
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;

    public void addVote(User user) {
        this.votes.add(user);
    }
    public Question() {
        this.creationDate = LocalDate.now();
        this.modificationDate = LocalDate.now();
    }

}
