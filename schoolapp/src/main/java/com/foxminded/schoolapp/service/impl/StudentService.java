package com.foxminded.schoolapp.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.foxminded.schoolapp.dao.StudentDao;
import com.foxminded.schoolapp.dao.entity.StudentEntity;
import com.foxminded.schoolapp.exception.DaoException;
import com.foxminded.schoolapp.exception.ServiceException;
import com.foxminded.schoolapp.service.PopulateGeneratedData;
import com.foxminded.schoolapp.service.PopulateStudentsToCorses;
import com.foxminded.schoolapp.service.StudentServices;
import com.foxminded.schoolapp.service.generator.StudentGenerator;

@Service
public class StudentService implements StudentServices<StudentEntity>, PopulateGeneratedData, PopulateStudentsToCorses {

    private final StudentDao<StudentEntity> studentDao;
    private final StudentGenerator<StudentEntity> studentGenerator;
    private static final Logger LOGGER = LoggerFactory.getLogger(StudentService.class);

    @Autowired
    public StudentService(StudentDao<StudentEntity> studentDao, StudentGenerator<StudentEntity> studentGenerator) {
        this.studentDao = studentDao;
        this.studentGenerator = studentGenerator;
    }

    @Override
    public void populate() throws ServiceException {
        LOGGER.debug("StudentService populate - starts");
        try {
            studentGenerator.generate().forEach(studentDao::save);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public void populateStudentsToCourses() throws ServiceException {
        LOGGER.debug("StudentService populateStudentsToCourses - starts");
        try {
            studentGenerator.studentToCourseGenerator().entrySet()
                    .forEach(s -> s.getValue().forEach(set -> studentDao.addStudentToCourse(s.getKey(), set)));
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public void save(StudentEntity student) throws ServiceException {
        LOGGER.debug("StudentService save ({}) - starts", student);
        try {
            studentDao.save(student);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public List<StudentEntity> getAll() throws ServiceException {
        LOGGER.debug("StudentService getAll - starts");
        try {
            return studentDao.getAll();
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public StudentEntity getByID(int id) throws ServiceException {
        LOGGER.debug("StudentService getByID - starts with id = {}", id);
        try {
            return studentDao.getByID(id);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public void update(StudentEntity student) throws ServiceException {
        LOGGER.debug("StudentService update ({}) - starts", student);
        try {
            studentDao.update(student);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public void deleteById(int id) throws ServiceException {
        LOGGER.debug("StudentService deleteById starts with id = {}", id);
        try {
            studentDao.getByID(id);
            studentDao.deleteById(id);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public void addStudentToCourse(StudentEntity student, int courseId) throws ServiceException {
        LOGGER.debug("StudentService addStudentToCourse ({}) - starts", student);
        try {
            studentDao.addStudentToCourse(student, courseId);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public List<StudentEntity> findAllStudentsRelatedToCourse(int courseId) throws ServiceException {
        LOGGER.debug("StudentsService findAllStudentsRelatedToCourse - starts with id = {}", courseId);
        try {
            return studentDao.findAllStudentsRelatedToCourse(courseId);

        } catch (DaoException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public void removeStudentByIDFromCourse(int studentId, int courseId) throws ServiceException {
        LOGGER.debug("StudentsService removeStudentByIDFromCourse - starts with studentId = {}, courseId = {}",
                studentId, courseId);
        try {
            studentDao.removeStudentByIDFromCourse(studentId, courseId);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage());
        }
    }

}
