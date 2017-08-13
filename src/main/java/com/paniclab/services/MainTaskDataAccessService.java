package com.paniclab.services;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

public class MainTaskDataAccessService implements TaskService<Integer> {
    private final String URL;
    private static final Logger LOGGER = Logger.getAnonymousLogger();

    public MainTaskDataAccessService(String url) {
        URL = url;
    }


    @Override
    public void populate(TaskData<Integer> taskData) {
        try {
            Class.forName("org.h2.Driver");
            LOGGER.info("Driver successfully loaded");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try (Connection connection = DriverManager.getConnection(URL)) {
            String sql;
            Statement statement;

            sql = "DROP TABLE IF EXISTS TEST";
            statement = connection.createStatement();
            statement.executeUpdate(sql);
            LOGGER.info("Table TEST successfully deleted");

            sql = "CREATE TABLE TEST(id BIGINT AUTO_INCREMENT, N INTEGER NOT NULL)";
            statement = connection.createStatement();
            statement.executeUpdate(sql);
            LOGGER.info("Table TEST successfully created");

            sql = "INSERT INTO TEST (N) VALUES (?)";
            PreparedStatement ps = connection.prepareStatement(sql);
            LOGGER.info("Table TEST primary key constraint successfully added");

            LOGGER.info("Data access service begin to populate data...");
            for (int i : taskData.getData()) {
                ps.setInt(1, i);
                ps.addBatch();
            }

            connection.setAutoCommit(false);
            try {
                ps.executeBatch();
                connection.commit();
                LOGGER.info("Table TEST successfully populated");
            } catch (BatchUpdateException bue) {
                connection.rollback();
                bue.printStackTrace();
            } finally {
                connection.setAutoCommit(true);
                statement.close();
            }

        } catch (SQLException e) {
            processSQLException(e);
        }
    }


    private void processSQLException(SQLException e) {
        e.printStackTrace(System.err);
        System.err.println("SQLState: " + e.getSQLState());

        System.err.println("Error Code: " + e.getErrorCode());

        System.err.println("Message: " + e.getMessage());

        Throwable t = e.getCause();
        while (t != null) {
            System.out.println("Cause: " + t);
            t = t.getCause();
        }
    }


    @Override
    public TaskData<Integer> loadPersisted() {
        List<Integer> resultList;

        try {
            Class.forName("org.h2.Driver");
            LOGGER.info("Driver successfully loaded");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try (Connection connection = DriverManager.getConnection(URL)){
            String sql;
            PreparedStatement statement;

            sql = "SELECT N FROM TEST";
            statement = connection.prepareStatement(sql,
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = statement.executeQuery();
            rs.last();
            final int NUMBER_OF_ENTRIES = rs.getRow();
            resultList = new ArrayList<>(NUMBER_OF_ENTRIES);
            LOGGER.info("Number of entries: " + NUMBER_OF_ENTRIES);

            rs.beforeFirst();
            for(int i = 0; i < NUMBER_OF_ENTRIES; i++) {
                rs.next();
                resultList.add(rs.getInt("N"));
            }
            rs.close();
            statement.close();

            LOGGER.info("Data retrieved successfully. Number of results: " + resultList.size());
            return TaskData.get(resultList);

        } catch (SQLException e) {
            processSQLException(e);
        }

        LOGGER.warning("Has no data.");
        return TaskData.get(Collections.emptyList());
    }
}
