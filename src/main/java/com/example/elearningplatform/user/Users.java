//package com.example.elearningplatform.user;
//
//import jakarta.persistence.Column;
//import jakarta.persistence.Id;
//import jakarta.persistence.Lob;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//
//import java.sql.Blob;
//import java.time.LocalDateTime;
//import java.util.Arrays;
//import java.util.Collection;
//@Data
//@AllArgsConstructor
//public class Users implements UserDetails {
//
//    private String email;
//    private String password;
//    private boolean enabled;
//    private String role;
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return Arrays.asList(new SimpleGrantedAuthority(role));
//    }
//
//    @Override
//    public String getPassword() {
//        return password;
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return true;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return enabled;
//    }
//
//    public Object orElseThrow(String msg) {
//
//        throw new RuntimeException(msg);
//    }
//
//    @Override
//    public String getUsername() {
//        return email;
//    }
//}
