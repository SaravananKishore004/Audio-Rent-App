
package com.example;

// Booking.java - Booking entity
import java.time.LocalDate;

public class Booking {
    private String bookingId;
    private String customerUsername;
    private AudioDevice device;
    private LocalDate startDate;
    private LocalDate endDate;
    private int quantity;
    private BookingStatus status;
    private double totalCost;

    public Booking(String bookingId, String customerUsername, AudioDevice device,
                   LocalDate startDate, LocalDate endDate, int quantity) {
        this.bookingId = bookingId;
        this.customerUsername = customerUsername;
        this.device = device;
        this.startDate = startDate;
        this.endDate = endDate;
        this.quantity = quantity;
        this.status = BookingStatus.PENDING;
        calculateTotalCost();
    }

    private void calculateTotalCost() {
        long days = java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate) + 1;
        this.totalCost = device.getPricePerDay() * days * quantity;
    }

    // Getters and setters omitted for brevity
}

// BookingStatus.java - Enum for booking state
public enum BookingStatus {
    PENDING, APPROVED, REJECTED, CANCELLED, COMPLETED
}

// InventoryManager.java - Inventory management
import java.util.*;

public class InventoryManager {
    private Map<String, AudioDevice> devices = new HashMap<>();

    public void addDevice(AudioDevice device) {
        devices.put(device.getDeviceId(), device);
    }

    public void updateDevice(AudioDevice device) {
        devices.put(device.getDeviceId(), device);
    }

    public void removeDevice(String deviceId) {
        devices.remove(deviceId);
    }

    public AudioDevice getDevice(String deviceId) {
        return devices.get(deviceId);
    }

    public List<AudioDevice> searchDevices(String keyword) {
        List<AudioDevice> results = new ArrayList<>();
        for (AudioDevice d : devices.values()) {
            if (d.getName().toLowerCase().contains(keyword.toLowerCase())) {
                results.add(d);
            }
        }
        return results;
    }
}

// BookingManager.java - Booking and availability management
import java.time.LocalDate;
import java.util.*;

public class BookingManager {
    private Map<String, Booking> bookings = new HashMap<>();

    public boolean checkAvailability(AudioDevice device, LocalDate start, LocalDate end, int quantity) {
        for (Booking booking : bookings.values()) {
            if (booking.getDevice().getDeviceId().equals(device.getDeviceId())
                && booking.getStatus() == BookingStatus.APPROVED) {
                // Check for overlapping dates
                if (!(end.isBefore(booking.getStartDate()) || start.isAfter(booking.getEndDate()))) {
                    // Check if quantity exceeds availability
                    if (quantity > (device.getQuantityAvailable() - booking.getQuantity())) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public void addBooking(Booking booking) {
        bookings.put(booking.getBookingId(), booking);
    }

    public List<Booking> getBookingsByCustomer(String username) {
        List<Booking> customerBookings = new ArrayList<>();
        for (Booking b : bookings.values()) {
            if (b.getCustomerUsername().equals(username)) {
                customerBookings.add(b);
            }
        }
        return customerBookings;
    }

    // Other administrative methods for approvals, cancellations etc.
}

// AuthenticationService.java - Simple user auth service
import java.util.*;

public class AuthenticationService {
    private Map<String, User> users = new HashMap<>();

    public void registerUser(User user) {
        users.put(user.getUsername(), user);
    }

    public User login(String username, String password) {
        User user = users.get(username);
        if (user != null && user.password.equals(password)) {
            return user;
        }
        return null;
    }
}

// Main.java - Entry point with simple console interaction (ideal for Swing extension)
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Main {
    private static AuthenticationService authService = new AuthenticationService();
    private static InventoryManager inventory = new InventoryManager();
    private static BookingManager bookingManager = new BookingManager();

    public static void main(String[] args) {
        // Setup some initial data (demo)
        inventory.addDevice(new AudioDevice("D001", "Speaker", "High power speaker", 100.0, 10));
        inventory.addDevice(new AudioDevice("D002", "Microphone", "Wireless mic", 50.0, 15));
        authService.registerUser(new Customer("cust1", "pass1"));
        authService.registerUser(new Administrator("admin", "adminpass"));

        // Launch Swing UI or continue console-based interaction
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Audio Device Renting System");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(600, 400);

            // Placeholder to add login form and subsequent workflow panels
            frame.add(new JLabel("Audio Device Renting System - UI under development")); // Basic placeholder

            frame.setVisible(true);
        });
    }
}
