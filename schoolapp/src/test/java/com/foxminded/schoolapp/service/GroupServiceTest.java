package com.foxminded.schoolapp.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.foxminded.schoolapp.dao.entity.GroupEntity;
import com.foxminded.schoolapp.dao.impl.GroupJdbcDao;
import com.foxminded.schoolapp.exception.ServiceException;
import com.foxminded.schoolapp.service.generator.GroupsGenerator;
import com.foxminded.schoolapp.service.impl.GroupService;

@SpringBootTest(classes = { GroupService.class })
class GroupServiceTest extends BasicServiceTest {

    @MockBean
    private GroupJdbcDao groupJdbcDao;

    @MockBean
    private GroupsGenerator groupsGenerator;

    @MockBean
    private GroupEntity testGroupEntity;

    @Autowired
    private GroupService groupService;

    private static final List<GroupEntity> EMPTY_LIST = new ArrayList<>();
    private static final List<GroupEntity> NOT_EMPTY_LIST = new ArrayList<>(Arrays.asList(new GroupEntity()));

    @Test
    void testGroupJdbcService_populateShouldPass() {
        when(groupJdbcDao.save(testGroupEntity)).thenReturn(POSITIVE_OPERATION_RETURN);

        assertAll(() -> groupService.populate());
    }

    @Test
    void testGroupService_populateShouldCallGeneratorAndDaoRightTimesInRightOrder() {
        when(groupsGenerator.generate()).thenReturn(Arrays.asList(testGroupEntity));
        when(groupJdbcDao.save(testGroupEntity)).thenReturn(POSITIVE_OPERATION_RETURN);

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
        when(groupJdbcDao.getAllGroupsAccordingStudentCount(1)).thenReturn(NOT_EMPTY_LIST);

        assertAll(() -> groupService.getAllGroupsAccordingStudentCount(1));
    }

    @Test
    void testGroupService_getAllGroupsAccordingStudentCount_ShouldCallDaoOneTime() {
        when(groupJdbcDao.getAllGroupsAccordingStudentCount(1)).thenReturn(NOT_EMPTY_LIST);

        groupService.getAllGroupsAccordingStudentCount(1);

        verify(groupJdbcDao, times(1)).getAllGroupsAccordingStudentCount(1);
    }

    @Test
    void testGroupService_getAllGroupsAccordingStudentCount_ShouldThrowExeption_whenReturnEmptyList() {
        when(groupJdbcDao.getAllGroupsAccordingStudentCount(1)).thenReturn(EMPTY_LIST);
        Exception exception = assertThrows(ServiceException.class,
                () -> groupService.getAllGroupsAccordingStudentCount(1));

        String expected = IS_EMPTY;
        String actual = exception.getMessage();

        assertEquals(expected, actual);
    }

    @Test
    void testGroupService_saveShouldPass() {
        when(groupJdbcDao.save(testGroupEntity)).thenReturn(POSITIVE_OPERATION_RETURN);

        assertAll(() -> groupService.save(testGroupEntity));
    }

    @Test
    void testGroupService_saveShouldCallDaoOneTime() {
        when(groupJdbcDao.save(testGroupEntity)).thenReturn(POSITIVE_OPERATION_RETURN);
        groupService.save(testGroupEntity);

        verify(groupJdbcDao, times(1)).save(testGroupEntity);
    }

    @Test
    void testGroupService_saveShouldThrowExeption_whenUnsuccessful() {
        when(groupJdbcDao.save(testGroupEntity)).thenReturn(NEGATIVE_OPERATION_RETURN);
        Exception exception = assertThrows(ServiceException.class,
                () -> groupService.save(testGroupEntity));

        String expected = NOT_SAVED;
        String actual = exception.getMessage();

        assertEquals(expected, actual);
    }

    @Test
    void testGroupService_getAllShouldPass() {
        when(groupJdbcDao.getAll()).thenReturn(NOT_EMPTY_LIST);

        assertAll(() -> groupService.getAll());
    }

    @Test
    void testGroupService_getAllShouldCallDaoOneTime() {
        when(groupJdbcDao.getAll()).thenReturn(NOT_EMPTY_LIST);
        groupService.getAll();

        verify(groupJdbcDao, times(1)).getAll();
    }

    @Test
    void testGroupService_getAllShouldThrowExeption_whenReturnEmptyList() {
        when(groupJdbcDao.getAll()).thenReturn(EMPTY_LIST);
        Exception exception = assertThrows(ServiceException.class, () -> groupService.getAll());

        String expected = IS_EMPTY;
        String actual = exception.getMessage();

        assertEquals(expected, actual);
    }

    @Test
    void testGroupService_getByIDShouldPass() {
        when(groupJdbcDao.getByID(1)).thenReturn(testGroupEntity);

        assertAll(() -> groupService.getByID(1));
    }

    @Test
    void testGroupService_getByIDShouldCallDaoOneTime() {
        when(groupJdbcDao.getByID(1)).thenReturn(testGroupEntity);
        groupService.getByID(1);

        verify(groupJdbcDao, times(1)).getByID(1);
    }

    @Test
    void testGroupService_updateShouldPass() {
        when(groupJdbcDao.update(testGroupEntity)).thenReturn(POSITIVE_OPERATION_RETURN);

        assertAll(() -> groupService.update(testGroupEntity));
    }

    @Test
    void testGroupService_updateShouldCallDaoOneTime() {
        when(groupJdbcDao.update(testGroupEntity)).thenReturn(POSITIVE_OPERATION_RETURN);
        groupService.update(testGroupEntity);

        verify(groupJdbcDao).update(testGroupEntity);
    }

    @Test
    void testGroupService_updateShouldThrowExeption_whenNotFoundRecord() {
        when(groupJdbcDao.update(testGroupEntity)).thenReturn(NEGATIVE_OPERATION_RETURN);
        Exception exception = assertThrows(ServiceException.class,
                () -> groupService.update(testGroupEntity));

        String expected = NOT_UPDATED;
        String actual = exception.getMessage();

        assertEquals(expected, actual);

    }

    @Test
    void testGroupService_deleteShouldPass() {
        when(groupJdbcDao.getByID(1)).thenReturn(testGroupEntity);
        when(groupJdbcDao.deleteById(1)).thenReturn(POSITIVE_OPERATION_RETURN);

        assertAll(() -> groupService.deleteById(1));
    }

    @Test
    void testGroupService_deleteShouldCallDaoRightTimesAndOrder() {
        when(groupJdbcDao.getByID(1)).thenReturn(testGroupEntity);
        when(groupJdbcDao.deleteById(1)).thenReturn(POSITIVE_OPERATION_RETURN);
        groupService.deleteById(1);

        verify(groupJdbcDao, times(1)).getByID(1);
        verify(groupJdbcDao, times(1)).deleteById(1);

        InOrder inOrder = Mockito.inOrder(groupJdbcDao);
        inOrder.verify(groupJdbcDao).getByID(1);
        inOrder.verify(groupJdbcDao).deleteById(1);
    }

    @Test
    void testGroupService_deleteShouldThrowExeption_whenUnsuccessful() {
        when(groupJdbcDao.getByID(1)).thenReturn(testGroupEntity);
        when(groupJdbcDao.deleteById(1)).thenReturn(NEGATIVE_OPERATION_RETURN);

        Exception exception = assertThrows(ServiceException.class, () -> groupService.deleteById(1));
        String expected = NOT_DELETED;
        String actual = exception.getMessage();

        assertEquals(expected, actual);
    }

}
