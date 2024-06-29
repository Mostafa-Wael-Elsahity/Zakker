package com.example.elearningplatform.course.section;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.elearningplatform.course.section.dto.CreateSectionRequest;
import com.example.elearningplatform.course.section.dto.UpdateSectionRequest;
import com.example.elearningplatform.response.Response;
import com.example.elearningplatform.validator.Validator;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@RestController
@SecurityRequirement(name = "bearerAuth")
public class SectionController {
    @Autowired
    private SectionService sectionService;

    @PostMapping("/create-section")
    public Response createSection(@RequestBody @Valid CreateSectionRequest createSectionRequest,
            BindingResult result)
            throws IOException, InterruptedException {
                if (result.hasErrors()) {
                    return Validator.validate(result);
                }
        return sectionService.createSection(createSectionRequest);
    }

    /**
     * ******************************************************************************
     */
    @PostMapping("/update-section")
    public Response updateSection(@RequestBody @Valid UpdateSectionRequest updateSectionRequest,
            BindingResult result)
            throws IOException, InterruptedException {
        if (result.hasErrors()) {
            return Validator.validate(result);
        }

        return sectionService.updateSection(updateSectionRequest);
    }

    /**
     * ******************************************************************************
     */
    @DeleteMapping("/delete-section")
    public Response deleteSection(@RequestParam("sectionId") Integer sectionId)
            throws IOException, InterruptedException {

        return sectionService.deleteSection(sectionId);
    }

    /**
     * ******************************************************************************
     */
}
