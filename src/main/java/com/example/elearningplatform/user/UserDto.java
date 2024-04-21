package com.example.elearningplatform.user;



import lombok.Data;

@Data
public class UserDto {
    
    private Integer id;

    private String email;

    public UserDto(User user)  {
        if(user == null) return;
        this.id = user.getId();
        this.email = user.getEmail();

    }

}
