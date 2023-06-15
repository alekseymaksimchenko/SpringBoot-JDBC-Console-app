package com.foxminded.schoolapp.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.foxminded.schoolapp.dao.GroupDao;
import com.foxminded.schoolapp.dao.entity.GroupEntity;
import com.foxminded.schoolapp.exception.DaoException;
import com.foxminded.schoolapp.exception.NotFoundException;
import com.foxminded.schoolapp.exception.UnsuccessfulOperationException;
import com.foxminded.schoolapp.service.GroupServices;
import com.foxminded.schoolapp.service.PopulateGeneratedData;
import com.foxminded.schoolapp.service.generator.Generator;

@Service
public class GroupService implements GroupServices<GroupEntity>, PopulateGeneratedData {

    private final GroupDao<GroupEntity> groupDao;
    private final Generator<GroupEntity> groupGenerator;
    private static final Logger LOGGER = LoggerFactory.getLogger(GroupService.class);
    private static final String NOT_SAVED = "Record was NOT saved due to unknown reason";
    private static final String NOT_UPDATED = "Record was NOT updated due to unknown reason";
    private static final String NOT_DELETED = "Record was NOT deleted due to unknown reason";
    private static final String IS_EMPTY = "Table doesn't contain any Records";

    @Autowired
    public GroupService(GroupDao<GroupEntity> groupDao, Generator<GroupEntity> groupGenerator) {
        this.groupDao = groupDao;
        this.groupGenerator = groupGenerator;
    }

    @Override
    public void populate() {
        LOGGER.debug("GroupService populate - starts");
        groupGenerator.generate().forEach(groupDao::save);
    }

    @Override
    public void save(GroupEntity group) {
        LOGGER.debug("GroupService save ({}) - starts", group);
        int result = groupDao.save(group);
        if (result != 1) {
            throw new UnsuccessfulOperationException(NOT_SAVED);
        }
    }

    @Override
    public List<GroupEntity> getAll() {
        LOGGER.debug("GroupService getAll - starts");
        List<GroupEntity> result = groupDao.getAll();
        if (!result.isEmpty()) {
            return result;
        } else {
            throw new NotFoundException(IS_EMPTY);
        }
    }

    @Override
    public GroupEntity getByID(int id) {
        LOGGER.debug("GroupService getByID - starts with id = {}", id);
        try {
            return groupDao.getByID(id);
        } catch (DaoException e) {
            throw new NotFoundException(e.getMessage());
        }
    }

    @Override
    public void update(GroupEntity group) {
        LOGGER.debug("GroupService update ({}) - starts", group);
        int result = groupDao.update(group);
        if (result != 1) {
            throw new UnsuccessfulOperationException(NOT_UPDATED);
        }
    }

    @Override
    public void deleteById(int id) {
        LOGGER.debug("GroupService deleteById starts with id = {}", id);
        try {
            groupDao.getByID(id);
            int result = groupDao.deleteById(id);
            if (result != 1) {
                throw new UnsuccessfulOperationException(NOT_DELETED);
            }
        } catch (DaoException e) {
            throw new NotFoundException(e.getMessage());
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
