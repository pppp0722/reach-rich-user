package com.reachrich.reachrichuser.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reachrich.reachrichuser.global.filter.UserAuthenticationFilter;
import com.reachrich.reachrichuser.global.handler.CustomAccessDeniedHandler;
import com.reachrich.reachrichuser.global.handler.CustomAuthenticationEntryPoint;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        configuration.setAllowedOriginPatterns(List.of("*"));
        configuration.setAllowedMethods(
            Arrays.asList("HEAD", "GET", "POST", "PUT", "PATCH", "DELETE"));
        configuration.setAllowedHeaders(Arrays.asList("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig)
        throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public UserAuthenticationFilter userAuthenticationFilter() {
        return new UserAuthenticationFilter();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint(ObjectMapper objectMapper) {
        return new CustomAuthenticationEntryPoint(objectMapper);
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler(ObjectMapper objectMapper) {
        return new CustomAccessDeniedHandler(objectMapper);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // Request
            .authorizeRequests()
            .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
            .antMatchers(
                "/users/login", "/users/register", "/users/verify-email/**"
            ).permitAll()
            .anyRequest().hasAnyRole("USER")
            .and()
            // 사용하지 않는 필터
            .formLogin()
            .disable()
            .csrf()
            .disable()
            .headers()
            .disable()
            .httpBasic()
            .disable()
            .rememberMe()
            .disable()
            .logout()
            .disable()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            // 인증/인가 실패 Response
            .exceptionHandling()
            .authenticationEntryPoint(authenticationEntryPoint(objectMapper()))
            .accessDeniedHandler(accessDeniedHandler(objectMapper()))
            .and()
            // Redis Session
            .addFilterBefore(userAuthenticationFilter(),
                UsernamePasswordAuthenticationFilter.class)
            // CORS
            .cors().configurationSource(corsConfigurationSource());
        return http.build();
    }
}
