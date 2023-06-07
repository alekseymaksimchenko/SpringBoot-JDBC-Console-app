package com.foxminded.schoolapp.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;

import com.foxminded.schoolapp.entity.CourseEntity;
import com.foxminded.schoolapp.exception.DomainException;


public class CoursesGenerator {

    private static final String GET_MESSAGE = "Property file missing";
    private static final String PROP_KEY = "courseNames";
    private static final String SPLIT = ", ";
    private static final Logger LOGGER = LoggerFactory.getLogger(CoursesGenerator.class);
    private final List<String> courseNameList;

    public CoursesGenerator(String file) throws DomainException {
        LOGGER.trace("Load course quantity from config file = {}", file);
        Properties properties = new Properties();
        try (InputStream fileInputStream = new FileInputStream(file)) {
            properties.load(fileInputStream);
            this.courseNameList = Arrays.asList(properties.getProperty(PROP_KEY).split(SPLIT));
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
            throw new DomainException(GET_MESSAGE);
        }
    }


    public List<CourseEntity> generate() {
        LOGGER.debug("CoursesGenerator generate() - starts");
        List<CourseEntity> courseList = Stream.generate(CourseEntity::new).limit(courseNameList.size())
                .collect(Collectors.toCollection(ArrayList::new));

        courseList.forEach(s -> s.setName(courseNameList.get(courseList.indexOf(s))));
        LOGGER.trace("Returning of CourseEntities List that contains ({}) elements", courseNameList.size());
        return courseList;
    }

}
