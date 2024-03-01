package com.example.elearningplatform.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.example.elearningplatform.user.User;
import com.example.elearningplatform.user.UserRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private UserRepository userRepository;

    @Bean
    public UserDetailsService userDetailsService() {

        return username -> {
            User user = userRepository.findByEmail(username);
            // System.out.println(user);
            if (user != null)
                return user;
            throw new UsernameNotFoundException("User '" + username + "' not found");
        };
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorize -> authorize.requestMatchers("/user/display/**").permitAll());
        http.authorizeHttpRequests(authorize -> authorize

                .requestMatchers("/user/**").hasAnyRole("ADMIN", "USER")
                .requestMatchers("/signup/**", "/login/**", "/forget-password/**", "/course/**").permitAll()

        );
        http.httpBasic();
        http.csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }
    // @Bean
    // public ObjectMapper objectMapper() {
    // return new ObjectMapper().disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    // }

}
