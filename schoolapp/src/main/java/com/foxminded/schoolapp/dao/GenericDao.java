package com.foxminded.schoolapp.dao;

import java.util.List;

import com.foxminded.schoolapp.exception.DaoException;

public interface GenericDao<T> {

    void save(T course);

    List<T> getAll();

    T getByID(int id) throws DaoException;

    void update(T course);

    void deleteById(int id);

}
