package com.foxminded.schoolapp.dao.impl;

import java.util.List;

import com.foxminded.schoolapp.dao.GenericDao;

public interface StudentCourse<T> extends GenericDao<T> {

    int addStudentToCourse(T studentCourse);

    List<T> findAllStudentsRelatedToCourse(int courseId);

    int removeStudentByIDFromCourse(int studentId, int courseId);
}
