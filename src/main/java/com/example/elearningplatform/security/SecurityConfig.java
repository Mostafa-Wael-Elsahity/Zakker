package com.example.elearningplatform.security;

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
import com.example.elearningplatform.user.UserRepository;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
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
        http.csrf(csrf -> csrf.disable());
        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/user/**", "/check-course-subscription/**", "/check-cart/**")
                .hasAnyRole("ADMIN", "USER")
                .requestMatchers("/test").hasAnyRole("ADMIN")
                .requestMatchers("/check-token/**", "/course/**", "/verifyEmail/**", "/signup/**", "/login/**",
                        "/forget-password/**")
                .permitAll())
                .addFilterBefore(authFilter(), UsernamePasswordAuthenticationFilter.class);

        http.formLogin(login -> login
                .loginPage("/login/get-login") // custom login page
                .permitAll());
        // http.httpBasic(Customizer.withDefaults());
        return http.build();
    }
    /***************************************************************************************************** */

}
