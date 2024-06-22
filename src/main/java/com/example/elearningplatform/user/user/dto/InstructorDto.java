package com.example.elearningplatform.user.user.dto;

import com.example.elearningplatform.user.user.User;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class InstructorDto {
    private Integer id;
    private String firstName;
    private String lastName;
    private String imageUrl;

    public InstructorDto(User user) {
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.imageUrl = user.getImageUrl();
    }
    
}
