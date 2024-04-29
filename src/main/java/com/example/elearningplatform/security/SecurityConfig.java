package com.example.elearningplatform.security;

import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.elearningplatform.user.user.User;
import com.example.elearningplatform.user.user.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@SuppressWarnings("null")
public class SecurityConfig {

    private final UserRepository userRepository;

    /*****************************************************************************************************/

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .build();
    }

    /*****************************************************************************************************/

    @Bean
    public UserDetailsService userDetailsService() {

        return username -> {
            User user = userRepository.findByEmail(username).orElse(null);
            // System.out.println(user.getRoles());
            // System.out.println(user);
            if (user != null)

                return user;
            throw new UsernameNotFoundException("User " + username + " not found");
        };
    }

    /*****************************************************************************************************/
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /***************************************************************************************************** */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings( CorsRegistry registry) {
                // registry.addMapping("/**").allowedOrigins("http://localhost:5173");
                registry.addMapping("/**").allowedOrigins("*");
            }
        };
    }
    /***************************************************************************************************** */

    @Bean
    AuthFilter authFilter() {
        return new AuthFilter();
    }

    /***************************************************************************************************** */
    // @Bean
    // public AccessDeniedHandler accessDeniedHandler() {
    //     return new AccessDeniedHandler() {
    //         @Override
    //         public void handle(HttpServletRequest request, HttpServletResponse response,
    //                 AccessDeniedException accessDeniedException) throws IOException {
    //             response.setContentType("application/json;charset=UTF-8");
    //             response.setStatus(HttpServletResponse.SC_GATEWAY_TIMEOUT);
    //             response.getWriter()
    //                     .write("{\"message\":\"Access Denied: " + accessDeniedException.getMessage() + "\"}");
    //         }
    //     };
    // }

    /***************************************************************************************************** */

    @SuppressWarnings("removal")
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable());
        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers(
                    "/api/v1/auth/**", "/v3/api-docs/**", "/swagger-ui/**",
                    
                    "/user/get-user/**", "/check-token/**", "/verifyEmail/**", "/signup/**", "/login/**",
                        "/forget-password/**", "/course/**")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .addFilterBefore(authFilter(), UsernamePasswordAuthenticationFilter.class));
                // .oauth2Login(login -> login.loginPage("/login").defaultSuccessUrl("/login/outh2"));
        // http.oauth2Login(login -> login.loginPage("/login").defaultSuccessUrl("/login/outh2"));
    
        return http.build();
    }
    /***************************************************************************************************** */

}
