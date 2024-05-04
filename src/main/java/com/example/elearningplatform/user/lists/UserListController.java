package com.example.elearningplatform.user.lists;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.elearningplatform.response.Response;
import com.example.elearningplatform.user.lists.dto.CreateUserList;
import com.example.elearningplatform.user.lists.dto.UpdateUserList;
import com.example.elearningplatform.validator.Validator;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/user")
public class UserListController {

    private final UserListService userListService;

    /********************************************************************************* */
    @PostMapping("/create-list")
    public Response createList(@RequestBody @Valid CreateUserList userlist, BindingResult result) {

        if (result.hasErrors()) {

            return Validator.validate(result);
        }

        return userListService.createList(userlist);

    }

    /********************************************************************************* */
    @GetMapping("/get-user-lists")
    public Response getLists() {
        return userListService.getLists();
    }

    /********************************************************************************* */

    @PostMapping("/add-to-list")
    public Response addTolist(@RequestParam("courseId") Integer courseId, @RequestParam("listId") Integer listId) {
        return userListService.addTolist(listId, courseId);

    }

    /********************************************************************************* */

    @DeleteMapping("/remove-from-list")
    public Response removeFromlist(@RequestParam("courseId") Integer courseId, @RequestParam("listId") Integer listId) {

        return userListService.deleteCourseFromList(listId, courseId);

    }

    /********************************************************************************* */
    @DeleteMapping("/delete-list")
    public Response deleteList(@RequestParam("listId") Integer listId) {

        return userListService.deleteList(listId);
    }
    /*****************************************************************************************************/
    @PutMapping("/update-list")
    public Response updateList(@RequestBody @Valid UpdateUserList list, BindingResult result) {
        if (result.hasErrors()) {
            return Validator.validate(result);
        }

        return userListService.updateList(list);
    }

}
