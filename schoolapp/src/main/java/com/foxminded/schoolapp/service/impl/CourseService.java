package com.foxminded.schoolapp.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.foxminded.schoolapp.dao.CourseDao;
import com.foxminded.schoolapp.dao.entity.CourseEntity;
import com.foxminded.schoolapp.exception.DaoException;
import com.foxminded.schoolapp.exception.ServiceException;
import com.foxminded.schoolapp.service.CourseServices;
import com.foxminded.schoolapp.service.PopulateGeneratedData;
import com.foxminded.schoolapp.service.generator.Generator;

@Service
public class CourseService implements CourseServices<CourseEntity>, PopulateGeneratedData {

    private final CourseDao<CourseEntity> courseDao;
    private final Generator<CourseEntity> courseGenerator;
    private static final Logger LOGGER = LoggerFactory.getLogger(CourseService.class);

    @Autowired
    public CourseService(CourseDao<CourseEntity> courseDao, Generator<CourseEntity> courseGenerator) {
        this.courseDao = courseDao;
        this.courseGenerator = courseGenerator;
    }

    @Override
    public void populate() throws ServiceException {
        LOGGER.debug("CourseService populate - starts");
        try {
            courseGenerator.generate().forEach(courseDao::save);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public void save(CourseEntity course) throws ServiceException {
        LOGGER.debug("CourseService save ({}) - starts", course);
        try {
            courseDao.save(course);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public List<CourseEntity> getAll() throws ServiceException {
        LOGGER.debug("CourseService getAll - starts");
        try {
            return courseDao.getAll();
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public CourseEntity getByID(int id) throws ServiceException {
        LOGGER.debug("CourseService getByID - starts with id = {}", id);
        try {
            return courseDao.getByID(id);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public void update(CourseEntity course) throws ServiceException {
        LOGGER.debug("CourseService update ({}) - starts", course);
        try {
            courseDao.update(course);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public void deleteById(int id) throws ServiceException {
        LOGGER.debug("CourseService deleteById starts with id = {}", id);
        try {
            courseDao.getByID(id);
            courseDao.deleteById(id);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage());
        }
    }
}
