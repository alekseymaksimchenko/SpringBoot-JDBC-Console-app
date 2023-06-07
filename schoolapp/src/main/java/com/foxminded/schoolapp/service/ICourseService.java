package com.foxminded.schoolapp.service;

import java.util.List;

public interface ICourseService <T> {

    void save(T course);

    List<T> getAll();

    T getByID(int id);

    void update(T course, String[] parameters);

    void deleteById(int id);
}
