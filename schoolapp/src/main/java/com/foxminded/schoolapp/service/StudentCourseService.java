package com.foxminded.schoolapp.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.foxminded.schoolapp.dao.impl.StudentCourse;
import com.foxminded.schoolapp.entity.StudentCourseEntity;


@Service
public class StudentCourseService implements IStudentCourseService<StudentCourseEntity> {
    
    private final StudentCourse<StudentCourseEntity> studentCourseDao;
    private static final Logger LOGGER = LoggerFactory.getLogger(StudentCourseService.class);
    private static final String NOT_SAVED = "Record was NOT saved due to unknown reason";
    private static final String NOT_UPDATED = "Record was NOT updated due to unknown reason";
    private static final String NOT_DELETED = "Record was NOT deleted due to unknown reason";
    private static final String NOT_EXIST = "Record under provided id - not exist";

    
    @Autowired    
    public StudentCourseService(StudentCourse<StudentCourseEntity> studentCourseDao) {
        this.studentCourseDao = studentCourseDao;
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
