package com.foxminded.schoolapp.service;

import java.util.List;

public interface GroupService<T> extends GenericService<T> {

    List<T> getAllGroupsAccordingStudentCount(int count);

}
