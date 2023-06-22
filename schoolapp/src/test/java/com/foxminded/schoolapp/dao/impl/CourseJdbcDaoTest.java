package com.foxminded.schoolapp.dao.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import com.foxminded.schoolapp.dao.entity.CourseEntity;
import com.foxminded.schoolapp.exception.DaoException;

@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = { "/clear_tables.sql", }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)

class CourseJdbcDaoTest extends BasicJdbcDaoTest {

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
    void testCourseJdbcDao_ShouldReturnDaoException_inCaseOfNotSaved() {
        jdbcTemplate = Mockito.mock(JdbcTemplate.class);
        when(jdbcTemplate.update(anyString(), any(), any())).thenReturn(0);
        courseJdbcDao = new CourseJdbcDao(jdbcTemplate);

        Exception exception = assertThrows(DaoException.class, () -> {
            courseJdbcDao.save(testCourseEntity);
        });

        String actual = exception.getMessage();
        String expected = NOT_SUCCESSFUL_OPERATION;
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
    void testCourseJdbcDao_ShouldReturnDaoException_inCaseOfNotFoundId() {
        Exception exception = assertThrows(DaoException.class, () -> courseJdbcDao.getByID(0));

        String actual = exception.getMessage();
        String expected = GET_BY_ID_EXCEPTION;
        assertEquals(expected, actual);
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
    void testCourseJdbcDao_ShouldReturnDaoException_inCaseOfEmptyTable() {
        Exception exception = assertThrows(DaoException.class, () -> {
            courseJdbcDao.getAll();
            });

        String actual = exception.getMessage();
        String expected = IS_EMPTY;
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
    void testCourseJdbcDao_ShouldReturnDaoException_inCaseOfNotUpdated() {
        Exception exception = assertThrows(DaoException.class, () -> {
            courseJdbcDao.update(testCourseEntity);
        });

        String actual = exception.getMessage();
        String expected = NOT_SUCCESSFUL_OPERATION;
        assertEquals(expected, actual);
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
    void testCourseJdbcDao_ShouldReturnDaoException_inCaseOfNotDeleted() {
        Exception exception = assertThrows(DaoException.class, () -> {
            courseJdbcDao.deleteById(1);
        });

        String actual = exception.getMessage();
        String expected = NOT_SUCCESSFUL_OPERATION;
        assertEquals(expected, actual);
    }

}
