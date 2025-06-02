package greenhome.input;

import com.toedter.calendar.JDateChooser;
import greenhome.household.*;
import greenhome.time.DateTime;
import greenhome.validation.Validator;
import greenhome.reporting.Report;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class WhatIfScenarios {
    // Store original state for restoration
    private static HouseBackup originalHouseBackup = null;
    private static boolean scenarioActive = false;

    public static void openWhatIfWindow() {
        // Create backup of original house state
        if (originalHouseBackup == null) {
            createHouseBackup();
            System.out.println("💾 Original house data backed up for What-If scenarios");
        }

        JDialog mainDialog = new JDialog((Frame) null, "🔮 What-If Scenarios", true);
        mainDialog.setSize(600, 500);
        mainDialog.setLayout(new BorderLayout(10, 10));
        mainDialog.setLocationRelativeTo(null);

        // Header with scenario status
        String statusText = scenarioActive ?
                "<p style='color: orange;'>⚠️ Scenario Mode Active - Changes are temporary</p>" :
                "<p style='color: green;'>✅ Normal Mode - Using real data</p>";

        JLabel headerLabel = new JLabel("<html><div style='text-align: center; padding: 10px;'>" +
                "<h2>🔮 What-If Scenarios</h2>" +
                "<p>Test fictional changes and see their impact on your carbon footprint</p>" +
                statusText +
                "</div></html>");
        headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainDialog.add(headerLabel, BorderLayout.NORTH);

        // Main options panel
        JPanel optionsPanel = new JPanel(new GridLayout(6, 1, 10, 10));
        optionsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Option 1: General House Info
        JButton generalInfoButton = new JButton("🏠 Test Different Region & Tariff");
        generalInfoButton.setToolTipText("Change region and electricity tariff to see impact");
        generalInfoButton.setPreferredSize(new Dimension(500, 50));
        generalInfoButton.addActionListener(e -> {
            mainDialog.setVisible(false);
            openGeneralInfoEditor(() -> {
                scenarioActive = true;
                mainDialog.dispose();
                showResultsAndOptions();
            });
        });

        // Option 2: Appliance Changes
        JButton applianceButton = new JButton("🔌 Test Different Appliances & Usage");
        applianceButton.setToolTipText("Modify appliances or add/remove them to see impact");
        applianceButton.setPreferredSize(new Dimension(500, 50));
        applianceButton.addActionListener(e -> {
            mainDialog.setVisible(false);
            openApplianceEditor(() -> {
                scenarioActive = true;
                mainDialog.dispose();
                showResultsAndOptions();
            });
        });

        // Option 3: Generate Report for Current Scenario
        JButton reportButton = new JButton("📊 Generate Report with Current Changes");
        reportButton.setToolTipText("See the carbon footprint report with your what-if changes");
        reportButton.setEnabled(scenarioActive);
        reportButton.setPreferredSize(new Dimension(500, 50));
        reportButton.addActionListener(e -> {
            mainDialog.dispose();
            generateWhatIfReport();
        });

        // Option 4: Compare Original vs What-If
        JButton compareButton = new JButton("⚖️ Compare Original vs What-If Results");
        compareButton.setToolTipText("Side-by-side comparison of original and modified scenarios");
        compareButton.setEnabled(scenarioActive);
        compareButton.setPreferredSize(new Dimension(500, 50));
        compareButton.addActionListener(e -> {
            mainDialog.setVisible(false);
            openComparisonView(() -> mainDialog.setVisible(true));
        });

        // Option 5: Restore Original Data
        JButton restoreButton = new JButton("🔄 Restore Original Data");
        restoreButton.setToolTipText("Go back to your real household data");
        restoreButton.setEnabled(scenarioActive);
        restoreButton.setPreferredSize(new Dimension(500, 50));
        restoreButton.addActionListener(e -> {
            restoreOriginalData();
            scenarioActive = false;
            JOptionPane.showMessageDialog(mainDialog,
                    "✅ Original household data restored!\nYou're now back to using real data.");
            mainDialog.dispose();
            openWhatIfWindow(); // Refresh the window
        });

        // Close button
        JButton closeButton = new JButton("❌ Close What-If Mode");
        closeButton.addActionListener(e -> {
            mainDialog.dispose();
            if (scenarioActive) {
                int choice = JOptionPane.showConfirmDialog(null,
                        "You have active What-If changes.\nDo you want to restore original data before closing?",
                        "Restore Original Data?", JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.YES_OPTION) {
                    restoreOriginalData();
                    scenarioActive = false;
                }
            }
        });

        optionsPanel.add(generalInfoButton);
        optionsPanel.add(applianceButton);
        optionsPanel.add(reportButton);
        optionsPanel.add(compareButton);
        optionsPanel.add(restoreButton);
        optionsPanel.add(closeButton);

        mainDialog.add(optionsPanel, BorderLayout.CENTER);
        mainDialog.setVisible(true);
    }

    // 🏠 GENERAL INFO EDITOR - Only Region and Tariff
    private static void openGeneralInfoEditor(Runnable onClose) {
        JDialog dialog = new JDialog((Frame) null, "Edit House Settings", true);
        dialog.setSize(400, 250);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setLocationRelativeTo(null);

        // Warning header
        JLabel warningLabel = new JLabel("<html><div style='text-align: center; color: orange; padding: 10px;'>" +
                "⚠️ These changes are temporary for What-If analysis only" +
                "</div></html>");
        dialog.add(warningLabel, BorderLayout.NORTH);

        House house = House.getInstance();

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.anchor = GridBagConstraints.WEST;

        // Region
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Region:"), gbc);
        gbc.gridx = 1;
        JComboBox<String> regionCombo = new JComboBox<>(new String[]{
                "Netherlands", "Germany", "France", "Belgium", "United Kingdom",
                "Spain", "Italy", "United States", "Canada", "Other"
        });
        regionCombo.setSelectedItem(house.getRegion());
        formPanel.add(regionCombo, gbc);

        // Electricity Tariff
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Electricity Tariff (EUR/kWh):"), gbc);
        gbc.gridx = 1;
        JTextField tariffField = new JTextField(String.valueOf(house.getElectricityTariff()), 15);
        formPanel.add(tariffField, gbc);

        dialog.add(formPanel, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton applyButton = new JButton("✅ Apply Changes");
        JButton cancelButton = new JButton("❌ Cancel");

        applyButton.addActionListener(e -> {
            try {
                // Validate and apply changes
                String region = (String) regionCombo.getSelectedItem();
                double tariff = Double.parseDouble(tariffField.getText());

                if (!Validator.validateElectricityTariff(tariff)) {
                    JOptionPane.showMessageDialog(dialog, "Invalid tariff. Must be between 0.05 and 0.50 EUR/kWh");
                    return;
                }

                // Apply changes to house
                house.setRegion(region);
                house.setTariff(tariff);

                JOptionPane.showMessageDialog(dialog,
                        "✅ What-If changes applied!\n" +
                                "Region: " + region + "\n" +
                                "Tariff: " + tariff + " EUR/kWh\n\n" +
                                "📊 Generate a report to see the impact of these changes.\n" +
                                "🔮 Remember: These are temporary changes for analysis only!");
                dialog.dispose();
                onClose.run();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "❌ Please enter a valid number for the tariff.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "❌ Error: " + ex.getMessage());
            }
        });

        cancelButton.addActionListener(e -> {
            dialog.dispose();
            openWhatIfWindow(); // Go back to main what-if menu
        });

        buttonPanel.add(applyButton);
        buttonPanel.add(cancelButton);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    // 🔌 APPLIANCE EDITOR
    private static void openApplianceEditor(Runnable onClose) {
        JDialog dialog = new JDialog((Frame) null, "Modify Appliances", true);
        dialog.setSize(800, 600);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setLocationRelativeTo(null);

        House house = House.getInstance();

        // Header with warning
        JLabel header = new JLabel("<html><div style='text-align: center;'>" +
                "<h3>🔌 Appliance Editor</h3>" +
                "<p>Modify appliance details to see their impact:</p>" +
                "<p style='color: orange;'>⚠️ Changes are temporary for What-If analysis only</p>" +
                "</div></html>");
        dialog.add(header, BorderLayout.NORTH);

        // Main panel with scroll
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        refreshApplianceList(mainPanel, house, dialog);

        dialog.add(scrollPane, BorderLayout.CENTER);

        // Bottom buttons
        JPanel bottomPanel = new JPanel(new FlowLayout());
        JButton addNewButton = new JButton("➕ Add Temporary Appliance");
        JButton showReportButton = new JButton("📊 Generate What-If Report");
        JButton closeButton = new JButton("❌ Back to What-If Menu");

        addNewButton.addActionListener(e -> {
            dialog.setVisible(false);
            addNewAppliance(() -> {
                refreshApplianceList(mainPanel, house, dialog);
                dialog.setVisible(true);
            });
        });

        showReportButton.addActionListener(e -> {
            dialog.dispose();
            generateWhatIfReport();
        });

        closeButton.addActionListener(e -> {
            dialog.dispose();
            onClose.run();
        });

        bottomPanel.add(addNewButton);
        bottomPanel.add(showReportButton);
        bottomPanel.add(closeButton);
        dialog.add(bottomPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    // Helper method to refresh appliance list
    private static void refreshApplianceList(JPanel mainPanel, House house, JDialog parentDialog) {
        mainPanel.removeAll();

        if (house.getAppliances().isEmpty()) {
            JLabel noAppliancesLabel = new JLabel("<html><div style='text-align: center; padding: 20px;'>" +
                    "No appliances found.<br>Add some appliances first using the original form." +
                    "</div></html>");
            noAppliancesLabel.setHorizontalAlignment(SwingConstants.CENTER);
            mainPanel.add(noAppliancesLabel);
        } else {
            for (Appliance appliance : house.getAppliances()) {
                JPanel appliancePanel = createAppliancePanel(appliance, house, mainPanel, parentDialog);
                mainPanel.add(appliancePanel);
                mainPanel.add(Box.createVerticalStrut(10));
            }
        }

        mainPanel.revalidate();
        mainPanel.repaint();
    }

    // Create individual appliance panel
    private static JPanel createAppliancePanel(Appliance appliance, House house, JPanel parentPanel, JDialog parentDialog) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(),
                "🔌 " + appliance.getName(),
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 14)
        ));

        // Info panel
        JPanel infoPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        infoPanel.add(new JLabel("Power Consumption (W):"));
        JTextField powerField = new JTextField(String.valueOf(appliance.getPowerConsumption()));
        infoPanel.add(powerField);

        infoPanel.add(new JLabel("Embodied Emissions (kg CO₂):"));
        JTextField emissionField = new JTextField(String.valueOf(appliance.getEmbodiedEmissions()));
        infoPanel.add(emissionField);

        infoPanel.add(new JLabel("Current Carbon Footprint:"));
        infoPanel.add(new JLabel(String.format("%.2f kg CO₂", appliance.getGeneratedFootprint())));

        panel.add(infoPanel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton updateButton = new JButton("✏️ Update");
        JButton removeButton = new JButton("🗑️ Remove");

        updateButton.addActionListener(e -> {
            try {
                double newPower = Double.parseDouble(powerField.getText());
                double newEmission = Double.parseDouble(emissionField.getText());

                if (!Validator.validatePowerConsumption((int) newPower)) {
                    JOptionPane.showMessageDialog(panel, "Power consumption must be between 10-5000W");
                    return;
                }
                if (!Validator.validateEmbodiedEmissions(newEmission)) {
                    JOptionPane.showMessageDialog(panel, "Embodied emissions must be between 10-500 kg CO₂");
                    return;
                }

                // Store original values for potential restoration
                double originalPower = appliance.getPowerConsumption();
                double originalEmission = appliance.getEmbodiedEmissions();

                // Update appliance properties directly (temporary for What-If)
                try {
                    // Using reflection since we don't know if setters exist
                    appliance.getClass().getMethod("setPowerConsumption", int.class).invoke(appliance, (int) newPower);
                } catch (Exception ex) {
                    // If setter doesn't exist, use reflection to access field directly
                    try {
                        java.lang.reflect.Field powerField1 = appliance.getClass().getDeclaredField("powerConsumption");
                        powerField1.setAccessible(true);
                        powerField1.set(appliance, (int) newPower);
                    } catch (Exception ex2) {
                        System.err.println("Could not update power consumption: " + ex2.getMessage());
                    }
                }

                try {
                    appliance.getClass().getMethod("setEmbodiedEmissions", double.class).invoke(appliance, newEmission);
                } catch (Exception ex) {
                    try {
                        java.lang.reflect.Field emissionField1 = appliance.getClass().getDeclaredField("embodiedEmissions");
                        emissionField1.setAccessible(true);
                        emissionField1.set(appliance, newEmission);
                    } catch (Exception ex2) {
                        System.err.println("Could not update embodied emissions: " + ex2.getMessage());
                    }
                }

                JOptionPane.showMessageDialog(panel,
                        "✅ What-If changes applied to " + appliance.getName() + "!\n" +
                                "New Power: " + (int)newPower + "W\n" +
                                "New Emission: " + newEmission + " kg CO₂\n\n" +
                                "🔮 These are temporary changes for analysis.\n" +
                                "📊 Generate a report to see the impact!");

                // Refresh the display
                refreshApplianceList(parentPanel, house, parentDialog);

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(panel, "❌ Please enter valid numbers");
            }
        });

        removeButton.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(panel,
                    "Are you sure you want to temporarily remove " + appliance.getName() + "?\n" +
                            "This is only for What-If analysis - not permanently deleted.",
                    "Confirm Temporary Removal", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                // Remove associated timeframes first
                List<Timeframe> timeframesToRemove = new ArrayList<>();
                for (Timeframe tf : house.getTimeframes()) {
                    if (tf.getAppliance().equals(appliance)) {
                        timeframesToRemove.add(tf);
                    }
                }
                for (Timeframe tf : timeframesToRemove) {
                    house.getTimeframes().remove(tf);
                }

                // Remove the appliance (temporarily)
                house.getAppliances().remove(appliance);

                JOptionPane.showMessageDialog(panel,
                        "✅ " + appliance.getName() + " temporarily removed!\n" +
                                "🔮 This is a What-If scenario change.\n" +
                                "🔄 Use 'Restore Original Data' to get it back.");

                // Refresh the display
                refreshApplianceList(parentPanel, house, parentDialog);
            }
        });

        buttonPanel.add(updateButton);
        buttonPanel.add(removeButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    // Add new appliance for What-If scenario (temporary only)
    private static void addNewAppliance(Runnable onComplete) {
        JDialog addDialog = new JDialog((Frame) null, "Add Temporary Appliance", true);
        addDialog.setSize(400, 300);
        addDialog.setLayout(new BorderLayout(10, 10));
        addDialog.setLocationRelativeTo(null);

        // Header warning
        JLabel warningLabel = new JLabel("<html><div style='text-align: center; color: orange;'>" +
                "⚠️ This appliance will be added temporarily for What-If analysis only<br>" +
                "It will not be permanently saved to your household data" +
                "</div></html>");
        warningLabel.setHorizontalAlignment(SwingConstants.CENTER);
        addDialog.add(warningLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        // Name field
        formPanel.add(new JLabel("Appliance Name:"));
        JTextField nameField = new JTextField();
        formPanel.add(nameField);

        // Power consumption field
        formPanel.add(new JLabel("Power Consumption (W):"));
        JTextField powerField = new JTextField();
        formPanel.add(powerField);

        // Embodied emissions field
        formPanel.add(new JLabel("Embodied Emissions (kg CO₂):"));
        JTextField emissionsField = new JTextField();
        formPanel.add(emissionsField);

        addDialog.add(formPanel, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton addButton = new JButton("✅ Add Temporary Appliance");
        JButton cancelButton = new JButton("❌ Cancel");

        addButton.addActionListener(e -> {
            try {
                String name = nameField.getText().trim();
                String powerStr = powerField.getText().trim();
                String emissionsStr = emissionsField.getText().trim();

                if (name.isEmpty() || powerStr.isEmpty() || emissionsStr.isEmpty()) {
                    JOptionPane.showMessageDialog(addDialog, "All fields must be filled.");
                    return;
                }

                int power = Integer.parseInt(powerStr);
                double emissions = Double.parseDouble(emissionsStr);

                if (!Validator.validatePowerConsumption(power)) {
                    JOptionPane.showMessageDialog(addDialog, "Power consumption must be between 10 and 5000 W.");
                    return;
                }
                if (!Validator.validateEmbodiedEmissions(emissions)) {
                    JOptionPane.showMessageDialog(addDialog, "Embodied emissions must be between 10 and 500 kg CO₂e.");
                    return;
                }

                // Create and add new appliance (temporarily)
                Appliance newAppliance = new Appliance(name, power, emissions);
                House.getInstance().getAppliances().add(newAppliance);

                JOptionPane.showMessageDialog(addDialog,
                        "✅ Temporary appliance '" + name + "' added!\n" +
                                "Power: " + power + "W\n" +
                                "Emissions: " + emissions + " kg CO₂\n\n" +
                                "🔮 This is a What-If scenario change.\n" +
                                "📊 Generate a report to see the impact!");

                addDialog.dispose();
                onComplete.run();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(addDialog, "❌ Please enter valid numbers for power and emissions.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(addDialog, "❌ Error adding appliance: " + ex.getMessage());
            }
        });

        cancelButton.addActionListener(e -> {
            addDialog.dispose();
            onComplete.run();
        });

        buttonPanel.add(addButton);
        buttonPanel.add(cancelButton);
        addDialog.add(buttonPanel, BorderLayout.SOUTH);

        addDialog.setVisible(true);
    }

    // 🔄 BACKUP AND RESTORE FUNCTIONS - FIXED FOR PRIVATE CONSTRUCTOR
    private static void createHouseBackup() {
        try {
            House original = House.getInstance();

            // Since House constructor is private, we'll store the data in simple structures
            originalHouseBackup = new HouseBackup(
                    original.getRegion(),
                    original.getElectricityTariff(),
                    original.getStart(),
                    original.getEnd(),
                    new ArrayList<>(original.getResidents()),
                    new ArrayList<>(original.getAppliances()),
                    new ArrayList<>(original.getTimeframes())
            );

            System.out.println("✅ House backup created successfully");
        } catch (Exception e) {
            System.err.println("⚠️ Error creating house backup: " + e.getMessage());
        }
    }

    private static void restoreOriginalData() {
        if (originalHouseBackup != null) {
            try {
                House current = House.getInstance();

                // Restore basic properties
                current.setRegion(originalHouseBackup.region);
                current.setTariff(originalHouseBackup.electricityTariff);
                current.setStart(originalHouseBackup.startDate);
                current.setEnd(originalHouseBackup.endDate);

                // Clear current collections
                current.getAppliances().clear();
                current.getTimeframes().clear();
                current.getResidents().clear();

                // Restore users
                for (User originalUser : originalHouseBackup.residents) {
                    User newUser = new User(originalUser.getName());
                    current.getResidents().add(newUser);
                }

                // Restore appliances
                for (Appliance originalAppliance : originalHouseBackup.appliances) {
                    Appliance newAppliance = new Appliance(
                            originalAppliance.getName(),
                            originalAppliance.getPowerConsumption(),
                            originalAppliance.getEmbodiedEmissions()
                    );
                    current.getAppliances().add(newAppliance);
                }

                // Restore timeframes using the Timeframe constructor
                for (Timeframe originalTimeframe : originalHouseBackup.timeframes) {
                    // Find matching appliance in restored house
                    Appliance matchedAppliance = null;
                    for (Appliance app : current.getAppliances()) {
                        if (app.getName().equals(originalTimeframe.getAppliance().getName())) {
                            matchedAppliance = app;
                            break;
                        }
                    }

                    if (matchedAppliance != null) {
                        // Find matching users in restored house
                        List<User> matchedUsers = new ArrayList<>();
                        for (User originalUser : originalTimeframe.getUsers()) {
                            for (User user : current.getResidents()) {
                                if (user.getName().equals(originalUser.getName())) {
                                    matchedUsers.add(user);
                                    break;
                                }
                            }
                        }

                        // Create new timeframe using the public constructor
                        Timeframe restoredTimeframe = new Timeframe(
                                matchedUsers,
                                matchedAppliance,
                                originalTimeframe.getStart(),
                                originalTimeframe.getEnd()
                        );
                        current.getTimeframes().add(restoredTimeframe);
                    }
                }

                System.out.println("🔄 Original house data restored successfully");

            } catch (Exception e) {
                System.err.println("⚠️ Error restoring original data: " + e.getMessage());
                e.printStackTrace();
                JOptionPane.showMessageDialog(null,
                        "Error restoring original data: " + e.getMessage() +
                                "\nYou may need to restart the application.");
            }
        } else {
            System.err.println("⚠️ No original house data available to restore");
            JOptionPane.showMessageDialog(null,
                    "No original data available to restore. You may need to restart the application.");
        }
    }

    // 🆕 SIMPLE BACKUP CLASS - Since we can't instantiate House directly
    private static class HouseBackup {
        public final String region;
        public final double electricityTariff;
        public final DateTime startDate;
        public final DateTime endDate;
        public final List<User> residents;
        public final List<Appliance> appliances;
        public final List<Timeframe> timeframes;

        public HouseBackup(String region, double electricityTariff, DateTime startDate, DateTime endDate,
                           List<User> residents, List<Appliance> appliances, List<Timeframe> timeframes) {
            this.region = region;
            this.electricityTariff = electricityTariff;
            this.startDate = startDate;
            this.endDate = endDate;
            this.residents = new ArrayList<>(residents);
            this.appliances = new ArrayList<>(appliances);
            this.timeframes = new ArrayList<>(timeframes);
        }

        public double getFootPrint() {
            double totalFootPrint = 0.0;
            for (Appliance appliance : appliances) {
                if (appliance != null) {
                    totalFootPrint += appliance.getGeneratedFootprint();
                }
            }
            return totalFootPrint;
        }

        public double getCosts() {
            double totalCost = 0.0;
            for (Appliance appliance : appliances) {
                if (appliance != null) {
                    totalCost += appliance.getGeneratedCost();
                }
            }
            return totalCost;
        }

        public int getEcoScore() {
            // Simple eco score calculation similar to House
            double totalFootPrint = Math.max(getFootPrint(), 0.1);
            double ecoScore = 100 / (1 + Math.exp(0.04 * (totalFootPrint - 1125)));
            return (int) Math.max(0, Math.min(100, ecoScore));
        }
    }

    // 📊 GENERATE WHAT-IF REPORT
    private static void generateWhatIfReport() {
        JOptionPane.showMessageDialog(null,
                "📊 Generating What-If Report...\n\n" +
                        "This report shows the impact of your temporary changes.\n" +
                        "🔮 Changes are NOT permanently saved!\n" +
                        "🔄 Use 'Restore Original Data' to return to normal.",
                "What-If Report", JOptionPane.INFORMATION_MESSAGE);

        Report.main(new String[]{});
    }

    // 📊 SHOW RESULTS AND OPTIONS AFTER WHAT-IF CHANGES
    private static void showResultsAndOptions() {
        JDialog resultsDialog = new JDialog((Frame) null, "What-If Results", true);
        resultsDialog.setSize(600, 400);
        resultsDialog.setLayout(new BorderLayout(10, 10));
        resultsDialog.setLocationRelativeTo(null);

        // Header
        JLabel headerLabel = new JLabel("<html><div style='text-align: center; padding: 10px;'>" +
                "<h2>🎯 What-If Changes Applied!</h2>" +
                "<p style='color: orange;'>You're now in scenario mode with fictional changes</p>" +
                "</div></html>");
        headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        resultsDialog.add(headerLabel, BorderLayout.NORTH);

        // Quick summary
        House house = House.getInstance();
        JTextArea summaryArea = new JTextArea();
        summaryArea.setEditable(false);
        summaryArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        summaryArea.setText(
                "📋 CURRENT SCENARIO SUMMARY:\n" +
                        "═══════════════════════════════════════\n" +
                        "Region: " + house.getRegion() + "\n" +
                        "Tariff: " + house.getElectricityTariff() + " EUR/kWh\n" +
                        "Appliances: " + house.getAppliances().size() + " devices\n" +
                        "Users: " + house.getResidents().size() + " people\n" +
                        "Current Carbon Footprint: " + String.format("%.2f kg CO₂", house.getFootPrint()) + "\n" +
                        "Current Eco-Score: " + house.getEcoScore() + "/100\n\n" +
                        "💡 These are FICTIONAL changes for testing!\n" +
                        "Choose an option below to see the impact:"
        );
        JScrollPane summaryScroll = new JScrollPane(summaryArea);
        resultsDialog.add(summaryScroll, BorderLayout.CENTER);

        // Action buttons
        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton reportButton = new JButton("📊 Generate What-If Report");
        reportButton.addActionListener(e -> {
            resultsDialog.dispose();
            generateWhatIfReport();
        });

        JButton compareButton = new JButton("⚖️ Compare with Original");
        compareButton.addActionListener(e -> {
            resultsDialog.setVisible(false);
            openComparisonView(() -> resultsDialog.setVisible(true));
        });

        JButton moreChangesButton = new JButton("🔧 Make More Changes");
        moreChangesButton.addActionListener(e -> {
            resultsDialog.dispose();
            openWhatIfWindow();
        });

        JButton restoreButton = new JButton("🔄 Restore & Exit");
        restoreButton.addActionListener(e -> {
            restoreOriginalData();
            scenarioActive = false;
            resultsDialog.dispose();
            JOptionPane.showMessageDialog(null, "✅ Original data restored. Back to normal mode.");
        });

        buttonPanel.add(reportButton);
        buttonPanel.add(compareButton);
        buttonPanel.add(moreChangesButton);
        buttonPanel.add(restoreButton);

        resultsDialog.add(buttonPanel, BorderLayout.SOUTH);
        resultsDialog.setVisible(true);
    }

    // 📊 COMPARISON VIEW - Original vs What-If
    private static void openComparisonView(Runnable onClose) {
        JDialog dialog = new JDialog((Frame) null, "⚖️ Original vs What-If Comparison", true);
        dialog.setSize(900, 600);
        dialog.setLayout(new BorderLayout());
        dialog.setLocationRelativeTo(null);

        // Header
        JLabel headerLabel = new JLabel("<html><div style='text-align: center; padding: 10px;'>" +
                "<h2>⚖️ Impact Analysis</h2>" +
                "<p>Compare your original household with the what-if scenario</p>" +
                "</div></html>");
        headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        dialog.add(headerLabel, BorderLayout.NORTH);

        JPanel comparisonPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        comparisonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Original data
        JTextArea originalArea = new JTextArea();
        originalArea.setEditable(false);
        originalArea.setFont(new Font("Monospaced", Font.PLAIN, 11));
        originalArea.setBorder(BorderFactory.createTitledBorder("📋 ORIGINAL DATA"));

        if (originalHouseBackup != null) {
            originalArea.setText("🏠 ORIGINAL HOUSEHOLD:\n" +
                    "═══════════════════════════\n" +
                    "Region: " + originalHouseBackup.region + "\n" +
                    "Tariff: " + originalHouseBackup.electricityTariff + " EUR/kWh\n" +
                    "Appliances: " + originalHouseBackup.appliances.size() + " devices\n" +
                    "Users: " + originalHouseBackup.residents.size() + " people\n\n" +
                    "📊 ORIGINAL METRICS:\n" +
                    "Carbon Footprint: " + String.format("%.2f kg CO₂", originalHouseBackup.getFootPrint()) + "\n" +
                    "Energy Costs: " + String.format("%.2f EUR", originalHouseBackup.getCosts()) + "\n" +
                    "Eco-Score: " + originalHouseBackup.getEcoScore() + "/100\n\n" +
                    "🔌 ORIGINAL APPLIANCES:\n");

            for (Appliance app : originalHouseBackup.appliances) {
                originalArea.append(String.format("• %-15s: %6.2f kg CO₂\n",
                        app.getName(), app.getGeneratedFootprint()));
            }
        } else {
            originalArea.setText("No original data available");
        }

        // Current (what-if) data
        JTextArea currentArea = new JTextArea();
        currentArea.setEditable(false);
        currentArea.setFont(new Font("Monospaced", Font.PLAIN, 11));
        currentArea.setBorder(BorderFactory.createTitledBorder("🔮 WHAT-IF SCENARIO"));

        House current = House.getInstance();
        currentArea.setText("🏠 MODIFIED HOUSEHOLD:\n" +
                "═══════════════════════════\n" +
                "Region: " + current.getRegion() + "\n" +
                "Tariff: " + current.getElectricityTariff() + " EUR/kWh\n" +
                "Appliances: " + current.getAppliances().size() + " devices\n" +
                "Users: " + current.getResidents().size() + " people\n\n" +
                "📊 CURRENT METRICS:\n" +
                "Carbon Footprint: " + String.format("%.2f kg CO₂", current.getFootPrint()) + "\n" +
                "Energy Costs: " + String.format("%.2f EUR", current.getCosts()) + "\n" +
                "Eco-Score: " + current.getEcoScore() + "/100\n\n" +
                "🔌 CURRENT APPLIANCES:\n");

        for (Appliance app : current.getAppliances()) {
            currentArea.append(String.format("• %-15s: %6.2f kg CO₂\n",
                    app.getName(), app.getGeneratedFootprint()));
        }

        // Add comparison summary
        if (originalHouseBackup != null) {
            double footprintDiff = current.getFootPrint() - originalHouseBackup.getFootPrint();
            double costDiff = current.getCosts() - originalHouseBackup.getCosts();
            int scoreDiff = current.getEcoScore() - originalHouseBackup.getEcoScore();

            currentArea.append("\n🔍 CHANGES FROM ORIGINAL:\n");
            currentArea.append(String.format("Carbon Footprint: %+.2f kg CO₂ (%s)\n",
                    footprintDiff, footprintDiff > 0 ? "⬆️ increased" : "⬇️ decreased"));
            currentArea.append(String.format("Energy Costs: %+.2f EUR (%s)\n",
                    costDiff, costDiff > 0 ? "⬆️ increased" : "⬇️ decreased"));
            currentArea.append(String.format("Eco-Score: %+d points (%s)\n",
                    scoreDiff, scoreDiff > 0 ? "⬆️ improved" : "⬇️ worsened"));
        }

        comparisonPanel.add(new JScrollPane(originalArea));
        comparisonPanel.add(new JScrollPane(currentArea));

        dialog.add(comparisonPanel, BorderLayout.CENTER);

        // Bottom buttons
        JPanel bottomPanel = new JPanel(new FlowLayout());
        JButton generateReportButton = new JButton("📊 Generate What-If Report");
        JButton restoreButton = new JButton("🔄 Restore Original");
        JButton closeButton = new JButton("❌ Close");

        generateReportButton.addActionListener(e -> {
            dialog.dispose();
            generateWhatIfReport();
        });

        restoreButton.addActionListener(e -> {
            restoreOriginalData();
            scenarioActive = false;
            JOptionPane.showMessageDialog(dialog, "✅ Original data restored!");
            dialog.dispose();
            onClose.run();
        });

        closeButton.addActionListener(e -> {
            dialog.dispose();
            onClose.run();
        });

        bottomPanel.add(generateReportButton);
        bottomPanel.add(restoreButton);
        bottomPanel.add(closeButton);
        dialog.add(bottomPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    // 🆕 PUBLIC UTILITY METHODS
    public static boolean isScenarioActive() {
        return scenarioActive;
    }

    public static void resetWhatIfMode() {
        if (scenarioActive) {
            restoreOriginalData();
            scenarioActive = false;
        }
        originalHouseBackup = null;
    }
}