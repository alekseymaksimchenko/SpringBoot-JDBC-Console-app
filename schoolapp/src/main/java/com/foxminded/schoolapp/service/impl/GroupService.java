package com.foxminded.schoolapp.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.foxminded.schoolapp.dao.GroupDao;
import com.foxminded.schoolapp.dao.entity.GroupEntity;
import com.foxminded.schoolapp.exception.DaoException;
import com.foxminded.schoolapp.exception.ServiceException;
import com.foxminded.schoolapp.service.GroupServices;
import com.foxminded.schoolapp.service.PopulateGeneratedData;
import com.foxminded.schoolapp.service.generator.Generator;

@Service
public class GroupService implements GroupServices<GroupEntity>, PopulateGeneratedData {

    private final GroupDao<GroupEntity> groupDao;
    private final Generator<GroupEntity> groupGenerator;
    private static final Logger LOGGER = LoggerFactory.getLogger(GroupService.class);

    @Autowired
    public GroupService(GroupDao<GroupEntity> groupDao, Generator<GroupEntity> groupGenerator) {
        this.groupDao = groupDao;
        this.groupGenerator = groupGenerator;
    }

    @Override
    public void populate() throws ServiceException {
        LOGGER.debug("GroupService populate - starts");
        try {
            groupGenerator.generate().forEach(groupDao::save);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public void save(GroupEntity group) throws ServiceException {
        LOGGER.debug("GroupService save ({}) - starts", group);
        try {
            groupDao.save(group);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public List<GroupEntity> getAll() throws ServiceException {
        LOGGER.debug("GroupService getAll - starts");
        try {
            return groupDao.getAll();
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public GroupEntity getByID(int id) throws ServiceException {
        LOGGER.debug("GroupService getByID - starts with id = {}", id);
        try {
            return groupDao.getByID(id);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public void update(GroupEntity group) throws ServiceException {
        LOGGER.debug("GroupService update ({}) - starts", group);
        try {
            groupDao.update(group);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public void deleteById(int id) throws ServiceException {
        LOGGER.debug("GroupService deleteById starts with id = {}", id);
        try {
            groupDao.getByID(id);
            groupDao.deleteById(id);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public List<GroupEntity> getAllGroupsAccordingStudentCount(int count) throws ServiceException {
        LOGGER.debug("GroupService getAllGroupsAccordingStudentCount - starts with students count = {}", count);
        try {
            return groupDao.getAllGroupsAccordingStudentCount(count);

        } catch (DaoException e) {
            throw new ServiceException(e.getMessage());
        }
    }
}
