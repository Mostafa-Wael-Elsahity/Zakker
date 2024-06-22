package com.example.elearningplatform.course.section;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.elearningplatform.course.course.Course;
import com.example.elearningplatform.course.course.CourseRepository;
import com.example.elearningplatform.course.course.CourseService;
import com.example.elearningplatform.course.section.dto.CreateSectionRequest;
import com.example.elearningplatform.course.section.dto.SectionDto;
import com.example.elearningplatform.course.section.dto.UpdateSectionRequest;
import com.example.elearningplatform.exception.CustomException;
import com.example.elearningplatform.response.Response;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;
import lombok.Setter;


@Service
@Setter
@Transactional
public class SectionService {

    @Autowired
    private SectionRepository sectionRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    CourseService courseService;


    /************************************************************************************** */
    /************************************************************************************** */

    public List<Section> getSectionsByCourseId(Integer courseID) {
        return sectionRepository.findByCourseId(courseID);
    }

    /*******************************************************************************************/

    public Response createSection(CreateSectionRequest createSectionRequest) throws IOException, InterruptedException {
        try {
            Course course = courseRepository.findById(createSectionRequest.getCourseId())
                    .orElseThrow(() -> new CustomException("Course not found", HttpStatus.NOT_FOUND));
            // User user = courseRepository.findOwner(course.getId())
            //         .orElseThrow(() -> new CustomException("User not found", HttpStatus.NOT_FOUND));
            // if (user.getId().equals(tokenUtil.getUserId())) {
            //     throw new CustomException("You are not the owner of this course", HttpStatus.UNAUTHORIZED);
            // }

            String ApiKey = course.getApiKey();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(
                            String.format(
                                    "https://video.bunnycdn.com/library/%s/collections", course.getGuid())))
                    .header("accept", "application/json")
                    .header("content-type", "application/json")
                    .header("AccessKey", ApiKey)
                    .method("POST",
                            HttpRequest.BodyPublishers
                                    .ofString("{\"name\":\"" + createSectionRequest.getTitle() + "\"}"))
                    .build();
            HttpResponse<String> response = HttpClient.newHttpClient().send(request,
                    HttpResponse.BodyHandlers.ofString());
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> responseMap = mapper.readValue(response.body(),
                    new TypeReference<Map<String, Object>>() {
                    });
            if (responseMap.get("error") != null) {
                throw new CustomException(responseMap.get("error").toString(), HttpStatus.BAD_REQUEST);
            }
            // System.out.println(response.body());

            Section section = new Section(createSectionRequest);
            section.setGuid(responseMap.get("guid").toString());
            section.setCourse(course);
            sectionRepository.save(section);
            return new Response(HttpStatus.CREATED, "Section created successfully", new SectionDto(section));

        } catch (CustomException e) {
            return new Response(e.getStatus(), e.getMessage(), null);
        } catch (Exception e) {
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", e.getMessage());
        }
    }

    /******************************************************************************************************************/
    public Response updateSection(UpdateSectionRequest updateSectionRequest) throws IOException, InterruptedException {
        try {

            Section section = sectionRepository.findById(updateSectionRequest.getSectionId())
                    .orElseThrow(() -> new CustomException("Section not found", HttpStatus.NOT_FOUND));

            section.setDescription(updateSectionRequest.getDescription());
            section.setTitle(updateSectionRequest.getTitle());
            sectionRepository.save(section);
            return new Response(HttpStatus.CREATED, "Section created successfully", section);

        } catch (CustomException e) {
            return new Response(e.getStatus(), e.getMessage(), null);
        } catch (Exception e) {
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", e.getMessage());
        }
    }

    /****************************************************************************************************************/

    public Response deleteSection(Integer sectionId) {
        try {
            Section section = sectionRepository.findById(sectionId)
                    .orElseThrow(() -> new CustomException("Section not found", HttpStatus.NOT_FOUND));
            Course course = sectionRepository.findCourse(sectionId)
                    .orElseThrow(() -> new CustomException("Course not found", HttpStatus.NOT_FOUND));

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(
                            String.format(
                                    "https://video.bunnycdn.com/library/%s/collections/%s", course.getGuid(),
                                    section.getGuid())))
                    .header("content-type", "application/json")
                    .header("accept", "application/json")
                    .header("AccessKey", course.getApiKey())
                    .method("DELETE", HttpRequest.BodyPublishers.noBody())
                    .build();
            HttpResponse<String> response = HttpClient.newHttpClient().send(request,
                    HttpResponse.BodyHandlers.ofString());

            // System.out.println(response.body());
            if (response.statusCode() == 200)
                sectionRepository.delete(section);

            else
                throw new CustomException("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
            return new Response(HttpStatus.OK, "Section deleted successfully", null);

        } catch (CustomException e) {
            return new Response(e.getStatus(), e.getMessage(), null);
        } catch (Exception e) {
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", e.getMessage());
        }

    }
}
