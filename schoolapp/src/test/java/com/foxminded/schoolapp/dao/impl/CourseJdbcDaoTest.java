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

import com.foxminded.schoolapp.dao.entity.CourseEntity;

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
    private CourseJdbcDao courseJdbcDao;
    private CourseEntity testCourseEntity;

    @BeforeEach
    void setUp() {
        courseJdbcDao = new CourseJdbcDao(jdbcTemplate);
        testCourseEntity = new CourseEntity("testName", "testDescription");
    }

    @Test
    void testCourseJdbcDao_ShouldSave() {
        courseJdbcDao.save(testCourseEntity);

        int expected = 1;
        int actual = courseJdbcDao.getAll().size();
        assertEquals(expected, actual);
    }

    @Test
    void testCourseJdbcDao_shouldFindByIdEntry() {
        courseJdbcDao.save(testCourseEntity);

        String expectedName = "testName";
        String expectedDescription = "testDescription";

        courseJdbcDao.getByID(1).ifPresent(s -> {
            assertEquals(expectedName, s.getName());
            assertEquals(expectedDescription, s.getDescription());
        });
    }

    @Test
    void testCourseJdbcDao_ShouldFindAllEntry() {
        courseJdbcDao.save(testCourseEntity);
        courseJdbcDao.save(testCourseEntity);
        courseJdbcDao.save(testCourseEntity);

        int expected = 3;
        int actual = courseJdbcDao.getAll().size();
        assertEquals(expected, actual);
    }

    @Test
    void testCourseJdbcDao_ShouldUpdateEntry() {
        courseJdbcDao.save(testCourseEntity);

        CourseEntity savedEntiry = new CourseEntity(1, "testName", "testDescription");
        String[] update = { "Updated Bio", "Updated Bio" };

        courseJdbcDao.update(savedEntiry, update);

        String expectedName = "Updated Bio";
        String expectedDescription = "Updated Bio";

        courseJdbcDao.getByID(1).ifPresent(s -> {
            assertEquals(expectedName, s.getName());
            assertEquals(expectedDescription, s.getDescription());
        });
    }

    @Test
    void testCourseJdbcDao_ShouldDeleteEntry() {
        courseJdbcDao.save(testCourseEntity);
        courseJdbcDao.save(testCourseEntity);
        courseJdbcDao.save(testCourseEntity);

        int expected = courseJdbcDao.getAll().size() - 1;
        courseJdbcDao.deleteById(1);

        int actual = courseJdbcDao.getAll().size();
        assertEquals(expected, actual);
    }
}
