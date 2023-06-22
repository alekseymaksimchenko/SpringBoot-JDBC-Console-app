package com.foxminded.schoolapp.dao.impl;

import org.testcontainers.containers.PostgreSQLContainer;

public class BasicJdbcDaoTest {

    protected static final String NOT_SUCCESSFUL_OPERATION = "Operation failed from Data Base side";
    protected static final String GET_BY_ID_EXCEPTION = "Record under provided id - not exist";
    protected static final String IS_EMPTY = "Table doesn't contain any Records";

    protected static PostgreSQLContainer<?> postgresqlContainer;

    static {
        postgresqlContainer = new PostgreSQLContainer<>("postgres:latest");
        postgresqlContainer.start();
    }

}
