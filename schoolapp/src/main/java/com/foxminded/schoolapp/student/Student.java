package com.foxminded.schoolapp.student;

import java.util.List;
import java.util.Optional;

public interface Student<T> {

    int save(T student);

    List<T> getAll();

    Optional<T> getByID(int id);

    int update(T student, String[] parameters);

    int deleteById(int id);
}
