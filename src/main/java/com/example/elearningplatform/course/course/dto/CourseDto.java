package com.example.elearningplatform.course.course.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.example.elearningplatform.course.category.Category;
import com.example.elearningplatform.course.course.Course;
import com.example.elearningplatform.course.section.dto.SectionDto;
import com.example.elearningplatform.course.tag.CourseTag;
import com.example.elearningplatform.user.user.User;

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
    private Boolean isSubscribed = false;

    private List<SectionDto> sections = new ArrayList<>();

    public void addSection(SectionDto section) {
        if (section == null)
            return;
        this.sections.add(section);
    }


    public CourseDto(
            Course course, Boolean isSubscribed,
            List<User> instructorList, List<Category> categories, List<CourseTag> tags) {
        super(course, instructorList, categories, tags);
        if (course == null)
            return;
        this.description = course.getDescription();
        this.creationDate = course.getCreationDate();
        this.lastUpdateDate = course.getLastUpdateDate();
        this.whatYouWillLearn = course.getWhatYouWillLearn();
        this.prerequisite = course.getPrerequisite();
        this.isSubscribed = isSubscribed;

    }

}
