package com.foxminded.schoolapp.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.foxminded.schoolapp.dao.GroupDao;
import com.foxminded.schoolapp.dao.entity.GroupEntity;
import com.foxminded.schoolapp.dao.mapper.GroupRowMapper;
import com.foxminded.schoolapp.exception.DaoException;

@Repository
public class GroupJdbcDao implements GroupDao<GroupEntity> {

    private final JdbcTemplate jdbcTemplate;
    private static final String SQL_SAVE = "INSERT INTO school.groups (name) VALUES(?);";
    private static final String SQL_GET_ALL = "SELECT group_id, name FROM school.groups LIMIT 100;";
    private static final String SQL_GET_BY_ID = "SELECT group_id, name FROM school.groups WHERE group_id=?;";
    private static final String SQL_UPDATE = "UPDATE school.groups SET name=? WHERE group_id=?;";
    private static final String SQL_DELETE = "DELETE FROM school.groups WHERE group_id=?;";
    private static final String FIND_GROUPS_ACCORDING_STUDENT_COUNT = "SELECT school.students.group_id, school.groups.name FROM school.students INNER JOIN school.groups ON school.students.group_id = school.groups.group_id GROUP BY school.groups.name, school.students.group_id HAVING COUNT(*)<=? ORDER BY school.students.group_id;";
    private static final String GET_BY_ID_EXCEPTION = "Record under provided id - not exist";
    private static final String IS_EMPTY = "Table doesn't contain any Records";
    private static final String NOT_SUCCESSFUL_OPERATION = "Operation failed from Data Base side";

    @Autowired
    public GroupJdbcDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<GroupEntity> getAllGroupsAccordingStudentCount(int count) throws DaoException {
        List<GroupEntity> result = jdbcTemplate.query(FIND_GROUPS_ACCORDING_STUDENT_COUNT, new GroupRowMapper(), count);
        if (!result.isEmpty()) {
            return result;
        } else {
            throw new DaoException(IS_EMPTY);
        }
    }

    @Override
    public void save(GroupEntity group) throws DaoException  {
        int result = jdbcTemplate.update(SQL_SAVE, group.getName());
        if (result != 1) {
            throw new DaoException(NOT_SUCCESSFUL_OPERATION);
        }
    }

    @Override
    public List<GroupEntity> getAll() throws DaoException  {
        List<GroupEntity> result = jdbcTemplate.query(SQL_GET_ALL, new GroupRowMapper());
        if (!result.isEmpty()) {
            return result;
        } else {
            throw new DaoException(IS_EMPTY);
        }
    }

    @Override
    public GroupEntity getByID(int id) throws DaoException {
        try {
            return jdbcTemplate.queryForObject(SQL_GET_BY_ID, new GroupRowMapper(), id);
        } catch (EmptyResultDataAccessException e) {
            throw new DaoException(GET_BY_ID_EXCEPTION, e);
        }
    }

    @Override
    public void update(GroupEntity group) throws DaoException  {
        int result = jdbcTemplate.update(SQL_UPDATE, group.getName(), group.getGroupId());
        if (result != 1) {
            throw new DaoException(NOT_SUCCESSFUL_OPERATION);
        }
    }

    @Override
    public void deleteById(int id) throws DaoException  {
        int result = jdbcTemplate.update(SQL_DELETE, id);
        if (result != 1) {
            throw new DaoException(NOT_SUCCESSFUL_OPERATION);
        }
    }

}
