package com.foxminded.schoolapp.dao.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import com.foxminded.schoolapp.dao.StudentCourseRowMapper;
import com.foxminded.schoolapp.entity.StudentCourseEntity;

public class StudentCourseJdbcDao implements StudentCourse<StudentCourseEntity> {

    private final JdbcTemplate jdbcTemplate;
    private static final String SQL_ADD_STUDENT_TO_COURSE = "INSERT INTO school.students_courses (student_id, course_id) VALUES(?, ?)";
    private static final String SQL_FIND_ALL_STUDENTS_RELATED_TO_COURSE = "SELECT student_id, course_id FROM school.students_courses WHERE course_id=? LIMIT 100;";
    private static final String SQL_REMOVE_STUDENT_BY_ID_FROM_COURSE = "DELETE FROM school.students_courses WHERE student_id=? AND course_id=?;";

    @Autowired
    public StudentCourseJdbcDao(JdbcTemplate jdbcTemplate) {
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

    @Override
    public int save(StudentCourseEntity course) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Not implemented, yet");
    }

    @Override
    public List<StudentCourseEntity> getAll() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Not implemented, yet");
    }

    @Override
    public Optional<StudentCourseEntity> getByID(int id) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Not implemented, yet");
    }

    @Override
    public int update(StudentCourseEntity course, String[] parameters) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Not implemented, yet");
    }

    @Override
    public int deleteById(int id) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Not implemented, yet");
    }

}
