package com.example.elearningplatform.course;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.elearningplatform.course.review.Review;
import com.example.elearningplatform.course.review.ReviewDto;
import com.example.elearningplatform.course.review.ReviewRepository;
import com.example.elearningplatform.course.section.SectionService;
import com.example.elearningplatform.response.CourseResponse;
import com.example.elearningplatform.response.Response;
import com.example.elearningplatform.security.TokenUtil;
import com.example.elearningplatform.user.User;
import com.example.elearningplatform.user.UserDto;
import com.example.elearningplatform.user.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CourseService {
        private final CourseRepository courseRepository;
        private final UserRepository userRepository;
        private final TokenUtil tokenUtil;
        private final ReviewRepository reviewRepository;
        private final SectionService sectionService;
        private final HttpServletRequest request;



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
        public Response getCourse(Integer id) {
                Course course = courseRepository.findById(id).orElse(null);
                if (course == null)
                        return null;
                CourseDto courseDto = mapCourseToDto(course);

                Boolean check = ckeckCourseSubscribe(id);
                return new Response(HttpStatus.OK, "Success", new CourseResponse(check, courseDto));
        }

        /**************************************************************************************** */
        public Boolean ckeckCourseSubscribe(Integer id) {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                User user = userRepository.findByEmail(authentication.getName()).orElse(null);
                if (user == null) {
                        return false;
                }
                for (Course c : user.getCourses()) {
                        if (c.getId() == id)
                                return true;
                }
                return false;

        }

        /**************************************************************************************** */
        public CourseDto mapCourseToDto(Course course) {
                CourseDto courseDto = new CourseDto(course);
                courseDto.setDescription(course.getDescription());
                courseDto.setCreationDate(course.getCreationDate());
                courseDto.setLastUpdateDate(course.getLastUpdateDate());
                courseDto.setWhatYouWillLearn(course.getWhatYouWillLearn());
                courseDto.setPrerequisite(course.getPrerequisite());
                courseDto.setSections(sectionService.mapSectionToDto(course.getSections()));
                List<Review> votedreviews = reviewRepository.findByUserId(tokenUtil.getUserId());
                course.getReviews().forEach(review -> {
                        Boolean isVoted = votedreviews.contains(review);

                        if (review.getUser().getUsername()
                                        .equals(tokenUtil.getUserName(request.getHeader("Authorization")
                                                        .substring("Bearer ".length())))) {

                                courseDto.getReviews().addFirst(new ReviewDto(review, isVoted));

                        } else
                                courseDto.getReviews().add(new ReviewDto(review, isVoted));
                });
                course.getInstructors().forEach(instructor -> {
                        UserDto user = new UserDto(instructor);
                        courseDto.getInstructors().add(user);
                });

                course.getCategories().forEach(category -> {
                        courseDto.getCategories().add(category);
                });
                return courseDto;

        }
        /****************************************************************************************/


}
