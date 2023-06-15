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

import com.foxminded.schoolapp.dao.entity.CourseEntity;
import com.foxminded.schoolapp.dao.impl.CourseJdbcDao;
import com.foxminded.schoolapp.exception.NotFoundException;
import com.foxminded.schoolapp.exception.UnsuccessfulOperationException;
import com.foxminded.schoolapp.service.generator.CoursesGenerator;
import com.foxminded.schoolapp.service.impl.CourseService;

@SpringBootTest(classes = { CourseService.class })
class CourseServiceTest extends BasicServiceTest {

    @MockBean
    private CourseJdbcDao courseJdbcDao;

    @MockBean
    private CoursesGenerator courseGenerator;

    @MockBean
    private CourseEntity testCourseEntity;

    @Autowired
    private CourseService courseService;

    private static final List<CourseEntity> EMPTY_LIST = new ArrayList<>();
    private static final List<CourseEntity> NOT_EMPTY_LIST = new ArrayList<>(Arrays.asList(new CourseEntity()));

    @Test
    void testCourseService_populateShouldPass() {
        when(courseJdbcDao.save(testCourseEntity)).thenReturn(POSITIVE_OPERATION_RETURN);

        assertAll(() -> courseService.populate());
    }

    @Test
    void testCourseService_populateShouldCallGeneratorAndDaoRightTimesInRightOrder() {
        when(courseGenerator.generate()).thenReturn(Arrays.asList(testCourseEntity));
        when(courseJdbcDao.save(testCourseEntity)).thenReturn(POSITIVE_OPERATION_RETURN);

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
        when(courseJdbcDao.save(testCourseEntity)).thenReturn(POSITIVE_OPERATION_RETURN);

        assertAll(() -> courseService.save(testCourseEntity));
    }

    @Test
    void testCourseService_saveShouldCallDaoOneTime() {
        when(courseJdbcDao.save(testCourseEntity)).thenReturn(POSITIVE_OPERATION_RETURN);
        courseService.save(testCourseEntity);

        verify(courseJdbcDao, times(1)).save(testCourseEntity);
    }

    @Test
    void testCourseService_saveShouldThrowExeption_whenUnsuccessful() {
        when(courseJdbcDao.save(testCourseEntity)).thenReturn(NEGATIVE_OPERATION_RETURN);
        Exception exception = assertThrows(UnsuccessfulOperationException.class,
                () -> courseService.save(testCourseEntity));

        String expected = NOT_SAVED;
        String actual = exception.getMessage();

        assertEquals(expected, actual);
    }

    @Test
    void testCourseService_getAllShouldPass() {
        when(courseJdbcDao.getAll()).thenReturn(NOT_EMPTY_LIST);

        assertAll(() -> courseService.getAll());
    }

    @Test
    void testCourseService_getAllShouldCallDaoOneTime() {
        when(courseJdbcDao.getAll()).thenReturn(NOT_EMPTY_LIST);
        courseService.getAll();

        verify(courseJdbcDao, times(1)).getAll();
    }

    @Test
    void testCourseService_getAllShouldThrowExeption_whenReturnEmptyList() {
        when(courseJdbcDao.getAll()).thenReturn(EMPTY_LIST);
        Exception exception = assertThrows(NotFoundException.class, () -> courseService.getAll());

        String expected = IS_EMPTY;
        String actual = exception.getMessage();

        assertEquals(expected, actual);
    }

    @Test
    void testCourseService_getByIDShouldPass() throws Exception {
        when(courseJdbcDao.getByID(1)).thenReturn(testCourseEntity);

        assertAll(() -> courseService.getByID(1));
    }

    @Test
    void testCourseService_getByIDShouldCallDaoOneTime() throws Exception {
        when(courseJdbcDao.getByID(1)).thenReturn(testCourseEntity);
        courseService.getByID(1);

        verify(courseJdbcDao, times(1)).getByID(1);
    }

    @Test
    void testCourseService_updateShouldPass() {
        when(courseJdbcDao.update(testCourseEntity)).thenReturn(POSITIVE_OPERATION_RETURN);

        assertAll(() -> courseService.update(testCourseEntity));
    }

    @Test
    void testCourseService_updateShouldCallDaoOneTime() {
        when(courseJdbcDao.update(testCourseEntity)).thenReturn(POSITIVE_OPERATION_RETURN);
        courseService.update(testCourseEntity);

        verify(courseJdbcDao).update(testCourseEntity);
    }

    @Test
    void testCourseService_updateShouldThrowExeption_whenNotFoundRecord() {
        when(courseJdbcDao.update(testCourseEntity)).thenReturn(NEGATIVE_OPERATION_RETURN);
        Exception exception = assertThrows(UnsuccessfulOperationException.class,
                () -> courseService.update(testCourseEntity));

        String expected = NOT_UPDATED;
        String actual = exception.getMessage();

        assertEquals(expected, actual);

    }

    @Test
    void testCourseService_deleteShouldPass() {
        when(courseJdbcDao.getByID(1)).thenReturn(testCourseEntity);
        when(courseJdbcDao.deleteById(1)).thenReturn(POSITIVE_OPERATION_RETURN);

        assertAll(() -> courseService.deleteById(1));
    }

    @Test
    void testCourseService_deleteShouldCallDaoRightTimesAndOrder() {
        when(courseJdbcDao.getByID(1)).thenReturn(testCourseEntity);
        when(courseJdbcDao.deleteById(1)).thenReturn(POSITIVE_OPERATION_RETURN);
        courseService.deleteById(1);

        verify(courseJdbcDao, times(1)).getByID(1);
        verify(courseJdbcDao, times(1)).deleteById(1);

        InOrder inOrder = Mockito.inOrder(courseJdbcDao);
        inOrder.verify(courseJdbcDao).getByID(1);
        inOrder.verify(courseJdbcDao).deleteById(1);
    }

    @Test
    void testCourseService_deleteShouldThrowExeption_whenUnsuccessful() {
        when(courseJdbcDao.getByID(1)).thenReturn(testCourseEntity);
        when(courseJdbcDao.deleteById(1)).thenReturn(NEGATIVE_OPERATION_RETURN);

        Exception exception = assertThrows(UnsuccessfulOperationException.class, () -> courseService.deleteById(1));
        String expected = NOT_DELETED;
        String actual = exception.getMessage();

        assertEquals(expected, actual);
    }

}
