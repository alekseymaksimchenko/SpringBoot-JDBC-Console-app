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

import com.foxminded.schoolapp.dao.entity.StudentCourseEntity;

@Testcontainers
@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = {"/clear_tables.sql"},
statements = "insert into school.courses (name) values ('Bio'), ('Math'), ('Music'); insert into school.groups (name) values ('group1'), ('group2'), ('group3'); insert into school.students (firstname, lastname, group_id) values ('test1', 'test1', 1), ('test1', 'test1', 1), ('test2', 'test2', 2); insert into school.students_courses (student_id, course_id) values ('1', '1'), ('1', '2'), ('2', '2'), ('3', '2');",
executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class StudentCourseJdbcDaoTest {

    @Container
    private static PostgreSQLContainer<?> postgresqlContainer = new PostgreSQLContainer<>("postgres:latest")
            .withReuse(true);

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private StudentCourseJdbcDao studentCourseJdbcDao;
    private StudentCourseEntity testStudentCourseEntity;

    @BeforeEach
    void setUp() {
        studentCourseJdbcDao = new StudentCourseJdbcDao(jdbcTemplate);
    }

    @Test
    void testStudentCourseJdbcDao_ShouldAddStudentToCourse() {
        testStudentCourseEntity = new StudentCourseEntity(3, 3);
        studentCourseJdbcDao.addStudentToCourse(testStudentCourseEntity);
        int expected = 1;
        int actual = studentCourseJdbcDao.findAllStudentsRelatedToCourse(1).size();
        assertEquals(expected, actual);

    }

    @Test
    void testStudentCourseJdbcDao_ShouldfindAllStudentsRelatedToCourseByCourseId() {
       int expected = 3;
       int actual = studentCourseJdbcDao.findAllStudentsRelatedToCourse(2).size();

       assertEquals(expected, actual);
    }

    @Test
    void testStudentCourseJdbcDao_ShouldRemoveStudentFromCourse() {

        int expected = studentCourseJdbcDao.findAllStudentsRelatedToCourse(2).size() - 1;

        studentCourseJdbcDao.removeStudentByIDFromCourse(1, 2);
        int actual = studentCourseJdbcDao.findAllStudentsRelatedToCourse(2).size();

        assertEquals(expected, actual);
    }
}
