package com.example.elearningplatform.course;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.example.elearningplatform.course.category.Category;
import com.example.elearningplatform.course.review.ReviewDto;
import com.example.elearningplatform.course.section.SectionDto;
import com.example.elearningplatform.user.UserDto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CourseDto extends SearchCourseDto {

    private String description;
    private LocalDate creationDate;
    private LocalDate lastUpdateDate;
    private String whatYouWillLearn;
    private String prerequisite;
    private Boolean isReviewd=false;
    private Boolean isSubscribed=false;

    private List<SectionDto> sections = new ArrayList<>();
    private List<ReviewDto> reviews = new ArrayList<>();
    private List<Category> categories = new ArrayList<>();
    private List<UserDto> instructors = new ArrayList<>();

    public void addSection(SectionDto section) {
        if (section == null)
            return;
        this.sections.add(section);
    }

    public void addReview(ReviewDto review) {
        if (review == null)
            return;
        this.reviews.add(review);
    }
    public void addInstructor(UserDto user) {
        if (user == null)
            return;
        this.instructors.add(user);
    }

    public void addCategory(Category category) {
        if (category == null)
            return;
        this.categories.add(category);
    }

    public void addReviewinFront(ReviewDto review) {
        if (review == null)
            return;
        this.reviews.addFirst(review);
    }

    public CourseDto(Course course, Boolean isSubscribed) {
        super(course);
  
        this.isSubscribed = isSubscribed;

    }

}
