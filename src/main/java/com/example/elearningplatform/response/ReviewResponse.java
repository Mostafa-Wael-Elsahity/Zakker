package com.example.elearningplatform.response;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class ReviewResponse extends Response {
    Boolean isReviewd = false;

    public ReviewResponse() {
        super();
        this.isReviewd = false;
    }

    public ReviewResponse(HttpStatus status, String message, Object data) {
        super(status, message, data);

    }

}
