package com.foxminded.schoolapp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.foxminded.schoolapp.dao.entity.CourseEntity;
import com.foxminded.schoolapp.service.impl.CourseService;
import com.foxminded.schoolapp.service.impl.GroupService;
import com.foxminded.schoolapp.service.impl.StudentService;

@SpringBootTest (classes = { SchoolappMenu.class })
class SchoolappMenuTest {

    @MockBean
    private GroupService groupService;
    @MockBean
    private StudentService studentService;
    @MockBean
    private CourseService courseService;

    @Autowired
    private SchoolappMenu schoolappMenu;
    

    @Test
    void testSchoolappMenu___________www__________() {
       // doReturn(new ArrayList<>()).when(courseService).getAll();
        
        when(courseService.getAll()).thenReturn(new ArrayList<CourseEntity>());
        
        
     

        schoolappMenu.runMenu();
        verify(courseService, times(1)).populate();
        verify(groupService, times(1)).populate();
        verify(studentService, times(1)).populate();
        verify(studentService, times(1)).populateStudentsToCourses();

 
    }
    
//    @Test
//    void testSchoolappMenu___() {
//        String input = "7";
//        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
//        
//        
//        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//        
//        PrintStream printStream = new PrintStream(outputStream);
//
//        // Redirect System.in and System.out
//        InputStream originalInputStream = System.in;
//        PrintStream originalPrintStream = System.out;
//        System.setIn(inputStream);
//        System.setOut(printStream);
//        
//        // Call the method that reads user input and prints a string
//        schoolappMenu.runMenu();
//
//        // Restore original System.in and System.out
//        System.setIn(originalInputStream);
//        System.setOut(originalPrintStream);
//
//        // Verify the printed output
//        String expectedOutput = "Your expected output";
//        assertEquals(expectedOutput, outputStream.toString().trim());
//        
//        
//        
//    }
}
