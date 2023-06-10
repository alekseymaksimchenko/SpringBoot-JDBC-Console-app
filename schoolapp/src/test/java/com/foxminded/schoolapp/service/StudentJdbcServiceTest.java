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

import com.foxminded.schoolapp.dao.entity.StudentEntity;
import com.foxminded.schoolapp.dao.impl.StudentJdbcDao;
import com.foxminded.schoolapp.exception.NotFoundException;
import com.foxminded.schoolapp.exception.UnsuccessfulOperationException;
import com.foxminded.schoolapp.service.generator.StudentsGenerator;
import com.foxminded.schoolapp.service.impl.StudentJdbcService;

@SpringBootTest(classes = { StudentJdbcService.class })
class StudentJdbcServiceTest extends BasicServiceTest {


    @MockBean
    private StudentJdbcDao studentJdbcDao;

    @MockBean
    private StudentsGenerator studentGenerator;

    @MockBean
    private StudentEntity testStudentEntity;

    @Autowired
    private StudentJdbcService studentJdbcService;

    private static final Optional<StudentEntity> EMPTY_OPTIONAL = Optional.empty();
    private static final Optional<StudentEntity> NOT_EMPTY_OPTIONAL = Optional.of(new StudentEntity());
    private static final List<StudentEntity> EMPTY_LIST = new ArrayList<>();
    private static final List<StudentEntity> NOT_EMPTY_LIST = new ArrayList<>(Arrays.asList(new StudentEntity()));

    @Test
    void testStudentJdbcService_populateShouldPass() {
        when(studentJdbcDao.save(testStudentEntity)).thenReturn(POSITIVE_OPERATION_RETURN);

        assertAll(() -> studentJdbcService.populate());
    }

    @Test
    void testStudentJdbcService_populateShouldCallGeneratorAndDaoRightTimesInRightOrder() {

        when(studentGenerator.generate()).thenReturn(Arrays.asList(testStudentEntity));
        when(studentJdbcDao.save(testStudentEntity)).thenReturn(POSITIVE_OPERATION_RETURN);

        studentJdbcService.populate();
        verify(studentGenerator, times(1)).generate();
        verify(studentJdbcDao, atLeastOnce()).save(testStudentEntity);

        InOrder inOrder = inOrder(studentGenerator, studentJdbcDao);
        inOrder.verify(studentGenerator).generate();
        inOrder.verify(studentJdbcDao).save(testStudentEntity);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void testStudentJdbcService_saveShouldPass() {
        when(studentJdbcDao.save(testStudentEntity)).thenReturn(POSITIVE_OPERATION_RETURN);

        assertAll(() -> studentJdbcService.save(testStudentEntity));
    }

    @Test
    void testStudentJdbcService_saveShouldCallDaoOneTime() {
        when(studentJdbcDao.save(testStudentEntity)).thenReturn(POSITIVE_OPERATION_RETURN);
        studentJdbcService.save(testStudentEntity);

        verify(studentJdbcDao, times(1)).save(testStudentEntity);
    }

    @Test
    void testStudentJdbcService_saveShouldThrowExeption_whenUnsuccessful() {
        when(studentJdbcDao.save(testStudentEntity)).thenReturn(NEGATIVE_OPERATION_RETURN);
        Exception exception = assertThrows(UnsuccessfulOperationException.class,
                () -> studentJdbcService.save(testStudentEntity));

        String expected = NOT_SAVED;
        String actual = exception.getMessage();

        assertEquals(expected, actual);
    }

    @Test
    void testStudentJdbcService_getAllShouldPass() {
        when(studentJdbcDao.getAll()).thenReturn(NOT_EMPTY_LIST);

        assertAll(() -> studentJdbcService.getAll());
    }

    @Test
    void testStudentJdbcService_getAllShouldCallDaoOneTime() {
        when(studentJdbcDao.getAll()).thenReturn(NOT_EMPTY_LIST);
        studentJdbcService.getAll();

        verify(studentJdbcDao, times(1)).getAll();
    }

    @Test
    void testStudentJdbcService_getAllShouldThrowExeption_whenReturnEmptyList() {
        when(studentJdbcDao.getAll()).thenReturn(EMPTY_LIST);
        Exception exception = assertThrows(NotFoundException.class, () -> studentJdbcService.getAll());

        String expected = IS_EMPTY;
        String actual = exception.getMessage();

        assertEquals(expected, actual);
    }

    @Test
    void testStudentJdbcService_getByIDShouldPass() {
        when(studentJdbcDao.getByID(1)).thenReturn(NOT_EMPTY_OPTIONAL);

        assertAll(() -> studentJdbcService.getByID(1));
    }

    @Test
    void testStudentJdbcService_getByIDShouldCallDaoOneTime() {
        when(studentJdbcDao.getByID(1)).thenReturn(NOT_EMPTY_OPTIONAL);
        studentJdbcService.getByID(1);

        verify(studentJdbcDao, times(1)).getByID(1);
    }

    @Test
    void testStudentJdbcService_getByIDShouldThrowExeption_whenNotFoundRecord() {
        when(studentJdbcDao.getByID(1)).thenReturn(EMPTY_OPTIONAL);
        Exception exception = assertThrows(NotFoundException.class, () -> studentJdbcService.getByID(1));

        String expected = NOT_EXIST;
        String actual = exception.getMessage();

        assertEquals(expected, actual);
    }

    @Test
    void testStudentJdbcService_updateShouldPass() {
        when(studentJdbcDao.update(testStudentEntity, PARAMETERS)).thenReturn(POSITIVE_OPERATION_RETURN);

        assertAll(() -> studentJdbcService.update(testStudentEntity, PARAMETERS));
    }

    @Test
    void testStudentJdbcService_updateShouldCallDaoOneTime() {
        when(studentJdbcDao.update(testStudentEntity, PARAMETERS)).thenReturn(POSITIVE_OPERATION_RETURN);
        studentJdbcService.update(testStudentEntity, PARAMETERS);

        verify(studentJdbcDao).update(testStudentEntity, PARAMETERS);
    }

    @Test
    void testStudentJdbcService_updateShouldThrowExeption_whenNotFoundRecord() {
        when(studentJdbcDao.update(testStudentEntity, PARAMETERS)).thenReturn(NEGATIVE_OPERATION_RETURN);
        Exception exception = assertThrows(UnsuccessfulOperationException.class,
                () -> studentJdbcService.update(testStudentEntity, PARAMETERS));

        String expected = NOT_UPDATED;
        String actual = exception.getMessage();

        assertEquals(expected, actual);

    }

    @Test
    void testStudentJdbcService_deleteShouldPass() {
        when(studentJdbcDao.getByID(1)).thenReturn(NOT_EMPTY_OPTIONAL);
        when(studentJdbcDao.deleteById(1)).thenReturn(POSITIVE_OPERATION_RETURN);

        assertAll(() -> studentJdbcService.deleteById(1));
    }

    @Test
    void testStudentJdbcService_deleteShouldCallDaoRightTimesAndOrder() {
        when(studentJdbcDao.getByID(1)).thenReturn(NOT_EMPTY_OPTIONAL);
        when(studentJdbcDao.deleteById(1)).thenReturn(POSITIVE_OPERATION_RETURN);
        studentJdbcService.deleteById(1);

        verify(studentJdbcDao, times(1)).getByID(1);
        verify(studentJdbcDao, times(1)).deleteById(1);

        InOrder inOrder = Mockito.inOrder(studentJdbcDao);
        inOrder.verify(studentJdbcDao).getByID(1);
        inOrder.verify(studentJdbcDao).deleteById(1);
    }

    @Test
    void testStudentJdbcService_deleteShouldThrowExeption_whenUnsuccessful() {
        when(studentJdbcDao.getByID(1)).thenReturn(NOT_EMPTY_OPTIONAL);
        when(studentJdbcDao.deleteById(1)).thenReturn(NEGATIVE_OPERATION_RETURN);

        Exception exception = assertThrows(UnsuccessfulOperationException.class, () -> studentJdbcService.deleteById(1));
        String expected = NOT_DELETED;
        String actual = exception.getMessage();

        assertEquals(expected, actual);
    }

    @Test
    void testStudentJdbcService_deleteShouldThrowExeption_whenNotFoundRecord() {
        when(studentJdbcDao.getByID(1)).thenReturn(EMPTY_OPTIONAL);
        when(studentJdbcDao.deleteById(1)).thenReturn(POSITIVE_OPERATION_RETURN);

        Exception exception = assertThrows(NotFoundException.class, () -> studentJdbcService.deleteById(1));
        String expected = NOT_EXIST;
        String actual = exception.getMessage();

        assertEquals(expected, actual);
    }

}
