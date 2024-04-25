package com.example.elearningplatform.payment.copoun;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.elearningplatform.course.Course;
import com.example.elearningplatform.course.CourseRepository;
import com.example.elearningplatform.payment.copoun.Dto.CreateRequest;
import com.example.elearningplatform.response.Response;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CopounService {
    private final CopounRepository copounRepository;
    private final CourseRepository courseRepository;

    /******************************************************************************************** */
    public Response createCopoun(CreateRequest request) {
        try {

            List<Copoun> copouns = copounRepository.findByCourseId(request.getCourseId());
            for (Copoun copoun : copouns) {
                if (copoun.getCode().equals(request.getCode())) {
                    return new Response(HttpStatus.BAD_REQUEST, "copoun code already exists", null);

                }
            }

            Course course = courseRepository.findById(request.getCourseId())
                    .orElseThrow(() -> new Exception("Course not found"));

            Copoun copoun = new Copoun();
            copoun.setExpirationDate(LocalDateTime.now().plusDays(request.getExpirationDate()));
            copoun.setNumberOfCopouns(request.getNumberOfCopouns());
            copoun.setDiscount(request.getDiscount());
            copoun.setCourse(course);
            copoun.setCode(request.getCode());
            copounRepository.save(copoun);

            return new Response(HttpStatus.OK, "copoun created successfully", copoun);
        } catch (

        Exception e) {
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", e.getMessage());
        }
    }

    /******************************************************************************************** */
    public Response deleteCopoun(Integer copounId) {
        try {
            Copoun copoun = copounRepository.findById(copounId).orElseThrow(() -> new Exception("copoun not found"));
            copounRepository.delete(copoun);
            return new Response(HttpStatus.OK, "copoun deleted successfully", null);
        } catch (Exception e) {
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", e.getMessage());
        }
    }

    /******************************************************************************************** */

}
