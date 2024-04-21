package com.example.elearningplatform.user;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.elearningplatform.course.Course;
import com.example.elearningplatform.course.question.Question;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import java.util.*;

@Data
@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;
    private String email;
    private String firstName;

    private String lastName;

    private String password;

    private String phoneNumber;

    private byte[] profilePicture;

    private Boolean enabled;

    private LocalDateTime registrationDate;

    private String bio;

    private Integer age;


    @ManyToMany(mappedBy = "votes")
    @ToString.Exclude
    // @JoinTable(name = "question_votes", joinColumns = @JoinColumn(name ="user_id"), inverseJoinColumns = @JoinColumn(name =  "question_id"))
    private List<Question> questionVotes ;

    private LocalDateTime lastLogin;
    @ToString.Exclude
    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "user_role")
    @Column(name = "role")
    private List<Role> roles ;


    @ManyToMany(fetch = FetchType.EAGER)
    @ToString.Exclude
    @JoinTable(name = "course_users", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "course_id"))
    private List<Course> courses;

    @ManyToMany(mappedBy = "instructors",fetch = FetchType.EAGER)
    @ToString.Exclude
    // @JoinTable(name = "instructed_courses", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "course_id"))
    private List<Course> instructedCourses;
    public  void addCourse(Course course) {
        courses.add(course);
    }
    public  void addInstructedCourse(Course course) {
        instructedCourses.add(course);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (roles == null) {
            return Collections.emptyList();
        }

        Collection<? extends GrantedAuthority> mapRoles = roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.toString()))
                .collect(Collectors.toList());
        return mapRoles;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public Object orElseThrow(String msg) {

        throw new RuntimeException(msg);
    }

    @Override
    public String getUsername() {
        return email;
    }

}
