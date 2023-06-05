package com.foxminded.schoolapp.student;

import static org.junit.jupiter.api.Assertions.*;

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


@Testcontainers
@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = { "/clear_tables.sql", }, statements = "INSERT INTO school.groups (name) VALUES ('group1'), ('group2');" , executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class StudentDaoTest {

    @Container
    private static PostgreSQLContainer<?> postgresqlContainer = new PostgreSQLContainer<>("postgres:latest")
            .withReuse(true);

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private StudentDao studentDao;
    private StudentEntity testStudentEntity;


    @BeforeEach
    void setUp() {
        studentDao = new StudentDao(jdbcTemplate);
        testStudentEntity = new StudentEntity("testName", "testDescription", 1);
    }  
    
    
    
    @Test
    void testStudentDao_ShouldCreateEntry() {
        studentDao.save(testStudentEntity);
        int expected = 1;
        int actual = studentDao.getAll().size();
        assertEquals(expected, actual);

    }

    @Test
    void testStudentDao_ShouldFindByIdEntry() {
        studentDao.save(testStudentEntity);

        String expected = "Optional[StudentEntity [id=1, firstname=testName, lastname=testDescription, groupId=1]]";
        String actual = studentDao.getByID(1).toString();

        assertEquals(expected, actual);
    }

    @Test
    void testStudentDao_ShouldFindAllEntry() {
        studentDao.save(testStudentEntity);
        studentDao.save(testStudentEntity);
        studentDao.save(testStudentEntity);

        int expected = 3;
        int actual = studentDao.getAll().size();
        assertEquals(expected, actual);
    }

    @Test
    void testStudentDao_ShouldUpdateEntry()  {
        studentDao.save(testStudentEntity);

        String[] update = {"Updated NEW_Firstname", "Updated NEW_Lastname"};

        studentDao.update(testStudentEntity, update);

        String expected = "Optional[StudentEntity [id=1, firstname=testName, lastname=testDescription, groupId=1]]";
        String actual = studentDao.getByID(1).toString();

        assertEquals(expected, actual);
    }

    @Test
    void testStudentDao_ShouldDeleteEntry() {
        studentDao.save(testStudentEntity);
        studentDao.save(testStudentEntity);
        studentDao.save(testStudentEntity);

        int expected = studentDao.getAll().size() - 1;

        studentDao.deleteById(1);
        int actual = studentDao.getAll().size();

        assertEquals(expected, actual);
    }
}
