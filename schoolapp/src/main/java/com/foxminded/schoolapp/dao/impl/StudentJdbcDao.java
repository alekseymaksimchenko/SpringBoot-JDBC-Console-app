package com.foxminded.schoolapp.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import com.foxminded.schoolapp.dao.StudentDao;
import com.foxminded.schoolapp.dao.entity.StudentEntity;
import com.foxminded.schoolapp.dao.mapper.StudentRowMapper;
import com.foxminded.schoolapp.exception.DaoException;

public class StudentJdbcDao implements StudentDao<StudentEntity> {

    private final JdbcTemplate jdbcTemplate;
    private static final String SQL_SAVE = "INSERT INTO school.students (firstname, lastname, group_id) VALUES(?, ?, ?);";
    private static final String SQL_GET_ALL = "SELECT id, firstname, lastname, group_id FROM school.students LIMIT 100;";
    private static final String SQL_GET_BY_ID = "SELECT id, firstname, lastname, group_id FROM school.students WHERE id=?;";
    private static final String SQL_UPDATE = "UPDATE school.students SET firstname=?, lastname=?, group_id=? WHERE id=?;";
    private static final String SQL_DELETE = "DELETE FROM school.students WHERE id=?;";
    private static final String SQL_ADD_STUDENT_TO_COURSE = "INSERT INTO school.students_courses (student_id, course_id) VALUES(?, ?)";
    private static final String SQL_FIND_ALL_STUDENTS_RELATED_TO_COURSE = "SELECT id, firstname, lastname, group_id FROM school.students_courses INNER JOIN school.students ON school.students_courses.student_id = school.students.id WHERE course_id=?";
    private static final String SQL_REMOVE_STUDENT_BY_ID_FROM_COURSE = "DELETE FROM school.students_courses WHERE student_id=? AND course_id=?;";
    private static final String GET_BY_ID_EXCEPTION = "Record under provided id - not exist";

    @Autowired
    public StudentJdbcDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int save(StudentEntity student) {
        return jdbcTemplate.update(SQL_SAVE, student.getFirstname(), student.getLastname(), student.getGroupId());
    }

    @Override
    public List<StudentEntity> getAll() {
        return jdbcTemplate.query(SQL_GET_ALL, new StudentRowMapper());
    }

    @Override
    public StudentEntity getByID(int id) throws DaoException {
        try {
            return jdbcTemplate.queryForObject(SQL_GET_BY_ID, new StudentRowMapper(), id);
        } catch (EmptyResultDataAccessException e) {
            throw new DaoException(GET_BY_ID_EXCEPTION);
        }
    }

    @Override
    public int update(StudentEntity student) {
        return jdbcTemplate.update(SQL_UPDATE, student.getFirstname(), student.getLastname(), student.getGroupId(),
                student.getId());
    }

    @Override
    public int deleteById(int id) {
        return jdbcTemplate.update(SQL_DELETE, id);
    }

    @Override
    public int addStudentToCourse(StudentEntity student, int courseId) {
        return jdbcTemplate.update(SQL_ADD_STUDENT_TO_COURSE, student.getId(), courseId);
    }

    @Override
    public List<StudentEntity> findAllStudentsRelatedToCourse(int courseId) {
        return jdbcTemplate.query(SQL_FIND_ALL_STUDENTS_RELATED_TO_COURSE, new StudentRowMapper(), courseId);
    }

    @Override
    public int removeStudentByIDFromCourse(int studentId, int courseId) {
        return jdbcTemplate.update(SQL_REMOVE_STUDENT_BY_ID_FROM_COURSE, studentId, courseId);
    }

}
