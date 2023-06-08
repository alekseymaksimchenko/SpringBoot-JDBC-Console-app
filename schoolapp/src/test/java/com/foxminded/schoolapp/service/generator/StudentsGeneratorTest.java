package com.foxminded.schoolapp.service.generator;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.foxminded.schoolapp.dao.entity.StudentEntity;
import com.foxminded.schoolapp.exception.DomainException;

@SpringBootTest(classes = {StudentsGenerator.class})
class StudentsGeneratorTest {

    private static final String PATH = "./src/test/resources/generatorsConfigurationTest.properties";
    private static final int STUDENTS_QUANTITY = 200;
    private static final int GROUPS_QUANTITY = 10;
    private static final int NO_GROUP_OPTION = GROUPS_QUANTITY + 1;
    private static final List<String> NAME = new ArrayList<>(Arrays.asList("James", "Connor", "Callum", "Jacob", "Kyle",
            "Joe", "Reece", "Charlie", "Ethan", "William", "Mason", "Robert", "David", "Joseph", "John", "Liam",
            "Charles", "Richard", "Michael", "Alexander"));
    private static final List<String> LASTNAME = new ArrayList<>(Arrays.asList("Smith", "Johnson", "Williams", "Brown",
            "Jones", "Garcia", "Miller", "Davis", "Rodriguez", "Martinez", "Lopez", "Gonzalez", "Wilson", "Anderson",
            "Taylor", "Lee", "Perez", "White", "Harris", "Sanchez"));
    
    @Autowired
    private StudentsGenerator students;
    private List<StudentEntity> studentsList;

    @BeforeEach
    public void initEach() throws Exception {
        students = new StudentsGenerator(PATH);
        studentsList = students.generate();
    }

    @Test
    void testStudentsGenerator_throwsDomainException() {
        DomainException ex = assertThrows(DomainException.class, () -> new StudentsGenerator("test"));
        String expectedMessage = "Property file missing";
        assertEquals(expectedMessage, ex.getMessage());
    }

    @Test
    void testStudentsGenerator_thatListSizeEqualsToDefined() {
        int actual = studentsList.size();
        int expected = STUDENTS_QUANTITY;
        assertEquals(expected, actual);
    }

    @Test
    void testStudentsGenerator_thatAllStudentsHaveNotNullFirstName() {
        studentsList.stream().map(s -> s.getFirstname()).forEach(name -> assertNotNull(name));

    }

    @Test
    void testStudentsGenerator_thatAllStudentsHaveNotNullLastName() {
        studentsList.stream().map(s -> s.getLastname()).forEach(lastName -> assertNotNull(lastName));

    }

    @Test
    void testStudentsGenerator_thatStudentsListContainsAllDefinedNameFilds() {
        List<String> actual = studentsList.stream().map(s -> s.getFirstname()).collect(Collectors.toList());
        assertTrue(actual.containsAll(NAME));
    }

    @Test
    void testStudentsGenerator_thatStudentsListContainsAllDefinedLastNameFilds() {
        List<String> actual = studentsList.stream().map(s -> s.getLastname()).collect(Collectors.toList());
        assertTrue(actual.containsAll(LASTNAME));

    }

    @Test
    void testStudentsGenerator_thatCourseListNotNull() {
        assertNotNull(studentsList);
    }

    @Test
    void testStudentsGenerator_thatCourseListNotEmpty() {
        assertFalse(studentsList.isEmpty());
    }

    @Test
    void testStudentsGenerator_thatListDoestCantainGroupsWithLessTenMoreThanThirtyStudents() {
        AtomicInteger val = new AtomicInteger(0);
        boolean counter = true;
        while (counter) {
            int count = val.incrementAndGet();
            if (count < 10) {
                int groupSize = studentsList.stream().filter(i -> i.getGroupId() == count).toArray().length;
                assertTrue(groupSize >= 10 && groupSize <= 30);
            } else {
                counter = false;
            }
        }
    }

    @Test
    void testStudentsGenerator_thatAllStudentsHaveNotZeroGroupId() {
        studentsList.stream().map(s -> s.getGroupId()).forEach(id -> assertNotEquals(0, id.intValue()));

    }

    @Test
    void testStudentsGenerator_thatAnyStudentHaveNoGroupOption() {
        assertAll(() -> studentsList.stream().map(s -> s.getGroupId()).limit(NO_GROUP_OPTION));

    }

}
