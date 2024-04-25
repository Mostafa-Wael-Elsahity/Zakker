package com.example.elearningplatform.payment.copoun;

import java.time.LocalDateTime;
import java.util.Random;

import com.example.elearningplatform.course.Course;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Entity
@Data
@RequiredArgsConstructor
public class Copoun {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String code;
    private LocalDateTime expirationDate;
    private Integer numberOfCopouns;
    private Double discount;
    @ManyToOne(fetch = jakarta.persistence.FetchType.EAGER)
    @ToString.Exclude
    @JoinColumn(name = "course_id")
    private Course course;

  

}
