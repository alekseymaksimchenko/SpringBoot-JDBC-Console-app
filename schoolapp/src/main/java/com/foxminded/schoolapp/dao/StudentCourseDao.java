package com.foxminded.schoolapp.dao;

import java.util.List;

public interface StudentCourseDao<T> extends GenericDao<T> {

    int addStudentToCourse(T studentCourse);

    List<T> findAllStudentsRelatedToCourse(int courseId);

    int removeStudentByIDFromCourse(int studentId, int courseId);
}
