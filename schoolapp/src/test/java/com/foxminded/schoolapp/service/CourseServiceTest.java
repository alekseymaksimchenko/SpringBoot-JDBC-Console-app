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

import com.foxminded.schoolapp.dao.entity.CourseEntity;
import com.foxminded.schoolapp.dao.impl.CourseJdbcDao;
import com.foxminded.schoolapp.exception.DaoException;
import com.foxminded.schoolapp.exception.ServiceException;
import com.foxminded.schoolapp.service.generator.CoursesGenerator;
import com.foxminded.schoolapp.service.impl.CourseService;

@SpringBootTest(classes = { CourseService.class })
class CourseServiceTest {

    @MockBean
    private CourseJdbcDao courseJdbcDao;

    @MockBean
    private CoursesGenerator courseGenerator;

    @MockBean
    private CourseEntity testCourseEntity;

    @Autowired
    private CourseService courseService;

    @Test
    void testCourseService_populateShouldPass() {
        assertAll(() -> courseService.populate());
    }

    @Test
    void testCourseService_populateShouldCallGeneratorAndDaoRightTimesInRightOrder() {
        when(courseGenerator.generate()).thenReturn(Arrays.asList(testCourseEntity));

        courseService.populate();
        verify(courseGenerator, times(1)).generate();
        verify(courseJdbcDao, atLeastOnce()).save(testCourseEntity);

        InOrder inOrder = inOrder(courseGenerator, courseJdbcDao);
        inOrder.verify(courseGenerator).generate();
        inOrder.verify(courseJdbcDao).save(testCourseEntity);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void testCourseService_saveShouldPass() {
        assertAll(() -> courseService.save(testCourseEntity));
    }

    @Test
    void testCourseService_saveShouldCallDaoOneTime() {
        courseService.save(testCourseEntity);

        verify(courseJdbcDao, times(1)).save(testCourseEntity);
    }

    @Test
    void testCourseService_saveShouldThrowExeption_whenUnsuccessful() {
        doThrow(DaoException.class).when(courseJdbcDao).save(testCourseEntity);

        assertThrows(ServiceException.class, () -> courseService.save(testCourseEntity));
    }

    @Test
    void testCourseService_getAllShouldPass() {
        assertAll(() -> courseService.getAll());
    }

    @Test
    void testCourseService_getAllShouldCallDaoOneTime() {
        courseService.getAll();

        verify(courseJdbcDao, times(1)).getAll();
    }

    @Test
    void testCourseService_getAllShouldThrowExeption_whenReturnEmptyList() {
       doThrow(DaoException.class).when(courseJdbcDao).getAll();
        assertThrows(ServiceException.class, () -> courseService.getAll());
    }

    @Test
    void testCourseService_getByIDShouldPass() {
        when(courseJdbcDao.getByID(1)).thenReturn(testCourseEntity);

        assertAll(() -> courseService.getByID(1));
    }

    @Test
    void testCourseService_getByIDShouldCallDaoOneTime() {
        when(courseJdbcDao.getByID(1)).thenReturn(testCourseEntity);
        courseService.getByID(1);

        verify(courseJdbcDao, times(1)).getByID(1);
    }

    @Test
    void testCourseService_updateShouldPass() {
        assertAll(() -> courseService.update(testCourseEntity));
    }

    @Test
    void testCourseService_updateShouldCallDaoOneTime() {
        courseService.update(testCourseEntity);

        verify(courseJdbcDao).update(testCourseEntity);
    }

    @Test
    void testCourseService_updateShouldThrowExeption_whenNotFoundRecord() {
        doThrow(DaoException.class).when(courseJdbcDao).update(testCourseEntity);
       
        assertThrows(ServiceException.class, () -> courseService.update(testCourseEntity));
    }

    @Test
    void testCourseService_deleteShouldPass() {
        when(courseJdbcDao.getByID(1)).thenReturn(testCourseEntity);

        assertAll(() -> courseService.deleteById(1));
    }

    @Test
    void testCourseService_deleteShouldCallDaoRightTimesAndOrder() {
        when(courseJdbcDao.getByID(1)).thenReturn(testCourseEntity);
        courseService.deleteById(1);

        verify(courseJdbcDao, times(1)).getByID(1);
        verify(courseJdbcDao, times(1)).deleteById(1);

        InOrder inOrder = Mockito.inOrder(courseJdbcDao);
        inOrder.verify(courseJdbcDao).getByID(1);
        inOrder.verify(courseJdbcDao).deleteById(1);
    }

    @Test
    void testCourseService_deleteShouldThrowExeption_whenUnsuccessful() {
        doThrow(DaoException.class).when(courseJdbcDao).deleteById(1);

       assertThrows(ServiceException.class, () -> courseService.deleteById(1));
    }

}
