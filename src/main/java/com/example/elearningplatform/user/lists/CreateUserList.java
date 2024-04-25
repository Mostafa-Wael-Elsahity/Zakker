package com.example.elearningplatform.user.lists;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class CreateUserList {

    @NotEmpty(message = "Name cannot be empty")
    private String name;
    @NotEmpty(message = "Description cannot be empty")
    private String description;


}
