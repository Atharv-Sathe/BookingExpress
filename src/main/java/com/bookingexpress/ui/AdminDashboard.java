package com.bookingexpress.ui;

import com.bookingexpress.models.User;
import javax.swing.*;
import java.awt.*;

public class AdminDashboard extends JFrame {
    private User adminUser;

    public AdminDashboard(User user) {
        this.adminUser = user;
        initComponents();
    }

    private void initComponents() {
        setTitle("BookingExpress - Admin Dashboard");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create a tabbed pane
        JTabbedPane tabbedPane = new JTabbedPane();

        // Manage Trains Tab
        JPanel manageTrainsPanel = createManageTrainsPanel();
        tabbedPane.addTab("Manage Trains", manageTrainsPanel);

        // Add more tabs as needed
        JPanel userManagementPanel = new JPanel();
        userManagementPanel.add(new JLabel("User Management Under Construction"));
        tabbedPane.addTab("User Management", userManagementPanel);

        // Add logout button
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> logout());

        // Layout
        setLayout(new BorderLayout());
        add(tabbedPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(logoutButton);
        add(bottomPanel, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
    }

    private JPanel createManageTrainsPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Train Search Section
        JPanel searchPanel = new JPanel(new FlowLayout());
        JTextField trainSearchField = new JTextField(20);
        JButton searchButton = new JButton("Search Train");
        searchPanel.add(new JLabel("Train Number:"));
        searchPanel.add(trainSearchField);
        searchPanel.add(searchButton);

        // Train Details Section
        JTextArea trainDetailsArea = new JTextArea(10, 40);
        trainDetailsArea.setEditable(false);

        // Action Buttons
        JPanel actionPanel = new JPanel();
        JButton addTrainButton = new JButton("Add Train");
        JButton editTrainButton = new JButton("Edit Train");
        JButton removeTrainButton = new JButton("Remove Train");
        actionPanel.add(addTrainButton);
        actionPanel.add(editTrainButton);
        actionPanel.add(removeTrainButton);

        panel.add(searchPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(trainDetailsArea), BorderLayout.CENTER);
        panel.add(actionPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void logout() {
        // Return to login screen
        new LoginRegistrationFrame().setVisible(true);
        dispose();
    }

    // Additional methods for train management can be added here
}