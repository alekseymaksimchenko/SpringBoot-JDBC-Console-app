package com.foxminded.schoolapp.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.foxminded.schoolapp.dao.entity.GroupEntity;
import com.foxminded.schoolapp.dao.impl.GroupJdbcDao;
import com.foxminded.schoolapp.exception.DaoException;
import com.foxminded.schoolapp.exception.ServiceException;
import com.foxminded.schoolapp.service.generator.GroupsGenerator;
import com.foxminded.schoolapp.service.impl.GroupService;

@SpringBootTest(classes = { GroupService.class })
class GroupServiceTest {

    @MockBean
    private GroupJdbcDao groupJdbcDao;

    @MockBean
    private GroupsGenerator groupsGenerator;

    @MockBean
    private GroupEntity testGroupEntity;

    @Autowired
    private GroupService groupService;

    @Test
    void testGroupJdbcService_populateShouldPass() {
        assertAll(() -> groupService.populate());
    }

    @Test
    void testGroupService_populateShouldCallGeneratorAndDaoRightTimesInRightOrder() {
        when(groupsGenerator.generate()).thenReturn(Arrays.asList(testGroupEntity));

        groupService.populate();
        verify(groupsGenerator, times(1)).generate();
        verify(groupJdbcDao, atLeastOnce()).save(testGroupEntity);

        InOrder inOrder = inOrder(groupsGenerator, groupJdbcDao);
        inOrder.verify(groupsGenerator).generate();
        inOrder.verify(groupJdbcDao).save(testGroupEntity);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void testGroupService_getAllGroupsAccordingStudentCount_ShouldPass() {
        assertAll(() -> groupService.getAllGroupsAccordingStudentCount(1));
    }

    @Test
    void testGroupService_getAllGroupsAccordingStudentCount_ShouldCallDaoOneTime() {
        groupService.getAllGroupsAccordingStudentCount(1);

        verify(groupJdbcDao, times(1)).getAllGroupsAccordingStudentCount(1);
    }

    @Test
    void testGroupService_getAllGroupsAccordingStudentCount_ShouldThrowExeption_whenReturnEmptyList() {
        doThrow(DaoException.class).when(groupJdbcDao).getAllGroupsAccordingStudentCount(1);

        assertThrows(ServiceException.class, () -> groupService.getAllGroupsAccordingStudentCount(1));
    }

    @Test
    void testGroupService_saveShouldPass() {
        assertAll(() -> groupService.save(testGroupEntity));
    }

    @Test
    void testGroupService_saveShouldCallDaoOneTime() {
        groupService.save(testGroupEntity);

        verify(groupJdbcDao, times(1)).save(testGroupEntity);
    }

    @Test
    void testGroupService_saveShouldThrowExeption_whenUnsuccessful() {
        doThrow(DaoException.class).when(groupJdbcDao).save(testGroupEntity);

        assertThrows(ServiceException.class, () -> groupService.save(testGroupEntity));
    }

    @Test
    void testGroupService_getAllShouldPass() {
        assertAll(() -> groupService.getAll());
    }

    @Test
    void testGroupService_getAllShouldCallDaoOneTime() {
        groupService.getAll();

        verify(groupJdbcDao, times(1)).getAll();
    }

    @Test
    void testGroupService_getAllShouldThrowExeption_whenReturnEmptyList() {
        doThrow(DaoException.class).when(groupJdbcDao).getAll();

        assertThrows(ServiceException.class, () -> groupService.getAll());
    }

    @Test
    void testGroupService_getByIDShouldPass() {
        assertAll(() -> groupService.getByID(1));
    }

    @Test
    void testGroupService_getByIDShouldCallDaoOneTime() {
        groupService.getByID(1);

        verify(groupJdbcDao, times(1)).getByID(1);
    }

    @Test
    void testGroupService_updateShouldPass() {

        assertAll(() -> groupService.update(testGroupEntity));
    }

    @Test
    void testGroupService_updateShouldCallDaoOneTime() {
        groupService.update(testGroupEntity);

        verify(groupJdbcDao).update(testGroupEntity);
    }

    @Test
    void testGroupService_updateShouldThrowExeption_whenNotFoundRecord() {
        doThrow(DaoException.class).when(groupJdbcDao).update(testGroupEntity);

        assertThrows(ServiceException.class, () -> groupService.update(testGroupEntity));
    }

    @Test
    void testGroupService_deleteShouldPass() {
        assertAll(() -> groupService.deleteById(1));
    }

    @Test
    void testGroupService_deleteShouldCallDaoRightTimesAndOrder() {
        groupService.deleteById(1);

        verify(groupJdbcDao, times(1)).getByID(1);
        verify(groupJdbcDao, times(1)).deleteById(1);

        InOrder inOrder = Mockito.inOrder(groupJdbcDao);
        inOrder.verify(groupJdbcDao).getByID(1);
        inOrder.verify(groupJdbcDao).deleteById(1);
    }

    @Test
    void testGroupService_deleteShouldThrowExeption_whenUnsuccessful() {
        doThrow(DaoException.class).when(groupJdbcDao).getByID(1);

        assertThrows(ServiceException.class, () -> groupService.deleteById(1));
    }

}
