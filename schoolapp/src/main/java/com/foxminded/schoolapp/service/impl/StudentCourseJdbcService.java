package com.foxminded.schoolapp.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.foxminded.schoolapp.dao.GenericDao;
import com.foxminded.schoolapp.dao.entity.StudentCourseEntity;
import com.foxminded.schoolapp.exception.UnsuccessfulOperationException;
import com.foxminded.schoolapp.service.StudentCourseService;
import com.foxminded.schoolapp.service.generator.Generator;


@Service
public class StudentCourseJdbcService implements StudentCourseService<StudentCourseEntity> {
    
    private final GenericDao<StudentCourseEntity> studentCourseDao;
    private final Generator<StudentCourseEntity> studentCourseGenerator;
    private static final Logger LOGGER = LoggerFactory.getLogger(StudentCourseJdbcService.class);
    private static final String NOT_SAVED = "Record was NOT saved due to unknown reason";
    private static final String GENERATED_RECORD_NOT_SAVED = "Generated Record was NOT saved during populate method due to unknown reason";
    private static final String NOT_UPDATED = "Record was NOT updated due to unknown reason";
    private static final String NOT_DELETED = "Record was NOT deleted due to unknown reason";
    private static final String NOT_EXIST = "Record under provided id - not exist";

    
    @Autowired    
    public StudentCourseJdbcService(GenericDao<StudentCourseEntity> studentCourseDao, Generator<StudentCourseEntity> studentCourseGenerator) {
        this.studentCourseDao = studentCourseDao;
        this.studentCourseGenerator = studentCourseGenerator;
    }
    
    @Override
    public void populate() {
        LOGGER.debug("StudentCourseJdbcService populate - starts");
        studentCourseGenerator.generate().forEach(studentCourse -> {
            int result = studentCourseDao.save(studentCourse);
            if (result != 1) {
                throw new UnsuccessfulOperationException(GENERATED_RECORD_NOT_SAVED);
            }
        });
    }

    @Override
    public void save(StudentCourseEntity studentCourse) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public List<StudentCourseEntity> getAll() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public StudentCourseEntity getByID(int id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void update(StudentCourseEntity studentCourse, String[] parameters) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void deleteById(int id) {
        // TODO Auto-generated method stub
        
    }

}
