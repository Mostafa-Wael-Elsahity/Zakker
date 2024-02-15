package com.example.elearningplatform.entity.course;

import com.example.elearningplatform.entity.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Entity
@Table(name = "comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "content")
    private String content;

    // @ManyToOne
    // @JoinColumn(name = "user_id")
    // private User user;

    // @ManyToOne
    // @JoinColumn(name = "lesson_id")
    // private Lesson lesson;

    @Column(name = "parent_id")
    private Long parentId;

}
