package com.example.elearningplatform.entity.user;

import com.example.elearningplatform.entity.course.Course;
import jakarta.persistence.*;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Entity
@Data
@Table(name = "list_of_courses")
public class ListOfCourses {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank
    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;
}
