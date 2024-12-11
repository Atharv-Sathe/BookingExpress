package com.bookingexpress.ui;

import com.bookingexpress.dao.TicketDAO;
import com.bookingexpress.dao.TrainDAO;
import com.bookingexpress.models.Ticket;
import com.bookingexpress.models.Train;
import com.bookingexpress.models.User;
import com.bookingexpress.utils.DatabaseUtil;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class BookTicketPanel extends JPanel {
    private Train selectedTrain;
    private User currentUser;
//    private TrainDAO trainDAO;
//    private TicketDAO ticketDAO;

    // Passengers Panel
    private JPanel passengersPanel;
    private List<PassengerForm> passengerForms = new ArrayList<>();
    private JButton addPassengerButton;

    // Contact Panel
    private JTextField mobileNumberField;

    // Payment Panel
    private JLabel totalAmountLabel = new JLabel("Total Amount: ");
    private JTextField payeeNameField;
    private JTextField cardNumberField;
    private JTextField cvvField;
    private JTextField expiryField;
    private JButton confirmPaymentButton;

    public BookTicketPanel(Train train, User user) {
        this.selectedTrain = train;
        this.currentUser = user;

        // Initialize DAOs
//        try (Connection conn = DatabaseUtil.getConnection()) {
//            this.trainDAO = new TrainDAO(conn);
//            this.ticketDAO = new TicketDAO(conn);
//        } catch (Exception e) {
//            e.printStackTrace();
//            JOptionPane.showMessageDialog(this, "Database Connection Error");
//        }

        setLayout(new BorderLayout());

        // Create three sections
        JPanel mainPanel = new JPanel(new GridLayout(3, 1, 10, 10));

        // Passengers Section
        JPanel passengersSection = createPassengersSection();
        mainPanel.add(passengersSection);

        // Contact Section
        JPanel contactSection = createContactSection();
        mainPanel.add(contactSection);

        // Payment Section
        JPanel paymentSection = createPaymentSection();
        mainPanel.add(paymentSection);

        add(mainPanel, BorderLayout.CENTER);
    }

    private JPanel createPassengersSection() {
        JPanel section = new JPanel(new BorderLayout());
        section.setBorder(BorderFactory.createTitledBorder("Passenger Details"));

        passengersPanel = new JPanel(new GridLayout(0, 1, 5, 5));

        // Add first passenger form
        addPassengerForm();

        addPassengerButton = new JButton("Add Passenger");
        addPassengerButton.addActionListener(e -> addPassengerForm());

        section.add(passengersPanel, BorderLayout.CENTER);
        section.add(addPassengerButton, BorderLayout.SOUTH);

        return section;
    }

    private void addPassengerForm() {
        if (passengerForms.size() < 4) {
            PassengerForm passengerForm = new PassengerForm(this);
            passengerForms.add(passengerForm);
            passengersPanel.add(passengerForm);

            // Disable add passenger button if 4 passengers added
            if (passengerForms.size() == 4) {
                addPassengerButton.setEnabled(false);
            }

            revalidate();
            repaint();
            updateTotalAmount();
        }
    }

    private void removePassengerForm(PassengerForm form) {
        passengerForms.remove(form);
        passengersPanel.remove(form);

        // Re-enable add passenger button
        addPassengerButton.setEnabled(true);

        revalidate();
        repaint();
        updateTotalAmount();
    }

    private JPanel createContactSection() {
        JPanel section = new JPanel(new FlowLayout(FlowLayout.LEFT));
        section.setBorder(BorderFactory.createTitledBorder("Contact Information"));

        section.add(new JLabel("Mobile Number:"));
        mobileNumberField = new JTextField(15);
        section.add(mobileNumberField);

        return section;
    }

    private JPanel createPaymentSection() {
        JPanel section = new JPanel(new BorderLayout());
        section.setBorder(BorderFactory.createTitledBorder("Payment Details"));

        JPanel amountPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        totalAmountLabel = new JLabel("Total Amount: ₹0");
        amountPanel.add(totalAmountLabel);

        JPanel paymentForm = new JPanel(new GridLayout(4, 2, 5, 5));
        paymentForm.add(new JLabel("Payee Name:"));
        payeeNameField = new JTextField(15);
        paymentForm.add(payeeNameField);

        paymentForm.add(new JLabel("Card Number:"));
        cardNumberField = new JTextField(15);
        paymentForm.add(cardNumberField);

        paymentForm.add(new JLabel("CVV:"));
        cvvField = new JTextField(5);
        paymentForm.add(cvvField);

        paymentForm.add(new JLabel("Expiry (MM/YY):"));
        expiryField = new JTextField(5);
        paymentForm.add(expiryField);

        confirmPaymentButton = new JButton("Confirm Ticket and Pay");
        confirmPaymentButton.addActionListener(e -> confirmTicketAndPay());

        section.add(amountPanel, BorderLayout.NORTH);
        section.add(paymentForm, BorderLayout.CENTER);
        section.add(confirmPaymentButton, BorderLayout.SOUTH);

        return section;
    }

    private void updateTotalAmount() {
        int passengerCount = passengerForms.size();
        double totalAmount = passengerCount * selectedTrain.getCostPerSeat();
        totalAmountLabel.setText(String.format("₹%.2f", totalAmount));
    }

    private void confirmTicketAndPay() {
        // Validate inputs
        if (!validateInputs()) {
            return;
        }

        // Check seat availability
        try (Connection conn = DatabaseUtil.getConnection()) {
            TrainDAO trainDAO = new TrainDAO(conn);
            Train updatedTrain = trainDAO.getTrainByNumber(selectedTrain.getTrainNo());

            if (updatedTrain.getAvailableSeats() < passengerForms.size()) {
                JOptionPane.showMessageDialog(this,
                        "Only " + updatedTrain.getAvailableSeats() + " seats available.",
                        "Seat Unavailable",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Generate unique PNR
            String pnr = generateUniquePNR();

            // Create Ticket object
            Ticket ticket = new Ticket();
            ticket.setPnr(pnr);
            ticket.setUsername(currentUser.getUsername());
            ticket.setTrainNo(updatedTrain.getTrainNo());
            ticket.setTicketStatus("active");
            ticket.setUserMobile(mobileNumberField.getText());

            // Set passenger details
            List<String> passengerDetails = new ArrayList<>();
            for (PassengerForm form : passengerForms) {
                passengerDetails.add(form.getPassengerDetails());
            }

            // Fill remaining passenger slots if less than 4
            while (passengerDetails.size() < 4) {
                passengerDetails.add("");
            }

            ticket.setPassenger1(passengerDetails.get(0));
            ticket.setPassenger2(passengerDetails.get(1));
            ticket.setPassenger3(passengerDetails.get(2));
            ticket.setPassenger4(passengerDetails.get(3));

            // Save ticket
            TicketDAO ticketDAO = new TicketDAO(conn);
            boolean ticketSaved = ticketDAO.bookTicket(ticket);

            if (ticketSaved) {
                // Update train available seats
                updatedTrain.setAvailableSeats(updatedTrain.getAvailableSeats() - passengerForms.size());
                trainDAO.updateTrain(updatedTrain);

                JOptionPane.showMessageDialog(this,
                        "Ticket Booked Successfully!\nPNR: " + pnr,
                        "Booking Confirmed",
                        JOptionPane.INFORMATION_MESSAGE);

                // Here you might want to navigate back to the train search or show ticket details
                bookingCompleted();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Failed to book ticket. Please try again.",
                        "Booking Failed",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Database Connection Error: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean validateInputs() {
        // Validate mobile number
        if (mobileNumberField.getText().trim().isEmpty() ||
                !mobileNumberField.getText().matches("\\d{10}")) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a valid 10-digit mobile number.",
                    "Invalid Input",
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }

        // Validate passenger details
        if (passengerForms.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please add at least one passenger.",
                    "Invalid Input",
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }

        // Validate payment details
        if (payeeNameField.getText().trim().isEmpty() ||
                cardNumberField.getText().trim().isEmpty() ||
                cvvField.getText().trim().isEmpty() ||
                expiryField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please fill in all payment details.",
                    "Invalid Input",
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }

        return true;
    }

    private String generateUniquePNR() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(1000000));
    }

    public abstract void bookingCompleted();

    // Inner class for Passenger Form
    private class PassengerForm extends JPanel {
        private JTextField nameField;
        private JTextField ageField;
        private JComboBox<String> genderComboBox;
        private JComboBox<String> classComboBox;
        private JButton removeButton;

        public PassengerForm(BookTicketPanel parent) {
            setLayout(new FlowLayout(FlowLayout.LEFT));

            nameField = new JTextField(10);
            ageField = new JTextField(3);

            genderComboBox = new JComboBox<>(new String[]{"Male", "Female", "Other"});
            classComboBox = new JComboBox<>(new String[]{"General", "Sleeper", "3AC", "2AC", "1AC"});

            removeButton = new JButton("X");
            removeButton.setForeground(Color.RED);
            removeButton.addActionListener(e -> parent.removePassengerForm(this));

            add(new JLabel("Name:"));
            add(nameField);
            add(new JLabel("Age:"));
            add(ageField);
            add(new JLabel("Gender:"));
            add(genderComboBox);
            add(new JLabel("Class:"));
            add(classComboBox);
            add(removeButton);
        }

        public String getPassengerDetails() {
            return String.format("%s,%s,%s,%s",
                    nameField.getText(),
                    ageField.getText(),
                    genderComboBox.getSelectedItem(),
                    classComboBox.getSelectedItem());
        }

    }
}