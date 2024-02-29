package com.example.elearningplatform.course;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.example.elearningplatform.entity.course.Cart;
import com.example.elearningplatform.entity.course.Category;
import com.example.elearningplatform.entity.course.Review;
import com.example.elearningplatform.entity.course.Section;
import com.example.elearningplatform.entity.course.Tag;
import com.example.elearningplatform.user.User;

import jakarta.persistence.*;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Entity
@Data
@Table(name = "course")
public class Course {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id")
        private Long id;

        @NotBlank
        @Column(name = "title")
        private String title;

        @NotBlank
        @Column(name = "description")
        private String description;

        @NotBlank
        @Column(name = "language")
        private String language;

        @NotBlank
        @Column(name = "level")
        private String level;

        @NotBlank
        @Column(name = "price")
        private double price;

        @Column(name = "duration")
        private Duration duration;

        @NotBlank
        @Column(name = "imageUrl")
        private String imageUrl;

        @Column(name = "is_published")
        private boolean isPublished;

        @Column(name = "creation_date")
        private LocalDate creationDate;

        @Column(name = "last_update_date")
        private LocalDate lastUpdateDate;

        @Column(name = "average_rating")
        private float averageRating;

        @Column(name = "number_of_ratings")
        private int numberOfRatings;

        @Column(name = "number_of_enrollments")
        private int numberOfEnrollments;

        @ManyToMany
        @JoinTable(name = "course_tag", joinColumns = @JoinColumn(name = "course_id"), inverseJoinColumns = @JoinColumn(name = "tag_id"))
        private List<Tag> tags;

        @OneToMany(mappedBy = "course")
        private List<Review> reviews;
        @ManyToMany
        @JoinTable(name = "instructed_courses", joinColumns = @JoinColumn(name = "course_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
        private List<User> instructors;
        @ManyToMany
        @JoinTable(name = "course_users", joinColumns = @JoinColumn(name = "course_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
        private List<User> users;

        @OneToMany(mappedBy = "course")
        private List<Section> sections;

        @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
        @JoinTable(name = "course_category", joinColumns = {
                        @JoinColumn(name = "COURSE_ID", referencedColumnName = "ID") }, inverseJoinColumns = {
                                        @JoinColumn(name = "CATEGORY_ID", referencedColumnName = "ID") })
        private List<Category> categories = new ArrayList<>();

        @ManyToMany(mappedBy = "courses")
        private List<Cart> carts = new ArrayList<>();
}
