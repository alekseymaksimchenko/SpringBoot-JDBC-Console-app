package com.foxminded.schoolapp.dao.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.foxminded.schoolapp.dao.CourseDao;
import com.foxminded.schoolapp.dao.entity.CourseEntity;
import com.foxminded.schoolapp.dao.mapper.CourseRowMapper;

@Repository
public class CourseJdbcDao implements CourseDao<CourseEntity> {

    private final JdbcTemplate jdbcTemplate;
    private static final String SQL_SAVE = "INSERT INTO school.courses(name, description) VALUES (?, ?)";
    private static final String SQL_GET_ALL = "SELECT id, name, description FROM school.courses LIMIT 100;";
    private static final String SQL_GET_BY_ID = "SELECT id, name, description FROM school.courses WHERE id=?;";
    private static final String SQL_UPDATE = "UPDATE school.courses SET name=?, description=? WHERE id=?;";
    private static final String SQL_DELETE = "DELETE FROM school.courses WHERE id=?;";
    
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
    public Optional<CourseEntity> getByID(int id) {
         return jdbcTemplate.query(SQL_GET_BY_ID, new CourseRowMapper(), id).stream()
                .findFirst();
    }

    @Override
    public int update(CourseEntity course, String[] parameters) {
        String newName = (parameters.length >= 1 && parameters[0] != null) ? parameters[0] : course.getName();
        String newDescription = (parameters.length >= 2 && parameters[1] != null) ? parameters[1] : course.getDescription();

         return jdbcTemplate.update(SQL_UPDATE, newName, newDescription, course.getId());
    }

    @Override
    public int deleteById(int id) {
               return jdbcTemplate.update(SQL_DELETE, id);
    }

}
