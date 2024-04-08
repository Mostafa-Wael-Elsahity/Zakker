package com.example.elearningplatform.course.dto;

import java.sql.SQLException;

import com.example.elearningplatform.course.CourseService;
import com.example.elearningplatform.user.User;

import lombok.Data;

@Data
public class InstructorDto {
    private Integer id;

    private String email;
    private String profilePicture;

    public InstructorDto(User user) throws SQLException {
        this.id = user.getId();
        this.email = user.getEmail();
        this.profilePicture = CourseService.imageConverter(user.getProfilePicture());

    }

}
