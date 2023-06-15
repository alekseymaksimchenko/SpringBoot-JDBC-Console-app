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

import com.foxminded.schoolapp.dao.entity.StudentEntity;
import com.foxminded.schoolapp.exception.DaoException;

@Testcontainers
@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = { "/clear_tables.sql", "/sample_data.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class StudentJdbcDaoTest {

    @Container
    private static PostgreSQLContainer<?> postgresqlContainer = new PostgreSQLContainer<>("postgres:latest");

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private StudentJdbcDao studentJdbcDao;
    private StudentEntity testStudentEntity;
    private static final String GET_BY_ID_EXCEPTION = "Record under provided id - not exist";

    @BeforeEach
    void setUp() {
        studentJdbcDao = new StudentJdbcDao(jdbcTemplate);
        testStudentEntity = new StudentEntity("testFirstname", "testLastname", 1);
    }

    @Test
    void testStudentJdbcDao_ShouldCreateEntry() {
        int currentSize = studentJdbcDao.getAll().size();

        studentJdbcDao.save(testStudentEntity);
        int expected = currentSize + 1;
        int actual = studentJdbcDao.getAll().size();
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
    void testStudentJdbcDao_ShouldFindAllEntry() {
        int expected = 3;
        int actual = studentJdbcDao.getAll().size();
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
    void testStudentCourseJdbcDao_ShouldAddStudentToCourse() {
        StudentEntity student = new StudentEntity(1, "testFirstname", "testLastname", 1);

        studentJdbcDao.addStudentToCourse(student, 2);
        int expected = 3;
        int actual = studentJdbcDao.findAllStudentsRelatedToCourse(2).size();
        assertEquals(expected, actual);
    }

    @Test
    void testStudentCourseJdbcDao_ShouldfindAllStudentsRelatedToCourseByCourseId() {
        int expected = 2;
        int actual = studentJdbcDao.findAllStudentsRelatedToCourse(2).size();

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
    void testGroupJdbcDao_ShouldReturnException_inCaseOfNotFoundId() {
        Exception exception = assertThrows(DaoException.class, () -> studentJdbcDao.getByID(0));

        String actual = exception.getMessage();
        String expected = GET_BY_ID_EXCEPTION;
        assertEquals(expected, actual);
    }
}
