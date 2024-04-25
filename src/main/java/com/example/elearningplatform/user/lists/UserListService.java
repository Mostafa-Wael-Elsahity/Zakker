package com.example.elearningplatform.user.lists;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.elearningplatform.course.Course;
import com.example.elearningplatform.course.CourseRepository;
import com.example.elearningplatform.course.SearchCourseDto;
import com.example.elearningplatform.response.Response;
import com.example.elearningplatform.security.TokenUtil;
import com.example.elearningplatform.user.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserListService {
    private final UserListRepository userListRepository;
    private final CourseRepository courseRepository;


    /************************************************************************************************** */

    public UserListDto mapUserListToDto(UserList UserList) {

        UserListDto UserListDto = new UserListDto();
        UserListDto.setId(UserList.getId());
        UserListDto.setName(UserList.getName());
        UserListDto.setCourses(UserList.getCourses().stream().map(course -> {
            return new SearchCourseDto(course);
        }).toList());
        return UserListDto;

    }

    /************************************************************************************************** */

    public List<SearchCourseDto> getlist(Integer listId) {
        try { // User user = userRepository.findById(tokenUtil.getUserId()).orElse(null);
            UserList list = userListRepository.findById(listId)
                    .orElseThrow(() -> new RuntimeException("List not found"));
            List<SearchCourseDto> courses = list.getCourses().stream().map(course -> new SearchCourseDto(course))
                    .toList();
            return courses;
        } catch (Exception e) {
            return null;
        }
    }

    /************************************************************************************************** */

    public Response addTolist(Integer listId, Integer courseId) {
        try {
            UserList list = userListRepository.findById(listId)
                    .orElseThrow(() -> new RuntimeException("List not found"));

            Course course = courseRepository.findById(courseId)
                    .orElseThrow(() -> new RuntimeException("Course not found"));

            list.addCourse(course);
            userListRepository.save(list);
            return new Response(HttpStatus.OK, "Success", null);
        } catch (Exception e) {
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", e.getMessage());
        }

    }

    /************************************************************************************************** */
    public Response deleteList(Integer listId) {
        try {
            UserList list = userListRepository.findById(listId)
                    .orElseThrow(() -> new RuntimeException("List not found"));

            userListRepository.delete(list);
            return new Response(HttpStatus.OK, "Success", null);
        } catch (Exception e) {
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", e.getMessage());
        }
    }

    /************************************************************************************************** */

    public Response updateList(UpdateUserList newlist) {

        try {
            UserList list = userListRepository.findById(newlist.getListId())
                    .orElseThrow(() -> new RuntimeException("List not found"));

            if (newlist.getName() != null)
                list.setName(newlist.getName());
            if (newlist.getDescription() != null)
                list.setDescription(newlist.getDescription());
            userListRepository.save(list);

            return new Response(HttpStatus.OK, "Success", null);
        } catch (Exception e) {

            return new Response(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", e.getMessage());
        }
    }

    /************************************************************************************************** */

    public Response deleteCourseFromList(Integer listId, Integer courseId) {
        try {
            UserList list = userListRepository.findById(listId)
                    .orElseThrow(() -> new RuntimeException("List not found"));

            Course course = courseRepository.findById(courseId)
                    .orElseThrow(() -> new RuntimeException("Course not found"));

            list.removeCourse(course);
            userListRepository.save(list);
            return new Response(HttpStatus.OK, "Success", mapUserListToDto(list));
        } catch (Exception e) {
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", e.getMessage());
        }
    }
    /************************************************************************************************** */

}
