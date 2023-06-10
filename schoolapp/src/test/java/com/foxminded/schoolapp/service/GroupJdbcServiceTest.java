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
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.foxminded.schoolapp.dao.entity.GroupEntity;
import com.foxminded.schoolapp.dao.impl.GroupJdbcDao;
import com.foxminded.schoolapp.exception.NotFoundException;
import com.foxminded.schoolapp.exception.UnsuccessfulOperationException;
import com.foxminded.schoolapp.service.generator.GroupsGenerator;
import com.foxminded.schoolapp.service.impl.GroupJdbcService;

@SpringBootTest(classes = { GroupJdbcService.class })
class GroupJdbcServiceTest extends BasicServiceTest {

    @MockBean
    private GroupJdbcDao groupJdbcDao;

    @MockBean
    private GroupsGenerator groupsGenerator;

    @MockBean
    private GroupEntity testGroupEntity;

    @Autowired
    private GroupJdbcService groupJdbcService;

    private static final Optional<GroupEntity> EMPTY_OPTIONAL = Optional.empty();
    private static final Optional<GroupEntity> NOT_EMPTY_OPTIONAL = Optional.of(new GroupEntity());
    private static final List<GroupEntity> EMPTY_LIST = new ArrayList<>();
    private static final List<GroupEntity> NOT_EMPTY_LIST = new ArrayList<>(Arrays.asList(new GroupEntity()));

    @Test
    void testGroupJdbcService_populateShouldPass() {
        when(groupJdbcDao.save(testGroupEntity)).thenReturn(POSITIVE_OPERATION_RETURN);

        assertAll(() -> groupJdbcService.populate());
    }

    @Test
    void testGroupJdbcService_populateShouldCallGeneratorAndDaoRightTimesInRightOrder() {

        when(groupsGenerator.generate()).thenReturn(Arrays.asList(testGroupEntity));
        when(groupJdbcDao.save(testGroupEntity)).thenReturn(POSITIVE_OPERATION_RETURN);

        groupJdbcService.populate();
        verify(groupsGenerator, times(1)).generate();
        verify(groupJdbcDao, atLeastOnce()).save(testGroupEntity);

        InOrder inOrder = inOrder(groupsGenerator, groupJdbcDao);
        inOrder.verify(groupsGenerator).generate();
        inOrder.verify(groupJdbcDao).save(testGroupEntity);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void testGroupJdbcService_getAllGroupsAccordingStudentCount_ShouldPass() {
        when(groupJdbcDao.getAllGroupsAccordingStudentCount(1)).thenReturn(NOT_EMPTY_LIST);

        assertAll(() -> groupJdbcService.getAllGroupsAccordingStudentCount(1));
    }

    @Test
    void testGroupJdbcService_getAllGroupsAccordingStudentCount_ShouldCallDaoOneTime() {
        when(groupJdbcDao.getAllGroupsAccordingStudentCount(1)).thenReturn(NOT_EMPTY_LIST);

        groupJdbcService.getAllGroupsAccordingStudentCount(1);

        verify(groupJdbcDao, times(1)).getAllGroupsAccordingStudentCount(1);
    }

    @Test
    void testGroupJdbcService_getAllGroupsAccordingStudentCount_ShouldThrowExeption_whenReturnEmptyList() {
        when(groupJdbcDao.getAllGroupsAccordingStudentCount(1)).thenReturn(EMPTY_LIST);
        Exception exception = assertThrows(NotFoundException.class,
                () -> groupJdbcService.getAllGroupsAccordingStudentCount(1));

        String expected = IS_EMPTY;
        String actual = exception.getMessage();

        assertEquals(expected, actual);
    }

    @Test
    void testGroupJdbcService_saveShouldPass() {
        when(groupJdbcDao.save(testGroupEntity)).thenReturn(POSITIVE_OPERATION_RETURN);

        assertAll(() -> groupJdbcService.save(testGroupEntity));
    }

    @Test
    void testGroupJdbcService_saveShouldCallDaoOneTime() {
        when(groupJdbcDao.save(testGroupEntity)).thenReturn(POSITIVE_OPERATION_RETURN);
        groupJdbcService.save(testGroupEntity);

        verify(groupJdbcDao, times(1)).save(testGroupEntity);
    }

    @Test
    void testGroupJdbcService_saveShouldThrowExeption_whenUnsuccessful() {
        when(groupJdbcDao.save(testGroupEntity)).thenReturn(NEGATIVE_OPERATION_RETURN);
        Exception exception = assertThrows(UnsuccessfulOperationException.class,
                () -> groupJdbcService.save(testGroupEntity));

        String expected = NOT_SAVED;
        String actual = exception.getMessage();

        assertEquals(expected, actual);
    }

    @Test
    void testGroupJdbcService_getAllShouldPass() {
        when(groupJdbcDao.getAll()).thenReturn(NOT_EMPTY_LIST);

        assertAll(() -> groupJdbcService.getAll());
    }

    @Test
    void testGroupJdbcService_getAllShouldCallDaoOneTime() {
        when(groupJdbcDao.getAll()).thenReturn(NOT_EMPTY_LIST);
        groupJdbcService.getAll();

        verify(groupJdbcDao, times(1)).getAll();
    }

    @Test
    void testGroupJdbcService_getAllShouldThrowExeption_whenReturnEmptyList() {
        when(groupJdbcDao.getAll()).thenReturn(EMPTY_LIST);
        Exception exception = assertThrows(NotFoundException.class, () -> groupJdbcService.getAll());

        String expected = IS_EMPTY;
        String actual = exception.getMessage();

        assertEquals(expected, actual);
    }

    @Test
    void testGroupJdbcService_getByIDShouldPass() {
        when(groupJdbcDao.getByID(1)).thenReturn(NOT_EMPTY_OPTIONAL);

        assertAll(() -> groupJdbcService.getByID(1));
    }

    @Test
    void testGroupJdbcService_getByIDShouldCallDaoOneTime() {
        when(groupJdbcDao.getByID(1)).thenReturn(NOT_EMPTY_OPTIONAL);
        groupJdbcService.getByID(1);

        verify(groupJdbcDao, times(1)).getByID(1);
    }

    @Test
    void testGroupJdbcService_getByIDShouldThrowExeption_whenNotFoundRecord() {
        when(groupJdbcDao.getByID(1)).thenReturn(EMPTY_OPTIONAL);
        Exception exception = assertThrows(NotFoundException.class, () -> groupJdbcService.getByID(1));

        String expected = NOT_EXIST;
        String actual = exception.getMessage();

        assertEquals(expected, actual);
    }

    @Test
    void testGroupJdbcService_updateShouldPass() {
        when(groupJdbcDao.update(testGroupEntity, PARAMETERS)).thenReturn(POSITIVE_OPERATION_RETURN);

        assertAll(() -> groupJdbcService.update(testGroupEntity, PARAMETERS));
    }

    @Test
    void testGroupJdbcService_updateShouldCallDaoOneTime() {
        when(groupJdbcDao.update(testGroupEntity, PARAMETERS)).thenReturn(POSITIVE_OPERATION_RETURN);
        groupJdbcService.update(testGroupEntity, PARAMETERS);

        verify(groupJdbcDao).update(testGroupEntity, PARAMETERS);
    }

    @Test
    void testGroupJdbcService_updateShouldThrowExeption_whenNotFoundRecord() {
        when(groupJdbcDao.update(testGroupEntity, PARAMETERS)).thenReturn(NEGATIVE_OPERATION_RETURN);
        Exception exception = assertThrows(UnsuccessfulOperationException.class,
                () -> groupJdbcService.update(testGroupEntity, PARAMETERS));

        String expected = NOT_UPDATED;
        String actual = exception.getMessage();

        assertEquals(expected, actual);

    }

    @Test
    void testGroupJdbcService_deleteShouldPass() {
        when(groupJdbcDao.getByID(1)).thenReturn(NOT_EMPTY_OPTIONAL);
        when(groupJdbcDao.deleteById(1)).thenReturn(POSITIVE_OPERATION_RETURN);

        assertAll(() -> groupJdbcService.deleteById(1));
    }

    @Test
    void testGroupJdbcService_deleteShouldCallDaoRightTimesAndOrder() {
        when(groupJdbcDao.getByID(1)).thenReturn(NOT_EMPTY_OPTIONAL);
        when(groupJdbcDao.deleteById(1)).thenReturn(POSITIVE_OPERATION_RETURN);
        groupJdbcService.deleteById(1);

        verify(groupJdbcDao, times(1)).getByID(1);
        verify(groupJdbcDao, times(1)).deleteById(1);

        InOrder inOrder = Mockito.inOrder(groupJdbcDao);
        inOrder.verify(groupJdbcDao).getByID(1);
        inOrder.verify(groupJdbcDao).deleteById(1);
    }

    @Test
    void testGroupJdbcService_deleteShouldThrowExeption_whenUnsuccessful() {
        when(groupJdbcDao.getByID(1)).thenReturn(NOT_EMPTY_OPTIONAL);
        when(groupJdbcDao.deleteById(1)).thenReturn(NEGATIVE_OPERATION_RETURN);

        Exception exception = assertThrows(UnsuccessfulOperationException.class, () -> groupJdbcService.deleteById(1));
        String expected = NOT_DELETED;
        String actual = exception.getMessage();

        assertEquals(expected, actual);
    }

    @Test
    void testGroupJdbcService_deleteShouldThrowExeption_whenNotFoundRecord() {
        when(groupJdbcDao.getByID(1)).thenReturn(EMPTY_OPTIONAL);
        when(groupJdbcDao.deleteById(1)).thenReturn(POSITIVE_OPERATION_RETURN);

        Exception exception = assertThrows(NotFoundException.class, () -> groupJdbcService.deleteById(1));
        String expected = NOT_EXIST;
        String actual = exception.getMessage();

        assertEquals(expected, actual);
    }

}
