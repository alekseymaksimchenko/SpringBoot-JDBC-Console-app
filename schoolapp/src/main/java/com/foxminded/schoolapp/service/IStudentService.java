package com.foxminded.schoolapp.service;

import java.util.List;

public interface IStudentService<T> {

    void save(T student);

    List<T> getAll();

    T getByID(int id);

    void update(T student, String[] parameters);

    void deleteById(int id);

}
