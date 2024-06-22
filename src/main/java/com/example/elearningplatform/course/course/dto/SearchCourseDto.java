package com.example.elearningplatform.course.course.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.example.elearningplatform.course.category.Category;
import com.example.elearningplatform.course.course.Course;
import com.example.elearningplatform.course.tag.CourseTag;
import com.example.elearningplatform.course.tag.CourseTagDto;
import com.example.elearningplatform.user.user.User;
import com.example.elearningplatform.user.user.dto.InstructorDto;

import lombok.Data;

@Data
public class SearchCourseDto {
    private Integer id;
    private Integer numberOfEnrollments = 0;
    private String title;
    private Double price;
    private BigDecimal duration;
    private String language;
    private String level;
    private Integer numberOfRatings;
    // private byte[] image;
    private String imageUrl;
    private Double averageRating;
    private List<Category> categories = new ArrayList<>();
    private List<InstructorDto> instructors = new ArrayList<>();
    private List<CourseTagDto> tags = new ArrayList<>();

    public void addInstructor(InstructorDto instructor) {
        if (instructor == null)
            return;
        this.instructors.add(instructor);
    }

    public void addCategory(Category category) {
        if (category == null)
            return;
        this.categories.add(category);
    }

    public SearchCourseDto(Course course, List<User> instructors, List<Category> categories, List<CourseTag> tags) {
        if (course == null)
            return;
        this.numberOfEnrollments = course.getNumberOfEnrollments();
        this.id = course.getId();
        this.title = course.getTitle();
        this.price = course.getPrice();
        this.duration = course.getDuration();
        this.language = course.getLanguage();
        this.level = course.getLevel();
        this.numberOfRatings = course.getNumberOfRatings();
        this.imageUrl = course.getImageUrl();
        if (course.getTotalRatings() == 0)
            this.averageRating = 0.0;
        else if (course.getNumberOfRatings() != null && course.getTotalRatings() != null
                && course.getNumberOfRatings() != 0) {
            this.averageRating = (course.getTotalRatings() /
                    course.getNumberOfRatings());
        }
        this.tags = tags.stream().map(tag -> new CourseTagDto(tag)).toList();
        this.instructors = instructors.stream().map(user -> new InstructorDto(user)).toList();
        this.categories = categories;

    }

}
