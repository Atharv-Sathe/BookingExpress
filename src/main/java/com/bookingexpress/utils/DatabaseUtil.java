package com.bookingexpress.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DatabaseUtil {
    private static Connection connection = null;

    // Private constructor to prevent instantiation
    private DatabaseUtil() {}

    public static Connection getConnection() {
        if (connection != null && isConnectionValid()) {
            return connection;
        }

        try {
            // Load properties from resources
            Properties props = new Properties();
            try (InputStream input = DatabaseUtil.class
                    .getClassLoader()
                    .getResourceAsStream("database.properties")) {

                if (input == null) {
                    throw new IOException("Unable to find database.properties");
                }
                props.load(input);
            }

            // Load JDBC driver
            Class.forName(props.getProperty("db.driver"));

            // Establish connection
            connection = DriverManager.getConnection(
                    props.getProperty("db.url"),
                    props.getProperty("db.username"),
                    props.getProperty("db.password")
            );

            return connection;
        } catch (SQLException | IOException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("Could not connect to the database", e);
        }
    }

    // Check if existing connection is still valid
    private static boolean isConnectionValid() {
        try {
            return connection != null && !connection.isClosed() &&
                    connection.isValid(5); // 5-second timeout
        } catch (SQLException e) {
            return false;
        }
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null; // Ensure we can create a new connection next time
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Utility method to safely close resources
    public static void closeResources(Connection conn, PreparedStatement pstmt, ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (pstmt != null) {
            try {
                pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}