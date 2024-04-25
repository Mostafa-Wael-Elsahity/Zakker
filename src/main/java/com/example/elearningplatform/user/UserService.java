package com.example.elearningplatform.user;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.elearningplatform.course.Course;
import com.example.elearningplatform.security.TokenUtil;
import com.example.elearningplatform.signup.SignUpRequest;
import com.example.elearningplatform.user.address.Address;
import com.example.elearningplatform.user.address.AddressDto;
import com.example.elearningplatform.user.address.AddressRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

public class UserService {
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final TokenUtil tokenUtil;
    private final PasswordEncoder passwordEncoder;


    /************************************************************************** */
    public List<UserDto> findBySearchKey(String searchKey, Integer pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, 8);
        return userRepository.findBySearchKey(searchKey, pageable).stream().map(user -> {
            UserDto instructor = new UserDto(user);
            return instructor;
        }).toList();

    }

    /************************************************************************** */

    public User saveUser(SignUpRequest request) throws SQLException, IOException {

        User user = User.builder().email(request.getEmail()).password(passwordEncoder.encode(request.getPassword()))
                .age(request.getAge())
                .bio(request.getBio()).firstName(request.getFirstName()).lastName(request.getLastName())
                .enabled(false)
                .phoneNumber(request.getPhoneNumber()).registrationDate(LocalDateTime.now()).build();
        if (request.getProfilePicture() != null)
            user.setProfilePicture(request.getProfilePicture().getBytes());
        userRepository.save(user);
        user.setRoles(List.of(Role.ROLE_USER));
        userRepository.save(user);
        Address address = Address.builder().user(user).city(request.getCity()).country(request.getCountry())
                .street(request.getStreet()).state(request.getState()).zipCode(request.getZipCode()).build();
        addressRepository.save(address);
        return user;
    }

    /********************************************************************************************************* */
    /********************************************************************************************************* */

    // public void addToArchived(Course course) {
    // User user = userRepository.findById(tokenUtil.getUserId()).orElse(null);
    // user.addToArchived(course);
    // userRepository.save(user);

    // }

    /********************************************************************************************************* */
    public ProfileDto getProfile() {
        User user = userRepository.findById(tokenUtil.getUserId()).orElse(null);
        ProfileDto profileDto = new ProfileDto();
        if (user == null)
            return profileDto;
        profileDto.setId(user.getId());
        profileDto.setEmail(user.getEmail());
        profileDto.setFirstName(user.getFirstName());
        profileDto.setLastName(user.getLastName());
        profileDto.setPhoneNumber(user.getPhoneNumber());
        profileDto.setEnabled(user.isEnabled());
        profileDto.setRegistrationDate(user.getRegistrationDate());
        profileDto.setBio(user.getBio());
        profileDto.setLastLogin(user.getLastLogin());
        profileDto.setAge(user.getAge());
        profileDto.setAddress(new AddressDto(user.getAddress()));
        return profileDto;
    }

    /********************************************************************************************************* */
    public List<Course> getEnrolledCourses() {
        User user = userRepository.findById(tokenUtil.getUserId()).orElse(null);
        List<Course> courses = user.getEnrolledCourses();
        return courses;
    }

    // public List<SearchCourseDto> getArchived() {
    // User user = userRepository.findById(tokenUtil.getUserId()).orElse(null);
    // List<SearchCourseDto> courses = user.getArchivedCourses().stream().map(course
    // -> new SearchCourseDto(course))
    // .toList();
    // return courses;
    // }
}
