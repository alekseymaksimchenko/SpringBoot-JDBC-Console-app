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

import com.foxminded.schoolapp.dao.entity.StudentEntity;

@Testcontainers
@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = {
        "/clear_tables.sql", }, statements = "INSERT INTO school.groups (name) VALUES ('group1'), ('group2');", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class StudentJdbcDaoTest {

    @Container
    private static PostgreSQLContainer<?> postgresqlContainer = new PostgreSQLContainer<>("postgres:latest")
            .withReuse(true);

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
    void testStudentJdbcDao_ShouldCreateEntry() {
        studentJdbcDao.save(testStudentEntity);
        int expected = 1;
        int actual = studentJdbcDao.getAll().size();
        assertEquals(expected, actual);

    }

    @Test
    void testStudentJdbcDao_ShouldFindByIdEntry() {
        studentJdbcDao.save(testStudentEntity);

        String expectedFirstname = "testFirstname";
        String expectedLastname = "testLastname";

        studentJdbcDao.getByID(1).ifPresent(student -> {
            assertEquals(expectedFirstname, student.getFirstname());
            assertEquals(expectedLastname, student.getLastname());
        });
    }

    @Test
    void testStudentJdbcDao_ShouldFindAllEntry() {
        studentJdbcDao.save(testStudentEntity);
        studentJdbcDao.save(testStudentEntity);
        studentJdbcDao.save(testStudentEntity);

        int expected = 3;
        int actual = studentJdbcDao.getAll().size();
        assertEquals(expected, actual);
    }

    @Test
    void testStudentJdbcDao_ShouldUpdateEntry() {
        studentJdbcDao.save(testStudentEntity);

        StudentEntity savedEntity = new StudentEntity(1, "testFirstname", "testLastname", 1);
        String[] update = { "Updated NEW_Firstname", "Updated NEW_Lastname" };

        studentJdbcDao.update(savedEntity, update);

        String expectedFirstname = "Updated NEW_Firstname";
        String expectedLastname = "Updated NEW_Lastname";

        studentJdbcDao.getByID(1).ifPresent(student -> {
            assertEquals(expectedFirstname, student.getFirstname());
            assertEquals(expectedLastname, student.getLastname());
        });
        ;

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
}
