package com.foxminded.schoolapp.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface GroupDao<T> extends GenericDao<T> {

    List<T> getAllGroupsAccordingStudentCount(int count);

}
