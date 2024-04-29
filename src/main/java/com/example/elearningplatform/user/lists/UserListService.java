package com.example.elearningplatform.user.lists;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.elearningplatform.course.course.CourseRepository;
import com.example.elearningplatform.course.course.dto.SearchCourseDto;
import com.example.elearningplatform.exception.CustomException;
import com.example.elearningplatform.response.Response;
import com.example.elearningplatform.security.TokenUtil;
import com.example.elearningplatform.user.lists.dto.CreateUserList;
import com.example.elearningplatform.user.lists.dto.UpdateUserList;
import com.example.elearningplatform.user.lists.dto.UserListDto;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class UserListService {
    private final UserListRepository userListRepository;

    private final TokenUtil tokenUtil;
    private final CourseRepository courseRepository;

    /************************************************************************************************** */
    public Response createList(CreateUserList userlist) {
        try {
            UserList list = new UserList();
            list.setName(userlist.getName());
            list.setDescription(userlist.getDescription());
            list.setUser(tokenUtil.getUser());
            userListRepository.save(list);
            return new Response(HttpStatus.CREATED, "Success", null);
        } catch (Exception e) {
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", e.getMessage());
        }

    }

    /************************************************************************************************** */

    public Response getLists() {
        try {
            List<UserList> lists = userListRepository.findByUserId(tokenUtil.getUserId());
            List<UserListDto> listsDto = lists.stream().map(list -> new UserListDto(list)).toList();
            return new Response(HttpStatus.OK, "Success", listsDto);
        } catch (Exception e) {
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", e.getMessage());
        }
    }

    /************************************************************************************************** */

    public Response getlist(Integer listId) {
        try { // User user = userRepository.findById(tokenUtil.getUserId()).orElse(null);
            UserList list = userListRepository.findById(listId)
                    .orElseThrow(() -> new CustomException("List not found", HttpStatus.NOT_FOUND));
            UserListDto UserListDto = new UserListDto(list);

            List<SearchCourseDto> courses = userListRepository.findCourses(listId).stream()
                    .map(course -> new SearchCourseDto(
                            course, courseRepository.findCourseInstructors(course.getId()),
                            courseRepository.findCourseCategory(course.getId()),
                            courseRepository.findCourseTags(course.getId())))
                    .toList();
            UserListDto.setCourses(courses);
            return new Response(HttpStatus.OK, "Success", UserListDto);
        }
        catch (CustomException e) {
            return new Response(e.getStatus(), e.getMessage(), null);
        } 
        catch (Exception e) {
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", e.getMessage());
        }
        
    }

    /************************************************************************************************** */

    public Response addTolist(Integer listId, Integer courseId) {
        try {
            if (userListRepository.findCourse(listId, courseId).isPresent())
                throw new CustomException("Course already in list", HttpStatus.BAD_REQUEST);

            userListRepository.addToUserList(listId, courseId);

            return new Response(HttpStatus.OK, "Success", null);
        }
        catch (CustomException e) {
            return new Response(e.getStatus(), e.getMessage(), null);
        } 
        catch (Exception e) {
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", e.getMessage());
        }

    }

    /************************************************************************************************** */
    public Response deleteList(Integer listId) {
        try {
            UserList list = userListRepository.findById(listId)
                    .orElseThrow(() ->new CustomException("List not found", HttpStatus.NOT_FOUND));

            userListRepository.delete(list);
            return new Response(HttpStatus.OK, "Success", null);
        }
        catch (CustomException e) {
            return new Response(e.getStatus(), e.getMessage(), null);
        } 
        catch (Exception e) {
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", e.getMessage());
        }
    }

    /************************************************************************************************** */

    public Response updateList(UpdateUserList newlist) {

        try {
            UserList list = userListRepository.findById(newlist.getListId())
                    .orElseThrow(() -> new CustomException("List not found", HttpStatus.NOT_FOUND));

            if (newlist.getName() != null)
                list.setName(newlist.getName());
            if (newlist.getDescription() != null)
                list.setDescription(newlist.getDescription());
            userListRepository.save(list);

            return new Response(HttpStatus.OK, "Success", new UserListDto(list));
        } 
        catch (CustomException e) {
            return new Response(e.getStatus(), e.getMessage(), null);
        }
        catch (Exception e) {

            return new Response(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", e.getMessage());
        }
    }

    /************************************************************************************************** */

    public Response deleteCourseFromList(Integer listId, Integer courseId) {
        try {
            if (!userListRepository.findCourse(listId, courseId).isPresent())
                throw new CustomException("Course not in list", HttpStatus.BAD_REQUEST);
            userListRepository.removeFromUserList(listId, courseId);
            return new Response(HttpStatus.OK, "Success", null);
        }
        catch (CustomException e) {
            return new Response(e.getStatus(), e.getMessage(), null);
        }
         catch (Exception e) {
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", e.getMessage());
        }
    }
    /************************************************************************************************** */

}
