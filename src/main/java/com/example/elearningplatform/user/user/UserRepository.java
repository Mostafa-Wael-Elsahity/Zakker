package com.example.elearningplatform.user.user;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.elearningplatform.course.course.Course;
import com.example.elearningplatform.user.address.Address;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);

    Optional<User> findByFirstName(String firstName);

    Optional<User> findByLastName(String lastName);

    /************************************************************************************************/
    @Query("""
                                SELECT new com.example.elearningplatform.user.user.User(u) FROM User u
                WHERE lower(u.email) LIKE lower(concat('%', :searchKey, '%'))
                OR lower(u.firstName) LIKE lower(concat('%', :searchKey, '%'))
                OR lower(u.lastName) LIKE lower(concat('%', :searchKey, '%'))
            """)
    Page<User> findBySearchKey(@Param("searchKey") String searchKey, Pageable pageable);


    @Query("""
            SELECT u.enrolledCourses FROM User u
            WHERE u.id = :id
            """)
    List<Course> findEnrolledCourses(Integer id);

    /************************************************************************************************/

    @Query("""
            SELECT u.instructoredCourses FROM User u
            WHERE u.id = :id
            """)
    List<Course> findInstructedCourses(Integer id);

    /************************************************************************************************/
    @Query("""
                    SELECT u.ownedCourses FROM User u
                    WHERE u.id = :id
                    """)
    List<Course> findOwnerCourses(Integer id);

    /************************************************************************************************/
    @Query("""
                    SELECT u.address FROM User u WHERE u.id = :userId
                    """)
    Optional<Address> findAdress(@Param("userId") Integer userId);

    /************************************************************************************************/

    @Query("""
            SELECT u.enrolledCourses FROM User u join u.enrolledCourses e
            WHERE u.id = :userId And e.id = :id
            """)
    Optional<Course> findCourseInEnrolled(@Param("id") Integer id, @Param("userId") Integer userId);



}
