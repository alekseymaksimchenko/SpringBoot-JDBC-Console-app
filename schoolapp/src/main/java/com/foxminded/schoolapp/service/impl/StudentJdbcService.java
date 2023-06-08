package com.foxminded.schoolapp.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.foxminded.schoolapp.dao.GenericDao;
import com.foxminded.schoolapp.dao.StudentDao;
import com.foxminded.schoolapp.dao.entity.StudentEntity;
import com.foxminded.schoolapp.exception.UnsuccessfulOperationException;
import com.foxminded.schoolapp.service.StudentService;
import com.foxminded.schoolapp.service.generator.Generator;

@Service
public class StudentJdbcService implements StudentService<StudentEntity> {
    
    private final GenericDao<StudentEntity> studentDao;
    private final Generator<StudentEntity> studentGenerator;
    private static final Logger LOGGER = LoggerFactory.getLogger(StudentJdbcService.class);
    private static final String NOT_SAVED = "Record was NOT saved due to unknown reason";
    private static final String GENERATED_RECORD_NOT_SAVED = "Generated Record was NOT saved during populate method due to unknown reason";
    private static final String NOT_UPDATED = "Record was NOT updated due to unknown reason";
    private static final String NOT_DELETED = "Record was NOT deleted due to unknown reason";
    private static final String NOT_EXIST = "Record under provided id - not exist";

    
    @Autowired
    public StudentJdbcService(StudentDao<StudentEntity> studentDao, Generator<StudentEntity> studentGenerator) {
        this.studentDao = studentDao;
        this.studentGenerator = studentGenerator;
    }
    
    @Override
    public void populate() {
        LOGGER.debug("StudentJdbcService populate - starts");
        studentGenerator.generate().forEach(student -> {
            int result = studentDao.save(student);
            if (result != 1) {
                throw new UnsuccessfulOperationException(GENERATED_RECORD_NOT_SAVED);
            }
        });
    }

    @Override
    public void save(StudentEntity student) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public List<StudentEntity> getAll() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public StudentEntity getByID(int id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void update(StudentEntity student, String[] parameters) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void deleteById(int id) {
        // TODO Auto-generated method stub
        
    }


}
