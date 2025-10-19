package com.uq.alojamientos.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.*;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsConfig {

    @Value("${cors.allowed-origins:http://localhost:4200}")
    private String allowedOrigins;

    @Value("${cors.allowed-methods:GET,POST,PUT,DELETE,OPTIONS}")
    private String allowedMethods;

    @Value("${cors.allowed-headers:*}")
    private String allowedHeaders;

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        for (String origin : allowedOrigins.split(",")) config.addAllowedOrigin(origin.trim());
        for (String m : allowedMethods.split(",")) config.addAllowedMethod(m.trim());
        if ("*".equals(allowedHeaders)) {
            config.addAllowedHeader(CorsConfiguration.ALL);
        } else {
            for (String h : allowedHeaders.split(",")) config.addAllowedHeader(h.trim());
        }
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
