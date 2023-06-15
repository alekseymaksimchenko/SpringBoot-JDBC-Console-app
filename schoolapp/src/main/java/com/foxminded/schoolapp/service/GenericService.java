package com.foxminded.schoolapp.service;

import java.util.List;

public interface GenericService<T> {

    void save(T course);

    List<T> getAll();

    T getByID(int id);

    void update(T course);

    void deleteById(int id);

}
