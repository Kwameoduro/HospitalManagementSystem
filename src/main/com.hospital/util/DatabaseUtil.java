package com.hospital.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Database connection utility class.
 * Provides methods to get and close database connections.
 */
public class DatabaseUtil {
    // JDBC URL, username and password of MySQL server
    private static final String JDBC_URL = "/////////////"; // JDBC url
    private static final String USERNAME = "//////////"; // My username
    private static final String PASSWORD = "///////////"; // MySQL password

    // JDBC variables for opening and managing connection
    private static Connection connection;

    /**
     * Get a database connection
     * @return a Connection object
     * @throws SQLException if a database access error occurs
     */
    public static Connection getConnection() throws SQLException {
        try {
            // Register JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Open a connection
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
                System.out.println("Database connection established successfully!");
            }
            return connection;
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found", e);
        } catch (SQLException e) {
            System.out.println("Connection failed! Check output console");
            throw e;
        }
    }

    /**
     * Close the database connection
     */
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            System.out.println("Error closing database connection: " + e.getMessage());
        }
    }
}
