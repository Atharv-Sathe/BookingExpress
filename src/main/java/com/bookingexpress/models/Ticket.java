package com.bookingexpress.models;

public class Ticket {
    private int id;
    private String pnr;
    private String trainNo;
    private String ticketStatus;
    private String username;
    private String userMobile;
    private String passenger1;
    private String passenger2;
    private String passenger3;
    private String passenger4;
    private String dateOfDeparture;

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

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    public String getPassenger1() {
        return passenger1;
    }

    public void setPassenger1(String passenger1) {
        this.passenger1 = passenger1;
    }

    public String getPassenger2() {
        return passenger2;
    }

    public void setPassenger2(String passenger2) {
        this.passenger2 = passenger2;
    }

    public String getPassenger3() {
        return passenger3;
    }

    public void setPassenger3(String passenger3) {
        this.passenger3 = passenger3;
    }

    public String getPassenger4() {
        return passenger4;
    }

    public void setPassenger4(String passenger4) {
        this.passenger4 = passenger4;
    }

    public String getDateOfDeparture() {
        return dateOfDeparture;
    }

    public void setDateOfDeparture(String dateOfDeparture) {
        this.dateOfDeparture = dateOfDeparture;
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