package com.foxminded.schoolapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.foxminded.schoolapp.dao.impl.CourseJdbcDao;

@Configuration
public class DaoConfig {
    
    @Bean
    CourseJdbcDao courseJdbcDao() {
        return new CourseJdbcDao(null);
        
    }

}
