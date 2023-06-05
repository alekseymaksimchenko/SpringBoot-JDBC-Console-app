package com.foxminded.schoolapp.group;

import java.util.List;
import java.util.Optional;

public interface Group<T> {

    List<T> getAllGroupsAccordingStudentCount(int count);

    int save(T group);

    List<T> getAll();

    Optional<T> getByID(int id);

    int update(T group, String[] parameters);

    int deleteById(int id);
}
