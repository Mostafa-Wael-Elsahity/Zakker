package com.example.elearningplatform.course.review;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
public class ReviewController {
    @GetMapping("/review")
    public String review() {
        return "review";
    }
    
}
