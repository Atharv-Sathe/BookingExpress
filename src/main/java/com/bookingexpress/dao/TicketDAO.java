package com.bookingexpress.dao;

import com.bookingexpress.models.Ticket;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TicketDAO {
    private Connection connection;

    public TicketDAO(Connection connection) {
        this.connection = connection;
    }

    // Book a new ticket
    public boolean bookTicket(Ticket ticket) {
        String query = "INSERT INTO Tickets (pnr, username, trainNo, ticket_status) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, ticket.getPnr());
            pstmt.setString(2, ticket.getUsername());
            pstmt.setString(3, ticket.getTrainNo());
            pstmt.setString(4, ticket.getTicketStatus());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get ticket by PNR
    public Ticket getTicketByPNR(String pnr) {
        String query = "SELECT * FROM Tickets WHERE pnr = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, pnr);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Ticket ticket = new Ticket();
                    ticket.setPnr(rs.getString("pnr"));
                    ticket.setUsername(rs.getString("username"));
                    ticket.setTrainNo(rs.getString("trainNo"));
                    ticket.setTicketStatus(rs.getString("ticket_status"));
                    return ticket;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Get tickets by username
    public List<Ticket> getTicketByUsername(String username) {
        List<Ticket> tickets = new ArrayList<>();
        String query = "SELECT * FROM Tickets WHERE username = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, username);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Ticket ticket = new Ticket();
                    ticket.setPnr(rs.getString("pnr"));
                    ticket.setUsername(rs.getString("username"));
                    ticket.setTrainNo(rs.getString("trainNo"));
                    ticket.setTicketStatus(rs.getString("ticket_status"));
                    tickets.add(ticket);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tickets;
    }

    // Update ticket status
    public boolean updateTicketStatus(String pnr, String newStatus) {
        String query = "UPDATE Tickets SET ticket_status = ? WHERE pnr = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, newStatus);
            pstmt.setString(2, pnr);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Cancel ticket
    public boolean cancelTicket(String pnr) {
        String query = "DELETE FROM Tickets WHERE pnr = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, pnr);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}