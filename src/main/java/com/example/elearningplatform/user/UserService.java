//package com.example.elearningplatform.user;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Service;
//
//import java.util.Collection;
//
//@Service
//
//public class UserService {
//@Autowired UserRepository userRepository;
//public  UserDetails toUserDetails(String email) {
//    User user = userRepository.findByEmail(email);
//
//       return new Users( user.getEmail(), user.getPassword(),user.isEnabled() ,user.getRole());
//    };
//}
//
//
