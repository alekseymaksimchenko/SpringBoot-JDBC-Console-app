package com.foxminded.schoolapp.student_course;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

public class StudentCourseDao implements StudentCourse<StudentCourseEntity> {

    private final JdbcTemplate jdbcTemplate;
    private static final String SQL_ADD_STUDENT_TO_COURSE = "INSERT INTO school.students_courses (student_id, course_id) VALUES(?, ?)";
    private static final String SQL_FIND_ALL_STUDENTS_RELATED_TO_COURSE = "SELECT student_id, course_id FROM school.students_courses WHERE course_id=? LIMIT 100;";
    private static final String SQL_REMOVE_STUDENT_BY_ID_FROM_COURSE = "DELETE FROM school.students_courses WHERE student_id=? AND course_id=?;";

    @Autowired
    public StudentCourseDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int addStudentToCourse(StudentCourseEntity studentCourse) {
        return jdbcTemplate.update(SQL_ADD_STUDENT_TO_COURSE, studentCourse.getStudentId(),
                studentCourse.getCourseId());
    }

    @Override
    public List<StudentCourseEntity> findAllStudentsRelatedToCourse(int courseId) {
        return jdbcTemplate.query(SQL_FIND_ALL_STUDENTS_RELATED_TO_COURSE, new StudentCourseRowMapper(), courseId);
    }

    @Override
    public int removeStudentByIDFromCourse(int studentId, int courseId) {
        return jdbcTemplate.update(SQL_REMOVE_STUDENT_BY_ID_FROM_COURSE, studentId, courseId);
    }

}
