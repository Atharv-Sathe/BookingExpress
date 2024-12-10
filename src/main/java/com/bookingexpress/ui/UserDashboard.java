package com.bookingexpress.ui;

import com.bookingexpress.dao.TrainDAO;
import com.bookingexpress.models.Train;
import com.bookingexpress.models.User;
import com.bookingexpress.utils.DatabaseUtil;
import com.bookingexpress.utils.RouteManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.stream.Collectors;
import org.jdesktop.swingx.JXDatePicker;
import java.text.SimpleDateFormat;
import java.util.stream.Stream;

public class UserDashboard extends JFrame {
    private User currentUser;
//    private TrainDAO trainDAO;
    private JComboBox<String> fromCityComboBox;
    private JComboBox<String> toCityComboBox;
    private JTable trainsTable;
    private JButton searchButton;
    private JButton bookTicketButton;
    private JXDatePicker datePicker;

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

        // Populate city dropdowns from RouteManager
        String[] cities = RouteManager.getAllRoutes().values().stream()
                .flatMap(route -> Stream.of(route[0], route[1]))
                .distinct()
                .toArray(String[]::new);

        fromCityComboBox = new JComboBox<>(cities);
        toCityComboBox = new JComboBox<>(cities);

        searchPanel.add(new JLabel("From:"));
        searchPanel.add(fromCityComboBox);
        searchPanel.add(new JLabel("To:"));
        searchPanel.add(toCityComboBox);

        datePicker = new JXDatePicker();
        searchPanel.add(datePicker);

        searchButton = new JButton("Search Trains");
        searchButton.addActionListener(e -> searchTrains());
        searchPanel.add(searchButton);

        // Trains Table
        String[] columnNames = {"#", "Train No", "Available Seats", "Train Status", "Select"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 4) return Boolean.class;
                return super.getColumnClass(columnIndex);
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4;
            }
        };

        trainsTable = new JTable(model);

        // Single selection for train
        trainsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Book Ticket Button (initially disabled)
        bookTicketButton = new JButton("Book Ticket");
        bookTicketButton.setEnabled(false);

        // Add listener to enable/disable book ticket button based on train selection
        trainsTable.getModel().addTableModelListener(e -> {
            boolean anySelected = false;
            for (int i = 0; i < trainsTable.getRowCount(); i++) {
                if ((Boolean) trainsTable.getValueAt(i, 4)) {
                    anySelected = true;
                    break;
                }
            }
            bookTicketButton.setEnabled(anySelected);
        });

        JScrollPane scrollPane = new JScrollPane(trainsTable);

        panel.add(searchPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Add book ticket button at bottom
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.add(bookTicketButton);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void searchTrains() {
        // Clear previous results
        DefaultTableModel model = (DefaultTableModel) trainsTable.getModel();
        model.setRowCount(0);

        String fromCity = (String) fromCityComboBox.getSelectedItem();
        String toCity = (String) toCityComboBox.getSelectedItem();

        // Validate cities are different
        if (fromCity.equals(toCity)) {
            JOptionPane.showMessageDialog(this, "Source and Destination cannot be same");
            return;
        }

        // Find matching routes
        List<String> validRoutes = RouteManager.getAllRoutes().entrySet().stream()
                .filter(entry ->
                        (entry.getValue()[0].equals(fromCity) && entry.getValue()[1].equals(toCity)) ||
                                (entry.getValue()[0].equals(toCity) && entry.getValue()[1].equals(fromCity))
                )
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());


        // Initialize TrainDAO
        try (Connection conn = DatabaseUtil.getConnection()) {
            TrainDAO trainDAO = new TrainDAO(conn);
            // Fetch trains for these routes
            List<Train> availableTrains = trainDAO.getAllTrains().stream()
                    .filter(train -> validRoutes.contains(train.getRoute()))
                    .collect(Collectors.toList());

            // Populate table
            for (int i = 0; i < availableTrains.size(); i++) {
                Train train = availableTrains.get(i);
                model.addRow(new Object[]{
                        i + 1,
                        train.getTrainNo(),
                        train.getAvailableSeats(),
                        train.getTrainStatus(),
                        false
                });
            }

            if (availableTrains.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No trains found on this route");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database Connection Error");
        }
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