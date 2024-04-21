package com.example.elearningplatform.course.review;

import java.time.LocalDate;


import com.example.elearningplatform.user.UserDto;


import lombok.Data;

@Data
public class ReviewDto {

    private Integer id;

    private String content;
    private Double rating;

    private Boolean isVoted = false;

    private LocalDate creationDate;

    private LocalDate modificationDate;

    private UserDto user;

    public ReviewDto(Review review, Boolean isVoted) {
        if (review == null)
            return;

        this.isVoted = isVoted;
        this.id = review.getId();
        this.content = review.getContent();
        this.rating = review.getRating();
        this.creationDate = review.getCreationDate();
        this.modificationDate = review.getModificationDate();
        this.user = new UserDto(review.getUser());

    }

}
