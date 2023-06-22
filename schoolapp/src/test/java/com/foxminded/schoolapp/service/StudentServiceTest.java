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

import com.foxminded.schoolapp.dao.entity.StudentEntity;
import com.foxminded.schoolapp.dao.impl.StudentJdbcDao;
import com.foxminded.schoolapp.exception.DaoException;
import com.foxminded.schoolapp.exception.ServiceException;
import com.foxminded.schoolapp.service.generator.StudentsGenerator;
import com.foxminded.schoolapp.service.impl.StudentService;

@SpringBootTest(classes = { StudentService.class })
class StudentServiceTest {

    @MockBean
    private StudentJdbcDao studentJdbcDao;

    @MockBean
    private StudentsGenerator studentGenerator;

    @MockBean
    private StudentEntity testStudentEntity;

    @Autowired
    private StudentService studentService;

    @Test
    void testStudentJdbcService_populateShouldPass() {
        assertAll(() -> studentService.populate());
    }

    @Test
    void testStudentService_populateShouldCallGeneratorAndDaoRightTimesInRightOrder() {
        when(studentGenerator.generate()).thenReturn(Arrays.asList(testStudentEntity));
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
        assertAll(() -> studentService.save(testStudentEntity));
    }

    @Test
    void testStudentService_saveShouldCallDaoOneTime() {
        studentService.save(testStudentEntity);

        verify(studentJdbcDao, times(1)).save(testStudentEntity);
    }

    @Test
    void testStudentService_saveShouldThrowExeption_whenUnsuccessful() {
        doThrow(DaoException.class).when(studentJdbcDao).save(testStudentEntity);

        assertThrows(ServiceException.class, () -> studentService.save(testStudentEntity));
    }

    @Test
    void testStudentService_getAllShouldPass() {
        assertAll(() -> studentService.getAll());
    }

    @Test
    void testStudentService_getAllShouldCallDaoOneTime() {
        studentService.getAll();

        verify(studentJdbcDao, times(1)).getAll();
    }

    @Test
    void testStudentService_getAllShouldThrowExeption_whenReturnEmptyList() {
        doThrow(DaoException.class).when(studentJdbcDao).getAll();

        assertThrows(ServiceException.class, () -> studentService.getAll());
    }

    @Test
    void testStudentService_getByIDShouldPass() {
        assertAll(() -> studentService.getByID(1));
    }

    @Test
    void testStudentService_getByIDShouldCallDaoOneTime() {
        studentService.getByID(1);

        verify(studentJdbcDao, times(1)).getByID(1);
    }

    @Test
    void testStudentService_updateShouldPass() {
        assertAll(() -> studentService.update(testStudentEntity));
    }

    @Test
    void testStudentService_updateShouldCallDaoOneTime() {
        studentService.update(testStudentEntity);

        verify(studentJdbcDao).update(testStudentEntity);
    }

    @Test
    void testStudentService_updateShouldThrowExeption_whenNotFoundRecord() {
        doThrow(DaoException.class).when(studentJdbcDao).update(testStudentEntity);

        assertThrows(ServiceException.class, () -> studentService.update(testStudentEntity));
    }

    @Test
    void testStudentService_deleteShouldPass() {
        assertAll(() -> studentService.deleteById(1));
    }

    @Test
    void testStudentService_deleteShouldCallDaoRightTimesAndOrder() {
        studentService.deleteById(1);

        verify(studentJdbcDao, times(1)).getByID(1);
        verify(studentJdbcDao, times(1)).deleteById(1);

        InOrder inOrder = Mockito.inOrder(studentJdbcDao);
        inOrder.verify(studentJdbcDao).getByID(1);
        inOrder.verify(studentJdbcDao).deleteById(1);
    }

    @Test
    void testStudentService_deleteShouldThrowExeption_whenUnsuccessful() {
        doThrow(DaoException.class).when(studentJdbcDao).deleteById(1);

        assertThrows(ServiceException.class, () -> studentService.deleteById(1));
    }

    @Test
    void testStudentService_addStudentToCourse_shouldPass() {
        assertAll(() -> studentJdbcDao.addStudentToCourse(testStudentEntity, 1));
    }

    @Test
    void testStudentService_addStudentToCourse_shouldCallDaoOneTime() {
        studentJdbcDao.addStudentToCourse(testStudentEntity, 1);

        verify(studentJdbcDao, times(1)).addStudentToCourse(testStudentEntity, 1);
    }

    @Test
    void testStudentService_addStudentToCourse_shouldThrowExeption_whenUnsuccessful() {
        doThrow(DaoException.class).when(studentJdbcDao).addStudentToCourse(testStudentEntity, 1);

        assertThrows(ServiceException.class, () -> studentService.addStudentToCourse(testStudentEntity, 1));
    }

    @Test
    void testStudentService_findAllStudentsRelatedToCourse_shouldPass() {
        assertAll(() -> studentJdbcDao.findAllStudentsRelatedToCourse(1));
    }

    @Test
    void testStudentService_findAllStudentsRelatedToCourse_ShouldCallDaoOneTime() {
        studentService.findAllStudentsRelatedToCourse(1);

        verify(studentJdbcDao, times(1)).findAllStudentsRelatedToCourse(1);
    }

    @Test
    void testStudentService_findAllStudentsRelatedToCourse_shouldThrowExeption_whenNotFoundRecord() {
        doThrow(DaoException.class).when(studentJdbcDao).findAllStudentsRelatedToCourse(1);

        assertThrows(ServiceException.class, () -> studentService.findAllStudentsRelatedToCourse(1));
    }

    @Test
    void testStudentService_removeStudentByIDFromCourse_shouldPass() {
        assertAll(() -> studentService.removeStudentByIDFromCourse(1, 1));
    }

    @Test
    void testStudentService_removeStudentByIDFromCourse_shouldCallDaoRightTimesAndOrder() {
        studentService.removeStudentByIDFromCourse(1, 1);

        verify(studentJdbcDao, times(1)).removeStudentByIDFromCourse(1, 1);
    }

    @Test
    void testStudentService_removeStudentByIDFromCourse_shouldThrowExeption_whenUnsuccessful() {
        doThrow(DaoException.class).when(studentJdbcDao).removeStudentByIDFromCourse(1, 1);

        assertThrows(ServiceException.class, () -> studentService.removeStudentByIDFromCourse(1, 1));
    }

}
