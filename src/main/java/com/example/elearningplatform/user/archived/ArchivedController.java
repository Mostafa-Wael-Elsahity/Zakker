package com.example.elearningplatform.user.archived;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.elearningplatform.response.Response;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/user")
public class ArchivedController {
    @Autowired
    private ArchivedService archivedService;

    /*****************************************************************************************************/

    @GetMapping("/get-archived")
    public Response getarchived() {
        return archivedService.getArchived();
    }

    /****************************************************************************************************/
    @DeleteMapping("/delete-from-archived")
    public Response deleteFromArchived(@RequestParam("courseId") Integer courseId) {

        return archivedService.deleteFromArchived(courseId);
    }

    /****************************************************************************************************/
    @PostMapping("/add-to-archived")
    public Response addToArchived(@RequestParam("courseId") Integer courseId)  {

        return archivedService.addToArchived(courseId);
    }

    /****************************************************************************************************/

}
