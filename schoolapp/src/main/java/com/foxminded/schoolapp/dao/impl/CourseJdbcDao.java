package com.foxminded.schoolapp.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    public CourseJdbcDao(JdbcTemplate jdbcTemplat) {
        this.jdbcTemplate = jdbcTemplat;
    }

    @Override
    public int save(CourseEntity course) {
        return jdbcTemplate.update(SQL_SAVE, course.getName(), course.getDescription());
    }

    @Override
    public List<CourseEntity> getAll() {
        return jdbcTemplate.query(SQL_GET_ALL, new CourseRowMapper());
    }

    @Override
    public CourseEntity getByID(int id) throws DaoException {
        try {
            return jdbcTemplate.queryForObject(SQL_GET_BY_ID, new CourseRowMapper(), id);
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            throw new DaoException(GET_BY_ID_EXCEPTION);
        }
    }

    @Override
    public int update(CourseEntity course) {
        return jdbcTemplate.update(SQL_UPDATE, course.getName(), course.getDescription(), course.getId());
    }

    @Override
    public int deleteById(int id) {
        return jdbcTemplate.update(SQL_DELETE, id);
    }

}
