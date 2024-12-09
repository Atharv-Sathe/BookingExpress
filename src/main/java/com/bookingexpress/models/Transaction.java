package com.bookingexpress.models;

import java.time.LocalDateTime;

public class Transaction {
    private int id;
    private String transactionId;
    private String pnr;
    private String username;
    private LocalDateTime dateTime;
    private double amount;

    // Default constructor
    public Transaction() {}

    // Parameterized constructor
    public Transaction(int id, String transactionId, String pnr,
                       String username, LocalDateTime dateTime, double amount) {
        this.id = id;
        this.transactionId = transactionId;
        this.pnr = pnr;
        this.username = username;
        this.dateTime = dateTime;
        this.amount = amount;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getPnr() {
        return pnr;
    }

    public void setPnr(String pnr) {
        this.pnr = pnr;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "transactionId='" + transactionId + '\'' +
                ", username='" + username + '\'' +
                ", amount=" + amount +
                ", dateTime=" + dateTime +
                '}';
    }
}
