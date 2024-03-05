package com.example.elearningplatform.course.cart;

import java.util.ArrayList;
import java.util.List;

import com.example.elearningplatform.course.Course;
import com.example.elearningplatform.user.User;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;

@Entity
@Table(name = "cart")
@Data
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "total_price")
    @NotBlank
    private Double totalPrice;

    @Column(name = "total_quantity")
    private Integer totalQuantity;

    @OneToOne
    @ToString.Exclude
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToMany
    @ToString.Exclude
    @JoinTable(name = "courses-in-cart", joinColumns = {
            @JoinColumn(name = "COURSE_ID", referencedColumnName = "ID") }, inverseJoinColumns = {
                    @JoinColumn(name = "CART_ID", referencedColumnName = "ID") })
    private List<Course> courses = new ArrayList<>();
}
