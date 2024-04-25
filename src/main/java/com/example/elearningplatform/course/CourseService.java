package com.example.elearningplatform.course;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.elearningplatform.course.review.ReviewDto;
import com.example.elearningplatform.course.review.ReviewRepository;
import com.example.elearningplatform.course.section.SectionDto;
import com.example.elearningplatform.course.section.SectionRepository;
import com.example.elearningplatform.course.section.SectionService;
import com.example.elearningplatform.security.TokenUtil;
import com.example.elearningplatform.user.User;
import com.example.elearningplatform.user.UserDto;
import com.example.elearningplatform.user.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CourseService {
        private final CourseRepository courseRepository;
        private final UserRepository userRepository;
        private final TokenUtil tokenUtil;
        private final SectionRepository sectionRepository;
        private final SectionService sectionService;

        private final ReviewRepository reviewRepository;

        /***************************************************************************************** */
        public List<CourseDto> getAllCourses() {
                List<Course> courses = courseRepository.findAll();
                List<CourseDto> courseDtos = new ArrayList<>();
                courses.forEach(course -> {
                        courseDtos.add(mapCourseToDto(course));
                });
                return courseDtos;
        }

        /****************************************************************************************/
        public List<SearchCourseDto> findByCategory(Integer categoryId, Integer pageNumber) {

                Pageable pageable = PageRequest.of(pageNumber, 8);

                List<SearchCourseDto> courses = courseRepository.findByCategoryId(categoryId, pageable).stream()
                                .map(course -> {
                                        SearchCourseDto searchCourseDto = new SearchCourseDto(course);
                                        return searchCourseDto;
                                }).toList();
                return courses;
        }

        /****************************************************************************************/
        public List<SearchCourseDto> findByTitle(String searchKey, Integer pageNumber) {

                Pageable pageable = PageRequest.of(pageNumber, 8);

        List<SearchCourseDto> courses = courseRepository.findByTitle(searchKey, pageable).stream()
                        .map(course -> {
                                SearchCourseDto searchCourseDto = new SearchCourseDto(course);
                                return searchCourseDto;
                        }).toList();
                return courses;
        }

        /**************************************************************************************** */
        public List<SearchCourseDto> findByInstructorName(Integer instructorId, Integer pageNumber) {

                Pageable pageable = PageRequest.of(pageNumber, 8);

                List<SearchCourseDto> courses = courseRepository.findByInstructorId(instructorId, pageable).stream()
                                .map(course -> {
                                        SearchCourseDto searchCourseDto = new SearchCourseDto(course);
                                        return searchCourseDto;
                                }).toList();
                return courses;
        }

        /**************************************************************************************** */
        public CourseDto getCourse(Integer id) {
                Course course = courseRepository.findById(id).orElse(null);

                if (course == null)
                        return null;
                CourseDto courseDto = mapCourseToDto(course);

                return courseDto;

        }

        /**************************************************************************************** */
        public Boolean ckeckCourseSubscribe(Integer id) {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                if (authentication == null) {
                        return false;
                }

                User user = userRepository.findByEmail(authentication.getName()).orElse(null);
                if (user == null) {
                        return false;
                }
                for (Course c : user.getEnrolledCourses()) {
                        if (c.getId() == id)
                                return true;
                }
                return false;

        }

        /**************************************************************************************** */
        public CourseDto mapCourseToDto(Course course) {
                CourseDto courseDto = new CourseDto(course, ckeckCourseSubscribe(course.getId()));
                courseDto.setDescription(course.getDescription());
                courseDto.setCreationDate(course.getCreationDate());
                courseDto.setLastUpdateDate(course.getLastUpdateDate());
                courseDto.setWhatYouWillLearn(course.getWhatYouWillLearn());
                courseDto.setPrerequisite(course.getPrerequisite());
                List<SectionDto> sectionDtos = sectionRepository.findByCourseId(course.getId()).stream()
                                .map(section -> sectionService.mapSectionToDto(section)).toList();

                courseDto.setSections(sectionDtos);

                List<ReviewDto> courseReviewDtos = reviewRepository.findByCourseId(course.getId()).stream().map(
                                review -> {
                                        ReviewDto reviewDto = new ReviewDto(review);
                                        if (review.getUser().getId().equals(tokenUtil.getUserId())) {
                                                courseDto.setIsReviewd(true);
                                                courseDto.addReviewinFront(reviewDto);
                                        }
                                        return reviewDto;
                                })
                                .toList();
                courseDto.setReviews(courseReviewDtos);

                course.getInstructors().forEach(instructor -> {
                        UserDto user = new UserDto(instructor);
                        courseDto.addInstructor(user);
                });

                course.getCategories().forEach(category -> {
                        courseDto.getCategories().add(category);
                });
                return courseDto;

        }
        /****************************************************************************************/


}
