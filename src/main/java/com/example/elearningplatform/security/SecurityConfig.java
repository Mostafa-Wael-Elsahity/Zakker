package com.example.elearningplatform.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.elearningplatform.user.User;
import com.example.elearningplatform.user.UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private UserService userService;

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
            User user = userService.findByEmail(username);
            // System.out.println(user);
            if (user != null)
                return user;
            throw new UsernameNotFoundException("User '" + username + "' not found");
        };
    }

    /*****************************************************************************************************/
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /***************************************************************************************************** */

    /***************************************************************************************************** */

    @Bean
    AuthFilter authFilter() {
        return new AuthFilter();
    }

    /***************************************************************************************************** */

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorize -> authorize.requestMatchers("/user/display/**").permitAll());
        http.authorizeHttpRequests(authorize -> authorize

                .requestMatchers("/user/**", "/get-course/**", "/course/**").hasAnyRole("ADMIN", "USER")
                .requestMatchers("/check-token/**", "/verifyEmail/**", "/signup/**", "/login/**", "/forget-password/**")
                .permitAll())
                .addFilterBefore(authFilter(), UsernamePasswordAuthenticationFilter.class);
        // http.httpBasic(Customizer.withDefaults());
        return http.build();
    }
    /***************************************************************************************************** */

}
