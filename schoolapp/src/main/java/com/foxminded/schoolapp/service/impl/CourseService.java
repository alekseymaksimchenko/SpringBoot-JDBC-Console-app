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
    private static final String NOT_SAVED = "Record was NOT saved due to unknown reason";
    private static final String NOT_UPDATED = "Record was NOT updated due to unknown reason";
    private static final String NOT_DELETED = "Record was NOT deleted due to unknown reason";
    private static final String IS_EMPTY = "Table doesn't contain any Records";

    @Autowired
    public CourseService(CourseDao<CourseEntity> courseDao, Generator<CourseEntity> courseGenerator) {
        this.courseDao = courseDao;
        this.courseGenerator = courseGenerator;
    }

    @Override
    public void populate() {
        LOGGER.debug("CourseService populate - starts");
        courseGenerator.generate().forEach(courseDao::save);
    }

    @Override
    public void save(CourseEntity course) {
        LOGGER.debug("CourseService save ({}) - starts", course);
        int result = courseDao.save(course);
        if (result != 1) {
            throw new ServiceException(NOT_SAVED);
        }
    }

    @Override
    public List<CourseEntity> getAll() {
        LOGGER.debug("CourseService getAll - starts");
        List<CourseEntity> result = courseDao.getAll();
        if (!result.isEmpty()) {
            return result;
        } else {
            throw new ServiceException(IS_EMPTY);
        }
    }

    @Override
    public CourseEntity getByID(int id) {
        LOGGER.debug("CourseService getByID - starts with id = {}", id);
        try {
            return courseDao.getByID(id);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public void update(CourseEntity course) {
        LOGGER.debug("CourseService update ({}) - starts", course);
        int result = courseDao.update(course);
        if (result != 1) {
            throw new ServiceException(NOT_UPDATED);
        }
    }

    @Override
    public void deleteById(int id) {
        LOGGER.debug("CourseService deleteById starts with id = {}", id);
        try {
            courseDao.getByID(id);
            int result = courseDao.deleteById(id);
            if (result != 1) {
                throw new ServiceException(NOT_DELETED);
            }
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage());
        }
    }
}
