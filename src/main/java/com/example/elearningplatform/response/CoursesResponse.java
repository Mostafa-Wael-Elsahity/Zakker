package com.example.elearningplatform.response;

import org.springframework.http.HttpStatus;

import lombok.Data;

@Data
public class CoursesResponse {
    private HttpStatus status;
    private String message;
    private Integer numberOfPages;
    private Object data;

    public CoursesResponse(HttpStatus status, String message, Integer numberOfPages, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
        this.numberOfPages = numberOfPages;

    }
}
