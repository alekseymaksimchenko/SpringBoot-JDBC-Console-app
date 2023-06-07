package com.foxminded.schoolapp.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.foxminded.schoolapp.dao.impl.Student;
import com.foxminded.schoolapp.entity.StudentEntity;

@Service
public class StudentService implements IStudentService<StudentEntity> {
    
    private final Student<StudentEntity> studentDao;
    private static final Logger LOGGER = LoggerFactory.getLogger(StudentService.class);
    private static final String NOT_SAVED = "Record was NOT saved due to unknown reason";
    private static final String NOT_UPDATED = "Record was NOT updated due to unknown reason";
    private static final String NOT_DELETED = "Record was NOT deleted due to unknown reason";
    private static final String NOT_EXIST = "Record under provided id - not exist";

    
    @Autowired
    public StudentService(Student<StudentEntity> studentDao) {
        this.studentDao = studentDao;
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
