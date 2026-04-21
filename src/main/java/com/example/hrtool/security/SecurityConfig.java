package com.example.hrtool.security;

import org.springframework.http.HttpMethod;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        System.out.println("SecurityFilterChain");

        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/login").permitAll()
                        .requestMatchers("/api/system-users/numOfUsers").permitAll()
                        .requestMatchers(
                                //"/auth/login",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**",
                                "/swagger-resources/**",
                                "/webjars/**"
                        ).permitAll()
                        .requestMatchers("/auth/refresh").permitAll()
                        .requestMatchers("/api/dashboard/**").hasAnyRole("ADMIN", "RECRUITING", "HR", "IT", "KL", "BEREICHSLEITER")
                        .requestMatchers("/api/locations/**").hasAnyRole("ADMIN", "RECRUITING", "HR", "IT", "KL", "BEREICHSLEITER")
                        .requestMatchers("/api/positions/**").hasAnyRole("ADMIN", "RECRUITING", "HR", "IT", "KL", "BEREICHSLEITER")
                        .requestMatchers("/api/certifications/**").hasAnyRole("ADMIN", "RECRUITING", "HR", "IT", "KL", "BEREICHSLEITER")
                        .requestMatchers("/api/contracts/**").hasAnyRole("ADMIN", "RECRUITING", "HR", "IT", "KL", "BEREICHSLEITER")
                        .requestMatchers("/api/inventory/**").hasAnyRole("ADMIN", "RECRUITING", "HR", "IT", "KL", "BEREICHSLEITER")
                        .requestMatchers("/api/access/**").hasAnyRole("ADMIN", "RECRUITING", "HR", "IT", "KL", "BEREICHSLEITER")
                        .requestMatchers("/auth/register").hasAnyRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}

