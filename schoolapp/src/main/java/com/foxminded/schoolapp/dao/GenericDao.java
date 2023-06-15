package com.foxminded.schoolapp.dao;

import java.util.List;

import com.foxminded.schoolapp.exception.DaoException;

public interface GenericDao<T> {

    int save(T course);

    List<T> getAll();

    T getByID(int id) throws DaoException;

    int update(T course);

    int deleteById(int id);

}
