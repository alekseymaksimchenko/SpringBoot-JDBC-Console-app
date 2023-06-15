package com.foxminded.schoolapp.dao;

import java.util.List;

public interface StudentDao<T> extends GenericDao<T> {

    int addStudentToCourse(T studentCourse, int courseId);

    List<T> findAllStudentsRelatedToCourse(int courseId);

    int removeStudentByIDFromCourse(int studentId, int courseId);

}
