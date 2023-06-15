package com.foxminded.schoolapp.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.foxminded.schoolapp.dao.entity.StudentEntity;

public class StudentRowMapper implements RowMapper<StudentEntity> {

    private static final String STUDENT_ID = "id";
    private static final String STUDENT_FIRSTNAME = "firstname";
    private static final String STUDENT_LASTNAME = "lastname";
    private static final String STUDENT_GROUP_ID = "group_id";

    @Override
    public StudentEntity mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
        return new StudentEntity(resultSet.getInt(STUDENT_ID), resultSet.getString(STUDENT_FIRSTNAME),
                resultSet.getString(STUDENT_LASTNAME), resultSet.getInt(STUDENT_GROUP_ID));
    }

}
