package com.foxminded.schoolapp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.foxminded.schoolapp.dao.entity.GroupEntity;
import com.foxminded.schoolapp.dao.entity.StudentEntity;
import com.foxminded.schoolapp.exception.ServiceException;
import com.foxminded.schoolapp.service.impl.CourseService;
import com.foxminded.schoolapp.service.impl.GroupService;
import com.foxminded.schoolapp.service.impl.StudentService;

@Component
public class SchoolappMenu {

    private static final Logger LOGGER = LoggerFactory.getLogger(SchoolappMenu.class);
    private final GroupService groupService;
    private final StudentService studentService;
    private final CourseService courseService;
    private static final List<String> OPTIONS_LIST = new ArrayList<>(
            Arrays.asList("1- Find all the groups with less or equal student count.",
                    "2- Find all the students related to the course with the given name.", "3- Add a new student.",
                    "4- Delete student by STUDENT_ID.", "5- Add a student to the course (from the list).",
                    "6- Remove the student from one of their courses.", "7- Exit."));
    private Scanner scanner = new Scanner(System.in);
    private static final int ARRAY_SHIFT = 1;
    private static final String LOGGER_START_MESSAGE = "Starting option {}.";
    private static final String LOGGER_END_MESSAGE = "Option {} fulfilled.";

    public SchoolappMenu(GroupService groupService, StudentService studentService, CourseService courseService) {
        this.groupService = groupService;
        this.studentService = studentService;
        this.courseService = courseService;
    }

    @PostConstruct
    public void runMenu() {
        try {
            if (!courseService.getAll().isEmpty()) {
                LOGGER.info("Tables contain data. Generate option is skipped.");
            }
        } catch (ServiceException e) {
            LOGGER.error("Tables are empty. Start to generate Data.");
            generateData();
            LOGGER.info("Data were generated.");
        }
        int option = 1;
        loop: while (option != OPTIONS_LIST.size()) {
            printMenu(OPTIONS_LIST);
            option = scanner.nextInt();
            if (option <= 0 && option > OPTIONS_LIST.size()) {
                LOGGER.error("Please enter an integer value between 1 and {}.", OPTIONS_LIST.size());
                scanner.next();
            }
            try {
                switch (option) {
                case 1:
                    getAllGroupsAccordingStudentCount(option);
                    break;
                case 2:
                    findAllStudentsRelatedToCourse(option);
                    break;
                case 3:
                    saveStudent(option);
                    break;
                case 4:
                    deleteStudentById(option);
                    break;
                case 5:
                    addStudentToCourse(option);
                    break;
                case 6:
                    removeStudentByIDFromCourse(option);
                    break;
                case 7:
                    doExit(option);
                    break loop;
                }
            } catch (ServiceException e) {
                LOGGER.error("Operation is failed due to {}", e.getMessage());
            }
        }
    }

    private void generateData() {
        courseService.populate();
        groupService.populate();
        studentService.populate();
        studentService.populateStudentsToCourses();
    }

    private void printMenu(List<String> options) {
        options.forEach(s -> System.out.println(s));
    }

    private void getAllGroupsAccordingStudentCount(int option) {
        LOGGER.info(LOGGER_START_MESSAGE, OPTIONS_LIST.get(option - ARRAY_SHIFT));
        System.out.println("Please enter an integer value of Students in a range (from 10 to 30).");
        Integer studentCount = Integer.valueOf(scanner.next().trim());
        List<GroupEntity> group = groupService.getAllGroupsAccordingStudentCount(studentCount);
        System.out.println(group);
        LOGGER.info(LOGGER_END_MESSAGE, option);
    }

    private void findAllStudentsRelatedToCourse(int option) {
        LOGGER.info(LOGGER_START_MESSAGE, OPTIONS_LIST.get(option - ARRAY_SHIFT));
        System.out.println(courseService.getAll());
        System.out.println("Please enter an Id of Course from above List.");
        Integer courseId = Integer.valueOf(scanner.next().trim());
        System.out.println(studentService.findAllStudentsRelatedToCourse(courseId));
        LOGGER.info(LOGGER_END_MESSAGE, option);
    }

    private void saveStudent(int option) {
        LOGGER.info(LOGGER_START_MESSAGE, OPTIONS_LIST.get(option - ARRAY_SHIFT));
        StudentEntity newStudent = new StudentEntity();
        System.out.println("Please enter student Firstname.");

        Optional<String> newStudentFirstname = Optional.ofNullable(scanner.next().trim());
        if (!newStudentFirstname.isPresent()) {
            System.out.println("Value can`t be null. Please enter student Firstname.");
            scanner.next().trim();
        }

        System.out.println("Please enter student Lastname.");
        Optional<String> newStudentLastname = Optional.ofNullable(scanner.next().trim());
        if (!newStudentLastname.isPresent()) {
            System.out.println("Value can`t be null. Please enter student Lastname.");
            scanner.next().trim();
        }

        System.out.println("Please enter group Id in a range (from 1 to 10)");
        Integer groupId = Integer.valueOf(scanner.next().trim());
        if (groupId < 0 && groupId > 10) {
            System.out.println("Group Id is out of range. Please enter group Id in a range (from 1 to 10)");
            scanner.next().trim();
        }

        newStudent.setFirstname(newStudentFirstname.get());
        newStudent.setLastname(newStudentLastname.get());
        newStudent.setGroupId(groupId);
        studentService.save(newStudent);

        LOGGER.info(LOGGER_END_MESSAGE, option);
    }

    private void deleteStudentById(int option) {
        LOGGER.info(LOGGER_START_MESSAGE, OPTIONS_LIST.get(option - ARRAY_SHIFT));
        System.out.println("Please enter an Id of Strudent than should be deleted.");
        Integer studentId = Integer.valueOf(scanner.next().trim());
        StudentEntity deletedStudent = studentService.getByID(studentId);
        studentService.deleteById(studentId);
        System.out.println("Next student was deleted:" + deletedStudent);
        LOGGER.info(LOGGER_END_MESSAGE, option);
    }

    private void addStudentToCourse(int option) {
        LOGGER.info(LOGGER_START_MESSAGE, OPTIONS_LIST.get(option - ARRAY_SHIFT));
        System.out.println("Please enter an Id of Strudent.");
        Integer studentId = Integer.valueOf(scanner.next().trim());
        System.out.println(courseService.getAll());
        System.out.println("Please enter an Id of Course from the list above.");
        Integer courseId = Integer.valueOf(scanner.next().trim());
        if (courseId < 0 && courseId > courseService.getAll().size()) {
            System.out.println("Course Id is not exist. Please enter course Id from the list.");
            scanner.next().trim();
        }

        Optional<Integer> check = studentService.findAllStudentsRelatedToCourse(courseId).stream().map(s -> s.getId())
                .filter(s -> s.equals(studentId)).findFirst();
        if (check.isPresent()) {
            System.out.println("Student under provided id has already added to this course.");
        }
        studentService.addStudentToCourse(studentService.getByID(studentId), courseId);
        LOGGER.info(LOGGER_END_MESSAGE, option);
    }

    private void removeStudentByIDFromCourse(int option) {
        LOGGER.info(LOGGER_START_MESSAGE, OPTIONS_LIST.get(option - ARRAY_SHIFT));
        System.out.println("Please enter an Id of Strudent.");
        Integer studentId = Integer.valueOf(scanner.next().trim());

        System.out.println(courseService.getAll());
        System.out.println("Please enter an Id of Course from the list above.");
        Integer courseId = Integer.valueOf(scanner.next().trim());

        Optional<Integer> check = studentService.findAllStudentsRelatedToCourse(courseId).stream().map(s -> s.getId())
                .filter(s -> s.equals(studentId)).findFirst();
        if (!check.isPresent()) {
            System.out.println("Student under provided id is not attend this course");
        }
        studentService.removeStudentByIDFromCourse(studentId, courseId);
        LOGGER.info(LOGGER_END_MESSAGE, option);
    }

    private void doExit(int option) {
        LOGGER.info(LOGGER_START_MESSAGE, OPTIONS_LIST.get(option - ARRAY_SHIFT));
        System.out.println("Shutdown completed");
    }

}
