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

import com.foxminded.schoolapp.entity.StudentEntity;


@Testcontainers
@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = { "/clear_tables.sql", }, statements = "INSERT INTO school.groups (name) VALUES ('group1'), ('group2');" , executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
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
        testStudentEntity = new StudentEntity("testName", "testDescription", 1);
    }



    @Test
    void testStudentDao_ShouldCreateEntry() {
        studentJdbcDao.save(testStudentEntity);
        int expected = 1;
        int actual = studentJdbcDao.getAll().size();
        assertEquals(expected, actual);

    }

    @Test
    void testStudentDao_ShouldFindByIdEntry() {
        studentJdbcDao.save(testStudentEntity);

        String expected = "StudentEntity [id=1, firstname=testName, lastname=testDescription, groupId=1]";
        String actual = studentJdbcDao.getByID(1).get().toString();

        assertEquals(expected, actual);
    }

    @Test
    void testStudentDao_ShouldFindAllEntry() {
        studentJdbcDao.save(testStudentEntity);
        studentJdbcDao.save(testStudentEntity);
        studentJdbcDao.save(testStudentEntity);

        int expected = 3;
        int actual = studentJdbcDao.getAll().size();
        assertEquals(expected, actual);
    }

    @Test
    void testStudentDao_ShouldUpdateEntry()  {
        studentJdbcDao.save(testStudentEntity);

        String[] update = {"Updated NEW_Firstname", "Updated NEW_Lastname"};

        studentJdbcDao.update(testStudentEntity, update);

        String expected = "StudentEntity [id=1, firstname=testName, lastname=testDescription, groupId=1]";
        String actual = studentJdbcDao.getByID(1).get().toString();

        assertEquals(expected, actual);
    }

    @Test
    void testStudentDao_ShouldDeleteEntry() {
        studentJdbcDao.save(testStudentEntity);
        studentJdbcDao.save(testStudentEntity);
        studentJdbcDao.save(testStudentEntity);

        int expected = studentJdbcDao.getAll().size() - 1;

        studentJdbcDao.deleteById(1);
        int actual = studentJdbcDao.getAll().size();

        assertEquals(expected, actual);
    }
}
