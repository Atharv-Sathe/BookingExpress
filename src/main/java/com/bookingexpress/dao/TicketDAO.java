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
        String query = "INSERT INTO Tickets (pnr, username, trainNo, ticket_status, userMobile, passenger1, passenger2, passenger3, passenger4, dateOfDeparture) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, ticket.getPnr());
            pstmt.setString(2, ticket.getUsername());
            pstmt.setString(3, ticket.getTrainNo());
            pstmt.setString(4, ticket.getTicketStatus());
            pstmt.setString(5, ticket.getUserMobile());
            pstmt.setString(6, ticket.getPassenger1());
            pstmt.setString(7, ticket.getPassenger2());
            pstmt.setString(8, ticket.getPassenger3());
            pstmt.setString(9, ticket.getPassenger4());
            pstmt.setString(10, ticket.getDateOfDeparture());


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
                    ticket.setUserMobile(rs.getString("userMobile"));
                    ticket.setPassenger1(rs.getString("passenger1"));
                    ticket.setPassenger2(rs.getString("passenger2"));
                    ticket.setPassenger3(rs.getString("passenger3"));
                    ticket.setPassenger4(rs.getString("passenger4"));
                    ticket.setDateOfDeparture(rs.getString("dateOfDeparture"));
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
                System.out.println("Getting ticket by username: " + username);
                while (rs.next()) {
                    Ticket ticket = new Ticket();
                    ticket.setPnr(rs.getString("pnr"));
                    ticket.setUsername(rs.getString("username"));
                    ticket.setTrainNo(rs.getString("trainNo"));
                    ticket.setTicketStatus(rs.getString("ticket_status"));
                    ticket.setUserMobile(rs.getString("userMobile"));
                    ticket.setPassenger1(rs.getString("passenger1"));
                    ticket.setPassenger2(rs.getString("passenger2"));
                    ticket.setPassenger3(rs.getString("passenger3"));
                    ticket.setPassenger4(rs.getString("passenger4"));
                    ticket.setDateOfDeparture(rs.getString("dateOfDeparture"));
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
    // To get the passenger count
    public int getPassengerCount(String pnr) {
        int count = 0;
        String query = "SELECT passenger1, passenger2, passenger3, passenger4 FROM Tickets WHERE pnr = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, pnr);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    System.out.println(rs.getString("passenger1"));
                    if (!rs.getString("passenger1").isEmpty()) count++;
                    System.out.println(rs.getString("passenger2"));
                    if (!rs.getString("passenger2").isEmpty()) count++;
                    System.out.println(rs.getString("passenger3"));
                    if (!rs.getString("passenger3").isEmpty()) count++;
                    System.out.println(rs.getString("passenger4"));
                    if (!rs.getString("passenger4").isEmpty()) count++;
                }
            }
            return count;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
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