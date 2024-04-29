package com.example.elearningplatform.user.user.dto;

import java.util.ArrayList;
import java.util.List;

import com.example.elearningplatform.course.course.dto.SearchCourseDto;
import com.example.elearningplatform.user.user.User;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDto {
    
    private Integer id;
    private String firstName;
    private String lastName;
    private String about;
    private Integer numberOfEnrollments=0;
    private Integer numberOfCourses=0;
    private String imageUrl;
    List<SearchCourseDto> instructoredCourses = new ArrayList<>();

    // private String email;
    public UserDto(User user) {
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.about = user.getAbout();
        this.numberOfEnrollments = 0;
        this.numberOfCourses = 0;
        this.imageUrl = "https://via.placeholder.com/300x150";
        // this.email = user.getEmail();
    
        
    }


}
