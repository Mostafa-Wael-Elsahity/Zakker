package com.example.elearningplatform.course;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.example.elearningplatform.course.category.Category;
import com.example.elearningplatform.course.review.Review;
import com.example.elearningplatform.course.review.ReviewDto;
import com.example.elearningplatform.course.section.Section;
import com.example.elearningplatform.course.section.SectionDto;
import com.example.elearningplatform.course.tag.Tag;
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
import lombok.ToString;

@Entity
@Data
@Table(name = "course")
public class Course {

        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE)
        private Integer id;

        private String title;
        private String whatYouWillLearn;
        private String prerequisite;
        private Integer numberOfStudents;

        private String description;

        private String language;

        private String level;

        private Double price;

        private BigDecimal duration;

        private byte[] image;

        private boolean isPublished;

        private LocalDate creationDate;

        private LocalDate lastUpdateDate;

        private Double averageRating;

        private Integer numberOfRatings;

        @Column(name = "number_of_enrollments")
        private Integer numberOfEnrollments;

        @OneToMany(fetch = jakarta.persistence.FetchType.EAGER)
        @ToString.Exclude
        private List<Section> sections;

        @OneToMany(mappedBy = "course")
        @ToString.Exclude
        private List<Review> reviews;
        // @ManyToMany(fetch = FetchType.EAGER)
        // @JoinTable(name = "course_users", joinColumns = @JoinColumn(name = "course_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
        // private List<User> courses;
        @ManyToMany(fetch = jakarta.persistence.FetchType.EAGER)
        @ToString.Exclude
        @JoinTable(name = "course_tag", joinColumns = @JoinColumn(name = "course_id", unique = false), inverseJoinColumns = @JoinColumn(name = "tag_id", unique = false))
        private List<Tag> tags;

        @ManyToMany(fetch = jakarta.persistence.FetchType.EAGER)
        @ToString.Exclude
        @JoinTable(name = "instructed_courses", joinColumns = @JoinColumn(name = "course_id", unique = false), inverseJoinColumns = @JoinColumn(name = "user_id", unique = false))
        private List<User> instructors;

        @ManyToMany(fetch = jakarta.persistence.FetchType.EAGER)
        @ToString.Exclude
        @JoinTable(name = "course_category", joinColumns = @JoinColumn(name = "course_id", unique = false), inverseJoinColumns = @JoinColumn(name = "category_id", unique = false))
        private List<Category> categories;

        // @ManyToMany(mappedBy = "courses")
        // private List<Cart> carts = new ArrayList<>();
       
}
