package com.foxminded.schoolapp.dao.impl;

import java.util.List;

import com.foxminded.schoolapp.dao.GenericDao;

public interface Group<T> extends GenericDao<T> {

    List<T> getAllGroupsAccordingStudentCount(int count);

}
