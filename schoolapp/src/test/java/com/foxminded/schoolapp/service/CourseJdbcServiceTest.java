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

import com.foxminded.schoolapp.dao.entity.CourseEntity;
import com.foxminded.schoolapp.dao.impl.CourseJdbcDao;
import com.foxminded.schoolapp.exception.NotFoundException;
import com.foxminded.schoolapp.exception.UnsuccessfulOperationException;
import com.foxminded.schoolapp.service.generator.CoursesGenerator;
import com.foxminded.schoolapp.service.impl.CourseJdbcService;

@SpringBootTest(classes = { CourseJdbcService.class })
class CourseJdbcServiceTest extends BasicServiceTest {

    @MockBean
    private CourseJdbcDao courseJdbcDao;

    @MockBean
    private CoursesGenerator courseGenerator;

    @MockBean
    private CourseEntity testCourseEntity;

    @Autowired
    private CourseJdbcService courseJdbcService;

    private static final Optional<CourseEntity> EMPTY_OPTIONAL = Optional.empty();
    private static final Optional<CourseEntity> NOT_EMPTY_OPTIONAL = Optional.of(new CourseEntity());
    private static final List<CourseEntity> EMPTY_LIST = new ArrayList<>();
    private static final List<CourseEntity> NOT_EMPTY_LIST = new ArrayList<>(Arrays.asList(new CourseEntity()));


    @Test
    void testCourseJdbcService_populateShouldPass() {
        when(courseJdbcDao.save(testCourseEntity)).thenReturn(POSITIVE_OPERATION_RETURN);

        assertAll(() -> courseJdbcService.populate());
    }

    @Test
    void testCourseJdbcService_populateShouldCallGeneratorAndDaoRightTimesInRightOrder() {

        when(courseGenerator.generate()).thenReturn(Arrays.asList(testCourseEntity));
        when(courseJdbcDao.save(testCourseEntity)).thenReturn(POSITIVE_OPERATION_RETURN);

        courseJdbcService.populate();
        verify(courseGenerator, times(1)).generate();
        verify(courseJdbcDao, atLeastOnce()).save(testCourseEntity);

        InOrder inOrder = inOrder(courseGenerator, courseJdbcDao);
        inOrder.verify(courseGenerator).generate();
        inOrder.verify(courseJdbcDao).save(testCourseEntity);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void testCourseJdbcService_saveShouldPass() {
        when(courseJdbcDao.save(testCourseEntity)).thenReturn(POSITIVE_OPERATION_RETURN);

        assertAll(() -> courseJdbcService.save(testCourseEntity));
    }

    @Test
    void testCourseJdbcService_saveShouldCallDaoOneTime() {
        when(courseJdbcDao.save(testCourseEntity)).thenReturn(POSITIVE_OPERATION_RETURN);
        courseJdbcService.save(testCourseEntity);

        verify(courseJdbcDao, times(1)).save(testCourseEntity);
    }

    @Test
    void testCourseJdbcService_saveShouldThrowExeption_whenUnsuccessful() {
        when(courseJdbcDao.save(testCourseEntity)).thenReturn(NEGATIVE_OPERATION_RETURN);
        Exception exception = assertThrows(UnsuccessfulOperationException.class,
                () -> courseJdbcService.save(testCourseEntity));

        String expected = NOT_SAVED;
        String actual = exception.getMessage();

        assertEquals(expected, actual);
    }

    @Test
    void testCourseJdbcService_getAllShouldPass() {
        when(courseJdbcDao.getAll()).thenReturn(NOT_EMPTY_LIST);

        assertAll(() -> courseJdbcService.getAll());
    }

    @Test
    void testCourseJdbcService_getAllShouldCallDaoOneTime() {
        when(courseJdbcDao.getAll()).thenReturn(NOT_EMPTY_LIST);
        courseJdbcService.getAll();

        verify(courseJdbcDao, times(1)).getAll();
    }

    @Test
    void testCourseJdbcService_getAllShouldThrowExeption_whenReturnEmptyList() {
        when(courseJdbcDao.getAll()).thenReturn(EMPTY_LIST);
        Exception exception = assertThrows(NotFoundException.class, () -> courseJdbcService.getAll());

        String expected = IS_EMPTY;
        String actual = exception.getMessage();

        assertEquals(expected, actual);
    }

    @Test
    void testCourseJdbcService_getByIDShouldPass() {
        when(courseJdbcDao.getByID(1)).thenReturn(NOT_EMPTY_OPTIONAL);

        assertAll(() -> courseJdbcService.getByID(1));
    }

    @Test
    void testCourseJdbcService_getByIDShouldCallDaoOneTime() {
        when(courseJdbcDao.getByID(1)).thenReturn(NOT_EMPTY_OPTIONAL);
        courseJdbcService.getByID(1);

        verify(courseJdbcDao, times(1)).getByID(1);
    }

    @Test
    void testCourseJdbcService_getByIDShouldThrowExeption_whenNotFoundRecord() {
        when(courseJdbcDao.getByID(1)).thenReturn(EMPTY_OPTIONAL);
        Exception exception = assertThrows(NotFoundException.class, () -> courseJdbcService.getByID(1));

        String expected = NOT_EXIST;
        String actual = exception.getMessage();

        assertEquals(expected, actual);
    }

    @Test
    void testCourseJdbcService_updateShouldPass() {
        when(courseJdbcDao.update(testCourseEntity, PARAMETERS)).thenReturn(POSITIVE_OPERATION_RETURN);

        assertAll(() -> courseJdbcService.update(testCourseEntity, PARAMETERS));
    }

    @Test
    void testCourseJdbcService_updateShouldCallDaoOneTime() {
        when(courseJdbcDao.update(testCourseEntity, PARAMETERS)).thenReturn(POSITIVE_OPERATION_RETURN);
        courseJdbcService.update(testCourseEntity, PARAMETERS);

        verify(courseJdbcDao).update(testCourseEntity, PARAMETERS);
    }

    @Test
    void testCourseJdbcService_updateShouldThrowExeption_whenNotFoundRecord() {
        when(courseJdbcDao.update(testCourseEntity, PARAMETERS)).thenReturn(NEGATIVE_OPERATION_RETURN);
        Exception exception = assertThrows(UnsuccessfulOperationException.class,
                () -> courseJdbcService.update(testCourseEntity, PARAMETERS));

        String expected = NOT_UPDATED;
        String actual = exception.getMessage();

        assertEquals(expected, actual);

    }

    @Test
    void testCourseJdbcService_deleteShouldPass() {
        when(courseJdbcDao.getByID(1)).thenReturn(NOT_EMPTY_OPTIONAL);
        when(courseJdbcDao.deleteById(1)).thenReturn(POSITIVE_OPERATION_RETURN);

        assertAll(() -> courseJdbcService.deleteById(1));
    }

    @Test
    void testCourseJdbcService_deleteShouldCallDaoRightTimesAndOrder() {
        when(courseJdbcDao.getByID(1)).thenReturn(NOT_EMPTY_OPTIONAL);
        when(courseJdbcDao.deleteById(1)).thenReturn(POSITIVE_OPERATION_RETURN);
        courseJdbcService.deleteById(1);

        verify(courseJdbcDao, times(1)).getByID(1);
        verify(courseJdbcDao, times(1)).deleteById(1);

        InOrder inOrder = Mockito.inOrder(courseJdbcDao);
        inOrder.verify(courseJdbcDao).getByID(1);
        inOrder.verify(courseJdbcDao).deleteById(1);
    }

    @Test
    void testCourseJdbcService_deleteShouldThrowExeption_whenUnsuccessful() {
        when(courseJdbcDao.getByID(1)).thenReturn(NOT_EMPTY_OPTIONAL);
        when(courseJdbcDao.deleteById(1)).thenReturn(NEGATIVE_OPERATION_RETURN);

        Exception exception = assertThrows(UnsuccessfulOperationException.class, () -> courseJdbcService.deleteById(1));
        String expected = NOT_DELETED;
        String actual = exception.getMessage();

        assertEquals(expected, actual);
    }

    @Test
    void testCourseJdbcService_deleteShouldThrowExeption_whenNotFoundRecord() {
        when(courseJdbcDao.getByID(1)).thenReturn(EMPTY_OPTIONAL);
        when(courseJdbcDao.deleteById(1)).thenReturn(POSITIVE_OPERATION_RETURN);

        Exception exception = assertThrows(NotFoundException.class, () -> courseJdbcService.deleteById(1));
        String expected = NOT_EXIST;
        String actual = exception.getMessage();

        assertEquals(expected, actual);
    }

}
