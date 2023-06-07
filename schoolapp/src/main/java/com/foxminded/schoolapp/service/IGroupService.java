package com.foxminded.schoolapp.service;

import java.util.List;

public interface IGroupService<T> {

    void save(T group);

    List<T> getAll();

    T getByID(int id);

    void update(T group, String[] parameters);

    void deleteById(int id);

}
