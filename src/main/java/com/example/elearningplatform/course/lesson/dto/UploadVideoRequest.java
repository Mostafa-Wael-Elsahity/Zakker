package com.example.elearningplatform.course.lesson.dto;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class UploadVideoRequest {

    // private Integer courseId;
    private Integer lessonId;
    @NotEmpty(message = "Video cannot be empty")
    private MultipartFile video;
}
