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
    
    /**
     * Creates a new user list based on the provided CreateUserList object.
     *
     * @param  userlist  the CreateUserList object containing the details of the user list to be created
     * @param  result    the BindingResult object for validation errors
     * @return           the Response object indicating the result of the list creation
     */
    @PostMapping("/create-list")
    public Response createList(@RequestBody @Valid CreateUserList userlist, BindingResult result) {

        if (result.hasErrors()) {

            return Validator.validate(result);
        }

        return userListService.createList(userlist);

    }

    /********************************************************************************* */
    
    /**
     * Retrieves the user lists.
     *
     * @return the Response object containing the user lists
     */
    @GetMapping("/get-user-lists")
    public Response getLists() {
        return userListService.getLists();
    }

    /********************************************************************************* */

    /**
     * A description of the entire Java function.
     *
     * @param  courseId   description of parameter
     * @param  listId     description of parameter
     * @return           description of return value
     */
    @PostMapping("/add-to-list")
    public Response addTolist(@RequestParam("courseId") Integer courseId, @RequestParam("listId") Integer listId) {
        return userListService.addTolist(listId, courseId);

    }

    /********************************************************************************* */

    /**
     * Removes a course from a user's list.
     *
     * @param  courseId   the ID of the course to be removed
     * @param  listId     the ID of the list from which the course is to be removed
     * @return            the response indicating the result of the operation
     */
    @DeleteMapping("/remove-from-list")
    public Response removeFromlist(@RequestParam("courseId") Integer courseId, @RequestParam("listId") Integer listId) {

        return userListService.deleteCourseFromList(listId, courseId);

    }

    /********************************************************************************* */
    
    /**
     * Deletes a user list by its ID.
     *
     * @param  listId  the ID of the list to be deleted
     * @return         a Response object indicating the result of the deletion
     */
    @DeleteMapping("/delete-list")
    public Response deleteList(@RequestParam("listId") Integer listId) {

        return userListService.deleteList(listId);
    }

    /*****************************************************************************************************/
    
    /**
     * Updates a user list based on the provided UpdateUserList object.
     *
     * @param  list     the UpdateUserList object containing the details of the list to be updated
     * @param  result   the BindingResult object for validation errors
     * @return          the Response object indicating the result of the list update
     */
    @PutMapping("/update-list")
    public Response updateList(@RequestBody @Valid UpdateUserList list, BindingResult result) {
        if (result.hasErrors()) {
            return Validator.validate(result);
        }

        return userListService.updateList(list);
    }

}
