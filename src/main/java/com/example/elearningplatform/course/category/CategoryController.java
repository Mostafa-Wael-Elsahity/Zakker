package com.example.elearningplatform.course.category;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.elearningplatform.response.Response;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    /**
     * Get all categories.
     *
     * @return          The response containing all categories.
     */
    @GetMapping("/category/all")
    public Response findAll() {
        return new Response(HttpStatus.OK, "Success", categoryService.findAll());
    }
}
