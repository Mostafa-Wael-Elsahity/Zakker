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

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@RestController
@SecurityRequirement(name = "bearerAuth")
public class SectionController {
    @Autowired
    private SectionService sectionService;

    @PostMapping("/create-section")
    public Response createSection(@RequestBody @Valid CreateSectionRequest createSectionRequest,
            BindingResult bindingResult)
            throws IOException, InterruptedException {
        if (bindingResult.hasErrors()) {
            return new Response(HttpStatus.BAD_REQUEST, "Validation Error", bindingResult.getAllErrors());
        }

        return sectionService.createSection(createSectionRequest);
    }

    /**
     * ******************************************************************************
     */
    @PostMapping("/update-section")
    public Response updateSection(@RequestBody @Valid UpdateSectionRequest updateSectionRequest,
            BindingResult bindingResult)
            throws IOException, InterruptedException {
        if (bindingResult.hasErrors()) {
            return new Response(HttpStatus.BAD_REQUEST, "Validation Error", bindingResult.getAllErrors());
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
