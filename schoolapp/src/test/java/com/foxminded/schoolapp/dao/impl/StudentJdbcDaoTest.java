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

import com.foxminded.schoolapp.dao.entity.StudentEntity;
import com.foxminded.schoolapp.exception.DaoException;

@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = { "/clear_tables.sql", "/sample_data.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class StudentJdbcDaoTest extends BasicJdbcDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private StudentJdbcDao studentJdbcDao;
    private StudentEntity testStudentEntity;

    @BeforeEach
    void setUp() {
        studentJdbcDao = new StudentJdbcDao(jdbcTemplate);
        testStudentEntity = new StudentEntity("testFirstname", "testLastname", 1);
    }

    @Test
    void testStudentJdbcDao_ShouldSave() {
        int currentSize = studentJdbcDao.getAll().size();

        studentJdbcDao.save(testStudentEntity);
        int expected = currentSize + 1;
        int actual = studentJdbcDao.getAll().size();
        assertEquals(expected, actual);
    }

    @Test
    void testStudentJdbcDao_ShouldReturnDaoException_inCaseOfNotSaved() {
        jdbcTemplate = Mockito.mock(JdbcTemplate.class);
        when(jdbcTemplate.update(anyString(), any(), any())).thenReturn(0);
        studentJdbcDao = new StudentJdbcDao(jdbcTemplate);

        Exception exception = assertThrows(DaoException.class, () -> {
            studentJdbcDao.save(testStudentEntity);
        });

        String actual = exception.getMessage();
        String expected = NOT_SUCCESSFUL_OPERATION;
        assertEquals(expected, actual);
    }

    @Test
    void testStudentJdbcDao_ShouldFindByIdEntry() {
        studentJdbcDao.save(testStudentEntity);

        String expectedFirstname = "test1";
        String expectedLastname = "test1";

        StudentEntity actual = studentJdbcDao.getByID(1);

        assertEquals(expectedFirstname, actual.getFirstname());
        assertEquals(expectedLastname, actual.getLastname());
    }

    @Test
    void testStudentJdbcDao_ShouldReturnDaoException_inCaseOfNotFoundId() {
        Exception exception = assertThrows(DaoException.class, () -> studentJdbcDao.getByID(0));

        String actual = exception.getMessage();
        String expected = GET_BY_ID_EXCEPTION;
        assertEquals(expected, actual);
    }

    @Test
    void testStudentJdbcDao_ShouldFindAllEntry() {
        int expected = 3;
        int actual = studentJdbcDao.getAll().size();
        assertEquals(expected, actual);
    }

    @Sql(scripts = { "/clear_tables.sql" })
    @Test
    void testStudentJdbcDao_ShouldReturnDaoException_inCaseOfEmptyTable() {
        Exception exception = assertThrows(DaoException.class, () -> {
            studentJdbcDao.getAll();
            });

        String actual = exception.getMessage();
        String expected = IS_EMPTY;
        assertEquals(expected, actual);
    }

    @Test
    void testStudentJdbcDao_ShouldUpdateEntry() {
        studentJdbcDao.save(testStudentEntity);
        StudentEntity savedEntity = new StudentEntity(1, "Updated NEW_Firstname", "Updated NEW_Lastname", 1);

        studentJdbcDao.update(savedEntity);

        String expectedFirstname = "Updated NEW_Firstname";
        String expectedLastname = "Updated NEW_Lastname";
        StudentEntity actual = studentJdbcDao.getByID(1);

        assertEquals(expectedFirstname, actual.getFirstname());
        assertEquals(expectedLastname, actual.getLastname());
    }

    @Test
    void testStudentJdbcDao_ShouldReturnDaoException_inCaseOfNotUpdated() {
        Exception exception = assertThrows(DaoException.class, () -> {
            studentJdbcDao.update(testStudentEntity);
        });

        String actual = exception.getMessage();
        String expected = NOT_SUCCESSFUL_OPERATION;
        assertEquals(expected, actual);
    }

    @Test
    void testStudentJdbcDao_ShouldDeleteEntry() {
        studentJdbcDao.save(testStudentEntity);
        studentJdbcDao.save(testStudentEntity);
        studentJdbcDao.save(testStudentEntity);

        int expected = studentJdbcDao.getAll().size() - 1;

        studentJdbcDao.deleteById(1);
        int actual = studentJdbcDao.getAll().size();

        assertEquals(expected, actual);
    }

    @Test
    void testStudentJdbcDao_ShouldReturnDaoException_inCaseOfNotDeleted() {
        Exception exception = assertThrows(DaoException.class, () -> {
            studentJdbcDao.deleteById(0);
        });

        String actual = exception.getMessage();
        String expected = NOT_SUCCESSFUL_OPERATION;
        assertEquals(expected, actual);
    }

    @Test
    void testStudentCourseJdbcDao_ShouldAddStudentToCourse() {
        StudentEntity student = new StudentEntity(1, "testFirstname", "testLastname", 1);

        studentJdbcDao.addStudentToCourse(student, 2);
        int expected = 3;
        int actual = studentJdbcDao.findAllStudentsRelatedToCourse(2).size();
        assertEquals(expected, actual);
    }

    @Test
    void testStudentJdbcDao_ShouldReturnDaoException_inCaseOfNotAddStudentToCourse() {
        jdbcTemplate = Mockito.mock(JdbcTemplate.class);
        when(jdbcTemplate.update(anyString(), any(), any())).thenReturn(0);
        studentJdbcDao = new StudentJdbcDao(jdbcTemplate);

        Exception exception = assertThrows(DaoException.class, () -> {
            studentJdbcDao.addStudentToCourse(testStudentEntity, 0);
        });

        String actual = exception.getMessage();
        String expected = NOT_SUCCESSFUL_OPERATION;
        assertEquals(expected, actual);
    }

    @Test
    void testStudentCourseJdbcDao_ShouldfindAllStudentsRelatedToCourseByCourseId() {
        int expected = 2;
        int actual = studentJdbcDao.findAllStudentsRelatedToCourse(2).size();

        assertEquals(expected, actual);
    }

    @Test
    void testStudentJdbcDao_ShouldReturnDaoException_inCaseOfEmptyCourseTable() {
        Exception exception = assertThrows(DaoException.class, () -> {
            studentJdbcDao.findAllStudentsRelatedToCourse(0);
            });

        String actual = exception.getMessage();
        String expected = IS_EMPTY;
        assertEquals(expected, actual);
    }

    @Test
    void testStudentCourseJdbcDao_ShouldRemoveStudentFromCourse() {
        int expected = studentJdbcDao.findAllStudentsRelatedToCourse(2).size() - 1;

        studentJdbcDao.removeStudentByIDFromCourse(2, 2);
        int actual = studentJdbcDao.findAllStudentsRelatedToCourse(2).size();

        assertEquals(expected, actual);
    }

    @Test
    void testStudentJdbcDao_ShouldReturnDaoException_inCaseOfNotRemovedStudentFromCourse() {
        Exception exception = assertThrows(DaoException.class, () -> {
            studentJdbcDao.removeStudentByIDFromCourse(0, 0);
        });

        String actual = exception.getMessage();
        String expected = NOT_SUCCESSFUL_OPERATION;
        assertEquals(expected, actual);
    }

}
