package com.example.elearningplatform.course;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.example.elearningplatform.course.category.Category;
import com.example.elearningplatform.course.review.Review;
import com.example.elearningplatform.course.section.Section;
import com.example.elearningplatform.course.tag.Tag;
import com.example.elearningplatform.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.ToString;

@Entity
@Data
@Table(name = "course")
public class Course {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id")
        private Integer id;

        @Column(name = "title")
        private String title;

        @Column(name = "description")
        private String description;

        @Column(name = "language")
        private String language;

        @Column(name = "level")
        private String level;

        @Column(name = "price")
        private Double price;

        @Column(name = "duration")
        private BigDecimal duration;

        @Column(name = "imageUrl")
        private String imageUrl;

        @Column(name = "is_published")
        private boolean isPublished;

        @Column(name = "creation_date")
        private LocalDate creationDate;

        @Column(name = "last_update_date")
        private LocalDate lastUpdateDate;

        @Column(name = "average_rating")
        private Double averageRating;

        @Column(name = "number_of_ratings")
        private Integer numberOfRatings;

        @Column(name = "number_of_enrollments")
        private Integer numberOfEnrollments;

        @OneToMany(mappedBy = "course")
        @ToString.Exclude
        private List<Section> sections;

        @OneToMany
        @ToString.Exclude
        private List<Review> reviews;

        @ManyToMany
        @ToString.Exclude
        @JoinTable(name = "course_tag", joinColumns = @JoinColumn(name = "course_id", unique = false), inverseJoinColumns = @JoinColumn(name = "tag_id", unique = false))
        private List<Tag> tags = new ArrayList<>();

        @ManyToMany
        @ToString.Exclude
        @JoinTable(name = "instructed_courses", joinColumns = @JoinColumn(name = "course_id", unique = false), inverseJoinColumns = @JoinColumn(name = "user_id", unique = false))
        private List<User> instructors = new ArrayList<>();;

        @ManyToMany
        @ToString.Exclude
        @JoinTable(name = "course_category", joinColumns = @JoinColumn(name = "course_id", unique = false), inverseJoinColumns = @JoinColumn(name = "category_id", unique = false))
        private List<Category> categories = new ArrayList<>();;

        // @ManyToMany(mappedBy = "courses")
        // private List<Cart> carts = new ArrayList<>();
}
