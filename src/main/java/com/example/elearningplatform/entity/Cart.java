package com.example.elearningplatform.entity;

import java.util.ArrayList;
import java.util.List;

import com.example.elearningplatform.course.Course;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Entity
@Table(name = "cart")
@Data
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // @OneToOne
    // @JoinColumn(name = "user_id")
    // private User user;

    @Column(name = "total_price")
    @NotBlank
    private double totalPrice;

    @Column(name = "total_quantity")
    private int totalQuantity;

    // @OneToMany(mappedBy = "cart")
    // private List<Course> courses;
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "courses-in-cart", joinColumns = {
            @JoinColumn(name = "COURSE_ID", referencedColumnName = "ID") }, inverseJoinColumns = {
                    @JoinColumn(name = "CART_ID", referencedColumnName = "ID") })
    private List<Course> courses = new ArrayList<>();
}
