package com.foxminded.schoolapp.student_course;

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
@Sql(scripts = {"/clear_tables.sql"}, 
statements = "insert into school.courses (name) values ('Bio'), ('Math'), ('Music'); insert into school.groups (name) values ('group1'), ('group2'), ('group3'); insert into school.students (firstname, lastname, group_id) values ('test1', 'test1', 1), ('test1', 'test1', 1), ('test2', 'test2', 2); insert into school.students_courses (student_id, course_id) values ('1', '1'), ('1', '2'), ('2', '2'), ('3', '2');", 
executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class StudentCourseDaoTest {

    @Container
    private static PostgreSQLContainer<?> postgresqlContainer = new PostgreSQLContainer<>("postgres:latest")
            .withReuse(true);

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private StudentCourseDao studentCourseDao;
    private StudentCourseEntity testStudentCourseEntity;

    @BeforeEach
    void setUp() {
        studentCourseDao = new StudentCourseDao(jdbcTemplate);
    }

    @Test
    void testStudentCourseDao_ShouldAddStudentToCourse() {
        testStudentCourseEntity = new StudentCourseEntity(3, 3);
        studentCourseDao.addStudentToCourse(testStudentCourseEntity);
        int expected = 1;
        int actual = studentCourseDao.findAllStudentsRelatedToCourse(1).size();
        assertEquals(expected, actual);

    }

    @Test
    void testStudentCourseDao_ShouldFindAllStudentsByCourseId() {
        String expected = "[StudentCourseEntity [studentId=1, courseId=2], StudentCourseEntity [studentId=2, courseId=2], StudentCourseEntity [studentId=3, courseId=2]]";
        String actual = studentCourseDao.findAllStudentsRelatedToCourse(2).toString();

        assertEquals(expected, actual);
    }

    @Test
    void testStudentCourseDao_ShouldRemoveStudentFromCourse() {

        int expected = studentCourseDao.findAllStudentsRelatedToCourse(2).size() - 1;

        studentCourseDao.removeStudentByIDFromCourse(1, 2);
        int actual = studentCourseDao.findAllStudentsRelatedToCourse(2).size();

        assertEquals(expected, actual);
    }
}
