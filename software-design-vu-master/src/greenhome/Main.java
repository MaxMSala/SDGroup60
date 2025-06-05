package greenhome;
import greenhome.household.Appliance;
import greenhome.household.House;
import greenhome.household.Parser;
import greenhome.household.User;
import greenhome.input.Form;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        showStartupChoice();
    }

    private static void showStartupChoice() {
        boolean hasData = checkExistingData();
        if (!hasData) {
            startFresh();
            return;
        }

        JDialog dialog = new JDialog((Frame) null, "GreenHome - Data Options", true);
        dialog.setSize(450, 300);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setLocationRelativeTo(null);
        dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        JLabel header = new JLabel("<html><h2>Welcome back to GreenHome!</h2>" +
                "<p>Previous data found. What would you like to do?</p></html>");
        header.setHorizontalAlignment(SwingConstants.CENTER);
        header.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        dialog.add(header, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 15, 15));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JButton keepButton = new JButton("<html><b>📊 Continue with Previous Data</b><br>" +
                "<small>Keep your existing household setup</small></html>");
        keepButton.setPreferredSize(new Dimension(350, 60));
        keepButton.addActionListener(e -> {
            dialog.dispose();
            continueWithData();
        });

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
                showStartupChoice();
            }
        });

        buttonPanel.add(keepButton);
        buttonPanel.add(freshButton);
        dialog.add(buttonPanel, BorderLayout.CENTER);

//        String preview = getDataPreview();
//        JLabel previewLabel = new JLabel("<html><small><b>Current data:</b> " +
//                preview + "</small></html>");
//        previewLabel.setHorizontalAlignment(SwingConstants.CENTER);
//        previewLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 15, 10));
//        dialog.add(previewLabel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    private static boolean checkExistingData() {
        try {
            Parser.loadHouse("json.json");
            House house = House.getInstance();
            return house != null &&
                    (!house.getAppliances().isEmpty() || !house.getResidents().isEmpty());
        } catch (IOException e) {
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

            // Filter unique appliances by name
            Set<String> uniqueApplianceNames = new HashSet<>();
            for (Appliance appliance : house.getAppliances()) {
                if (appliance != null) {
                    uniqueApplianceNames.add(appliance.getName());
                }
            }

            // Filter unique users by name
            Set<String> uniqueUserNames = new HashSet<>();
            for (User user : house.getResidents()) {
                if (user != null) {
                    uniqueUserNames.add(user.getName());
                }
            }

            JOptionPane.showMessageDialog(null,
                    "Loaded: " + uniqueApplianceNames.size() + " appliances, " +
                            uniqueUserNames.size() + " users",
                    "Data Loaded", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error loading data. Starting fresh.");
            startFresh();
            return;
        }

        Form.main(new String[]{});
    }


    private static void startFresh() {
        resetHouse();
        House.constructInstance(
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                "Netherlands",
                0.25
        );
        JOptionPane.showMessageDialog(null, "Starting with fresh data!");
        Form.main(new String[]{});
    }

    private static void resetHouse() {
        try {
            java.lang.reflect.Field field = House.class.getDeclaredField("instance");
            field.setAccessible(true);
            field.set(null, null);
        } catch (Exception ignored) {
        }
    }
}
