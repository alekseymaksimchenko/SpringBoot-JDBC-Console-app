package com.foxminded.schoolapp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class SchoolappApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(SchoolappApplication.class);

    public static void main(String[] args) {
        LOGGER.info("STARTING THE APPLICATION");
        SpringApplication application = new SpringApplicationBuilder(SchoolappApplication.class)
                .web(WebApplicationType.NONE).build();
        application.setBannerMode(Banner.Mode.OFF);
        application.run(args);
        LOGGER.info("APPLICATION FINISHED");
    }

}
