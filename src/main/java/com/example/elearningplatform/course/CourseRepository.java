package com.example.elearningplatform.course;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.elearningplatform.user.User;

public interface CourseRepository extends JpaRepository<Course, Long> {

}