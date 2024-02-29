package com.example.elearningplatform.entity.course;

import java.util.List;

import com.example.elearningplatform.course.Course;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Entity
@Data
@Table(name = "category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @NotBlank
    @Column(name = "description")
    private String description;

    @ManyToMany(mappedBy = "categories")
    private List<Course> courses;
}
