package com.foxminded.schoolapp.course;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class CourseRowMapper implements RowMapper<CourseEntity> {
    
    private static final String COURSE_ID = "id";
    private static final String COURSE_NAME = "name";
    private static final String COURSE_DESCRIPTION = "description";

    @Override
    public CourseEntity mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
         return new CourseEntity(
                 resultSet.getInt(COURSE_ID), 
                 resultSet.getString(COURSE_NAME), 
                 resultSet.getString(COURSE_DESCRIPTION));
     }

}
