package com.example.elearningplatform;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class Response {
    HttpStatus status;
    String message;
    Object data;

    public Response(HttpStatus status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;

    }

    public Response() {
    }
}
