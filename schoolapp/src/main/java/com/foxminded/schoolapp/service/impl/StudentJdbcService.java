package com.foxminded.schoolapp.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.foxminded.schoolapp.dao.StudentDao;
import com.foxminded.schoolapp.dao.entity.StudentEntity;
import com.foxminded.schoolapp.exception.NotFoundException;
import com.foxminded.schoolapp.exception.UnsuccessfulOperationException;
import com.foxminded.schoolapp.service.PopulateGeneratedData;
import com.foxminded.schoolapp.service.StudentService;
import com.foxminded.schoolapp.service.generator.Generator;

@Service
public class StudentJdbcService implements StudentService<StudentEntity>, PopulateGeneratedData {

    private final StudentDao<StudentEntity> studentDao;
    private final Generator<StudentEntity> studentGenerator;
    private static final Logger LOGGER = LoggerFactory.getLogger(StudentJdbcService.class);
    private static final String NOT_SAVED = "Record was NOT saved due to unknown reason";
    private static final String NOT_UPDATED = "Record was NOT updated due to unknown reason";
    private static final String NOT_DELETED = "Record was NOT deleted due to unknown reason";
    private static final String NOT_EXIST = "Record under provided id - not exist";
    private static final String IS_EMPTY = "Table doesn't contain any Records";

    @Autowired
    public StudentJdbcService(StudentDao<StudentEntity> studentDao, Generator<StudentEntity> studentGenerator) {
        this.studentDao = studentDao;
        this.studentGenerator = studentGenerator;
    }

    @Override
    public void populate() {
        LOGGER.debug("StudentJdbcService populate - starts");
        studentGenerator.generate().forEach(student -> studentDao.save(student));
    }

    @Override
    public void save(StudentEntity student) {
        LOGGER.debug("StudentJdbcService save ({}) - starts", student);
        int result = studentDao.save(student);
        if (result != 1) {
            throw new UnsuccessfulOperationException(NOT_SAVED);
        }

    }

    @Override
    public List<StudentEntity> getAll() {
        LOGGER.debug("StudentJdbcService getAll - starts");
        List<StudentEntity> result = studentDao.getAll();
        if (!result.isEmpty()) {
            return result;
        } else {
            throw new NotFoundException(IS_EMPTY);
        }
    }

    @Override
    public StudentEntity getByID(int id) {
        LOGGER.debug("StudentJdbcService getByID - starts with id = {}", id);
        return studentDao.getByID(id).orElseThrow(() -> new NotFoundException(NOT_EXIST));
    }

    @Override
    public void update(StudentEntity student, String[] parameters) {
        LOGGER.debug("StudentJdbcService update ({}) - starts with parameters ({})", student, parameters);
        int result = studentDao.update(student, parameters);
        if (result != 1) {
            throw new UnsuccessfulOperationException(NOT_UPDATED);
        }
    }

    @Override
    public void deleteById(int id) {
        LOGGER.debug("StudentJdbcService deleteById starts with id = {}", id);
        if (studentDao.getByID(id).isPresent()) {
            int result = studentDao.deleteById(id);
            if (result != 1) {
                throw new UnsuccessfulOperationException(NOT_DELETED);
            }
        } else {
            throw new NotFoundException(NOT_EXIST);
        }
    }

}
