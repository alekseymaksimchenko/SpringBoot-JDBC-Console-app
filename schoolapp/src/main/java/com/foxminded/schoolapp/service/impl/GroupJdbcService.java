package com.foxminded.schoolapp.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.foxminded.schoolapp.dao.GroupDao;
import com.foxminded.schoolapp.dao.entity.GroupEntity;
import com.foxminded.schoolapp.exception.NotFoundException;
import com.foxminded.schoolapp.exception.UnsuccessfulOperationException;
import com.foxminded.schoolapp.service.GroupService;
import com.foxminded.schoolapp.service.PopulateGeneratedData;
import com.foxminded.schoolapp.service.generator.Generator;

@Service
public class GroupJdbcService implements GroupService<GroupEntity>, PopulateGeneratedData {

    private final GroupDao<GroupEntity> groupDao;
    private final Generator<GroupEntity> groupGenerator;
    private static final Logger LOGGER = LoggerFactory.getLogger(GroupJdbcService.class);
    private static final String NOT_SAVED = "Record was NOT saved due to unknown reason";
    private static final String NOT_UPDATED = "Record was NOT updated due to unknown reason";
    private static final String NOT_DELETED = "Record was NOT deleted due to unknown reason";
    private static final String NOT_EXIST = "Record under provided id - not exist";
    private static final String IS_EMPTY = "Table doesn't contain any Records";

    @Autowired
    public GroupJdbcService(GroupDao<GroupEntity> groupDao, Generator<GroupEntity> groupGenerator) {
        this.groupDao = groupDao;
        this.groupGenerator = groupGenerator;
    }

    @Override
    public void populate() {
        LOGGER.debug("GroupJdbcService populate - starts");
        groupGenerator.generate().forEach(group -> groupDao.save(group));
    }

    @Override
    public void save(GroupEntity group) {
        LOGGER.debug("GroupJdbcService save ({}) - starts", group);
        int result = groupDao.save(group);
        if (result != 1) {
            throw new UnsuccessfulOperationException(NOT_SAVED);
        }
    }

    @Override
    public List<GroupEntity> getAll() {
        LOGGER.debug("GroupJdbcService getAll - starts");
        List<GroupEntity> result = groupDao.getAll();
        if (!result.isEmpty()) {
            return result;
        } else {
            throw new NotFoundException(IS_EMPTY);
        }
    }

    @Override
    public GroupEntity getByID(int id) {
        LOGGER.debug("GroupJdbcService getByID - starts with id = {}", id);
        return groupDao.getByID(id).orElseThrow(() -> new NotFoundException(NOT_EXIST));
    }

    @Override
    public void update(GroupEntity group, String[] parameters) {
        LOGGER.debug("GroupJdbcService update ({}) - starts with parameters ({})", group, parameters);
        int result = groupDao.update(group, parameters);
        if (result != 1) {
            throw new UnsuccessfulOperationException(NOT_UPDATED);
        }
    }

    @Override
    public void deleteById(int id) {
        LOGGER.debug("GroupJdbcService deleteById starts with id = {}", id);
        if (groupDao.getByID(id).isPresent()) {
            int result = groupDao.deleteById(id);
            if (result != 1) {
                throw new UnsuccessfulOperationException(NOT_DELETED);
            }
        } else {
            throw new NotFoundException(NOT_EXIST);
        }
    }

    @Override
    public List<GroupEntity> getAllGroupsAccordingStudentCount(int count) {
        LOGGER.debug("GroupService getAllGroupsAccordingStudentCount - starts with students count = {}", count);
        List<GroupEntity> result = groupDao.getAllGroupsAccordingStudentCount(count);
        if (!result.isEmpty()) {
            return result;
        } else {
            throw new NotFoundException(IS_EMPTY);
        }
    }
}
