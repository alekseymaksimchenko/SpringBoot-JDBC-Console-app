package com.foxminded.schoolapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GeneratorConfig {
    
    @Bean
    public String path() {
        return "src/main/resources/generatorsConfiguration.properties"; 
    }

}
