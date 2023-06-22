package com.foxminded.schoolapp.dao;

import java.util.List;

public interface StudentDao<T> extends GenericDao<T> {

    void addStudentToCourse(T studentCourse, int courseId);

    List<T> findAllStudentsRelatedToCourse(int courseId);

    void removeStudentByIDFromCourse(int studentId, int courseId);

}
