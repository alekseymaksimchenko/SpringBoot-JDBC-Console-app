package com.foxminded.schoolapp.dao;

import java.util.List;

public interface GroupDao<T> extends GenericDao<T> {

    List<T> getAllGroupsAccordingStudentCount(int count);

}
