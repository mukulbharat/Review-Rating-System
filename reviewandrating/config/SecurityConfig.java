package com.krenai.reviewandrating.config;

import com.krenai.reviewandrating.security.FirebaseAuthFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

//    @Bean
//    public FilterRegistrationBean<FirebaseAuthFilter> firebaseFilter() {
//        FilterRegistrationBean<FirebaseAuthFilter> registrationBean = new FilterRegistrationBean<>();
//        registrationBean.setFilter(new FirebaseAuthFilter());
//        registrationBean.addUrlPatterns("/api/*"); // secure endpoints
//        return registrationBean;
//    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disable CSRF for testing/postman
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/auth/**").permitAll()
                        .requestMatchers("/api/v1/login/**").permitAll()
                        .requestMatchers(HttpMethod.POST,"/api/v1/users").permitAll()// Public routes
                        .anyRequest().authenticated()             // Secure all others
                )
                .addFilterBefore(new FirebaseAuthFilter(), UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // important for JWT/Firebase
                .httpBasic(Customizer.withDefaults()); // Optional, but prevents default login form
        return http.build();
    }
}
