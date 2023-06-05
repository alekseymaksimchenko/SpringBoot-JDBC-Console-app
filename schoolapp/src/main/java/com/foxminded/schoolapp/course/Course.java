package com.foxminded.schoolapp.course;

import java.util.List;
import java.util.Optional;

public interface Course<T> {

    int save(T course);

    List<T> getAll();

    Optional<T> getByID(int id);

    int update(T course, String[] parameters);

    int deleteById(int id);
    
}
