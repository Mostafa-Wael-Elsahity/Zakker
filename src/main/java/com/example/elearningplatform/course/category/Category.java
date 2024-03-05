package com.example.elearningplatform.course.category;

import java.util.List;

import com.example.elearningplatform.course.Course;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.ToString;

@Entity
@Data
@Table(name = "category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @NotBlank
    @ToString.Exclude
    @Column(name = "description")
    private String description;

}
