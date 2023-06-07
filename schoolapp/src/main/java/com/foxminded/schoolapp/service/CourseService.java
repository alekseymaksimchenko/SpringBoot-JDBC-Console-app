package com.foxminded.schoolapp.service;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.foxminded.schoolapp.dao.impl.Course;
import com.foxminded.schoolapp.entity.CourseEntity;
import com.foxminded.schoolapp.exception.NotFoundException;
import com.foxminded.schoolapp.exception.UnsuccessfulOperationException;

@Service
public class CourseService implements ICourseService<CourseEntity> {

    private final Course<CourseEntity> courseDao;
    private final CoursesGenerator coursesGenerator;
    private static final Logger LOGGER = LoggerFactory.getLogger(CourseService.class);
    private static final String NOT_SAVED = "Record was NOT saved due to unknown reason";
    private static final String NOT_UPDATED = "Record was NOT updated due to unknown reason";
    private static final String NOT_DELETED = "Record was NOT deleted due to unknown reason";
    private static final String NOT_EXIST = "Record under provided id - not exist";

    @Autowired
    public CourseService(Course<CourseEntity> courseDao, CoursesGenerator coursesGenerator) {
        this.courseDao = courseDao;
        this.coursesGenerator = coursesGenerator;
    }

    public void save(CourseEntity course) {
        LOGGER.debug("CourseService save ({}) - starts", course);
        int result = courseDao.save(course);
        if (result != 1) {
            throw new UnsuccessfulOperationException(NOT_SAVED);
        }
    }

    public List<CourseEntity> getAll() {
        LOGGER.debug("CourseService getAll - starts");
        return courseDao.getAll();
    }

    public CourseEntity getByID(int id) {
        LOGGER.debug("CourseService getByID - starts with id = {}", id);
        return courseDao.getByID(id).orElseThrow(() -> new NotFoundException(NOT_EXIST));
    }

    public void update(CourseEntity course, String[] parameters) {
        LOGGER.debug("CourseService update ({}) - starts with parameters ({})", course, parameters);
        int result = courseDao.update(course, parameters);
        if (result != 1) {
            throw new UnsuccessfulOperationException(NOT_UPDATED);
        }
    }

    public void deleteById(int id) {
        LOGGER.debug("CourseService deleteById starts with id = {}", id);
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
