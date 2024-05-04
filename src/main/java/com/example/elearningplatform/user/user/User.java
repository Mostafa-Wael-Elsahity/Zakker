package com.example.elearningplatform.user.user;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.elearningplatform.course.course.Course;
import com.example.elearningplatform.user.address.Address;
import com.example.elearningplatform.user.role.Role;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

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
    @Column(unique = true)
    private String email;
    private String firstName;

    private String paypalEmail;

    private String lastName;

    private String password;

    private String phoneNumber;

    private String imageUrl;
    
    private String imageId;

    private Boolean enabled;

    private LocalDateTime registrationDate;

    private String about;
    private LocalDateTime lastLogin;
    private Integer age;


    @OneToOne(fetch = FetchType.LAZY,mappedBy = "user", cascade = CascadeType.REMOVE)
    @ToString.Exclude
    private Address address;


    @ManyToMany(fetch = FetchType.EAGER)
    @ToString.Exclude
  @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role> roles;


    @ManyToMany(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinTable(name = "user_enrolled_courses", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "course_id"))
    @Builder.Default
    private List<Course> enrolledCourses = new ArrayList<>();


    @ManyToMany(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinTable(name = "user_archived_courses", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "course_id"))
    @Builder.Default
    private List<Course> archivedCourses = new ArrayList<>();


    @ManyToMany(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinTable(name = "user_whishlist_courses", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "course_id"))
    @Builder.Default
    private List<Course> whishlist = new ArrayList<>();


    @ManyToMany(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinTable(name = "user_cart_courses", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "course_id"))
    @Builder.Default
    private List<Course> cart = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @ToString.Exclude
    @Builder.Default
    private List<Course> ownedCourses = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinTable(name = "instructed_courses", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "course_id"))
    private List<Course> instructoredCourses;

    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (roles == null) {
            return Collections.emptyList();
        }

        Collection<? extends GrantedAuthority> mapRoles = roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getRole()))
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
