package com.foxminded.schoolapp.service;

import java.util.List;

public interface StudentCourseService<T> extends GenericService<T>{

    void addStudentToCourse(T studentCourse);

    List<T> findAllStudentsRelatedToCourse(int courseId);

    void removeStudentByIDFromCourse(int studentId, int courseId);

}
