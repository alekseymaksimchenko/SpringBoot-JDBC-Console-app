package com.foxminded.schoolapp.student_course;

import java.util.Objects;

public class StudentCourseEntity {

    private int studentId;
    private int courseId;

    public StudentCourseEntity() {
        super();
    }

    public StudentCourseEntity(int studentId, int courseId) {
        this.studentId = studentId;
        this.courseId = courseId;
    }

    public StudentCourseEntity(int courseId) {
        this.courseId = courseId;
    }

    public int getStudentId() {
        return studentId;
    }

    public int getCourseId() {
        return courseId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(courseId, studentId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        StudentCourseEntity other = (StudentCourseEntity) obj;
        return courseId == other.courseId && studentId == other.studentId;
    }

    @Override
    public String toString() {
        return "StudentCourseEntity [studentId=" + studentId + ", courseId=" + courseId + "]";
    }

}
