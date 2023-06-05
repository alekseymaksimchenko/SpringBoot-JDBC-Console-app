package com.foxminded.schoolapp.student;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

public class StudentDao implements Student<StudentEntity> {

    private final JdbcTemplate jdbcTemplate;
    private static final String SQL_SAVE = "INSERT INTO school.students (firstname, lastname, group_id) VALUES(?, ?, ?);";
    private static final String SQL_GET_ALL = "SELECT id, firstname, lastname, group_id FROM school.students LIMIT 100;";
    private static final String SQL_GET_BY_ID = "SELECT id, firstname, lastname, group_id FROM school.students WHERE id=?;";
    private static final String SQL_UPDATE = "UPDATE school.students SET firstname=?, lastname=?, group_id=? WHERE id=?;";
    private static final String SQL_DELETE = "DELETE FROM school.students WHERE id=?;";

    @Autowired
    public StudentDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int save(StudentEntity student) {
        return jdbcTemplate.update(SQL_SAVE, student.getFirstname(), student.getLastname(), student.getGroupId());
    }

    @Override
    public List<StudentEntity> getAll() {
        return jdbcTemplate.query(SQL_GET_ALL, new StudentRowMapper());
    }

    @Override
    public Optional<StudentEntity> getByID(int id) {
        return jdbcTemplate.query(SQL_GET_BY_ID, new StudentRowMapper(), id).stream().findFirst();
    }

    @Override
    public int update(StudentEntity student, String[] parameters) {
        String newFirstname = 
                (parameters.length >= 1 && parameters[0] != null) ? parameters[0] : student.getFirstname();
        String newLastname = 
                (parameters.length >= 2 && parameters[1] != null) ? parameters[1] : student.getLastname();
        Integer newGroupId = 
                (parameters.length >= 3 && parameters[2] != null) ? Integer.valueOf(parameters[2]) : student.getGroupId();

        return jdbcTemplate.update(SQL_UPDATE, newFirstname, newLastname, newGroupId, student.getId());
    }

    @Override
    public int deleteById(int id) {
        return jdbcTemplate.update(SQL_DELETE, id);
    }

}
