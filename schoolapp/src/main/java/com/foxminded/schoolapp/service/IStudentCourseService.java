package com.foxminded.schoolapp.service;

import java.util.List;

public interface IStudentCourseService <T> {

    void save(T studentCourse);

    List<T> getAll();

    T getByID(int id);

    void update(T studentCourse, String[] parameters);

    void deleteById(int id);
    
}
