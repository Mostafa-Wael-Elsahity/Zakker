package com.example.elearningplatform.entity.course;

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
}
