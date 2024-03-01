package com.example.elearningplatform.course;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

import com.example.elearningplatform.entity.Category;
import com.example.elearningplatform.entity.Review;
import com.example.elearningplatform.entity.Section;
import com.example.elearningplatform.entity.Tag;
import com.example.elearningplatform.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "course")
public class Course {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id")
        private Long id;

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
        private Duration duration;

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

        @ManyToMany
        @JoinTable(name = "course_tag", joinColumns = @JoinColumn(name = "course_id"), inverseJoinColumns = @JoinColumn(name = "tag_id"))
        private List<Tag> tags;

        @OneToMany(mappedBy = "course")
        private List<Review> reviews;
        @ManyToMany
        @JoinTable(name = "instructed_courses", joinColumns = @JoinColumn(name = "course_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
        private List<User> instructors;

        @OneToMany(mappedBy = "course")
        private List<Section> sections;

        @ManyToMany
        @JoinTable(name = "course_category", joinColumns = {
                        @JoinColumn(name = "COURSE_ID", referencedColumnName = "ID") }, inverseJoinColumns = {
                                        @JoinColumn(name = "CATEGORY_ID", referencedColumnName = "ID") })
        private List<Category> categories;

        // @ManyToMany(mappedBy = "courses")
        // private List<Cart> carts = new ArrayList<>();
}
