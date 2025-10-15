// src/main/java/com/uq/alojamientos/config/ModelMapperConfig.java
package com.uq.alojamientos.config;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class ModelMapperConfig {
    @Bean public ModelMapper modelMapper() { return new ModelMapper(); }
}
