package com.foxminded.schoolapp.service.generator;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.foxminded.schoolapp.dao.entity.StudentCourseEntity;
import com.foxminded.schoolapp.exception.DomainException;

@Service
public class StudentCourseGenerator implements Generator<StudentCourseEntity> {

    private static final String STUDENTS_QUANTITY_KEY = "studentsQuantity";
    private static final String COURSE_QUANTITY_KEY = "coursesQuantity";
    private static final int LEVELING = 1;
    private static final String GET_MESSAGE = "Property file missing";
    private static final Logger LOGGER = LoggerFactory.getLogger(StudentCourseGenerator.class);
    private final int courseQuantity;
    private final int studentQuantity;
    private Random random = new Random();

    public StudentCourseGenerator(String file) throws DomainException {
        LOGGER.trace("Load student and course quantity from config file = {}", file);
        Properties properties = new Properties();
        try (InputStream fileInputStream = new FileInputStream(file)) {
            properties.load(fileInputStream);
            this.studentQuantity = Integer.parseInt(properties.getProperty(STUDENTS_QUANTITY_KEY));
            this.courseQuantity = Integer.parseInt(properties.getProperty(COURSE_QUANTITY_KEY));
        } catch (IOException e) {
            throw new DomainException(GET_MESSAGE, e);
        }
    }

    @Override
    public List<StudentCourseEntity> generate() {
        LOGGER.debug("StudentCourseGenerator generate() - starts");
        List<StudentCourseEntity> studentCourse = new ArrayList<>();
        AtomicInteger value = new AtomicInteger(0);
        boolean assigned = false;
        while (!assigned) {
            int count = value.incrementAndGet();
            if (count <= studentQuantity) {
                Stream.generate(StudentCourseEntity::new).limit(random.nextInt(3) + LEVELING).forEach(s -> {
                    s.setStudentId(count);
                    s.setCourseId(random.nextInt(courseQuantity) + LEVELING);
                    studentCourse.add(s);
                });
            } else {
                assigned = true;
            }
        }
        LOGGER.trace("Returning of StudentCourseEntities List that contains ({}) elements", studentCourse.size());
        return studentCourse.stream().distinct().collect(Collectors.toList());

    }

}
