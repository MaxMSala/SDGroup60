package greenhome.input;


import greenhome.household.*;

import greenhome.validation.Validator;
import greenhome.reporting.Report;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class WhatIfScenarios {

    public static void openWhatIfWindow() {
        JDialog mainDialog = new JDialog((Frame) null, "🔮 What-If Scenarios", true);
        mainDialog.setSize(600, 300);
        mainDialog.setLayout(new BorderLayout(10, 10));
        mainDialog.setLocationRelativeTo(null);


        JLabel headerLabel = new JLabel("<html><div style='text-align: center; padding: 10px;'>" +
                "<h2>🔮 What-If Scenarios</h2>" +
                "<p>Test fictional changes and see their impact on your carbon footprint</p>" +
                "<p style='color: orange;'>⚠️ Changes are temporary and will not be saved</p>" +
                "</div></html>");
        headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainDialog.add(headerLabel, BorderLayout.NORTH);


        JPanel optionsPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        optionsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));


        JButton generalInfoButton = new JButton("🏠 Change Region & Tariff");
        generalInfoButton.setPreferredSize(new Dimension(500, 50));
        generalInfoButton.addActionListener(e -> {
            mainDialog.setVisible(false);
            openGeneralInfoEditor(() -> mainDialog.setVisible(true));
        });


        JButton applianceButton = new JButton("🔌 Change Appliances");
        applianceButton.setPreferredSize(new Dimension(500, 50));
        applianceButton.addActionListener(e -> {
            mainDialog.setVisible(false);
            openApplianceEditor(() -> mainDialog.setVisible(true));
        });

        JButton reportButton = new JButton("📊 Generate What-If Report");
        reportButton.setPreferredSize(new Dimension(500, 50));
        reportButton.addActionListener(e -> {
            mainDialog.dispose();
            JOptionPane.showMessageDialog(null,
                    "📊 Generating What-If Report...\n\n" +
                            "This report shows your temporary changes.\n" +
                            "Changes will not be permanently saved!",
                    "What-If Report", JOptionPane.INFORMATION_MESSAGE);
            Report.main(new String[]{});
        });

        JButton closeButton = new JButton("❌ Close");
        closeButton.addActionListener(e -> mainDialog.dispose());

        optionsPanel.add(generalInfoButton);
        optionsPanel.add(applianceButton);
        optionsPanel.add(reportButton);
        optionsPanel.add(closeButton);

        mainDialog.add(optionsPanel, BorderLayout.CENTER);
        mainDialog.setVisible(true);
    }


    private static void openGeneralInfoEditor(Runnable onClose) {
        JDialog dialog = new JDialog((Frame) null, "Change Region & Tariff", true);
        dialog.setSize(400, 200);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setLocationRelativeTo(null);

        House house = House.getInstance();

        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        formPanel.add(new JLabel("Region:"));
        JComboBox<String> regionCombo = new JComboBox<>(new String[]{
                "Netherlands", "Germany", "France", "Belgium", "United Kingdom",
                "Spain", "Italy", "United States", "Canada", "Other"
        });
        regionCombo.setSelectedItem(house.getRegion());
        formPanel.add(regionCombo);

        formPanel.add(new JLabel("Electricity Tariff (EUR/kWh):"));
        JTextField tariffField = new JTextField(String.valueOf(house.getElectricityTariff()));
        formPanel.add(tariffField);

        dialog.add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton applyButton = new JButton("✅ Apply Changes");
        JButton cancelButton = new JButton("❌ Cancel");

        applyButton.addActionListener(e -> {
            try {
                String region = (String) regionCombo.getSelectedItem();
                double tariff = Double.parseDouble(tariffField.getText());

                if (!Validator.validateElectricityTariff(tariff)) {
                    JOptionPane.showMessageDialog(dialog, "Invalid tariff. Must be between 0.05 and 0.50 EUR/kWh");
                    return;
                }

                house.setRegion(region);
                house.setTariff(tariff);

                JOptionPane.showMessageDialog(dialog,
                        "✅ Changes applied!\n" +
                                "Region: " + region + "\n" +
                                "Tariff: " + tariff + " EUR/kWh\n\n" +
                                "Generate a report to see the impact.");
                dialog.dispose();
                onClose.run();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "❌ Please enter a valid number for the tariff.");
            }
        });

        cancelButton.addActionListener(e -> {
            dialog.dispose();
            onClose.run();
        });

        buttonPanel.add(applyButton);
        buttonPanel.add(cancelButton);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    private static void openApplianceEditor(Runnable onClose) {
        JDialog dialog = new JDialog((Frame) null, "Change Appliances", true);
        dialog.setSize(800, 600);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setLocationRelativeTo(null);

        House house = House.getInstance();

        JLabel header = new JLabel("<html><div style='text-align: center;'>" +
                "<h3>🔌 Appliance Editor</h3>" +
                "<p>Modify your appliances to see their impact</p>" +
                "</div></html>");
        dialog.add(header, BorderLayout.NORTH);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(mainPanel);

        refreshApplianceList(mainPanel, house);

        dialog.add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout());
       JButton closeButton = new JButton("✅ Done");


        closeButton.addActionListener(e -> {
            dialog.dispose();
            onClose.run();
        });

        bottomPanel.add(closeButton);
        dialog.add(bottomPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }


    private static void refreshApplianceList(JPanel mainPanel, House house) {
        mainPanel.removeAll();

        if (house.getAppliances().isEmpty()) {
            JLabel noAppliancesLabel = new JLabel("<html><div style='text-align: center; padding: 20px;'>" +
                    "No appliances found.<br>Add appliances using the original form first." +
                    "</div></html>");
            noAppliancesLabel.setHorizontalAlignment(SwingConstants.CENTER);
            mainPanel.add(noAppliancesLabel);
        } else {
            for (Appliance appliance : house.getAppliances()) {
                JPanel appliancePanel = createAppliancePanel(appliance, house, mainPanel);
                mainPanel.add(appliancePanel);
                mainPanel.add(Box.createVerticalStrut(10));
            }
        }

        mainPanel.revalidate();
        mainPanel.repaint();
    }


    private static JPanel createAppliancePanel(Appliance appliance, House house, JPanel parentPanel) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(),
                "🔌 " + appliance.getName(),
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 14)
        ));


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
                    JOptionPane.showMessageDialog(panel, "Embodied emissions must be between 10-2000 kg CO₂");
                    return;
                }


                try {
                    appliance.getClass().getMethod("setPowerConsumption", int.class).invoke(appliance, (int) newPower);
                    appliance.getClass().getMethod("setEmbodiedEmissions", double.class).invoke(appliance, newEmission);
                } catch (Exception ex) {

                    try {
                        java.lang.reflect.Field powerField1 = appliance.getClass().getDeclaredField("powerConsumption");
                        powerField1.setAccessible(true);
                        powerField1.set(appliance, (int) newPower);

                        java.lang.reflect.Field emissionField1 = appliance.getClass().getDeclaredField("embodiedEmissions");
                        emissionField1.setAccessible(true);
                        emissionField1.set(appliance, newEmission);
                    } catch (Exception ex2) {
                        System.err.println("Could not update appliance: " + ex2.getMessage());
                    }
                }

                JOptionPane.showMessageDialog(panel,
                        "✅ Changes applied to " + appliance.getName() + "!\n" +
                                "New Power: " + (int)newPower + "W\n" +
                                "New Emission: " + newEmission + " kg CO₂");

                refreshApplianceList(parentPanel, house);

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(panel, "❌ Please enter valid numbers");
            }
        });

        removeButton.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(panel,
                    "Remove " + appliance.getName() + "?",
                    "Confirm Removal", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {

                house.getTimeframes().removeIf(tf -> tf.getAppliance().equals(appliance));

                house.getAppliances().remove(appliance);

                JOptionPane.showMessageDialog(panel, "✅ " + appliance.getName() + " removed!");
                refreshApplianceList(parentPanel, house);
            }
        });

        buttonPanel.add(updateButton);
        buttonPanel.add(removeButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }


}