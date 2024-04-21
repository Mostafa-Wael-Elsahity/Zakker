package com.example.elearningplatform.course.question.reply;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.example.elearningplatform.course.question.Question;
import com.example.elearningplatform.user.User;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.ToString;
@Data
@Entity
public class Reply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
   

    @NotEmpty(message = "Content cannot be empty")
    private String content;

    private LocalDate creationDate;
    private LocalDate modificationDate;

    @ManyToMany
    @ToString.Exclude
    @JoinTable(name = "reply_votes", joinColumns = @JoinColumn(name = "reply_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> usersVoted = new ArrayList<>();

    @ManyToOne
    @ToString.Exclude
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @ToString.Exclude
    @JoinColumn(name = "question_id")
    private Question question;
    public Reply() {
        this.creationDate = LocalDate.now();
        this.modificationDate = LocalDate.now();
        
    }
}
