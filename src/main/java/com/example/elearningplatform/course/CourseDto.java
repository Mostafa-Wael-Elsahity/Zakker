package com.example.elearningplatform.course;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.example.elearningplatform.course.category.Category;
import com.example.elearningplatform.course.review.ReviewDto;
import com.example.elearningplatform.course.section.SectionDto;

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

    private List<SectionDto> sections = new ArrayList<>();
    private List<ReviewDto> reviews = new ArrayList<>();
    private List<Category> categories = new ArrayList<>();

    public  CourseDto(Course course) {
        super(course);

    }

}
