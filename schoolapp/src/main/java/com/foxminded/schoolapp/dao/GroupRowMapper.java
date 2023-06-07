package com.foxminded.schoolapp.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.foxminded.schoolapp.entity.GroupEntity;

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
