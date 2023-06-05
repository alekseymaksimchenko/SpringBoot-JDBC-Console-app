package com.foxminded.schoolapp.group;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class GroupRowMapper implements RowMapper<GroupEntity> {

    private static final String GROUP_ID = "group_id";
    private static final String GROUP_NAME = "name";

    @Override
    public GroupEntity mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
        return new GroupEntity(
                resultSet.getInt(GROUP_ID), 
                resultSet.getString(GROUP_NAME));
    }

}
