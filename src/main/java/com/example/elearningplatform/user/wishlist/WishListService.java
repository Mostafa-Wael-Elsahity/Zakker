package com.example.elearningplatform.user.wishlist;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.elearningplatform.course.course.CourseRepository;
import com.example.elearningplatform.course.course.dto.SearchCourseDto;
import com.example.elearningplatform.exception.CustomException;
import com.example.elearningplatform.response.Response;
import com.example.elearningplatform.security.TokenUtil;

import jakarta.transaction.Transactional;

 
@Service
@Transactional
public class WishListService {
    @Autowired
    private WishListRepository wishListRepository;
    @Autowired
    private TokenUtil tokenUtil;
    @Autowired
    private CourseRepository courseRepository;

    /*************************************************************************************************************** */
    public Response getWishlist() {
        try {

            List<SearchCourseDto> courses = wishListRepository.findWhishListCourses(tokenUtil.getUserId()).stream()
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

    /***************************************************************************************************************/
    public Response addToWishlist(Integer courseId) {

        try {

            if (wishListRepository.findCourseInWhishList(courseId, tokenUtil.getUserId()).isPresent()) {
                throw new CustomException("Course already in wishlist", HttpStatus.BAD_REQUEST);
            }

            wishListRepository.addToWishlist(tokenUtil.getUserId(), courseId);
            return new Response(HttpStatus.OK, "Course added to wishlist", null);
        } catch (CustomException e) {
            return new Response(e.getStatus(), e.getMessage(), null);
        } catch (Exception e) {
            return new Response(HttpStatus.NOT_FOUND, e.getMessage(), null);
        }

    }

    /********************************************************************************************************* */
    public Response deleteFromoWishlist(Integer courseId) {

        try {

            if (!wishListRepository.findCourseInWhishList(courseId, tokenUtil.getUserId()).isPresent()) {
                throw new CustomException("Course is not in wishlist", HttpStatus.BAD_REQUEST);
            }

            wishListRepository.removeFromWishlist(tokenUtil.getUserId(), courseId);
            return new Response(HttpStatus.OK, "Course deleted from wishlist", null);
        } catch (CustomException e) {
            return new Response(e.getStatus(), e.getMessage(), null);
        } catch (Exception e) {
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", e.getMessage());
        }

    }
}