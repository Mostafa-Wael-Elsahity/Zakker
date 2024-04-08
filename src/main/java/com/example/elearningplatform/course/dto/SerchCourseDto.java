package com.example.elearningplatform.course.dto;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.example.elearningplatform.course.Course;
import com.example.elearningplatform.course.CourseService;
import lombok.Data;

@Data
public class SerchCourseDto {
    private Integer id;
    private String title;
    private Double price;
    private BigDecimal duration;
    private String language;
    private String level;
    private Integer numberOfRatings;
    private String imageUrl;
    private Double averageRating;
    private List<InstructorDto> instructors = new ArrayList<>();;

    public void addInstructor(InstructorDto instructor) {

        this.instructors.add(instructor);
    }

    public SerchCourseDto(Course course) throws SQLException {
        this.id = course.getId();
        this.title = course.getTitle();
        this.price = course.getPrice();
        this.duration = course.getDuration();
        this.language = course.getLanguage();
        this.level = course.getLevel();
        this.numberOfRatings = course.getNumberOfRatings();
        this.imageUrl = CourseService.imageConverter(course.getImageUrl());
        this.averageRating = course.getAverageRating();
    }

}