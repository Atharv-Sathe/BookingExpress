package com.bookingexpress;

import com.bookingexpress.ui.LoginRegistrationFrame;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Use Swing's Event Dispatch Thread for thread-safe UI initialization
        SwingUtilities.invokeLater(() -> {
            // Launch the login/registration frame
            new LoginRegistrationFrame().setVisible(true);
        });
    }
}