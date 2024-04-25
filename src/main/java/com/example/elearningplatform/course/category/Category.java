package com.example.elearningplatform.course.category;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Entity
@Data
@Table(name = "category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    private String name;
    @NotBlank
    private String description;

    private Integer numberOfCourses = 0;

    public void incrementNumberOfCourses() {
        this.numberOfCourses++;
    }

    public void decrementNumberOfCourses() {
        this.numberOfCourses--;
    }

}
