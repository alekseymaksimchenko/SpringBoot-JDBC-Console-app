package com.foxminded.schoolapp.service;

import java.util.List;

public interface StudentServices<T> extends GenericService<T> {

    void addStudentToCourse(T studentCourse, int courseId);

    List<T> findAllStudentsRelatedToCourse(int courseId);

    void removeStudentByIDFromCourse(int studentId, int courseId);

}
