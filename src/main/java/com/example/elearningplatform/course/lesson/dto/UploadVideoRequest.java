package com.example.elearningplatform.course.lesson.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class UploadVideoRequest {

    private Integer courseId;
    private Integer lessonId;
    private MultipartFile video;
}
