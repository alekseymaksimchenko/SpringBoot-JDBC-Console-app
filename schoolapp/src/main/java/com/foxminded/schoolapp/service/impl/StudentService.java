package com.foxminded.schoolapp.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.foxminded.schoolapp.dao.StudentDao;
import com.foxminded.schoolapp.dao.entity.StudentEntity;
import com.foxminded.schoolapp.exception.DaoException;
import com.foxminded.schoolapp.exception.NotFoundException;
import com.foxminded.schoolapp.exception.UnsuccessfulOperationException;
import com.foxminded.schoolapp.service.PopulateGeneratedData;
import com.foxminded.schoolapp.service.PopulateStudentsToCorses;
import com.foxminded.schoolapp.service.StudentServices;
import com.foxminded.schoolapp.service.generator.StudentGenerator;

@Service
public class StudentService implements StudentServices<StudentEntity>, PopulateGeneratedData, PopulateStudentsToCorses {

    private final StudentDao<StudentEntity> studentDao;
    private final StudentGenerator<StudentEntity> studentGenerator;
    private static final Logger LOGGER = LoggerFactory.getLogger(StudentService.class);
    private static final String NOT_SAVED = "Record was NOT saved due to unknown reason";
    private static final String NOT_UPDATED = "Record was NOT updated due to unknown reason";
    private static final String NOT_DELETED = "Record was NOT deleted due to unknown reason";
    private static final String NOT_EXIST = "Record under provided id - not exist";
    private static final String IS_EMPTY = "Table doesn't contain any Records";

    @Autowired
    public StudentService(StudentDao<StudentEntity> studentDao, StudentGenerator<StudentEntity> studentGenerator) {
        this.studentDao = studentDao;
        this.studentGenerator = studentGenerator;
    }

    @Override
    public void populate() {
        LOGGER.debug("StudentService populate - starts");
        studentGenerator.generate().forEach(studentDao::save);
    }

    @Override
    public void populateStudentsToCourses() {
        LOGGER.debug("StudentService populateStudentsToCourses - starts");
        studentGenerator.studentToCourseGenerator().entrySet().forEach(s -> 
            s.getValue().forEach(set -> studentDao.addStudentToCourse(s.getKey(), set)));      
    }

    @Override
    public void save(StudentEntity student) {
        LOGGER.debug("StudentService save ({}) - starts", student);
        int result = studentDao.save(student);
        if (result != 1) {
            throw new UnsuccessfulOperationException(NOT_SAVED);
        }
    }

    @Override
    public List<StudentEntity> getAll() {
        LOGGER.debug("StudentService getAll - starts");
        List<StudentEntity> result = studentDao.getAll();
        if (!result.isEmpty()) {
            return result;
        } else {
            throw new NotFoundException(IS_EMPTY);
        }
    }

    @Override
    public StudentEntity getByID(int id) {
        LOGGER.debug("StudentService getByID - starts with id = {}", id);
        try {
            return studentDao.getByID(id);
        } catch (DaoException e) {
            throw new NotFoundException(e.getMessage());
        }
    }

    @Override
    public void update(StudentEntity student) {
        LOGGER.debug("StudentService update ({}) - starts", student);
        int result = studentDao.update(student);
        if (result != 1) {
            throw new UnsuccessfulOperationException(NOT_UPDATED);
        }
    }

    @Override
    public void deleteById(int id) {
        LOGGER.debug("StudentService deleteById starts with id = {}", id);
        try {
            studentDao.getByID(id);
            int result = studentDao.deleteById(id);
            if (result != 1) {
                throw new UnsuccessfulOperationException(NOT_DELETED);
            }
        } catch (DaoException e) {
            throw new NotFoundException(e.getMessage());
        }
    }

    @Override
    public void addStudentToCourse(StudentEntity student, int courseId) {
        LOGGER.debug("StudentService addStudentToCourse ({}) - starts", student);
        int result = studentDao.addStudentToCourse(student, courseId);
        if (result != 1) {
            throw new UnsuccessfulOperationException(NOT_SAVED);
        }

    }

    @Override
    public List<StudentEntity> findAllStudentsRelatedToCourse(int courseId) {
        LOGGER.debug("StudentsService findAllStudentsRelatedToCourse - starts with id = {}", courseId);
        List<StudentEntity> result = studentDao.findAllStudentsRelatedToCourse(courseId);
        if (!result.isEmpty()) {
            return result;
        } else {
            throw new NotFoundException(NOT_EXIST);
        }
    }

    @Override
    public void removeStudentByIDFromCourse(int studentId, int courseId) {
        LOGGER.debug("StudentsService removeStudentByIDFromCourse - starts with studentId = {}, courseId = {}",
                studentId, courseId);
        int result = studentDao.removeStudentByIDFromCourse(studentId, courseId);
        if (result != 1) {
            throw new UnsuccessfulOperationException(NOT_DELETED);
        }
    }

}
