package com.bookingexpress.dao;

import com.bookingexpress.models.Transaction;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {
    private Connection connection;

    public TransactionDAO(Connection connection) {
        this.connection = connection;
    }

    // Add a new transaction
    public boolean addTransaction(Transaction transaction) {
        String query = "INSERT INTO Transactions (transactionId, pnr, username, date_time, amount) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, transaction.getTransactionId());
            pstmt.setString(2, transaction.getPnr());
            pstmt.setString(3, transaction.getUsername());
            pstmt.setTimestamp(4, Timestamp.valueOf(transaction.getDateTime()));
            pstmt.setDouble(5, transaction.getAmount());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get transaction by transaction ID
    public Transaction getTransactionById(String transactionId) {
        String query = "SELECT * FROM Transactions WHERE transactionId = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, transactionId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Transaction transaction = new Transaction();
                    transaction.setTransactionId(rs.getString("transactionId"));
                    transaction.setPnr(rs.getString("pnr"));
                    transaction.setUsername(rs.getString("username"));
                    transaction.setDateTime(rs.getTimestamp("date_time").toLocalDateTime());
                    transaction.setAmount(rs.getDouble("amount"));
                    return transaction;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Get transactions by username
    public List<Transaction> getTransactionByUsername(String username) {
        List<Transaction> transactions = new ArrayList<>();
        String query = "SELECT * FROM Transactions WHERE username = ? ORDER BY date_time DESC";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, username);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Transaction transaction = new Transaction();
                    transaction.setTransactionId(rs.getString("transactionId"));
                    transaction.setPnr(rs.getString("pnr"));
                    transaction.setUsername(rs.getString("username"));
                    transaction.setDateTime(rs.getTimestamp("date_time").toLocalDateTime());
                    transaction.setAmount(rs.getDouble("amount"));
                    transactions.add(transaction);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }

    // Get transactions by PNR
    public List<Transaction> getTransactionsByPNR(String pnr) {
        List<Transaction> transactions = new ArrayList<>();
        String query = "SELECT * FROM Transactions WHERE pnr = ? ORDER BY date_time DESC";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, pnr);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Transaction transaction = new Transaction();
                    transaction.setTransactionId(rs.getString("transactionId"));
                    transaction.setPnr(rs.getString("pnr"));
                    transaction.setUsername(rs.getString("username"));
                    transaction.setDateTime(rs.getTimestamp("date_time").toLocalDateTime());
                    transaction.setAmount(rs.getDouble("amount"));
                    transactions.add(transaction);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }
}