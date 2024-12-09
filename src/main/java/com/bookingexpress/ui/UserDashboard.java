package com.bookingexpress.ui;

import com.bookingexpress.models.User;
import javax.swing.*;
import java.awt.*;
import java.util.Calendar;
import org.jdesktop.swingx.JXDatePicker;
import java.text.SimpleDateFormat;

public class UserDashboard extends JFrame {
    private User currentUser;

    public UserDashboard(User user) {
        this.currentUser = user;
        initComponents();
    }

    private void initComponents() {
        setTitle("BookingExpress - User Dashboard");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create a tabbed pane
        JTabbedPane tabbedPane = new JTabbedPane();

        // Book Tickets Tab
        JPanel bookTicketsPanel = createBookTicketsPanel();
        tabbedPane.addTab("Book Tickets", bookTicketsPanel);

        // Your Tickets Tab
        JPanel yourTicketsPanel = createYourTicketsPanel();
        tabbedPane.addTab("Your Tickets", yourTicketsPanel);

        // Transactions Tab
        JPanel transactionsPanel = createTransactionsPanel();
        tabbedPane.addTab("Transactions", transactionsPanel);

        // Add logout button
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> logout());

        // Layout
        setLayout(new BorderLayout());
        add(tabbedPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(new JLabel("Welcome, " + currentUser.getUsername()));
        bottomPanel.add(logoutButton);
        add(bottomPanel, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
    }

    private JPanel createBookTicketsPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Train Search Section
        JPanel searchPanel = new JPanel(new FlowLayout());
        JTextField trainSearchField = new JTextField(20);
        JButton searchButton = new JButton("Search Trains");
        searchPanel.add(new JLabel("From:"));
        searchPanel.add(new JTextField(10));
        searchPanel.add(new JLabel("To:"));
        searchPanel.add(new JTextField(10));

        JXDatePicker picker = new JXDatePicker();
        picker.setDate(Calendar.getInstance().getTime());
        picker.setFormats(new SimpleDateFormat("dd/MM/yyyy"));
        searchPanel.add(picker);

        searchPanel.add(searchButton);

        // Available Trains
        JTable trainsTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(trainsTable);

        panel.add(searchPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createYourTicketsPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Tickets Table
        JTable ticketsTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(ticketsTable);

        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createTransactionsPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Transactions Table
        JTable transactionsTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(transactionsTable);

        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void logout() {
        // Return to login screen
        new LoginRegistrationFrame().setVisible(true);
        dispose();
    }
}