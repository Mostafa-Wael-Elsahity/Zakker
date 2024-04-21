package com.example.elearningplatform.course;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.example.elearningplatform.user.UserDto;

import lombok.Data;

@Data
public class SearchCourseDto {
    private Integer id;
    private Integer numberOfStudents;
    private String title;
    private Double price;
    private BigDecimal duration;
    private String language;
    private String level;
    private Integer numberOfRatings;
    // private byte[] image;
    private Double averageRating;
    private List<UserDto> instructors = new ArrayList<>();
    public SearchCourseDto(Course course)  {
        if(course == null) return;
        this.numberOfStudents = course.getNumberOfStudents();
        this.id = course.getId();
        this.title = course.getTitle();
        this.price = course.getPrice();
        this.duration = course.getDuration();
        this.language = course.getLanguage();
        this.level = course.getLevel();
        this.numberOfRatings = course.getNumberOfRatings();
        // if(course.getImage()!=null)
        // this.image = course.getImage();
        this.averageRating = course.getAverageRating();
        course.getInstructors().forEach(instructor -> {
            this.instructors.add(new UserDto(instructor));
        });
    }
    
}

