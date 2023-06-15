package com.foxminded.schoolapp.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import com.foxminded.schoolapp.dao.GroupDao;
import com.foxminded.schoolapp.dao.entity.GroupEntity;
import com.foxminded.schoolapp.dao.mapper.GroupRowMapper;
import com.foxminded.schoolapp.exception.DaoException;

public class GroupJdbcDao implements GroupDao<GroupEntity> {

    private final JdbcTemplate jdbcTemplate;
    private static final String SQL_SAVE = "INSERT INTO school.groups (name) VALUES(?);";
    private static final String SQL_GET_ALL = "SELECT group_id, name FROM school.groups LIMIT 100;";
    private static final String SQL_GET_BY_ID = "SELECT group_id, name FROM school.groups WHERE group_id=?;";
    private static final String SQL_UPDATE = "UPDATE school.groups SET name=? WHERE group_id=?;";
    private static final String SQL_DELETE = "DELETE FROM school.groups WHERE group_id=?;";
    private static final String FIND_GROUPS_ACCORDING_STUDENT_COUNT = "SELECT school.students.group_id, school.groups.name FROM school.students INNER JOIN school.groups ON school.students.group_id = school.groups.group_id GROUP BY school.groups.name, school.students.group_id HAVING COUNT(*)<=? ORDER BY school.students.group_id;";
    private static final String GET_BY_ID_EXCEPTION = "Record under provided id - not exist";

    @Autowired
    public GroupJdbcDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<GroupEntity> getAllGroupsAccordingStudentCount(int count) {
        return jdbcTemplate.query(FIND_GROUPS_ACCORDING_STUDENT_COUNT, new GroupRowMapper(), count);
    }

    @Override
    public int save(GroupEntity group) {
        return jdbcTemplate.update(SQL_SAVE, group.getName());
    }

    @Override
    public List<GroupEntity> getAll() {
        return jdbcTemplate.query(SQL_GET_ALL, new GroupRowMapper());
    }

    @Override
    public GroupEntity getByID(int id) throws DaoException {
        try {
            return jdbcTemplate.queryForObject(SQL_GET_BY_ID, new GroupRowMapper(), id);
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            throw new DaoException(GET_BY_ID_EXCEPTION);
        }
    }

    @Override
    public int update(GroupEntity group) {
        return jdbcTemplate.update(SQL_UPDATE, group.getName(), group.getGroupId());
    }

    @Override
    public int deleteById(int id) {
        return jdbcTemplate.update(SQL_DELETE, id);
    }

}
