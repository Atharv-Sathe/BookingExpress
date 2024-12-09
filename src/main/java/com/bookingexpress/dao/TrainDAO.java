package com.bookingexpress.dao;

import com.bookingexpress.models.Train;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TrainDAO {
    private Connection connection;

    public TrainDAO(Connection connection) {
        this.connection = connection;
    }

    // Add a new train
    public boolean addTrain(Train train) {
        String query = "INSERT INTO Trains (trainNo, bogeys, max_capacity, available_seats, route, train_status) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, train.getTrainNo());
            pstmt.setInt(2, train.getBogeys());
            pstmt.setInt(3, train.getMaxCapacity());
            pstmt.setInt(4, train.getAvailableSeats());
            pstmt.setString(5, train.getRoute());
            pstmt.setString(6, train.getTrainStatus());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get train by train number
    public Train getTrainByNumber(String trainNo) {
        String query = "SELECT * FROM Trains WHERE trainNo = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, trainNo);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Train train = new Train();
                    train.setTrainNo(rs.getString("trainNo"));
                    train.setBogeys(rs.getInt("bogeys"));
                    train.setMaxCapacity(rs.getInt("max_capacity"));
                    train.setAvailableSeats(rs.getInt("available_seats"));
                    train.setRoute(rs.getString("route"));
                    train.setTrainStatus(rs.getString("train_status"));
                    return train;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Update train details
    public boolean updateTrain(Train train) {
        String query = "UPDATE Trains SET bogeys = ?, max_capacity = ?, available_seats = ?, route = ?, train_status = ? WHERE trainNo = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, train.getBogeys());
            pstmt.setInt(2, train.getMaxCapacity());
            pstmt.setInt(3, train.getAvailableSeats());
            pstmt.setString(4, train.getRoute());
            pstmt.setString(5, train.getTrainStatus());
            pstmt.setString(6, train.getTrainNo());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Delete a train
    public boolean deleteTrain(String trainNo) {
        String query = "DELETE FROM Trains WHERE trainNo = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, trainNo);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get all trains
    public List<Train> getAllTrains() {
        List<Train> trains = new ArrayList<>();
        String query = "SELECT * FROM Trains";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Train train = new Train();
                train.setTrainNo(rs.getString("trainNo"));
                train.setBogeys(rs.getInt("bogeys"));
                train.setMaxCapacity(rs.getInt("max_capacity"));
                train.setAvailableSeats(rs.getInt("available_seats"));
                train.setRoute(rs.getString("route"));
                train.setTrainStatus(rs.getString("train_status"));
                trains.add(train);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return trains;
    }
}