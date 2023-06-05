package com.foxminded.schoolapp.group;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = { "/clear_tables.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class GroupDaoTest {

    @Container
    private static PostgreSQLContainer<?> postgresqlContainer = new PostgreSQLContainer<>("postgres:latest")
            .withReuse(true);

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private GroupDao groupDao;
    private GroupEntity testGroupEntity;

    @BeforeEach
    void setUp() {
        groupDao = new GroupDao(jdbcTemplate);
        testGroupEntity = new GroupEntity("testName");
    }

    
    @Test
    void testGroupDao_ShouldCreateEntry() {
        groupDao.save(testGroupEntity);

        int expected = 1;
        int actual = groupDao.getAll().size();
        assertEquals(expected, actual);
    }

    @Test
    void testGroupDao_ShouldFindByIdEntry() {
        groupDao.save(testGroupEntity);

        String expected = "Optional[GroupEntity [id=1, name=testName]]";
        String actual = groupDao.getByID(1).toString();

        assertEquals(expected, actual);
    }

    @Test
    void testGroupDao_ShouldFindAllEntry() {
        groupDao.save(testGroupEntity);
        groupDao.save(testGroupEntity);
        groupDao.save(testGroupEntity);

        int expected = 3;
        int actual = groupDao.getAll().size();
        assertEquals(expected, actual);
    }

    @Test
    void testGroupDao_ShouldUpdateEntry() {
        groupDao.save(testGroupEntity);

        String[] update = { "Updated Bio" };

        groupDao.update(testGroupEntity, update);

        String expected = "Optional[GroupEntity [id=1, name=testName]]";
        String actual = groupDao.getByID(1).toString();

        assertEquals(expected, actual);
    }

    @Test
    void testGroupDao_ShouldDeleteEntry() {
        groupDao.save(testGroupEntity);
        groupDao.save(testGroupEntity);
        groupDao.save(testGroupEntity);
        int expected = groupDao.getAll().size() - 1;

        groupDao.deleteById(1);
        int actual = groupDao.getAll().size();

        assertEquals(expected, actual);
    }

    @Sql(statements = "insert into school.groups (name) values ('group1'), ('group2');"
            + "insert into school.students (firstname, lastname, group_id) values ('test1', 'test1', 1), ('test1', 'test1', 1), ('test2', 'test2', 2);")
    @Test
    void testGroupDao_ShouldFindGroupsByStudentCount() {
        String expected = "[GroupEntity [id=2, name=group2]]";
        String actual = groupDao.getAllGroupsAccordingStudentCount(1).toString();

        assertEquals(expected, actual);
    }

}
