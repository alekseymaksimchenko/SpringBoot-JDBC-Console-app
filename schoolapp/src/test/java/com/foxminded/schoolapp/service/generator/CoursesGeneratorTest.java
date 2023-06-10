package com.foxminded.schoolapp.service.generator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.foxminded.schoolapp.dao.entity.CourseEntity;
import com.foxminded.schoolapp.exception.DomainException;

class CoursesGeneratorTest {
    private static final String COURSE1 = "ACCOUNTING";
    private static final String COURSE2 = "BIOLOGY";
    private static final String COURSE3 = "CRIMINOLOGY";
    private static final String COURSE4 = "DESIGN";
    private static final String COURSE5 = "ECONOMICS";
    private static final String COURSE6 = "FINANCE";
    private static final String COURSE7 = "HISTORY";
    private static final String COURSE8 = "LAW";
    private static final String COURSE9 = "MUSIC";
    private static final String COURSE10 = "SCIENCE";
    private static final List<String> NAME_LIST = new ArrayList<>(
            Arrays.asList(COURSE1, COURSE2, COURSE3, COURSE4, COURSE5, COURSE6, COURSE7, COURSE8, COURSE9, COURSE10));

    private static final String PATH = "./src/test/resources/generatorsConfigurationTest.properties";
    private static final int COURSE_QUANTITY = NAME_LIST.size();

    private CoursesGenerator course;
    private List<CourseEntity> courseList;

    @BeforeEach
    public void initEach() throws Exception {
        course = new CoursesGenerator(PATH);
        courseList = course.generate();
    }

    @Test
    void testCoursesGenerator_throwsDomainException() {
        DomainException ex = assertThrows(DomainException.class, () -> new CoursesGenerator("test"));
        String expectedMessage = "Property file missing";
        assertEquals(expectedMessage, ex.getMessage());
    }

    @Test
    void testCoursesGenerator_thatCoursesInListAreDifferent() {
        int actual = courseList.stream().map(s -> s.getName()).distinct().collect(Collectors.toList()).size();
        int expected = COURSE_QUANTITY;
        assertEquals(expected, actual);
    }

    @Test
    void testCoursesGenerator_thatCourseListContainsAllDefinedFilds() {
        List<String> actual = courseList.stream().map(s -> s.getName()).collect(Collectors.toList());
        assertTrue(actual.containsAll(NAME_LIST));
    }

    @Test
    void testCoursesGenerator_thatCourseListNotNull() {
        assertNotNull(courseList);
    }

    @Test
    void testCoursesGenerator_thatCourseListNotEmpty() {
        assertFalse(courseList.isEmpty());
    }
}
