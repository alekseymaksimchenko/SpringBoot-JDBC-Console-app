package com.foxminded.schoolapp.dao.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import com.foxminded.schoolapp.dao.entity.GroupEntity;
import com.foxminded.schoolapp.exception.DaoException;

@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = { "/clear_tables.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class GroupJdbcDaoTest extends BasicJdbcDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private GroupJdbcDao groupJdbcDao;
    private GroupEntity testGroupEntity;

    @BeforeEach
    void setUp() {
        groupJdbcDao = new GroupJdbcDao(jdbcTemplate);
        testGroupEntity = new GroupEntity("testName");
    }

    @Test
    void testGroupJdbcDao_ShouldSave() {
        groupJdbcDao.save(testGroupEntity);

        int expected = 1;
        int actual = groupJdbcDao.getAll().size();
        assertEquals(expected, actual);
    }

    @Test
    void testGroupJdbcDao_ShouldReturnDaoException_inCaseOfNotSaved() {
        jdbcTemplate = Mockito.mock(JdbcTemplate.class);
        when(jdbcTemplate.update(anyString(), any(), any())).thenReturn(0);
        groupJdbcDao = new GroupJdbcDao(jdbcTemplate);

        Exception exception = assertThrows(DaoException.class, () -> {
            groupJdbcDao.save(testGroupEntity);
        });

        String actual = exception.getMessage();
        String expected = NOT_SUCCESSFUL_OPERATION;
        assertEquals(expected, actual);
    }

    @Test
    void testGroupJdbcDao_ShouldFindByIdEntry() {
        groupJdbcDao.save(testGroupEntity);

        String expectedName = "testName";
        GroupEntity actual = groupJdbcDao.getByID(1);

        assertEquals(expectedName, actual.getName());
    }

    @Test
    void testGroupJdbcDao_ShouldReturnDaoException_inCaseOfNotFoundId() {
        Exception exception = assertThrows(DaoException.class, () -> groupJdbcDao.getByID(0));

        String actual = exception.getMessage();
        String expected = GET_BY_ID_EXCEPTION;
        assertEquals(expected, actual);
    }

    @Test
    void testGroupJdbcDao_ShouldFindAllEntry() {
        groupJdbcDao.save(testGroupEntity);
        groupJdbcDao.save(testGroupEntity);
        groupJdbcDao.save(testGroupEntity);

        int expected = 3;
        int actual = groupJdbcDao.getAll().size();

        assertEquals(expected, actual);
    }

    @Test
    void testGroupJdbcDao_ShouldReturnDaoException_inCaseOfEmptyTable() {
        Exception exception = assertThrows(DaoException.class, () -> {
            groupJdbcDao.getAll();
            });

        String actual = exception.getMessage();
        String expected = IS_EMPTY;
        assertEquals(expected, actual);
    }

    @Test
    void testGroupJdbcDao_ShouldUpdateEntry() {
        groupJdbcDao.save(testGroupEntity);
        GroupEntity updatedEntiry = new GroupEntity(1, "Updated Bio");

        groupJdbcDao.update(updatedEntiry);

        String expectedName = "Updated Bio";
        GroupEntity actual = groupJdbcDao.getByID(1);

        assertEquals(expectedName, actual.getName());
    }

    @Test
    void testGroupJdbcDao_ShouldReturnDaoException_inCaseOfNotUpdated() {
        Exception exception = assertThrows(DaoException.class, () -> {
            groupJdbcDao.update(testGroupEntity);
        });

        String actual = exception.getMessage();
        String expected = NOT_SUCCESSFUL_OPERATION;
        assertEquals(expected, actual);
    }

    @Test
    void testGroupJdbcDao_ShouldDeleteEntry() {
        groupJdbcDao.save(testGroupEntity);
        groupJdbcDao.save(testGroupEntity);
        groupJdbcDao.save(testGroupEntity);
        int expected = groupJdbcDao.getAll().size() - 1;

        groupJdbcDao.deleteById(1);
        int actual = groupJdbcDao.getAll().size();

        assertEquals(expected, actual);
    }

    @Test
    void testGroupJdbcDao_ShouldReturnDaoException_inCaseOfNotDeleted() {
        Exception exception = assertThrows(DaoException.class, () -> {
            groupJdbcDao.deleteById(1);
        });

        String actual = exception.getMessage();
        String expected = NOT_SUCCESSFUL_OPERATION;
        assertEquals(expected, actual);
    }

    @Sql(statements = "insert into school.groups (name) values ('group1'), ('group2');"
            + "insert into school.students (firstname, lastname, group_id) values ('test1', 'test1', 1), ('test1', 'test1', 1), ('test2', 'test2', 2);")
    @Test
    void testGroupJdbcDao_ShouldFindGroupsByStudentCount() {
        String expectedGroupName = "group2";

        groupJdbcDao.getAllGroupsAccordingStudentCount(1)
                .forEach(entry -> assertEquals(expectedGroupName, entry.getName()));
    }

    @Test
    void testGroupJdbcDao_ShouldReturnDaoException_inCaseOfNoGroupList() {
        Exception exception = assertThrows(DaoException.class, () -> {
            groupJdbcDao.getAllGroupsAccordingStudentCount(0);
            });

        String actual = exception.getMessage();
        String expected = IS_EMPTY;
        assertEquals(expected, actual);
    }

}
