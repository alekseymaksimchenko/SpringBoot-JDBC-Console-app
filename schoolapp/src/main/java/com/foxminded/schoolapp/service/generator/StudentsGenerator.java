package com.foxminded.schoolapp.service.generator;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.foxminded.schoolapp.dao.entity.StudentEntity;
import com.foxminded.schoolapp.exception.ServiceException;

@Service
public class StudentsGenerator implements StudentGenerator<StudentEntity> {

    private static final String STUDENT_NAMES_KEY = "studentNames";
    private static final String STUDENT_LASTNAMES_KEY = "studentLastnames";
    private static final String STUDENTS_QUANTITY_KEY = "studentsQuantity";
    private static final String GROUP_QUANTITY_KEY = "groupsQuantity";
    private static final String GET_MESSAGE = "Property file missing";
    private static final String SPLIT = ", ";
    private static final String COURSE_QUANTITY_KEY = "coursesQuantity";
    private static final int LEVELING = 1;
    private static final Logger LOGGER = LoggerFactory.getLogger(StudentsGenerator.class);
    private final List<String> studentNames;
    private final List<String> studentLastnames;
    private final int studentQuantity;
    private final int groupQuantity;
    private final int courseQuantity;
    private Random random = new Random();

    @Autowired
    public StudentsGenerator(@Value("${generator.file}") String file) throws ServiceException {
        LOGGER.trace("Load student quantity from config file = {}", file);
        Properties properties = new Properties();
        try (InputStream fileInputStream = new FileInputStream(file)) {
            properties.load(fileInputStream);
            this.studentNames = Arrays.asList(properties.getProperty(STUDENT_NAMES_KEY).split(SPLIT));
            this.studentLastnames = Arrays.asList(properties.getProperty(STUDENT_LASTNAMES_KEY).split(SPLIT));
            this.studentQuantity = Integer.parseInt(properties.getProperty(STUDENTS_QUANTITY_KEY));
            this.groupQuantity = Integer.parseInt(properties.getProperty(GROUP_QUANTITY_KEY));
            this.courseQuantity = Integer.parseInt(properties.getProperty(COURSE_QUANTITY_KEY));
        } catch (IOException e) {
            throw new ServiceException(GET_MESSAGE, e);
        }
    }

    @Override
    public List<StudentEntity> generate() {
        LOGGER.debug("StudentsGenerator generate() - starts");
        List<StudentEntity> studentList = randomizeStudents();
        List<Integer> studentsPerGroups = getStudentsPerGroups(groupQuantity);
        AtomicInteger value = new AtomicInteger(-1);
        boolean assigned = false;
        while (!assigned) {
            int count = value.incrementAndGet();
            if (count < studentsPerGroups.size()) {
                studentList.stream().filter(s -> s.getGroupId() == 0).limit(studentsPerGroups.get(count))
                        .forEach(s -> s.setGroupId(count + 1));
            } else {
                assigned = true;
            }
        }
        int noGroupOption = groupQuantity + 1;
        studentList.stream().filter(s -> s.getGroupId() == 0).forEach(s -> s.setGroupId(noGroupOption));
        LOGGER.trace("Returning of StudentEntities List that contains ({}) elements", studentList.size());
        return studentList;
    }

    @Override
    public Map<StudentEntity, Set<Integer>> studentToCourseGenerator() {
        LOGGER.debug("StudentToCourseGenerator - starts");
        Map<StudentEntity, Set<Integer>> studentCourseMap = new HashMap<>();
        AtomicInteger value = new AtomicInteger(0);
        boolean assigned = false;
        while (!assigned) {
            int count = value.incrementAndGet();
            if (count <= studentQuantity) {
                Set<Integer> tempSet = new HashSet<>();
                StudentEntity tempStudent = new StudentEntity();
                tempStudent.setId(count);

                IntStream.iterate(0, i -> i + 1).limit(random.nextInt(3) + LEVELING).forEach(s -> {
                    Integer tempCourseId = random.nextInt(courseQuantity) + LEVELING;
                    tempSet.add(tempCourseId);
                });
                studentCourseMap.put(tempStudent, tempSet);
            } else {
                assigned = true;
            }
        }
        LOGGER.trace("Returning of StudentCourse Map that contains ({}) elements", studentCourseMap.size());
        return studentCourseMap;
    }

    private List<StudentEntity> randomizeStudents() {
        List<StudentEntity> studentList = Stream.generate(StudentEntity::new).limit(studentQuantity)
                .collect(Collectors.toCollection(ArrayList::new));

        studentList.forEach(s -> {
            s.setFirstname(getRandomFromList(studentNames, studentNames.size()));
            s.setLastname(getRandomFromList(studentLastnames, studentLastnames.size()));
        });
        return studentList;
    }

    private List<Integer> getStudentsPerGroups(int lenght) {
        return Stream.generate(() -> random.nextInt(20) + 10).limit(lenght)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private String getRandomFromList(List<String> list, int size) {
        return list.get(random.nextInt(size));
    }

}
