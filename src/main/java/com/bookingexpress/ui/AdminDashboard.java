package com.bookingexpress.ui;

import com.bookingexpress.dao.TrainDAO;
import com.bookingexpress.models.Train;
import com.bookingexpress.models.User;
import com.bookingexpress.utils.DatabaseUtil;
import com.bookingexpress.utils.RouteManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

public class AdminDashboard extends JFrame {
    private User adminUser;
    private JTextField trainNoField;
    private JTextField bogeysField;
    private JTextField maxCapacityField;
    private JTextField availableSeatsField;
    private JComboBox<String> routeDropdown;
    private JComboBox<String> statusDropdown;
    private JButton searchButton;
    private JButton editButton;
    private JButton removeButton;
    private JPanel trainManagementPanel;
    private JButton addTrainButton;


    public AdminDashboard(User user) {
        this.adminUser = user;
        initComponents();
    }

    private void setupTrainManagementPanel() {
        trainManagementPanel = new JPanel(new BorderLayout(10, 10));

        // Search Section
        JPanel searchPanel = new JPanel(new FlowLayout());
        JTextField trainSearchField = new JTextField(20);
        searchButton = new JButton("Search Train");
        searchPanel.add(new JLabel("Train Number:"));
        searchPanel.add(trainSearchField);
        searchPanel.add(searchButton);

        // Train Details Form
        JPanel formPanel = createTrainDetailsForm();

        // Action Buttons
        JPanel actionPanel = new JPanel(new FlowLayout());
        addTrainButton = new JButton("Add Train");
        editButton = new JButton("Edit");
        removeButton = new JButton("Remove");
        actionPanel.add(addTrainButton);
        actionPanel.add(editButton);
        actionPanel.add(removeButton);

        // Setup search functionality
        setupTrainSearchFunctionality(trainSearchField, formPanel);

        // Setup edit and remove functionality
        setupEditRemoveAddTrainFunctionality();

        // Combine panels
        trainManagementPanel.add(searchPanel, BorderLayout.NORTH);
        trainManagementPanel.add(formPanel, BorderLayout.CENTER);
        trainManagementPanel.add(actionPanel, BorderLayout.SOUTH);

        // Initially disable edit and remove buttons
        editButton.setEnabled(false);
        removeButton.setEnabled(false);
        addTrainButton.setEnabled(true);
    }

    private JPanel createTrainDetailsForm() {
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Train Details"));

        // Train Number
        trainNoField = new JTextField();
        trainNoField.setEditable(false);
        formPanel.add(new JLabel("Train Number:"));
        formPanel.add(trainNoField);

        // Bogeys
        bogeysField = new JTextField();
        bogeysField.setEditable(false);
        formPanel.add(new JLabel("Number of Bogeys:"));
        formPanel.add(bogeysField);

        // Max Capacity
        maxCapacityField = new JTextField();
        maxCapacityField.setEditable(false);
        formPanel.add(new JLabel("Max Capacity:"));
        formPanel.add(maxCapacityField);

        // Available Seats
        availableSeatsField = new JTextField();
        availableSeatsField.setEditable(false);
        formPanel.add(new JLabel("Available Seats:"));
        formPanel.add(availableSeatsField);

        // Route Dropdown
        Map<String, String[]> routes = RouteManager.getAllRoutes();
        String[] routeOptions = routes.keySet().toArray(new String[0]);
        routeDropdown = new JComboBox<>(routeOptions);
        routeDropdown.setEnabled(false);
        formPanel.add(new JLabel("Route:"));
        formPanel.add(routeDropdown);

        // Train Status Dropdown
        String[] statusOptions = {
                "InactiveAtSrc",
                "TransitToDest",
                "TransitToSrc",
                "InactiveAtDest"
        };
        statusDropdown = new JComboBox<>(statusOptions);
        statusDropdown.setEnabled(false);
        formPanel.add(new JLabel("Train Status:"));
        formPanel.add(statusDropdown);

        return formPanel;
    }

    private void setupTrainSearchFunctionality(JTextField trainSearchField, JPanel formPanel) {
        searchButton.addActionListener(e -> {
            String trainNo = trainSearchField.getText().trim();
            if (trainNo.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Please enter a train number",
                        "Search Error",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            try (Connection connection = DatabaseUtil.getConnection()) {
                TrainDAO trainDAO = new TrainDAO(connection);
                Train train = trainDAO.getTrainByNumber(trainNo);

                if (train != null) {
                    // Populate form with train details
                    populateTrainForm(train);

                    // Enable edit and remove buttons
                    editButton.setEnabled(true);
                    removeButton.setEnabled(true);
                } else {
                    JOptionPane.showMessageDialog(this,
                            "No train found with number: " + trainNo,
                            "Search Result",
                            JOptionPane.INFORMATION_MESSAGE);

                    // Disable edit and remove buttons
                    editButton.setEnabled(false);
                    removeButton.setEnabled(false);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this,
                        "Error searching for train",
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void populateTrainForm(Train train) {
        trainNoField.setText(train.getTrainNo());
        bogeysField.setText(String.valueOf(train.getBogeys()));
        maxCapacityField.setText(String.valueOf(train.getMaxCapacity()));
        availableSeatsField.setText(String.valueOf(train.getAvailableSeats()));
        routeDropdown.setSelectedItem(train.getRoute());
        statusDropdown.setSelectedItem(train.getTrainStatus());
    }

    private void setupEditRemoveAddTrainFunctionality() {

        // Add Train Functionality
        addTrainButton.addActionListener(e -> {
            if (addTrainButton.getText().equals("Add Train")) {
                // Clear existing fields
                clearTrainForm();

                // Enable all fields for input
                trainNoField.setEditable(true);
                bogeysField.setEditable(true);
                maxCapacityField.setEditable(true);
                availableSeatsField.setEditable(true);
                routeDropdown.setEnabled(true);
                statusDropdown.setEnabled(true);

                // Change button to Confirm Add
                addTrainButton.setText("Confirm Add");

                // Disable other buttons during add
                editButton.setEnabled(false);
                removeButton.setEnabled(false);
                searchButton.setEnabled(false);
            } else {
                // Validate inputs
                try {
                    String trainNo = trainNoField.getText().trim();
                    int bogeys = Integer.parseInt(bogeysField.getText().trim());
                    int maxCapacity = Integer.parseInt(maxCapacityField.getText().trim());
                    int availableSeats = Integer.parseInt(availableSeatsField.getText().trim());

                    // Validate train number is not empty
                    if (trainNo.isEmpty()) {
                        JOptionPane.showMessageDialog(this,
                                "Train Number cannot be empty",
                                "Validation Error",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Check available seats constraint
                    if (availableSeats > maxCapacity) {
                        JOptionPane.showMessageDialog(this,
                                "Available seats cannot be more than max capacity",
                                "Validation Error",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Create new train object
                    Train newTrain = new Train();
                    newTrain.setTrainNo(trainNo);
                    newTrain.setBogeys(bogeys);
                    newTrain.setMaxCapacity(maxCapacity);
                    newTrain.setAvailableSeats(availableSeats);
                    newTrain.setRoute((String) routeDropdown.getSelectedItem());
                    newTrain.setTrainStatus((String) statusDropdown.getSelectedItem());

                    // Add to database
                    try (Connection connection = DatabaseUtil.getConnection()) {
                        TrainDAO trainDAO = new TrainDAO(connection);
                        if (trainDAO.addTrain(newTrain)) {
                            JOptionPane.showMessageDialog(this,
                                    String.format("Train [%s] on Route [%s] added successfully",
                                            trainNo,
                                            newTrain.getRoute()),
                                    "Success",
                                    JOptionPane.INFORMATION_MESSAGE);

                            // Reset form and buttons
                            clearTrainForm();
                            disableFormEditing();
                            addTrainButton.setText("Add Train");
                            searchButton.setEnabled(true);
                        } else {
                            JOptionPane.showMessageDialog(this,
                                    "Failed to add train",
                                    "Error",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } catch (NumberFormatException | SQLException ex) {
                    JOptionPane.showMessageDialog(this,
                            "Invalid numeric input",
                            "Validation Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        editButton.addActionListener(e -> {
            if (editButton.getText().equals("Edit")) {
                // Enable editing
                bogeysField.setEditable(true);
                maxCapacityField.setEditable(true);
                availableSeatsField.setEditable(true);
                routeDropdown.setEnabled(true);
                statusDropdown.setEnabled(true);

                // Change button to Update
                editButton.setText("Update");
            } else {
                // Validate inputs
                try {
                    int bogeys = Integer.parseInt(bogeysField.getText().trim());
                    int maxCapacity = Integer.parseInt(maxCapacityField.getText().trim());
                    int availableSeats = Integer.parseInt(availableSeatsField.getText().trim());

                    // Check available seats constraint
                    if (availableSeats > maxCapacity) {
                        JOptionPane.showMessageDialog(this,
                                "Available seats cannot be more than max capacity",
                                "Validation Error",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Create updated train object
                    Train updatedTrain = new Train();
                    updatedTrain.setTrainNo(trainNoField.getText());
                    updatedTrain.setBogeys(bogeys);
                    updatedTrain.setMaxCapacity(maxCapacity);
                    updatedTrain.setAvailableSeats(availableSeats);
                    updatedTrain.setRoute((String) routeDropdown.getSelectedItem());
                    updatedTrain.setTrainStatus((String) statusDropdown.getSelectedItem());

                    // Update in database
                    try (Connection connection = DatabaseUtil.getConnection()) {
                        TrainDAO trainDAO = new TrainDAO(connection);
                        if (trainDAO.updateTrain(updatedTrain)) {
                            JOptionPane.showMessageDialog(this,
                                    "Train updated successfully",
                                    "Success",
                                    JOptionPane.INFORMATION_MESSAGE);

                            // Disable editing
                            disableFormEditing();
                        } else {
                            JOptionPane.showMessageDialog(this,
                                    "Failed to update train",
                                    "Error",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } catch (NumberFormatException | SQLException ex) {
                    JOptionPane.showMessageDialog(this,
                            "Invalid numeric input",
                            "Validation Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        removeButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to remove this train?",
                    "Confirm Remove",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                try (Connection connection = DatabaseUtil.getConnection()) {
                    TrainDAO trainDAO = new TrainDAO(connection);
                    if (trainDAO.deleteTrain(trainNoField.getText())) {
                        JOptionPane.showMessageDialog(this,
                                "Train removed successfully",
                                "Success",
                                JOptionPane.INFORMATION_MESSAGE);

                        // Clear and disable form
                        clearTrainForm();
                        editButton.setEnabled(false);
                        removeButton.setEnabled(false);
                    } else {
                        JOptionPane.showMessageDialog(this,
                                "Failed to remove train",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this,
                            "Error removing train",
                            "Database Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private void disableFormEditing() {
        bogeysField.setEditable(false);
        maxCapacityField.setEditable(false);
        availableSeatsField.setEditable(false);
        routeDropdown.setEnabled(false);
        statusDropdown.setEnabled(false);
        editButton.setText("Edit");
    }

    private void clearTrainForm() {
        trainNoField.setText("");
        bogeysField.setText("");
        maxCapacityField.setText("");
        availableSeatsField.setText("");
        routeDropdown.setSelectedIndex(0);
        statusDropdown.setSelectedIndex(0);
    }

    private void initComponents() {
        setTitle("BookingExpress - Admin Dashboard");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create a tabbed pane
        JTabbedPane tabbedPane = new JTabbedPane();

        // Manage Trains Tab
//        JPanel manageTrainsPanel = createManageTrainsPanel();
        setupTrainManagementPanel();
        tabbedPane.addTab("Manage Trains", trainManagementPanel);

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

//    private JPanel createManageTrainsPanel() {
//        JPanel panel = new JPanel(new BorderLayout());
//
//        // Train Search Section
//        JPanel searchPanel = new JPanel(new FlowLayout());
//        JTextField trainSearchField = new JTextField(20);
//        JButton searchButton = new JButton("Search Train");
//        searchPanel.add(new JLabel("Train Number:"));
//        searchPanel.add(trainSearchField);
//        searchPanel.add(searchButton);
//
//        // Train Details Section
//        JTextArea trainDetailsArea = new JTextArea(10, 40);
//        trainDetailsArea.setEditable(false);
//
//        // Action Buttons
//        JPanel actionPanel = new JPanel();
//        JButton addTrainButton = new JButton("Add Train");
//        JButton editTrainButton = new JButton("Edit Train");
//        JButton removeTrainButton = new JButton("Remove Train");
//        actionPanel.add(addTrainButton);
//        actionPanel.add(editTrainButton);
//        actionPanel.add(removeTrainButton);
//
//        panel.add(searchPanel, BorderLayout.NORTH);
//        panel.add(new JScrollPane(trainDetailsArea), BorderLayout.CENTER);
//        panel.add(actionPanel, BorderLayout.SOUTH);
//
//        return panel;
//    }

    private void logout() {
        // Return to login screen
        new LoginRegistrationFrame().setVisible(true);
        dispose();
    }

    // Additional methods for train management can be added here
}