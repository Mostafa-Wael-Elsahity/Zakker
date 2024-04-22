package com.example.elearningplatform.course.review;

import java.time.LocalDate;

import com.example.elearningplatform.course.Course;
import com.example.elearningplatform.user.User;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@Table(name = "review", uniqueConstraints = @UniqueConstraint(columnNames = { "user_id", "course_id" }))
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

    @ManyToOne(fetch = jakarta.persistence.FetchType.EAGER)
    @ToString.Exclude
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = jakarta.persistence.FetchType.EAGER)
    @ToString.Exclude
    @JoinColumn(name = "course_id")
    private Course course;

    public Review() {
        this.creationDate = LocalDate.now();
        this.modificationDate = LocalDate.now();
    }

}
