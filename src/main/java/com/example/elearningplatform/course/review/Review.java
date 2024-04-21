package com.example.elearningplatform.course.review;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.example.elearningplatform.course.Course;
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
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@Table(name = "review")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotEmpty(message = "Content cannot be empty")
    private String content;

    @NotNull(message = "Rating cannot be empty")
    private Double rating;

    private LocalDate creationDate;

    private LocalDate modificationDate;
    @ManyToMany
    @ToString.Exclude
    @JoinTable(name = "review_votes", joinColumns = @JoinColumn(name = "review_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> votes = new ArrayList<>();

    @ManyToOne
    @ToString.Exclude
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @ToString.Exclude
    @JoinColumn(name = "course_id")
    private Course course;

    public void addVote(User user) {
        this.votes.add(user);
    }
    public Review() {
        this.creationDate = LocalDate.now();
        this.modificationDate = LocalDate.now();
    }

}
