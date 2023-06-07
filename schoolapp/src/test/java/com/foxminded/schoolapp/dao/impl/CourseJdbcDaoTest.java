package com.foxminded.schoolapp.dao.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.foxminded.schoolapp.entity.CourseEntity;

@Testcontainers
@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = { "/clear_tables.sql", }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)

class CourseJdbcDaoTest {

    @Container
    private static PostgreSQLContainer<?> postgresqlContainer = new PostgreSQLContainer<>("postgres:latest")
            .withReuse(true);

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private CourseJdbcDao course;
    private CourseEntity testCourseEntity;

    @BeforeEach
    void setUp() {
        course = new CourseJdbcDao(jdbcTemplate);
        testCourseEntity = new CourseEntity("testName", "testDescription");
    }

    @Test
    void testCourseDaoShouldSave() {
        course.save(testCourseEntity);
        String expected = "CourseEntity [id=1, name=testName, description=testDescription]";
        String actual = course.getByID(1).get().toString();
        assertEquals(expected, actual);

    }

    @Test
    void testCourseDao_shouldFindByIdEntry() {
        course.save(testCourseEntity);
        String expected = "CourseEntity [id=1, name=testName, description=testDescription]";
        String actual = course.getByID(1).get().toString();
        assertEquals(expected, actual);
    }

    @Test
    void testCourseDao_ShouldFindAllEntry() {
        course.save(testCourseEntity);
        course.save(testCourseEntity);
        course.save(testCourseEntity);

        int expected = 3;
        int actual = course.getAll().size();
        assertEquals(expected, actual);
    }

    @Test
    void testCourseDao_ShouldUpdateEntry() {
        course.save(testCourseEntity);

        String[] update = {"Updated Bio", "Updated Bio"};

        course.update(testCourseEntity, update);
        String expected = "CourseEntity [id=1, name=testName, description=testDescription]";

        String actual = course.getByID(1).get().toString();
        assertEquals(expected, actual);
    }

    @Test
    void testCourseDao_ShouldDeleteEntry() {
        course.save(testCourseEntity);
        course.save(testCourseEntity);
        course.save(testCourseEntity);

        int expected = course.getAll().size() - 1;
        course.deleteById(1);

        int actual = course.getAll().size();
        assertEquals(expected, actual);
    }
}
