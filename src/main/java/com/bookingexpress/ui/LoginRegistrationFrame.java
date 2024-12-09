package com.bookingexpress.ui;

import com.bookingexpress.dao.UserDAO;
import com.bookingexpress.models.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginRegistrationFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    private UserDAO userDAO;

    public LoginRegistrationFrame() {
        userDAO = new UserDAO();
        initComponents();
    }

    private void initComponents() {
        setTitle("BookingExpress - Login");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel centerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel titleLabel = new JLabel("BookingExpress", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));

        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        loginButton = new JButton("Login");
        registerButton = new JButton("Register");

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        centerPanel.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;
        centerPanel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        centerPanel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        centerPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        centerPanel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 1;
        centerPanel.add(loginButton, gbc);
        gbc.gridx = 1;
        centerPanel.add(registerButton, gbc);

        add(centerPanel, BorderLayout.CENTER);

        // Login button action
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                try {
                    User user = userDAO.validateUser(username, password);
                    if (user != null) {
                        // Open main dashboard based on user type
                        if ("admin".equals(user.getRole())) {
                            new AdminDashboard(user).setVisible(true);
                        } else {
                            new UserDashboard(user).setVisible(true);
                        }
                        dispose(); // Close login window
                    } else {
                        JOptionPane.showMessageDialog(LoginRegistrationFrame.this,
                                "Invalid username or password",
                                "Login Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(LoginRegistrationFrame.this,
                            "Login failed: " + ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Register button action
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                try {
                    User newUser = new User(username, password, "user");
                    boolean registered = userDAO.registerUser(newUser);

                    if (registered) {
                        JOptionPane.showMessageDialog(LoginRegistrationFrame.this,
                                "Registration Successful!",
                                "Success",
                                JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(LoginRegistrationFrame.this,
                                "Registration Failed. Username might already exist.",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(LoginRegistrationFrame.this,
                            "Registration failed: " + ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        // Use Swing's Event Dispatch Thread for thread safety
        SwingUtilities.invokeLater(() -> {
            new LoginRegistrationFrame().setVisible(true);
        });
    }
}