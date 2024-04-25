package com.example.elearningplatform.user.lists;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.elearningplatform.response.Response;
import com.example.elearningplatform.security.TokenUtil;
import com.example.elearningplatform.user.UserRepository;
import com.example.elearningplatform.validator.Validator;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class UserListController {
    private final UserListRepository userListRepository;
    private final UserListService userListService;
    private final UserRepository userRepository;
    private final TokenUtil tokenUtil;

    /********************************************************************************* */

    @PostMapping("/create-list")
    public Response createList(@RequestBody @Valid CreateUserList userlist, BindingResult result) {

        if (result.hasErrors()) {

            return Validator.validate(result);
        }
        UserList list = new UserList();
        list.setName(userlist.getName());
        list.setDescription(userlist.getDescription());
        list.setUser(userRepository.findById(tokenUtil.getUserId()).get());
        userListRepository.save(list);
        return new Response(HttpStatus.CREATED, "Success", null);

    }

    /********************************************************************************* */
    @GetMapping("/get-lists")
    public Response getLists() {
        List<UserListDto> lists = userListRepository.findByUserId(tokenUtil.getUserId()).stream().map(
                list -> userListService.mapUserListToDto(list)).toList();
        return new Response(HttpStatus.OK, "success", lists);
    }

    /********************************************************************************* */
    @GetMapping("/add-to-list/{listId}/{courseId}")
    public Response addTolist(@PathVariable("courseId") Integer courseId, @PathVariable("listId") Integer listId) {
        return userListService.addTolist(listId, courseId);

    }

    /********************************************************************************* */
    @DeleteMapping("/remove-from-list/{listId}/{courseId}")
    public Response removeFromlist(@PathVariable("courseId") Integer courseId, @PathVariable("listId") Integer listId) {

        return userListService.deleteCourseFromList(listId, courseId);

    }

    /********************************************************************************* */
    @DeleteMapping("/delete-list/{listId}")
    public Response deleteList(@PathVariable("listId") Integer listId) {

        return userListService.deleteList(listId);
    }
    /*****************************************************************************************************/
    @PostMapping("/update-list")
    public Response updateList(@RequestBody UpdateUserList list) {

        return userListService.updateList(list);
    }

}
