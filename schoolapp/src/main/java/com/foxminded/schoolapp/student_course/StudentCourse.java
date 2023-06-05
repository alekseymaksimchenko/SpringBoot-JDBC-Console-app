package com.foxminded.schoolapp.student_course;

import java.util.List;

public interface StudentCourse<T> {

    int addStudentToCourse(T studentCourse);

    List<T> findAllStudentsRelatedToCourse(int courseId);

    int removeStudentByIDFromCourse(int studentId, int courseId);
}
