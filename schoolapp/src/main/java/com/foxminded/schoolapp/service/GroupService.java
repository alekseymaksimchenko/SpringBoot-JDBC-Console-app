package com.foxminded.schoolapp.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.foxminded.schoolapp.dao.impl.Group;
import com.foxminded.schoolapp.entity.GroupEntity;
import com.foxminded.schoolapp.exception.NotFoundException;
import com.foxminded.schoolapp.exception.UnsuccessfulOperationException;

@Service
public class GroupService implements IGroupService<GroupEntity> {

    private final Group<GroupEntity> groupDao;
    private static final Logger LOGGER = LoggerFactory.getLogger(GroupService.class);
    private static final String NOT_SAVED = "Record was NOT saved due to unknown reason";
    private static final String NOT_UPDATED = "Record was NOT updated due to unknown reason";
    private static final String NOT_DELETED = "Record was NOT deleted due to unknown reason";
    private static final String NOT_EXIST = "Record under provided id - not exist";

    @Autowired
    public GroupService(Group<GroupEntity> groupDao) {
        this.groupDao = groupDao;
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
        return groupDao.getAll();
    }

    @Override
    public GroupEntity getByID(int id) {
        LOGGER.debug("GroupService getByID - starts with id = {}", id);
        return groupDao.getByID(id).orElseThrow(() -> new NotFoundException(NOT_EXIST));
    }

    @Override
    public void update(GroupEntity group, String[] parameters) {
        LOGGER.debug("GroupService update ({}) - starts with parameters ({})", group, parameters);
        int result = groupDao.update(group, parameters);
        if (result != 1) {
            throw new UnsuccessfulOperationException(NOT_UPDATED);
        }
    }

    @Override
    public void deleteById(int id) {
        LOGGER.debug("CourseService deleteById starts with id = {}", id);
        if (groupDao.getByID(id).isPresent()) {
            int result = groupDao.deleteById(id);
            if (result != 1) {
                throw new UnsuccessfulOperationException(NOT_DELETED);
            }
        } else {
            throw new NotFoundException(NOT_EXIST);
        }
    }
}
