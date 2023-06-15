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

import com.foxminded.schoolapp.dao.entity.StudentEntity;
import com.foxminded.schoolapp.dao.impl.StudentJdbcDao;
import com.foxminded.schoolapp.exception.NotFoundException;
import com.foxminded.schoolapp.exception.UnsuccessfulOperationException;
import com.foxminded.schoolapp.service.generator.StudentsGenerator;
import com.foxminded.schoolapp.service.impl.StudentService;

@SpringBootTest(classes = { StudentService.class })
class StudentServiceTest extends BasicServiceTest {

    @MockBean
    private StudentJdbcDao studentJdbcDao;

    @MockBean
    private StudentsGenerator studentGenerator;

    @MockBean
    private StudentEntity testStudentEntity;

    @Autowired
    private StudentService studentService;

    private static final List<StudentEntity> EMPTY_LIST = new ArrayList<>();
    private static final List<StudentEntity> NOT_EMPTY_LIST = new ArrayList<>(Arrays.asList(new StudentEntity()));

    @Test
    void testStudentJdbcService_populateShouldPass() {
        when(studentJdbcDao.save(testStudentEntity)).thenReturn(POSITIVE_OPERATION_RETURN);

        assertAll(() -> studentService.populate());
    }

    @Test
    void testStudentService_populateShouldCallGeneratorAndDaoRightTimesInRightOrder() {

        when(studentGenerator.generate()).thenReturn(Arrays.asList(testStudentEntity));
        when(studentJdbcDao.save(testStudentEntity)).thenReturn(POSITIVE_OPERATION_RETURN);

        studentService.populate();
        verify(studentGenerator, times(1)).generate();
        verify(studentJdbcDao, atLeastOnce()).save(testStudentEntity);

        InOrder inOrder = inOrder(studentGenerator, studentJdbcDao);
        inOrder.verify(studentGenerator).generate();
        inOrder.verify(studentJdbcDao).save(testStudentEntity);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void testStudentService_saveShouldPass() {
        when(studentJdbcDao.save(testStudentEntity)).thenReturn(POSITIVE_OPERATION_RETURN);

        assertAll(() -> studentService.save(testStudentEntity));
    }

    @Test
    void testStudentService_saveShouldCallDaoOneTime() {
        when(studentJdbcDao.save(testStudentEntity)).thenReturn(POSITIVE_OPERATION_RETURN);
        studentService.save(testStudentEntity);

        verify(studentJdbcDao, times(1)).save(testStudentEntity);
    }

    @Test
    void testStudentService_saveShouldThrowExeption_whenUnsuccessful() {
        when(studentJdbcDao.save(testStudentEntity)).thenReturn(NEGATIVE_OPERATION_RETURN);
        Exception exception = assertThrows(UnsuccessfulOperationException.class,
                () -> studentService.save(testStudentEntity));

        String expected = NOT_SAVED;
        String actual = exception.getMessage();

        assertEquals(expected, actual);
    }

    @Test
    void testStudentService_getAllShouldPass() {
        when(studentJdbcDao.getAll()).thenReturn(NOT_EMPTY_LIST);

        assertAll(() -> studentService.getAll());
    }

    @Test
    void testStudentService_getAllShouldCallDaoOneTime() {
        when(studentJdbcDao.getAll()).thenReturn(NOT_EMPTY_LIST);
        studentService.getAll();

        verify(studentJdbcDao, times(1)).getAll();
    }

    @Test
    void testStudentService_getAllShouldThrowExeption_whenReturnEmptyList() {
        when(studentJdbcDao.getAll()).thenReturn(EMPTY_LIST);
        Exception exception = assertThrows(NotFoundException.class, () -> studentService.getAll());

        String expected = IS_EMPTY;
        String actual = exception.getMessage();

        assertEquals(expected, actual);
    }

    @Test
    void testStudentService_getByIDShouldPass() {
        when(studentJdbcDao.getByID(1)).thenReturn(testStudentEntity);

        assertAll(() -> studentService.getByID(1));
    }

    @Test
    void testStudentService_getByIDShouldCallDaoOneTime() {
        when(studentJdbcDao.getByID(1)).thenReturn(testStudentEntity);
        studentService.getByID(1);

        verify(studentJdbcDao, times(1)).getByID(1);
    }

    @Test
    void testStudentService_updateShouldPass() {
        when(studentJdbcDao.update(testStudentEntity)).thenReturn(POSITIVE_OPERATION_RETURN);

        assertAll(() -> studentService.update(testStudentEntity));
    }

    @Test
    void testStudentService_updateShouldCallDaoOneTime() {
        when(studentJdbcDao.update(testStudentEntity)).thenReturn(POSITIVE_OPERATION_RETURN);
        studentService.update(testStudentEntity);

        verify(studentJdbcDao).update(testStudentEntity);
    }

    @Test
    void testStudentService_updateShouldThrowExeption_whenNotFoundRecord() {
        when(studentJdbcDao.update(testStudentEntity)).thenReturn(NEGATIVE_OPERATION_RETURN);
        Exception exception = assertThrows(UnsuccessfulOperationException.class,
                () -> studentService.update(testStudentEntity));

        String expected = NOT_UPDATED;
        String actual = exception.getMessage();

        assertEquals(expected, actual);
    }

    @Test
    void testStudentService_deleteShouldPass() {
        when(studentJdbcDao.getByID(1)).thenReturn(testStudentEntity);
        when(studentJdbcDao.deleteById(1)).thenReturn(POSITIVE_OPERATION_RETURN);

        assertAll(() -> studentService.deleteById(1));
    }

    @Test
    void testStudentService_deleteShouldCallDaoRightTimesAndOrder() {
        when(studentJdbcDao.getByID(1)).thenReturn(testStudentEntity);
        when(studentJdbcDao.deleteById(1)).thenReturn(POSITIVE_OPERATION_RETURN);
        studentService.deleteById(1);

        verify(studentJdbcDao, times(1)).getByID(1);
        verify(studentJdbcDao, times(1)).deleteById(1);

        InOrder inOrder = Mockito.inOrder(studentJdbcDao);
        inOrder.verify(studentJdbcDao).getByID(1);
        inOrder.verify(studentJdbcDao).deleteById(1);
    }

    @Test
    void testStudentService_deleteShouldThrowExeption_whenUnsuccessful() {
        when(studentJdbcDao.getByID(1)).thenReturn(testStudentEntity);
        when(studentJdbcDao.deleteById(1)).thenReturn(NEGATIVE_OPERATION_RETURN);

        Exception exception = assertThrows(UnsuccessfulOperationException.class, () -> studentService.deleteById(1));
        String expected = NOT_DELETED;
        String actual = exception.getMessage();

        assertEquals(expected, actual);
    }

    @Test
    void testStudentService_addStudentToCourse_shouldPass() {
        when(studentJdbcDao.addStudentToCourse(testStudentEntity, 1)).thenReturn(POSITIVE_OPERATION_RETURN);

        assertAll(() -> studentJdbcDao.addStudentToCourse(testStudentEntity, 1));
    }

    @Test
    void testStudentService_addStudentToCourse_shouldCallDaoOneTime() {
        when(studentJdbcDao.addStudentToCourse(testStudentEntity, 1)).thenReturn(POSITIVE_OPERATION_RETURN);
        studentJdbcDao.addStudentToCourse(testStudentEntity, 1);

        verify(studentJdbcDao, times(1)).addStudentToCourse(testStudentEntity, 1);
    }

    @Test
    void testStudentService_addStudentToCourse_shouldThrowExeption_whenUnsuccessful() {
        when(studentJdbcDao.addStudentToCourse(testStudentEntity, 1)).thenReturn(NEGATIVE_OPERATION_RETURN);
        Exception exception = assertThrows(UnsuccessfulOperationException.class,
                () -> studentService.addStudentToCourse(testStudentEntity, 1));

        String expected = NOT_SAVED;
        String actual = exception.getMessage();

        assertEquals(expected, actual);
    }

    @Test
    void testStudentService_findAllStudentsRelatedToCourse_shouldPass() {
        when(studentJdbcDao.findAllStudentsRelatedToCourse(1)).thenReturn(NOT_EMPTY_LIST);

        assertAll(() -> studentJdbcDao.findAllStudentsRelatedToCourse(1));
    }

    @Test
    void testStudentService_findAllStudentsRelatedToCourse_ShouldCallDaoOneTime() {
        when(studentJdbcDao.findAllStudentsRelatedToCourse(1)).thenReturn(NOT_EMPTY_LIST);
        studentService.findAllStudentsRelatedToCourse(1);

        verify(studentJdbcDao, times(1)).findAllStudentsRelatedToCourse(1);
    }

    @Test
    void testStudentService_findAllStudentsRelatedToCourse_shouldThrowExeption_whenNotFoundRecord() {
        when(studentJdbcDao.findAllStudentsRelatedToCourse(1)).thenReturn(EMPTY_LIST);
        Exception exception = assertThrows(NotFoundException.class,
                () -> studentService.findAllStudentsRelatedToCourse(1));

        String expected = NOT_EXIST;
        String actual = exception.getMessage();

        assertEquals(expected, actual);
    }

    @Test
    void testStudentService_removeStudentByIDFromCourse_shouldPass() {
        when(studentJdbcDao.removeStudentByIDFromCourse(1, 1)).thenReturn(POSITIVE_OPERATION_RETURN);

        assertAll(() -> studentService.removeStudentByIDFromCourse(1, 1));
    }

    @Test
    void testStudentService_removeStudentByIDFromCourse_shouldCallDaoRightTimesAndOrder() {
        when(studentJdbcDao.removeStudentByIDFromCourse(1, 1)).thenReturn(POSITIVE_OPERATION_RETURN);
        studentService.removeStudentByIDFromCourse(1, 1);

        verify(studentJdbcDao, times(1)).removeStudentByIDFromCourse(1, 1);
    }

    @Test
    void testStudentService_removeStudentByIDFromCourse_shouldThrowExeption_whenUnsuccessful() {
        when(studentJdbcDao.removeStudentByIDFromCourse(1, 1)).thenReturn(NEGATIVE_OPERATION_RETURN);
        Exception exception = assertThrows(UnsuccessfulOperationException.class,
                () -> studentService.removeStudentByIDFromCourse(1, 1));

        String expected = NOT_DELETED;
        String actual = exception.getMessage();

        assertEquals(expected, actual);
    }

}
