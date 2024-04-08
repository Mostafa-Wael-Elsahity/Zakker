package com.example.elearningplatform.course.dto;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.example.elearningplatform.course.Course;
import com.example.elearningplatform.course.section.SectionLessonDto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CourseSubscripeDto extends SerchCourseDto {

    private String description;
    private LocalDate creationDate;
    private LocalDate lastUpdateDate;

    private List<SectionLessonDto> sections = new ArrayList<>();

    public void addSection(SectionLessonDto section) {

        this.sections.add(section);
    }

    public CourseSubscripeDto(Course course) throws SQLException {
        super(course);
        this.description = course.getDescription();
        this.creationDate = course.getCreationDate();
        this.lastUpdateDate = course.getLastUpdateDate();

    }
}
