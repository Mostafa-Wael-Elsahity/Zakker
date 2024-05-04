package com.example.elearningplatform.user.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.elearningplatform.course.course.CourseRepository;
import com.example.elearningplatform.course.course.dto.SearchCourseDto;
import com.example.elearningplatform.exception.CustomException;
import com.example.elearningplatform.response.Response;
import com.example.elearningplatform.security.TokenUtil;
import com.example.elearningplatform.user.address.Address;
import com.example.elearningplatform.user.address.AddressRepository;
import com.example.elearningplatform.user.user.dto.ProfileDto;
import com.example.elearningplatform.user.user.dto.UpdateProfileRequest;
import com.example.elearningplatform.user.user.dto.UserDto;

import jakarta.transaction.Transactional;
import lombok.Setter;

@Service
@Setter
@Transactional
public class UserService {
    @Autowired private UserRepository userRepository;
    @Autowired private TokenUtil tokenUtil;
    @Autowired private CourseRepository courseRepository;
    @Autowired
    private AddressRepository addressRepository;

    /************************************************************************************************************/
    public Response getUser(Integer userId) {
        try {

            User user = userRepository.findById(userId).orElseThrow(() -> new Exception("User not found"));
            UserDto userDto = new UserDto(user);

            List<SearchCourseDto> courses = userRepository.findInstructedCourses(userId).stream()
                    .map(course -> {
                        userDto.setNumberOfEnrollments(
                                userDto.getNumberOfEnrollments() + course.getNumberOfEnrollments());
                        return new SearchCourseDto(
                                course, courseRepository.findCourseInstructors(course.getId()),
                                courseRepository.findCourseCategory(course.getId()),
                                courseRepository.findCourseTags(course.getId()));
                    })

                    .toList();
            userDto.setInstructoredCourses(
                    courses);
            userDto.setNumberOfCourses(courses.size());

            return new Response(HttpStatus.OK, "Success", userDto);
        } catch (Exception e) {
            return new Response(HttpStatus.NOT_FOUND, e.getMessage(), null);
        }
    }


    /********************************************************************************************************* */
    public Response getProfile() {
        try {
            User user = userRepository.findById(tokenUtil.getUserId())
                    .orElseThrow(() -> new CustomException("User not found", HttpStatus.NOT_FOUND));
            Address address = userRepository.findAdress(tokenUtil.getUserId()).orElse(null);
            ProfileDto profileDto = new ProfileDto(user, address);

            return new Response(HttpStatus.OK, "Success", profileDto);
        } catch (CustomException e) {
            return new Response(e.getStatus(), e.getMessage(), null);
        } catch (Exception e) {
            return new Response(HttpStatus.NOT_FOUND, e.getMessage(), null);

        }
    }

    /*************************************************************************************************************** */

    // to do
    public Response updateprofile(UpdateProfileRequest updateProfileRequest) {

        try {
            User user = userRepository.findById(tokenUtil.getUserId())
                    .orElseThrow(() -> new CustomException("User not found", HttpStatus.NOT_FOUND));

            user.setFirstName(updateProfileRequest.getFirstName());
            user.setLastName(updateProfileRequest.getLastName());
            user.setPhoneNumber(updateProfileRequest.getPhoneNumber());
            user.setAbout(updateProfileRequest.getAbout());
            user.setAge(updateProfileRequest.getAge());
            user.setPaypalEmail(updateProfileRequest.getPaypalEmail());
            Address address = userRepository.findAdress(tokenUtil.getUserId()).orElse(null);
            address.setStreet(updateProfileRequest.getStreet());
            address.setCity(updateProfileRequest.getCity());
            address.setState(updateProfileRequest.getState());
            address.setCountry(updateProfileRequest.getCountry());
            addressRepository.save(address);
            userRepository.save(user);
            return new Response(HttpStatus.OK, "Profile updated successfully", null);

        } catch (Exception e) {
            return new Response(HttpStatus.NOT_FOUND, e.getMessage(), null);
        }

    }
    /*************************************************************************************************************** */
    public Response getEnrolledCourses() {
        try {
            List<SearchCourseDto> courses = userRepository.findEnrolledCourses(tokenUtil.getUserId()).stream()
                    .map(course -> new SearchCourseDto(
                            course, courseRepository.findCourseInstructors(course.getId()),
                            courseRepository.findCourseCategory(course.getId()),
                            courseRepository.findCourseTags(course.getId())))
                    .toList();
            return new Response(HttpStatus.OK, "Success", courses);
        } catch (Exception e) {
            return new Response(HttpStatus.NOT_FOUND, e.getMessage(), null);
        }
    }

    /*************************************************************************************************************** */

    public Response deleteUser() {
        try {
            User user = userRepository.findById(tokenUtil.getUserId())
                    .orElseThrow(() -> new CustomException("User not found", HttpStatus.NOT_FOUND));
                    
            // List<Course> courses = userRepository.findInstructedCourses(tokenUtil.getUserId());
            userRepository.delete(user);
            return new Response(HttpStatus.OK, "User deleted successfully", null);
        } catch (CustomException e) {
            return new Response(e.getStatus(), e.getMessage(), null);
        } catch (Exception e) {
            return new Response(HttpStatus.NOT_FOUND, e.getMessage(), null);
        }
    }

    
}
