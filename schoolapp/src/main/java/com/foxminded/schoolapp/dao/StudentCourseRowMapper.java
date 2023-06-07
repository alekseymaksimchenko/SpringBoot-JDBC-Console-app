package com.foxminded.schoolapp.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.foxminded.schoolapp.entity.StudentCourseEntity;

public class StudentCourseRowMapper implements RowMapper<StudentCourseEntity> {

    private static final String STIDENT_ID = "student_id";
    private static final String COURSE_ID = "course_id";

    @Override
    public StudentCourseEntity mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
        return new StudentCourseEntity(
                resultSet.getInt(STIDENT_ID),
                resultSet.getInt(COURSE_ID));
    }

}
