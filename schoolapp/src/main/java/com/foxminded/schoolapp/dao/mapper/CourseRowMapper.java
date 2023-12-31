package com.foxminded.schoolapp.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.foxminded.schoolapp.dao.entity.CourseEntity;

public class CourseRowMapper implements RowMapper<CourseEntity> {

    private static final String COURSE_ID = "id";
    private static final String COURSE_NAME = "name";
    private static final String COURSE_DESCRIPTION = "description";

    @Override
    public CourseEntity mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
        return new CourseEntity(resultSet.getInt(COURSE_ID), resultSet.getString(COURSE_NAME),
                resultSet.getString(COURSE_DESCRIPTION));
    }

}
