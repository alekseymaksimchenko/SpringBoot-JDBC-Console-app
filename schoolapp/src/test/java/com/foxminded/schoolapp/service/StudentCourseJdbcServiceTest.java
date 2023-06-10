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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.foxminded.schoolapp.dao.entity.StudentCourseEntity;
import com.foxminded.schoolapp.dao.impl.StudentCourseJdbcDao;
import com.foxminded.schoolapp.exception.NotFoundException;
import com.foxminded.schoolapp.exception.UnsuccessfulOperationException;
import com.foxminded.schoolapp.service.generator.StudentCourseGenerator;
import com.foxminded.schoolapp.service.impl.StudentCourseJdbcService;

@SpringBootTest(classes = { StudentCourseJdbcService.class })
class StudentCourseJdbcServiceTest extends BasicServiceTest {

    @MockBean
    private StudentCourseJdbcDao studentCourseJdbcDao;

    @MockBean
    private StudentCourseGenerator studentCourseGenerator;

    @MockBean
    private StudentCourseEntity studentCourseEntity;

    @Autowired
    private StudentCourseJdbcService studentCourseJdbcService;

    private static final Optional<StudentCourseEntity> NOT_EMPTY_OPTIONAL = Optional.of(new StudentCourseEntity());
    private static final List<StudentCourseEntity> EMPTY_LIST = new ArrayList<>();
    private static final List<StudentCourseEntity> NOT_EMPTY_LIST = new ArrayList<>(
            Arrays.asList(new StudentCourseEntity()));
    private static final String NOT_IMPLEMENTED = "Not implemented, yet";

    @Test
    void testStudentCourseJdbcService_populateShouldPass() {
        when(studentCourseJdbcDao.save(studentCourseEntity)).thenReturn(POSITIVE_OPERATION_RETURN);

        assertAll(() -> studentCourseJdbcService.populate());
    }

    @Test
    void testStudentCourseJdbcService_populateShouldCallGeneratorAndDaoRightTimesInRightOrder() {

        when(studentCourseGenerator.generate()).thenReturn(Arrays.asList(studentCourseEntity));
        when(studentCourseJdbcDao.save(studentCourseEntity)).thenReturn(POSITIVE_OPERATION_RETURN);

        studentCourseJdbcService.populate();
        verify(studentCourseGenerator, times(1)).generate();
        verify(studentCourseJdbcDao, atLeastOnce()).save(studentCourseEntity);

        InOrder inOrder = inOrder(studentCourseGenerator, studentCourseJdbcDao);
        inOrder.verify(studentCourseGenerator).generate();
        inOrder.verify(studentCourseJdbcDao).save(studentCourseEntity);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void testStudentCourseJdbcService_addStudentToCourse_shouldPass() {
        when(studentCourseJdbcDao.addStudentToCourse(studentCourseEntity)).thenReturn(POSITIVE_OPERATION_RETURN);

        assertAll(() -> studentCourseJdbcDao.addStudentToCourse(studentCourseEntity));
    }

    @Test
    void testStudentCourseJdbcService_addStudentToCourse_shouldCallDaoOneTime() {
        when(studentCourseJdbcDao.addStudentToCourse(studentCourseEntity)).thenReturn(POSITIVE_OPERATION_RETURN);
        studentCourseJdbcDao.addStudentToCourse(studentCourseEntity);

        verify(studentCourseJdbcDao, times(1)).addStudentToCourse(studentCourseEntity);

    }

    @Test
    void testStudentCourseJdbcService_addStudentToCourse_shouldThrowExeption_whenUnsuccessful() {
        when(studentCourseJdbcDao.addStudentToCourse(studentCourseEntity)).thenReturn(NEGATIVE_OPERATION_RETURN);
        Exception exception = assertThrows(UnsuccessfulOperationException.class,
                () -> studentCourseJdbcService.addStudentToCourse(studentCourseEntity));

        String expected = NOT_SAVED;
        String actual = exception.getMessage();

        assertEquals(expected, actual);
    }

    @Test
    void testStudentCourseJdbcService_findAllStudentsRelatedToCourse_shouldPass() {
        when(studentCourseJdbcDao.findAllStudentsRelatedToCourse(1)).thenReturn(NOT_EMPTY_LIST);

        assertAll(() -> studentCourseJdbcDao.findAllStudentsRelatedToCourse(1));

    }

    @Test
    void testStudentCourseJdbcService_findAllStudentsRelatedToCourse_ShouldCallDaoOneTime() {
        when(studentCourseJdbcDao.findAllStudentsRelatedToCourse(1)).thenReturn(NOT_EMPTY_LIST);
        studentCourseJdbcService.findAllStudentsRelatedToCourse(1);

        verify(studentCourseJdbcDao, times(1)).findAllStudentsRelatedToCourse(1);

    }

    @Test
    void testStudentCourseJdbcService_findAllStudentsRelatedToCourse_shouldThrowExeption_whenNotFoundRecord() {
        when(studentCourseJdbcDao.findAllStudentsRelatedToCourse(1)).thenReturn(EMPTY_LIST);
        Exception exception = assertThrows(NotFoundException.class,
                () -> studentCourseJdbcService.findAllStudentsRelatedToCourse(1));

        String expected = NOT_EXIST;
        String actual = exception.getMessage();

        assertEquals(expected, actual);
    }

    @Test
    void testStudentCourseJdbcService_removeStudentByIDFromCourse_shouldPass() {
        when(studentCourseJdbcDao.removeStudentByIDFromCourse(1, 1)).thenReturn(POSITIVE_OPERATION_RETURN);

        assertAll(() -> studentCourseJdbcService.removeStudentByIDFromCourse(1, 1));

    }

    @Test
    void testStudentCourseJdbcService_removeStudentByIDFromCourse_shouldCallDaoRightTimesAndOrder() {
        when(studentCourseJdbcDao.removeStudentByIDFromCourse(1, 1)).thenReturn(POSITIVE_OPERATION_RETURN);
        studentCourseJdbcService.removeStudentByIDFromCourse(1, 1);

        verify(studentCourseJdbcDao, times(1)).removeStudentByIDFromCourse(1, 1);
    }

    @Test
    void testStudentCourseJdbcService_removeStudentByIDFromCourse_shouldThrowExeption_whenUnsuccessful() {
        when(studentCourseJdbcDao.removeStudentByIDFromCourse(1, 1)).thenReturn(NEGATIVE_OPERATION_RETURN);
        Exception exception = assertThrows(UnsuccessfulOperationException.class,
                () -> studentCourseJdbcService.removeStudentByIDFromCourse(1, 1));

        String expected = NOT_DELETED;
        String actual = exception.getMessage();

        assertEquals(expected, actual);
    }

    @Test
    void testStudentCourseJdbcService_saveShouldThrowUnsupportedOperationException() {
        when(studentCourseJdbcDao.save(studentCourseEntity)).thenReturn(POSITIVE_OPERATION_RETURN);
        Exception exception = assertThrows(UnsupportedOperationException.class,
                () -> studentCourseJdbcService.save(studentCourseEntity));

        String expected = NOT_IMPLEMENTED;
        String actual = exception.getMessage();

        assertEquals(expected, actual);
    }

    @Test
    void testStudentCourseJdbcService_getAllShouldThrowUnsupportedOperationException() {
        when(studentCourseJdbcDao.getAll()).thenReturn(NOT_EMPTY_LIST);
        Exception exception = assertThrows(UnsupportedOperationException.class,
                () -> studentCourseJdbcService.save(studentCourseEntity));

        String expected = NOT_IMPLEMENTED;
        String actual = exception.getMessage();

        assertEquals(expected, actual);
    }

    @Test
    void testStudentCourseJdbcService_getByIDShouldThrowUnsupportedOperationException() {
        when(studentCourseJdbcDao.getByID(1)).thenReturn(NOT_EMPTY_OPTIONAL);
        Exception exception = assertThrows(UnsupportedOperationException.class,
                () -> studentCourseJdbcService.save(studentCourseEntity));

        String expected = NOT_IMPLEMENTED;
        String actual = exception.getMessage();

        assertEquals(expected, actual);
    }

    @Test
    void testStudentCourseJdbcServicee_updateShouldThrowUnsupportedOperationException() {
        when(studentCourseJdbcDao.update(studentCourseEntity, PARAMETERS)).thenReturn(POSITIVE_OPERATION_RETURN);
        Exception exception = assertThrows(UnsupportedOperationException.class,
                () -> studentCourseJdbcService.save(studentCourseEntity));

        String expected = NOT_IMPLEMENTED;
        String actual = exception.getMessage();

        assertEquals(expected, actual);
    }

    @Test
    void testStudentCourseJdbcService_DeleteShouldThrowUnsupportedOperationException() {
        when(studentCourseJdbcDao.getByID(1)).thenReturn(NOT_EMPTY_OPTIONAL);
        when(studentCourseJdbcDao.deleteById(1)).thenReturn(POSITIVE_OPERATION_RETURN);

        Exception exception = assertThrows(UnsupportedOperationException.class,
                () -> studentCourseJdbcService.save(studentCourseEntity));

        String expected = NOT_IMPLEMENTED;
        String actual = exception.getMessage();

        assertEquals(expected, actual);
    }

}
