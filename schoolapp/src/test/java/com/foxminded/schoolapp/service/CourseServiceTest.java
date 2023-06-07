package com.foxminded.schoolapp.service;
//package com.foxminded.schoolapp.course;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.atLeastOnce;
//import static org.mockito.Mockito.inOrder;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//import java.util.Arrays;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InOrder;
//import org.mockito.Mockito;
//
//import ua.foxminded.school.dao.database.CourseDao;
//import ua.foxminded.school.domain.entity.CourseEntity;
//import ua.foxminded.school.domain.generators.CoursesGenerator;
//import ua.foxminded.school.domain.service.CourseService;
//
//
//
//class CourseServiceTest {
//    
//    private CourseService course;
//    private CourseEntity testCourseEntity;
//    private CourseDao courseDao;
//    private CoursesGenerator courseGenerator;
//    
//    
//    @BeforeEach
//    public void init() throws Exception {
//        courseDao = Mockito.mock(CourseDao.class);
//        courseGenerator = Mockito.mock(CoursesGenerator.class);
//        course = new CourseService(courseDao, courseGenerator);
//        testCourseEntity = new CourseEntity();
//        testCourseEntity.setCourseName("test");
//        testCourseEntity.setCourseDescription("test");
//    }
//
//
//    
//    @Test
//    void testCoursesService_populateShouldPass() throws Exception {
//        assertAll(() -> course.populate());
//    }
//
//    @Test
//    void testCoursesService_populateShouldCallGeneratorAndDaoRightTimesInRightOrder() throws Exception {
//
//        when(courseGenerator.generate()).thenReturn(Arrays.asList(testCourseEntity));
//
//        course.populate();
//        verify(courseGenerator, times(1)).generate();
//        verify(courseDao, atLeastOnce()).save(testCourseEntity);
//
//        InOrder inOrder = inOrder(courseGenerator, courseDao);
//        inOrder.verify(courseGenerator).generate();
//        inOrder.verify(courseDao).save(testCourseEntity);
//        inOrder.verifyNoMoreInteractions();
//    }
//
//    @Test
//    void testCoursesService_CreateEntryShouldPass() throws Exception {
//        assertAll(() -> course.save(testCourseEntity));
//
//    }
//
//    @Test
//    void testCoursesService_FindByIdEntryShouldPass() throws Exception {
//        assertAll(() -> course.getByID(1));
//    }
//
//    @Test
//    void testCoursesService_FindAllEntryShouldPass() throws Exception {
//        assertAll(() -> course.getAll());
//
//    }
//
//    @Test
//    void testCoursesService_UpdateEntryShouldPass() throws Exception {
//        assertAll(() -> course.update(testCourseEntity));
//
//    }
//
//    @Test
//    void testCoursesService_DeleteEntryShouldPass() throws Exception {
//        assertAll(() -> course.deleteById(1));
//
//    }
//
//}
