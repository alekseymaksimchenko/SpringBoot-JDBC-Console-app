package com.foxminded.schoolapp.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.foxminded.schoolapp.dao.GenericDao;
import com.foxminded.schoolapp.dao.entity.CourseEntity;
import com.foxminded.schoolapp.exception.NotFoundException;
import com.foxminded.schoolapp.exception.UnsuccessfulOperationException;
import com.foxminded.schoolapp.service.CourseService;
import com.foxminded.schoolapp.service.generator.Generator;

@Service
public class CourseJdbcService implements CourseService<CourseEntity> {

    private final GenericDao<CourseEntity> courseDao;
    private final Generator<CourseEntity> courseGenerator;
    private static final Logger LOGGER = LoggerFactory.getLogger(CourseJdbcService.class);
    private static final String NOT_SAVED = "Record was NOT saved due to unknown reason";
    private static final String GENERATED_RECORD_NOT_SAVED = "Generated Record was NOT saved during populate method due to unknown reason";
    private static final String NOT_UPDATED = "Record was NOT updated due to unknown reason";
    private static final String NOT_DELETED = "Record was NOT deleted due to unknown reason";
    private static final String NOT_EXIST = "Record under provided id - not exist";

    @Autowired
    public CourseJdbcService(GenericDao<CourseEntity> courseDao, Generator<CourseEntity> courseGenerator) {
        this.courseDao = courseDao;
        this.courseGenerator = courseGenerator;
    }

    @Override
    public void populate() {
        LOGGER.debug("CourseJdbcService populate - starts");
        courseGenerator.generate().forEach(course -> {
            int result = courseDao.save(course);
            if (result != 1) {
                throw new UnsuccessfulOperationException(GENERATED_RECORD_NOT_SAVED);
            }
        });

    }

    @Override
    public void save(CourseEntity course) {
        LOGGER.debug("CourseJdbcService save ({}) - starts", course);
        int result = courseDao.save(course);
        if (result != 1) {
            throw new UnsuccessfulOperationException(NOT_SAVED);
        }
    }

    @Override
    public List<CourseEntity> getAll() {
        LOGGER.debug("CourseJdbcService getAll - starts");
        return courseDao.getAll();
    }

    @Override
    public CourseEntity getByID(int id) {
        LOGGER.debug("CourseJdbcService getByID - starts with id = {}", id);
        return courseDao.getByID(id).orElseThrow(() -> new NotFoundException(NOT_EXIST));
    }

    @Override
    public void update(CourseEntity course, String[] parameters) {
        LOGGER.debug("CourseJdbcService update ({}) - starts with parameters ({})", course, parameters);
        int result = courseDao.update(course, parameters);
        if (result != 1) {
            throw new UnsuccessfulOperationException(NOT_UPDATED);
        }
    }

    @Override
    public void deleteById(int id) {
        LOGGER.debug("CourseJdbcService deleteById starts with id = {}", id);
        if (courseDao.getByID(id).isPresent()) {
            int result = courseDao.deleteById(id);
            if (result != 1) {
                throw new UnsuccessfulOperationException(NOT_DELETED);
            }
        } else {
            throw new NotFoundException(NOT_EXIST);
        }
    }

}
