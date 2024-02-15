package com.example.elearningplatform.entity.course;

import com.example.elearningplatform.entity.user.User;
import jakarta.persistence.*;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "review")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "content")
    private String content;

    @Column(name = "rating")
    private float rating;

    // @ManyToOne
    // @JoinColumn(name = "user_id")
    // private User user;

    // @ManyToOne
    // @JoinColumn(name = "course_id")
    // private Course course;

    @Column(name = "creation_date")
    private LocalDate creationDate;

    @Column(name = "modification_date")
    private LocalDate modificationDate;
}
