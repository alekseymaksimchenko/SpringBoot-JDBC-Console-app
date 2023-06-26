package com.foxminded.schoolapp.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.foxminded.schoolapp.dao.CourseDao;
import com.foxminded.schoolapp.dao.entity.CourseEntity;
import com.foxminded.schoolapp.dao.mapper.CourseRowMapper;
import com.foxminded.schoolapp.exception.DaoException;

@Repository
public class CourseJdbcDao implements CourseDao<CourseEntity> {

    private final JdbcTemplate jdbcTemplate;
    private static final String SQL_SAVE = "INSERT INTO school.courses(name, description) VALUES (?, ?)";
    private static final String SQL_GET_ALL = "SELECT id, name, description FROM school.courses LIMIT 100;";
    private static final String SQL_GET_BY_ID = "SELECT id, name, description FROM school.courses WHERE id=?;";
    private static final String SQL_UPDATE = "UPDATE school.courses SET name=?, description=? WHERE id=?;";
    private static final String SQL_DELETE = "DELETE FROM school.courses WHERE id=?;";
    private static final String GET_BY_ID_EXCEPTION = "Record under provided id - not exist";
    private static final String IS_EMPTY = "Table doesn't contain any Records";
    private static final String NOT_SUCCESSFUL_OPERATION = "Operation failed from Data Base side";

    @Autowired
    public CourseJdbcDao(JdbcTemplate jdbcTemplat) {
        this.jdbcTemplate = jdbcTemplat;
    }

    @Override
    public void save(CourseEntity course) throws DaoException {
        int result = jdbcTemplate.update(SQL_SAVE, course.getName(), course.getDescription());
        if (result != 1) {
            throw new DaoException(NOT_SUCCESSFUL_OPERATION);
        }
    }

    @Override
    public List<CourseEntity> getAll() throws DaoException {
        List<CourseEntity> result = jdbcTemplate.query(SQL_GET_ALL, new CourseRowMapper());
        if (!result.isEmpty()) {
            return result;
        } else {
            throw new DaoException(IS_EMPTY);
        }
    }

    @Override
    public CourseEntity getByID(int id) throws DaoException {
        try {
            return jdbcTemplate.queryForObject(SQL_GET_BY_ID, new CourseRowMapper(), id);
        } catch (EmptyResultDataAccessException e) {
            throw new DaoException(GET_BY_ID_EXCEPTION, e);
        }
    }

    @Override
    public void update(CourseEntity course) throws DaoException {
        int result = jdbcTemplate.update(SQL_UPDATE, course.getName(), course.getDescription(), course.getId());
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
}
