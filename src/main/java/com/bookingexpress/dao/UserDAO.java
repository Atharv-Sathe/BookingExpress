package com.bookingexpress.dao;

import com.bookingexpress.models.User;
import java.sql.*;

public class UserDAO {
    // Method to validate user
    public User validateUser(String username, String password) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseUtil.getConnection();
            String query = "SELECT * FROM Users WHERE username = ? AND password = ?";
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, username);
            pstmt.setString(2, password);

            rs = pstmt.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setRole(rs.getString("role"));
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Use the utility method to close all resources
            DatabaseUtil.closeResources(conn, pstmt, rs);
        }
        return null;
    }

    // Similar pattern for other methods (registerUser, isUserExists, etc.)
    public boolean registerUser(User user) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DatabaseUtil.getConnection();
            String query = "INSERT INTO Users (username, password, role) VALUES (?, ?, ?)";
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getRole() != null ? user.getRole() : "user");

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            // Use the utility method to close all resources
            DatabaseUtil.closeResources(conn, pstmt, null);
        }
    }

    // Check if username already exists
    public boolean isUserExists(String username) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DatabaseUtil.getConnection();
            String query = "SELECT COUNT(*) FROM users WHERE username = ?";
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, username);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Use the utility method to close all resources
            DatabaseUtil.closeResources(conn, pstmt, null);
        }
        return false;
    }

    // Get user details by username
    public User getUserByUsername(String username) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DatabaseUtil.getConnection();
            String query = "SELECT * FROM users WHERE username = ?";
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, username);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setId(rs.getInt("id"));
                    user.setUsername(rs.getString("username"));
                    user.setPassword(rs.getString("password"));
                    return user;
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Use the utility method to close all resources
            DatabaseUtil.closeResources(conn, pstmt, null);
        }
        return null;
    }
}