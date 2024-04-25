package com.example.elearningplatform.payment.copoun;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.elearningplatform.payment.copoun.Dto.CreateRequest;
import com.example.elearningplatform.response.Response;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class CopounController {

    private final CopounService copounService;

    /********************************************************************************* */
    @GetMapping("/create-copoun")
    public Response createCopoun(CreateRequest request) {
        return copounService.createCopoun(request);
    }

    /********************************************************************************* */
    @DeleteMapping("/delete-copoun/{copounId}")
    public Response deleteCopoun(@PathVariable Integer copounId) {
        return copounService.deleteCopoun(copounId);
    }

}
