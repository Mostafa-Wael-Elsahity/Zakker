package com.example.elearningplatform.payment.coupon;

import java.time.LocalDateTime;

import com.example.elearningplatform.course.course.Course;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Entity
@Data
@RequiredArgsConstructor
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"code", "course_id"}))
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String code;
    private LocalDateTime expirationDate;
    private Integer numberOfCoupons;

    @Min(0)
    @Max(100)
    private Integer discount;
    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "course_id")
    private Course course;



}