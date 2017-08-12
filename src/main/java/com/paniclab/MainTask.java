package com.paniclab;


import java.io.Serializable;
import java.sql.*;
import java.util.Objects;
import java.util.logging.Logger;

public class MainTask implements Serializable {
    private int number;
    private String url;
    private Logger logger = Logger.getAnonymousLogger();

    public MainTask() {}

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, url);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (this == obj) return true;
        if (this.hashCode() != obj.hashCode()) return false;
        if (!(this.getClass().equals(obj.getClass()))) return false;
        MainTask other = MainTask.class.cast(obj);
        return (this.number == other.number) && (this.url.equals(other.url));
    }

    @Override
    public String toString() {
        return "Объект " + getClass().getCanonicalName() + " @" + hashCode() + "." + System.lineSeparator()
                + "Число N = " + number + ". Url = " + url;
    }


    public void execute() {
        populateDb();
        int[] entries = getData();
        XMLTask xmlTask = new XMLTask(entries);
        xmlTask.run();
    }


    private void populateDb() {
        try {
            Class.forName("org.h2.Driver");
            logger.info("Driver successfully loaded");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try (Connection connection = DriverManager.getConnection(url)) {
            String sql;
            Statement statement;

            sql = "DROP TABLE IF EXISTS TEST";
            statement = connection.createStatement();
            statement.executeUpdate(sql);
            logger.info("Table TEST successfully deleted");

            sql = "CREATE TABLE TEST(id BIGINT AUTO_INCREMENT, N INTEGER NOT NULL)";
            statement = connection.createStatement();
            statement.executeUpdate(sql);
            logger.info("Table TEST successfully created");

            sql = "INSERT INTO TEST (N) VALUES (?)";
            PreparedStatement ps = connection.prepareStatement(sql);
            logger.info("Table TEST primary key constraint successfully added");

            for(int i = 1; i <= number; i++) {
                ps.setInt(1, i);
                ps.addBatch();
            }

            connection.setAutoCommit(false);
            try {
                ps.executeBatch();
                connection.commit();
                logger.info("Table TEST successfully populated");
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


    private int[] getData() {
        int[] result;

        try {
            Class.forName("org.h2.Driver");
            logger.info("Driver successfully loaded");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try (Connection connection = DriverManager.getConnection(url)){
            String sql;
            PreparedStatement statement;

            sql = "SELECT N FROM TEST";
            statement = connection.prepareStatement(sql,
                                                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                                                    ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = statement.executeQuery();
            rs.last();
            final int NUMBER_OF_ENTRIES = rs.getRow();
            result = new int[NUMBER_OF_ENTRIES];
            logger.info("Number of entires: " + NUMBER_OF_ENTRIES);

            rs.beforeFirst();
            for(int i = 0; i < NUMBER_OF_ENTRIES; i++) {
                rs.next();
                result[i] = rs.getInt("N");
            }
            rs.close();
            statement.close();

            logger.info("Data retrieved successfully. Number of results: " + result.length);
            return result;

        } catch (SQLException e) {
            processSQLException(e);
        }

        logger.warning("Has no data.");
        return new int[0];
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
}
