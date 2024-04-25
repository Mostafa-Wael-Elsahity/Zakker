package com.example.elearningplatform.user.cart;

import java.util.ArrayList;
import java.util.List;
import com.example.elearningplatform.course.Course;
import com.example.elearningplatform.user.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Entity
@Table(name = "cart")
@Data
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)

    private Integer id;

    private Double totalPrice = 0.0;

    private Integer numberOfCourses = 0;

    @OneToOne
    @ToString.Exclude
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToMany(fetch = FetchType.EAGER)
    @ToString.Exclude
    @JoinTable(name = "courses_in_cart", joinColumns = {
            @JoinColumn(name = "COURSE_ID", referencedColumnName = "ID") }, inverseJoinColumns = {
                    @JoinColumn(name = "CART_ID", referencedColumnName = "ID") })
    private List<Course> courses = new ArrayList<>();

    public void addCourse(Course course) {
        this.courses.add(course);
        this.numberOfCourses++;
        this.totalPrice += course.getPrice();
    }
}
