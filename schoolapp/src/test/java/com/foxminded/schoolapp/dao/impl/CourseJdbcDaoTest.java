package com.foxminded.schoolapp.dao.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
import com.foxminded.schoolapp.exception.DaoException;

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
    private static final String GET_BY_ID_EXCEPTION = "Record under provided id - not exist";
    
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

        CourseEntity actual = courseJdbcDao.getByID(1);
        assertEquals(expectedName, actual.getName());
        assertEquals(expectedDescription, actual.getDescription());
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

        CourseEntity updatedEntiry = new CourseEntity(1, "Updated Bio", "Updated Bio");

        courseJdbcDao.update(updatedEntiry);

        String expectedName = "Updated Bio";
        String expectedDescription = "Updated Bio";

        CourseEntity actual = courseJdbcDao.getByID(1);
        assertEquals(expectedName, actual.getName());
        assertEquals(expectedDescription, actual.getDescription());
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

    @Test
    void testCourseJdbcDao_ShouldReturnException_inCaseOfNotFoundId() {
        Exception exception = assertThrows(DaoException.class, () -> courseJdbcDao.getByID(0));

        String actual = exception.getMessage();
        String expected = GET_BY_ID_EXCEPTION;
        assertEquals(expected, actual);
    }

}
