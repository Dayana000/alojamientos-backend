package com.uq.alojamientos.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;

@Configuration
public class CorsConfig {

    @Value("${cors.allowed-origins:*}")
    private String allowedOrigins;   // CSV o "*"

    @Value("${cors.allowed-methods:GET,POST,PUT,DELETE,OPTIONS}")
    private String allowedMethods;   // CSV

    @Value("${cors.allowed-headers:*}")
    private String allowedHeaders;   // CSV o "*"

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins(splitOrWildcard(allowedOrigins))
                        .allowedMethods(splitOrWildcard(allowedMethods))
                        .allowedHeaders(splitOrWildcard(allowedHeaders))
                        .allowCredentials(true);
            }
        };
    }

    private String[] splitOrWildcard(String v) {
        if (v == null || v.isBlank() || "*".equals(v.trim())) return new String[]{"*"};
        return Arrays.stream(v.split(",")).map(String::trim).filter(s -> !s.isEmpty()).toArray(String[]::new);
    }
}
