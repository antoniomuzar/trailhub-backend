package com.trailhub.backend.config;

import com.trailhub.backend.config.security.ApiAccessDeniedHandler;
import com.trailhub.backend.config.security.ApiAuthenticationEntryPoint;
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

    private final ApiAccessDeniedHandler apiAccessDeniedHandler;
    private final ApiAuthenticationEntryPoint apiAuthenticationEntryPoint;

    public SecurityConfig(ApiAccessDeniedHandler apiAccessDeniedHandler, ApiAuthenticationEntryPoint apiAuthenticationEntryPoint) {
        this.apiAccessDeniedHandler = apiAccessDeniedHandler;
        this.apiAuthenticationEntryPoint = apiAuthenticationEntryPoint;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth

                        //Entry endpoints: authenticated users
                        .requestMatchers(HttpMethod.POST, "/api/races/*/entries/me").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/races/*/entries/me").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/races/*/entries/*").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/races/*/entries").authenticated()

                        //Race read endpoints: authenticated users
                        .requestMatchers(HttpMethod.GET, "/api/races/**").authenticated()

                        //Race write endpoints: admin only
                        .requestMatchers(HttpMethod.POST, "/api/races/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/races/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/races/**").hasRole("ADMIN")

                        //Swagger endpoint
                        .requestMatchers("/v3/api-docs/**",
                                "swagger-ui/**",
                                "/swagger-ui.html").permitAll()

                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(apiAuthenticationEntryPoint) //401
                        .accessDeniedHandler(apiAccessDeniedHandler) //403
                )
                .httpBasic(Customizer.withDefaults());

        return http.build();

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
