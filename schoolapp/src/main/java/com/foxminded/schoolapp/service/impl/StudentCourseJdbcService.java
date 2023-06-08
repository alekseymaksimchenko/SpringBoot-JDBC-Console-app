package com.foxminded.schoolapp.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.foxminded.schoolapp.dao.StudentCourseDao;
import com.foxminded.schoolapp.dao.entity.StudentCourseEntity;
import com.foxminded.schoolapp.exception.NotFoundException;
import com.foxminded.schoolapp.exception.UnsuccessfulOperationException;
import com.foxminded.schoolapp.service.PopulateGeneratedData;
import com.foxminded.schoolapp.service.StudentCourseService;
import com.foxminded.schoolapp.service.generator.Generator;

@Service
public class StudentCourseJdbcService implements StudentCourseService<StudentCourseEntity>, PopulateGeneratedData {

    private final StudentCourseDao<StudentCourseEntity> studentCourseDao;
    private final Generator<StudentCourseEntity> studentCourseGenerator;
    private static final Logger LOGGER = LoggerFactory.getLogger(StudentCourseJdbcService.class);
    private static final String NOT_SAVED = "Record was NOT saved due to unknown reason";
    private static final String GENERATED_RECORD_NOT_SAVED = "Generated Record was NOT saved during populate method due to unknown reason";
    private static final String NOT_DELETED = "Record was NOT deleted due to unknown reason";
    private static final String NOT_EXIST = "Record(s) under provided id - not exist";
    private static final String NOT_IMPLEMENTED = "Not implemented, yet";

    @Autowired
    public StudentCourseJdbcService(StudentCourseDao<StudentCourseEntity> studentCourseDao,
            Generator<StudentCourseEntity> studentCourseGenerator) {
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
    public void addStudentToCourse(StudentCourseEntity studentCourse) {
        LOGGER.debug("StudentsCoursesJdbcService addStudentToCourse ({}) - starts", studentCourse);
        int result = studentCourseDao.addStudentToCourse(studentCourse);
        if (result != 1) {
            throw new UnsuccessfulOperationException(NOT_SAVED);
        }

    }

    @Override
    public List<StudentCourseEntity> findAllStudentsRelatedToCourse(int courseId) {
        LOGGER.debug("StudentsCoursesJdbcService findAllStudentsRelatedToCourse - starts with id = {}", courseId);
        List<StudentCourseEntity> result = studentCourseDao.findAllStudentsRelatedToCourse(courseId);
        if (!result.isEmpty()) {
            return result;
        } else {
            throw new NotFoundException(NOT_EXIST);
        }
    }

    @Override
    public void removeStudentByIDFromCourse(int studentId, int courseId) {
        LOGGER.debug("StudentsCoursesJdbcService removeStudentFromCourse - starts with studentId = {}, courseId = {}",
                studentId, courseId);
        int result = studentCourseDao.removeStudentByIDFromCourse(studentId, courseId);
        if (result != 1) {
            throw new UnsuccessfulOperationException(NOT_DELETED);
        }

    }

    @Override
    public void save(StudentCourseEntity course) {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    @Override
    public List<StudentCourseEntity> getAll() {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    @Override
    public StudentCourseEntity getByID(int id) {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    @Override
    public void update(StudentCourseEntity course, String[] parameters) {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    @Override
    public void deleteById(int id) {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

}
