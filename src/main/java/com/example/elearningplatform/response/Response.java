package com.example.elearningplatform.response;

import org.springframework.http.HttpStatus;

import lombok.Data;

@Data
public class Response {
   private HttpStatus status;
   private String message;
   private Object data;

    public Response(HttpStatus status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;

    }

    public Response() {
    }
}