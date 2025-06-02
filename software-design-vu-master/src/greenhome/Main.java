package greenhome;

import greenhome.household.House;
import greenhome.household.Parser;
import greenhome.input.Form;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        System.out.println("🌱 Welcome to GreenHome!");
        System.out.println("🔧 Initializing application...");

        // Show startup choice and start app
        showStartupChoice();

        System.out.println("✅ Application session completed!");
    }

    private static void showStartupChoice() {
        // Check if previous data exists
        boolean hasData = checkExistingData();

        if (!hasData) {
            // No previous data, start fresh
            startFresh();
            return;
        }

        // Show choice dialog
        JDialog dialog = new JDialog((Frame) null, "GreenHome - Data Options", true);
        dialog.setSize(450, 300);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setLocationRelativeTo(null);
        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

        // Header
        JLabel header = new JLabel("<html><h2>Welcome back to GreenHome!</h2>" +
                "<p>Previous data found. What would you like to do?</p></html>");
        header.setHorizontalAlignment(SwingConstants.CENTER);
        header.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        dialog.add(header, BorderLayout.NORTH);

        // Buttons panel
        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 15, 15));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        // Keep data button
        JButton keepButton = new JButton("<html><b>📊 Continue with Previous Data</b><br>" +
                "<small>Keep your existing household setup</small></html>");
        keepButton.setPreferredSize(new Dimension(350, 60));
        keepButton.addActionListener(e -> {
            dialog.dispose();
            continueWithData();
        });

        // Fresh start button
        JButton freshButton = new JButton("<html><b>🆕 Start with Fresh Data</b><br>" +
                "<small>Clear all previous data and start over</small></html>");
        freshButton.setPreferredSize(new Dimension(350, 60));
        freshButton.addActionListener(e -> {
            dialog.dispose();
            int choice = JOptionPane.showConfirmDialog(null,
                    "Delete all previous data? This cannot be undone.",
                    "Confirm", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                startFresh();
            } else {
                showStartupChoice(); // Show again
            }
        });

        buttonPanel.add(keepButton);
        buttonPanel.add(freshButton);
        dialog.add(buttonPanel, BorderLayout.CENTER);

        // Data preview
        String preview = getDataPreview();
        JLabel previewLabel = new JLabel("<html><small><b>Current data:</b> " +
                preview + "</small></html>");
        previewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        previewLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 15, 10));
        dialog.add(previewLabel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    private static boolean checkExistingData() {
        try {
            Parser.loadHouse("json.json");
            House house = House.getInstance();
            return house != null &&
                    (house.getAppliances().size() > 0 || house.getResidents().size() > 0);
        } catch (IOException e) {
            System.out.println("No existing data file found");
            return false;
        }
    }

    private static String getDataPreview() {
        try {
            House house = House.getInstance();
            if (house != null) {
                return String.format("%d appliances, %d users in %s",
                        house.getAppliances().size(),
                        house.getResidents().size(),
                        house.getRegion());
            }
        } catch (Exception e) {
            return "Data found but unreadable";
        }
        return "No data";
    }

    private static void continueWithData() {
        try {
            House house = House.getInstance();
            System.out.println("📊 Using existing data:");
            System.out.println("   - Region: " + house.getRegion());
            System.out.println("   - Appliances: " + house.getAppliances().size());
            System.out.println("   - Users: " + house.getResidents().size());

            JOptionPane.showMessageDialog(null,
                    "Loaded: " + house.getAppliances().size() + " appliances, " +
                            house.getResidents().size() + " users",
                    "Data Loaded", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            System.err.println("Error with existing data: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Error loading data. Starting fresh.");
            startFresh();
            return;
        }

        Form.main(new String[]{});
    }

    private static void startFresh() {
        System.out.println("🆕 Starting fresh");

        // Reset house
        resetHouse();

        // Create new house
        House house = House.constructInstance(
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                "Netherlands",
                0.25
        );

        System.out.println("✅ Fresh house created");
        JOptionPane.showMessageDialog(null, "Starting with fresh data!");

        Form.main(new String[]{});
    }

    private static void resetHouse() {
        try {
            java.lang.reflect.Field field = House.class.getDeclaredField("instance");
            field.setAccessible(true);
            field.set(null, null);
            System.out.println("🔄 House reset");
        } catch (Exception e) {
            System.err.println("Warning: Could not reset house: " + e.getMessage());
        }
    }
}