package com.example.elearningplatform.user.lists.dto;

import java.util.ArrayList;
import java.util.List;

import com.example.elearningplatform.course.course.dto.SearchCourseDto;
import com.example.elearningplatform.user.lists.UserList;

import lombok.Data;

@Data
public class UserListDto {

    private Integer id;
    private String name;
    private List<SearchCourseDto> courses = new ArrayList<>();

    public UserListDto(UserList userList) {
        this.id = userList.getId();
        this.name = userList.getName();

    }

}
