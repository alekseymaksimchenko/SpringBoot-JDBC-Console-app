package com.foxminded.schoolapp.service.generator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.foxminded.schoolapp.dao.entity.StudentCourseEntity;
import com.foxminded.schoolapp.exception.DomainException;


class StudentCourseGeneratorTest {

    private static final String PATH = "./src/test/resources/generatorsConfigurationTest.properties";
    private static final int STUDENTS_QUANTITY = 200;
    private static final int COURSE_QUANTITY = 10;
    

    private StudentCourseGenerator studentCourseGenerator;
    private List<StudentCourseEntity> studentCourse;

    @BeforeEach
    public void init() throws Exception {
        studentCourseGenerator = new StudentCourseGenerator(PATH);
        studentCourse = studentCourseGenerator.generate();
    }

    @Test
    void testStudentCourseGenerator_throwsDomainException() {
        DomainException ex = assertThrows(DomainException.class, () -> new StudentCourseGenerator("test"));
        String expectedMessage = "Property file missing";
        assertEquals(expectedMessage, ex.getMessage());
    }

    @Test
    void testStudentCourseGenerator_thatListStudentIdValueIsNotZero() {
        studentCourse.stream().map(s -> s.getStudentId()).forEach(s -> {
            assertNotEquals(0, s.intValue());
        });
    }

    @Test
    void testStudentCourseGenerator_thatListStudentIdIsInTheRange() {
        studentCourse.stream().map(s -> s.getStudentId()).forEach(s -> {
            assertTrue(s <= STUDENTS_QUANTITY);
        });
    }

    @Test
    void testStudentCourseGenerator_thatListCourseIdValueIsNotZero() {
        studentCourse.stream().map(s -> s.getCourseId()).forEach(s -> {
            assertNotEquals(0, s.intValue());
        });
    }

    @Test
    void testStudentCourseGenerator_thatListCourseIdIsInTheRange() {
        studentCourse.stream().map(s -> s.getCourseId()).forEach(s -> {
            assertTrue(s <= COURSE_QUANTITY);
        });
    }

    @Test
    void testStudentCourseGenerator_thatListNotNull() {
        assertNotNull(studentCourse);
    }

    @Test
    void testStudentCourseGenerator_thatListNotEmpty() {
        assertFalse(studentCourse.isEmpty());
    }

}
