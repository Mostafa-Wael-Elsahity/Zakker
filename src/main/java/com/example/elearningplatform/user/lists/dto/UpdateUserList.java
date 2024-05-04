package com.example.elearningplatform.user.lists.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class UpdateUserList {
    @NotEmpty(message = "Name cannot be empty")
    private String name;
    @NotEmpty(message = "Description cannot be empty")
    private String description;
    private Integer listId;

}
