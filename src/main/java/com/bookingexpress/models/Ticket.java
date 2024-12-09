package com.bookingexpress.models;

public class Ticket {
    private int id;
    private String pnr;
    private String trainNo;
    private String ticketStatus;
    private String username;

    // Default constructor
    public Ticket() {}

    // Parameterized constructor
    public Ticket(int id, String pnr, String trainNo, String ticketStatus, String username) {
        this.id = id;
        this.pnr = pnr;
        this.trainNo = trainNo;
        this.ticketStatus = ticketStatus;
        this.username = username;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPnr() {
        return pnr;
    }

    public void setPnr(String pnr) {
        this.pnr = pnr;
    }

    public String getTrainNo() {
        return trainNo;
    }

    public void setTrainNo(String trainNo) {
        this.trainNo = trainNo;
    }

    public String getTicketStatus() {
        return ticketStatus;
    }

    public void setTicketStatus(String ticketStatus) {
        this.ticketStatus = ticketStatus;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "pnr='" + pnr + '\'' +
                ", trainNo='" + trainNo + '\'' +
                ", ticketStatus='" + ticketStatus + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}