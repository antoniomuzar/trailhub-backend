package com.trailhub.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth

                        //Entry endpoints: authenticated users
                        .requestMatchers(HttpMethod.POST, "/api/races/*/entries/me").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/races/*/entries/me").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/races/*/entries").authenticated()

                        //Race read endpoints: authenticated users
                        .requestMatchers(HttpMethod.GET, "/api/races/**").authenticated()

                        //Race write endpoints: admin only
                        .requestMatchers(HttpMethod.POST, "/api/races/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/races/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/races/**").hasRole("ADMIN")


                        .anyRequest().authenticated()
                )

                .httpBasic(Customizer.withDefaults());

        return http.build();

    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
