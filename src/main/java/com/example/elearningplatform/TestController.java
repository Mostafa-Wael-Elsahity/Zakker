package com.example.elearningplatform;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {
    @GetMapping("/test")
    public ResponseEntity<String> getMethodName() {
        return new ResponseEntity<>("Hello World", HttpStatus.OK);

    }

}
