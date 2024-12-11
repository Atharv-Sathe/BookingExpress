package com.bookingexpress.models;

public class Train {
    private int id;
    private String trainNo;
    private int bogeys;
    private int maxCapacity;
    private int availableSeats;
    private String route;
    private String trainStatus;
    private int costPerSeat;

    // Default constructor
    public Train() {}

    // Parameterized constructor
    public Train(int id, String trainNo, int bogeys, int maxCapacity,
                 int availableSeats, String route, String trainStatus, int costPerSeat) {
        this.id = id;
        this.trainNo = trainNo;
        this.bogeys = bogeys;
        this.maxCapacity = maxCapacity;
        this.availableSeats = availableSeats;
        this.route = route;
        this.trainStatus = trainStatus;
        this.costPerSeat = costPerSeat;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTrainNo() {
        return trainNo;
    }

    public void setTrainNo(String trainNo) {
        this.trainNo = trainNo;
    }

    public int getBogeys() {
        return bogeys;
    }

    public void setBogeys(int bogeys) {
        this.bogeys = bogeys;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getTrainStatus() {
        return trainStatus;
    }

    public void setTrainStatus(String trainStatus) {
        this.trainStatus = trainStatus;
    }

    public int getCostPerSeat() {
        return costPerSeat;
    }

    public void setCostPerSeat(int costPerSeat) {
        this.costPerSeat = costPerSeat;
    }

    @Override
    public String toString() {
        return "Train{" +
                "trainNo='" + trainNo + '\'' +
                ", route='" + route + '\'' +
                ", availableSeats=" + availableSeats +
                '}';
    }
}