package com.example.elearningplatform.course.lesson;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LessonVideoDto extends LessonDto {
    private String videoUrl;
}
