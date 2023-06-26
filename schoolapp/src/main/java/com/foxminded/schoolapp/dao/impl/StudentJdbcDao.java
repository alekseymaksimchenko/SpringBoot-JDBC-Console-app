package com.foxminded.schoolapp.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.foxminded.schoolapp.dao.StudentDao;
import com.foxminded.schoolapp.dao.entity.StudentEntity;
import com.foxminded.schoolapp.dao.mapper.StudentRowMapper;
import com.foxminded.schoolapp.exception.DaoException;

@Repository
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
    private static final String IS_EMPTY = "Table doesn't contain any Records";
    private static final String NOT_SUCCESSFUL_OPERATION = "Operation failed from Data Base side";

    @Autowired
    public StudentJdbcDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(StudentEntity student) throws DaoException {
        int result = jdbcTemplate.update(SQL_SAVE, student.getFirstname(), student.getLastname(), student.getGroupId());
        if (result != 1) {
            throw new DaoException(NOT_SUCCESSFUL_OPERATION);
        }
    }

    @Override
    public List<StudentEntity> getAll() throws DaoException {
        List<StudentEntity> result = jdbcTemplate.query(SQL_GET_ALL, new StudentRowMapper());
        if (!result.isEmpty()) {
            return result;
        } else {
            throw new DaoException(IS_EMPTY);
        }
    }

    @Override
    public StudentEntity getByID(int id) throws DaoException {
        try {
            return jdbcTemplate.queryForObject(SQL_GET_BY_ID, new StudentRowMapper(), id);
        } catch (EmptyResultDataAccessException e) {
            throw new DaoException(GET_BY_ID_EXCEPTION, e);
        }
    }

    @Override
    public void update(StudentEntity student) throws DaoException {
        int result = jdbcTemplate.update(SQL_UPDATE, student.getFirstname(), student.getLastname(),
                student.getGroupId(), student.getId());
        if (result != 1) {
            throw new DaoException(NOT_SUCCESSFUL_OPERATION);
        }
    }

    @Override
    public void deleteById(int id) throws DaoException {
        int result = jdbcTemplate.update(SQL_DELETE, id);
        if (result != 1) {
            throw new DaoException(NOT_SUCCESSFUL_OPERATION);
        }
    }

    @Override
    public void addStudentToCourse(StudentEntity student, int courseId) throws DaoException {
        int result = jdbcTemplate.update(SQL_ADD_STUDENT_TO_COURSE, student.getId(), courseId);
        if (result != 1) {
            throw new DaoException(NOT_SUCCESSFUL_OPERATION);
        }
    }

    @Override
    public List<StudentEntity> findAllStudentsRelatedToCourse(int courseId) throws DaoException {
        List<StudentEntity> result = jdbcTemplate.query(SQL_FIND_ALL_STUDENTS_RELATED_TO_COURSE, new StudentRowMapper(),
                courseId);
        if (!result.isEmpty()) {
            return result;
        } else {
            throw new DaoException(IS_EMPTY);
        }
    }

    @Override
    public void removeStudentByIDFromCourse(int studentId, int courseId) throws DaoException {
        int result = jdbcTemplate.update(SQL_REMOVE_STUDENT_BY_ID_FROM_COURSE, studentId, courseId);
        if (result != 1) {
            throw new DaoException(NOT_SUCCESSFUL_OPERATION);
        }
    }

}
