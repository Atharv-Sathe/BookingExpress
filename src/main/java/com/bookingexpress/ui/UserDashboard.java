package com.bookingexpress.ui;

import com.bookingexpress.dao.TicketDAO;
import com.bookingexpress.dao.TrainDAO;
import com.bookingexpress.dao.TransactionDAO;
import com.bookingexpress.models.Ticket;
import com.bookingexpress.models.Train;
import com.bookingexpress.models.Transaction;
import com.bookingexpress.models.User;
import com.bookingexpress.utils.DatabaseUtil;
import com.bookingexpress.utils.RouteManager;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.jdesktop.swingx.JXDatePicker;
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
    private JPanel bookTicketsPanel;
    private CardLayout bookTicketsCardLayout;
    private DefaultTableModel ticketModel;
    private DefaultTableModel transactionModel;

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

        tabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JTabbedPane tabbedPane = (JTabbedPane) e.getSource();
                if (tabbedPane.getSelectedComponent() == yourTicketsPanel) {
                    populateTicketsPanel(ticketModel);
                }
                if (tabbedPane.getSelectedComponent() == transactionsPanel) {
                    populateTransactionsPanel(transactionModel);
                }
            }
        });


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
//        JPanel panel = new JPanel(new BorderLayout());
        bookTicketsCardLayout = new CardLayout();
        bookTicketsPanel = new JPanel(bookTicketsCardLayout);

        // Train Search Section
//        JPanel searchPanel = new JPanel(new FlowLayout());
        JPanel searchPanel = new JPanel(new BorderLayout());

        JPanel searchInputPanel = new JPanel(new FlowLayout());

        // Populate city dropdowns from RouteManager
        String[] cities = RouteManager.getAllRoutes().values().stream()
                .flatMap(route -> Stream.of(route[0], route[1]))
                .distinct()
                .toArray(String[]::new);

        fromCityComboBox = new JComboBox<>(cities);
        toCityComboBox = new JComboBox<>(cities);

        searchInputPanel.add(new JLabel("From:"));
        searchInputPanel.add(fromCityComboBox);
        searchInputPanel.add(new JLabel("To:"));
        searchInputPanel.add(toCityComboBox);

        datePicker = new JXDatePicker();
        searchInputPanel.add(datePicker);

        searchButton = new JButton("Search Trains");
        searchButton.addActionListener(e -> searchTrains());
        searchInputPanel.add(searchButton);

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
        bookTicketButton.addActionListener(e -> showBookTicketPanel());

        trainsTable.getModel().addTableModelListener(e -> {
            boolean anySelected = false;
            Train selectedTrain = null;
            for (int i = 0; i < trainsTable.getRowCount(); i++) {
                if ((Boolean) trainsTable.getValueAt(i, 4)) {
                    anySelected = true;
                    // Find the selected train
                    try (Connection conn = DatabaseUtil.getConnection()) {
                        TrainDAO trainDAO = new TrainDAO(conn);
                        selectedTrain = trainDAO.getTrainByNumber((String) trainsTable.getValueAt(i, 1));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    break;
                }
            }
            bookTicketButton.setEnabled(anySelected);
        });

        JScrollPane scrollPane = new JScrollPane(trainsTable);

        searchPanel.add(searchInputPanel, BorderLayout.NORTH);
        searchPanel.add(scrollPane, BorderLayout.CENTER);

        // Add book ticket button at bottom
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.add(bookTicketButton);
        searchPanel.add(bottomPanel, BorderLayout.SOUTH);

        // Add panels to card layout
        bookTicketsPanel.add(searchPanel, "SearchPanel");

        return bookTicketsPanel;
    }

    private void showBookTicketPanel() {
        // Find the selected train
        Train selectedTrain = null;
        for (int i = 0; i < trainsTable.getRowCount(); i++) {
            if ((Boolean) trainsTable.getValueAt(i, 4)) {
                try (Connection conn = DatabaseUtil.getConnection()) {
                    TrainDAO trainDAO = new TrainDAO(conn);
                    selectedTrain = trainDAO.getTrainByNumber((String) trainsTable.getValueAt(i, 1));
                    break;
                } catch (Exception ex) {
                    ex.printStackTrace();
                    return;
                }
            }
        }

        Date selectedDate = datePicker.getDate();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateOfDeparture = dateFormat.format(selectedDate);

        if (selectedTrain != null) {
            // Create BookTicketPanel
            BookTicketPanel bookTicketPanel = new BookTicketPanel(selectedTrain, currentUser, dateOfDeparture) {
                @Override
                public void bookingCompleted() {
                    // Reset to search panel after successful booking
                    bookTicketsCardLayout.show(bookTicketsPanel, "SearchPanel");
                    searchTrains(); // Refresh train list
                }
            };

            // Add book ticket panel to card layout if not already added
            if (bookTicketsPanel.getComponentCount() == 1) {
                bookTicketsPanel.add(bookTicketPanel, "BookTicketPanel");
            } else {
                // Replace existing book ticket panel
                bookTicketsPanel.remove(1);
                bookTicketsPanel.add(bookTicketPanel, "BookTicketPanel");
            }

            // Switch to book ticket panel
            bookTicketsCardLayout.show(bookTicketsPanel, "BookTicketPanel");
        }
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

//    private JPanel createYourTicketsPanel() {
//        JPanel panel = new JPanel(new BorderLayout());
//
//        // Tickets Table Columns
//        String[] ticketColumnNames = {"S.No", "PNR", "Train No", "Passenger 1", "Passenger 2", "Passenger 3", "Passenger 4", "Date of Departure"};
//        ticketModel = new DefaultTableModel(ticketColumnNames, 0);
//
//        JTable ticketsTable = new JTable(ticketModel);
//        JScrollPane scrollPane = new JScrollPane(ticketsTable);
//
//        panel.add(scrollPane, BorderLayout.CENTER);
//
//        return panel;
//    }

    private JPanel createYourTicketsPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Tickets Table Columns
        String[] ticketColumnNames = {"S.No", "PNR", "Train No", "Passenger 1", "Passenger 2", "Passenger 3", "Passenger 4", "Date of Departure", "Select"};
        ticketModel = new DefaultTableModel(ticketColumnNames, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 8) return Boolean.class;
                return super.getColumnClass(columnIndex);
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 8;
            }
        };

        populateTicketsPanel(ticketModel);
        JTable ticketsTable = new JTable(ticketModel);

        // Adjust column widths
        ticketsTable.getColumnModel().getColumn(8).setMaxWidth(70);

        JScrollPane scrollPane = new JScrollPane(ticketsTable);

        // Cancel Tickets Button
        JButton cancelTicketButton = new JButton("Cancel Selected Tickets");
        cancelTicketButton.setEnabled(false);

        // Add listener to enable/disable cancel button based on checkbox selection
        ticketsTable.getModel().addTableModelListener(e -> {
            boolean anySelected = false;
            for (int i = 0; i < ticketsTable.getRowCount(); i++) {
                if ((Boolean) (ticketsTable.getValueAt(i, 8) != null)) {
                    anySelected = true;
                    break;
                }
            }
            cancelTicketButton.setEnabled(anySelected);
        });

        // Cancel Ticket Button Action
        cancelTicketButton.addActionListener(e -> {
            int confirmCancel = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to cancel the selected tickets?",
                    "Confirm Ticket Cancellation",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirmCancel == JOptionPane.YES_OPTION) {
                try (Connection conn = DatabaseUtil.getConnection()) {
                    TicketDAO ticketDAO = new TicketDAO(conn);
                    TrainDAO trainDAO = new TrainDAO(conn);
                    TransactionDAO transactionDAO = new TransactionDAO(conn);

                    // Start transaction
                    conn.setAutoCommit(false);
                    for (int i = 0; i < ticketsTable.getRowCount(); i++) {
                        if ((Boolean) (ticketsTable.getValueAt(i, 8) != null)) {
                            String pnr = (String) ticketsTable.getValueAt(i, 1);
                            String trainNo = (String) ticketsTable.getValueAt(i, 2);
                            int seats = ticketDAO.getPassengerCount(pnr);
                            System.out.println("Number of seats: " + seats);

                            // Delete associated transaction
                            transactionDAO.deleteTransactionByPNR(pnr);

                            // Delete ticket
                            ticketDAO.cancelTicket(pnr);

                            // Update train available seats
                            trainDAO.incrementAvailableSeats(trainNo, seats);
                        }
                    }

                    // Commit transaction
                    conn.commit();

                    // Refresh tickets panel
                    populateTicketsPanel(ticketModel);

                    JOptionPane.showMessageDialog(
                            this,
                            "Selected tickets have been cancelled successfully.",
                            "Ticket Cancellation",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(
                            this,
                            "Error cancelling tickets: " + ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });

        // Bottom panel for cancel button
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.add(cancelTicketButton);

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void populateTicketsPanel(DefaultTableModel ticketModel) {
        ticketModel.setRowCount(0);
        // Fetch and populate tickets
        try (Connection conn = DatabaseUtil.getConnection()) {
            // Assuming you have a TicketDAO method to fetch tickets by username
            TicketDAO ticketDAO = new TicketDAO(conn);
            List<Ticket> userTickets = ticketDAO.getTicketByUsername(currentUser.getUsername());
            System.out.println(userTickets);

            for (int i = 0; i < userTickets.size(); i++) {
                Ticket ticket = userTickets.get(i);
                ticketModel.addRow(new Object[]{
                        i + 1,
                        ticket.getPnr(),
                        ticket.getTrainNo(),
                        ticket.getPassenger1(),
                        ticket.getPassenger2(),
                        ticket.getPassenger3(),
                        ticket.getPassenger4(),
                        ticket.getDateOfDeparture()
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching tickets: " + e.getMessage());
        }
    }


    private JPanel createTransactionsPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Transactions Table Columns
        String[] transactionColumnNames = {"S.No", "Transaction ID", "PNR", "Amount", "Date and Time"};
        transactionModel = new DefaultTableModel(transactionColumnNames, 0);

        JTable transactionsTable = new JTable(transactionModel);
        JScrollPane scrollPane = new JScrollPane(transactionsTable);

        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void populateTransactionsPanel(DefaultTableModel transactionModel) {
        transactionModel.setRowCount(0);
        // Fetch and populate transactions
        try (Connection conn = DatabaseUtil.getConnection()) {
            // Assuming you have a TransactionDAO method to fetch transactions by username
            TransactionDAO transactionDAO = new TransactionDAO(conn);
            List<Transaction> userTransactions = transactionDAO.getTransactionByUsername(currentUser.getUsername());
            System.out.println(userTransactions);
            for (int i = 0; i < userTransactions.size(); i++) {
                Transaction transaction = userTransactions.get(i);
                transactionModel.addRow(new Object[]{
                        i + 1,
                        transaction.getTransactionId(),
                        transaction.getPnr(),
                        transaction.getAmount(),
                        transaction.getDateTime()
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching transactions: " + e.getMessage());
        }
    }

    private void logout() {
        // Return to login screen
        new LoginRegistrationFrame().setVisible(true);
        dispose();
    }
}