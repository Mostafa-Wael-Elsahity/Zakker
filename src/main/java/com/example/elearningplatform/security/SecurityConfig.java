package com.example.elearningplatform.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.elearningplatform.user.user.User;
import com.example.elearningplatform.user.user.UserRepository;

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



    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable());

        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers(
                        "/api/v1/auth/**", "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html",
                        "/oauth2/**","/category/all/**","/user/get-all-users",
                    "/user/get-user/**", "/check-token/**", "/verifyEmail/**", "/signup/**", "/login/**",
                        "/forget-password/**", "/course/public/**",
                        "/review/get-reviews/**")
                .permitAll()
                .anyRequest().hasAnyRole("USER", "ADMIN", "INSTRUCTOR")
               
        ).addFilterAfter(authFilter(), UsernamePasswordAuthenticationFilter.class);

        http.exceptionHandling(exceptionHandling -> exceptionHandling
                .authenticationEntryPoint((request, response, authException) -> {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("Access Denied");
                }));

        http.oauth2Login(login -> login.defaultSuccessUrl("/login/oauth2/success"));


        return http.build();
    }
    /***************************************************************************************************** */
            
}
