package greenhome.input;

import com.toedter.calendar.JDateChooser;
import greenhome.household.Appliance;
import greenhome.household.House;
import greenhome.household.Parser;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class WhatIfScenarios {
    private static final House house = House.getInstance();
    private static final Map<String, Double> simulatedChanges = new HashMap<>();
    private static final Map<String, List<String>> applianceTimeframes = new HashMap<>();
    private static String houseInfoString = null;

    public static void openWhatIfWindow() {
        JDialog whatIfFrame = new JDialog((Frame) null, "What If Scenarios", true);
        whatIfFrame.setSize(400, 250);
        whatIfFrame.setLayout(new GridLayout(4, 1, 10, 10));
        whatIfFrame.setLocationRelativeTo(null);

        JLabel instruction = new JLabel("Choose a What-If scenario type:", SwingConstants.CENTER);
        JButton generalInfoButton = new JButton("Change General Info");
        JButton applianceButton = new JButton("Appliance-Specific Changes");
        JButton submitAllButton = new JButton("Submit All to Parser");

        whatIfFrame.add(instruction);
        whatIfFrame.add(generalInfoButton);
        whatIfFrame.add(applianceButton);
        whatIfFrame.add(submitAllButton);

        generalInfoButton.addActionListener(e -> {
            whatIfFrame.dispose();
            openGeneralChangesWindow();
        });

        applianceButton.addActionListener(e -> {
            whatIfFrame.dispose();
            openApplianceScenarioWindow();
        });

        submitAllButton.addActionListener(e -> {
            StringBuilder sb = new StringBuilder();
            sb.append("----- GreenHome Data Report -----\n\n");

            // HOUSE INFO
            sb.append("HOUSE INFO\n");
            sb.append(houseInfoString != null ? houseInfoString : "No house information provided.\n");

            // USERS
            sb.append("\nUSERS\n");
            Set<String> users = new HashSet<>();
            for (List<String> list : applianceTimeframes.values()) {
                for (String tf : list) {
                    int start = tf.indexOf("User: ") + 6;
                    int end = tf.indexOf(",", start);
                    if (start > 5 && end > start) {
                        users.add(tf.substring(start, end).trim());
                    }
                }
            }
            if (users.isEmpty()) {
                sb.append("No users added.\n");
            } else {
                for (String u : users) {
                    sb.append("- ").append(u).append("\n");
                }
            }

            // APPLIANCES
            sb.append("\nAPPLIANCES\n");
            List<Appliance> applianceList = new ArrayList<>(house.getAppliances());
            if (applianceList.isEmpty()) {
                sb.append("No appliances added.\n");
            } else {
                for (int i = 0; i < applianceList.size(); i++) {
                    Appliance a = applianceList.get(i);
                    String name = a.getName();
                    sb.append("Appliance ").append(i + 1).append(":\n");
                    sb.append("Name: ").append(name).append("\n");
                    sb.append("Power Consumption: N/A\n");
                    sb.append("Embodied Emission: N/A\n");

                    if (simulatedChanges.containsKey(name)) {
                        sb.append("What-If Emission: ").append(simulatedChanges.get(name)).append(" kg CO2\n");
                    } else {
                        sb.append("Original Emission: ").append(a.getGeneratedFootprint()).append(" kg CO2\n");
                    }
                }
            }

            // TIMEFRAMES PER APPLIANCE
            sb.append("\nTIMEFRAMES PER APPLIANCE\n");
            if (applianceTimeframes.isEmpty()) {
                sb.append("No usage timeframes recorded.\n");
            } else {
                for (Map.Entry<String, List<String>> entry : applianceTimeframes.entrySet()) {
                    sb.append("Appliance: ").append(entry.getKey()).append("\n");
                    for (String tf : entry.getValue()) {
                        sb.append("  - ").append(tf).append("\n");
                    }
                }
            }

            sb.append("\n----- End of Report -----");

            String finalResult = sb.toString();
            System.out.println("FINAL WHAT-IF SUBMISSION:\n" + finalResult);

            JOptionPane.showMessageDialog(null, new JScrollPane(new JTextArea(finalResult)),
                    "Final What-If Submission", JOptionPane.INFORMATION_MESSAGE);

            Parser.whatifStringModHouse(finalResult);
        });


        whatIfFrame.setVisible(true);
    }
    public static void openApplianceScenarioWindow() {
        JFrame frame = new JFrame("What If: Appliance Changes");
        frame.setSize(750, 600);
        frame.setLayout(new BorderLayout(10, 10));
        frame.setLocationRelativeTo(null);

        House house = House.getInstance();
        List<Appliance> appliances = house.getAppliances();

        if (appliances == null || appliances.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No appliances found.");
            return;
        }

        Map<String, Double> simulatedChanges = new HashMap<>();
        Map<String, java.util.List<String>> applianceTimeframes = new HashMap<>();
        for (Appliance appliance : appliances) {
            applianceTimeframes.put(appliance.getName(), new ArrayList<>());
        }

        JPanel topPanel = new JPanel(new GridLayout(3, 2, 10, 10));

        JLabel applianceLabel = new JLabel("Select Appliance:");
        JComboBox<String> applianceDropdown = new JComboBox<>();
        for (Appliance a : appliances) {
            applianceDropdown.addItem(a.getName());
        }

        JLabel originalEmissionLabel = new JLabel("N/A");
        JLabel newEmissionLabel = new JLabel("New Emission (kg CO2):");
        JTextField newEmissionField = new JTextField();

        topPanel.add(applianceLabel);
        topPanel.add(applianceDropdown);
        topPanel.add(new JLabel("Original Emission:"));
        topPanel.add(originalEmissionLabel);
        topPanel.add(newEmissionLabel);
        topPanel.add(newEmissionField);

        JPanel emissionPanel = new JPanel(new BorderLayout());
        emissionPanel.add(topPanel, BorderLayout.CENTER);

        DefaultListModel<String> timeframeModel = new DefaultListModel<>();
        JList<String> timeframeList = new JList<>(timeframeModel);
        JScrollPane timeframeScroll = new JScrollPane(timeframeList);

        JPanel timeframeButtons = new JPanel(new GridLayout(3, 1, 5, 5));
        JButton addTimeframe = new JButton("Add Timeframe");
        JButton editTimeframe = new JButton("Edit Timeframe");
        JButton deleteTimeframe = new JButton("Delete Timeframe");
        timeframeButtons.add(addTimeframe);
        timeframeButtons.add(editTimeframe);
        timeframeButtons.add(deleteTimeframe);

        JPanel timeframePanel = new JPanel(new BorderLayout(10, 10));
        timeframePanel.add(timeframeScroll, BorderLayout.CENTER);
        timeframePanel.add(timeframeButtons, BorderLayout.EAST);

        JTextArea previewArea = new JTextArea("Confirmed What-If Changes:\n");
        previewArea.setEditable(false);
        JScrollPane previewScroll = new JScrollPane(previewArea);
        JButton submitAll = new JButton("Submit All Changes");

        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        bottomPanel.add(previewScroll, BorderLayout.CENTER);
        bottomPanel.add(submitAll, BorderLayout.SOUTH);

        applianceDropdown.addActionListener(e -> {
            String selected = (String) applianceDropdown.getSelectedItem();
            for (Appliance a : appliances) {
                if (a.getName().equals(selected)) {
                    originalEmissionLabel.setText(a.getGeneratedFootprint() + " kg CO2");
                    newEmissionField.setText(simulatedChanges.containsKey(selected)
                            ? simulatedChanges.get(selected).toString()
                            : "");
                }
            }
            timeframeModel.clear();
            java.util.List<String> timeframes = applianceTimeframes.getOrDefault(selected, new ArrayList<>());
            for (String tf : timeframes) {
                timeframeModel.addElement(tf);
            }
        });
        applianceDropdown.setSelectedIndex(0);


        addTimeframe.addActionListener(e -> {
            String selected = (String) applianceDropdown.getSelectedItem();
            String tf = showTimeframeDialog(null);
            if (!tf.equals("None")) {
                applianceTimeframes.get(selected).add(tf);
                timeframeModel.addElement(tf);
            }
        });

        editTimeframe.addActionListener(e -> {
            String selected = (String) applianceDropdown.getSelectedItem();
            int index = timeframeList.getSelectedIndex();
            if (index >= 0) {
                String current = timeframeModel.get(index);
                String edited = showTimeframeDialog(current);
                if (!edited.equals("None")) {
                    applianceTimeframes.get(selected).set(index, edited);
                    timeframeModel.set(index, edited);
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Select a timeframe to edit.");
            }
        });

        deleteTimeframe.addActionListener(e -> {
            String selected = (String) applianceDropdown.getSelectedItem();
            int index = timeframeList.getSelectedIndex();
            if (index >= 0) {
                applianceTimeframes.get(selected).remove(index);
                timeframeModel.remove(index);
            } else {
                JOptionPane.showMessageDialog(frame, "Select a timeframe to delete.");
            }
        });

        submitAll.addActionListener(e -> {
            String selected = (String) applianceDropdown.getSelectedItem();
            try {
                double newEmission = Double.parseDouble(newEmissionField.getText().trim());
                simulatedChanges.put(selected, newEmission);
            } catch (NumberFormatException ignored) {}

            JOptionPane.showMessageDialog(frame, "Appliance changes saved temporarily.");
            frame.dispose();
            openWhatIfWindow();
        });

        frame.add(emissionPanel, BorderLayout.NORTH);
        frame.add(timeframePanel, BorderLayout.CENTER);
        frame.add(bottomPanel, BorderLayout.SOUTH);
        frame.setVisible(true);
    }
    private static String showTimeframeDialog(String existing) {
        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));

        JTextField userField = new JTextField();
        JDateChooser startDate = new JDateChooser();
        JSpinner startTime = new JSpinner(new SpinnerDateModel());
        startTime.setEditor(new JSpinner.DateEditor(startTime, "HH:mm"));

        JDateChooser endDate = new JDateChooser();
        JSpinner endTime = new JSpinner(new SpinnerDateModel());
        endTime.setEditor(new JSpinner.DateEditor(endTime, "HH:mm"));

        panel.add(new JLabel("User:"));
        panel.add(userField);
        panel.add(new JLabel("Start Date:"));
        panel.add(startDate);
        panel.add(new JLabel("Start Time:"));
        panel.add(startTime);
        panel.add(new JLabel("End Date:"));
        panel.add(endDate);
        panel.add(new JLabel("End Time:"));
        panel.add(endTime);

        if (existing != null) {
            userField.setText("Edited User");
        }

        int result = JOptionPane.showConfirmDialog(null, panel, existing == null ? "Add Timeframe" : "Edit Timeframe",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                Calendar start = Calendar.getInstance();
                start.setTime(startDate.getDate());
                Calendar startTimeCal = Calendar.getInstance();
                startTimeCal.setTime((Date) startTime.getValue());
                start.set(Calendar.HOUR_OF_DAY, startTimeCal.get(Calendar.HOUR_OF_DAY));
                start.set(Calendar.MINUTE, startTimeCal.get(Calendar.MINUTE));

                Calendar end = Calendar.getInstance();
                end.setTime(endDate.getDate());
                Calendar endTimeCal = Calendar.getInstance();
                endTimeCal.setTime((Date) endTime.getValue());
                end.set(Calendar.HOUR_OF_DAY, endTimeCal.get(Calendar.HOUR_OF_DAY));
                end.set(Calendar.MINUTE, endTimeCal.get(Calendar.MINUTE));

                return "User: " + userField.getText() + ", From: " + df.format(start.getTime())
                        + " → " + df.format(end.getTime());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Invalid date/time input.");
            }
        }

        return "None";
    }
    private static void openGeneralChangesWindow() {
        JDialog dialog = new JDialog((Frame) null, "General Info Changes", true);
        dialog.setSize(400, 250);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setLocationRelativeTo(null);

        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10));

        JLabel regionLabel = new JLabel("Select Region:");
        JComboBox<String> regionDropdown = new JComboBox<>(new String[]{
                "Andorra", "United Arab Emirates", "Afghanistan", "Antigua and Barbuda", "Albania", "Armenia", "Angola", "Argentina", "Austria", "Australia", "Lord Howe Island", "New South Wales", "Northern Territory", "Queensland", "South Australia", "Tasmania", "Cape Barren Island", "Flinders Island", "King Island", "Victoria", "Western Australia", "Rottnest Island", "Aruba", "Åland Islands", "Azerbaijan", "Bosnia and Herzegovina", "Barbados", "Bangladesh", "Belgium", "Burkina Faso", "Bulgaria", "Bahrain", "Burundi", "Benin", "Bermuda", "Brunei", "Bolivia", "Brazil", "Central Brazil", "North Brazil", "North-East Brazil", "South Brazil", "Bahamas", "Bhutan", "Botswana", "Belarus", "Belize", "Canada", "Alberta", "British Columbia", "Manitoba", "New Brunswick", "Newfoundland and Labrador", "Nova Scotia", "Northwest Territories", "Nunavut", "Ontario", "Prince Edward Island", "Québec", "Saskatchewan", "Yukon", "Democratic Republic of the Congo", "Central African Republic", "Congo", "Switzerland", "Ivory Coast", "Easter Island", "Sistema Eléctrico de Aysén", "Sistema Eléctrico de Magallanes", "Sistema Eléctrico Nacional", "Cameroon", "China", "Colombia", "Costa Rica", "Cuba", "Cabo Verde", "Curaçao", "Cyprus", "Czechia", "Germany", "Djibouti", "Denmark", "Bornholm", "West Denmark", "East Denmark", "Dominica", "Dominican Republic", "Algeria", "Ecuador", "Estonia", "Egypt", "Western Sahara", "Eritrea", "Spain", "Ceuta", "Fuerteventura", "Gran Canaria", "El Hierro", "Isla de la Gomera", "La Palma", "Lanzarote", "Tenerife", "Formentera", "Ibiza", "Mallorca", "Menorca", "Melilla", "Ethiopia", "Finland", "Fiji", "Falkland Islands", "Micronesia", "Faroe Islands", "Main Islands", "South Island", "France", "Corsica", "Gabon", "Great Britain", "Northern Ireland", "Orkney Islands", "Shetland Islands", "Georgia", "French Guiana", "Guernsey", "Ghana", "Gibraltar", "Greenland", "Gambia", "Guinea", "Guadeloupe", "Equatorial Guinea", "Greece", "South Georgia and the South Sandwich Islands", "Guatemala", "Guam", "Guinea-Bissau", "Guyana", "Hong Kong", "Heard Island and McDonald Islands", "Honduras", "Croatia", "Haiti", "Hungary", "Indonesia", "Ireland", "Israel", "Isle of Man", "Mainland India", "Andaman and Nicobar Islands", "Eastern India", "Himachal Pradesh", "North Eastern India", "Northern India", "Southern India", "Uttar Pradesh", "Uttarakhand", "Western India", "Iraq", "Iran", "Iceland", "Italy", "Central North Italy", "Central South Italy", "North Italy", "Sardinia", "Sicily", "South Italy", "Jersey", "Jamaica", "Jordan", "Japan", "Chūbu", "Chūgoku", "Hokkaidō", "Hokuriku", "Kansai", "Kyūshū", "Okinawa", "Shikoku", "Tōhoku", "Tōkyō", "Kenya", "Kyrgyzstan", "Cambodia", "Comoros", "North Korea", "South Korea", "Kuwait", "Cayman Islands", "Kazakhstan", "Laos", "Lebanon", "Saint Lucia", "Liechtenstein", "Sri Lanka", "Liberia", "Lesotho", "Lithuania", "Luxembourg", "Latvia", "Libya", "Morocco", "Monaco", "Moldova", "Montenegro", "Madagascar", "North Macedonia", "Mali", "Myanmar", "Mongolia", "Macao", "Martinique", "Mauritania", "Malta", "Mauritius", "Maldives", "Malawi", "Mexico", "Malaysia", "Borneo", "Peninsula", "Mozambique", "Namibia", "New Caledonia", "Niger", "Nigeria", "Nicaragua", "Netherlands", "Norway", "Southeast Norway", "Southwest Norway", "Middle Norway", "North Central Sweden", "South Central Sweden", "South Sweden", "Singapore", "Slovenia", "Svalbard and Jan Mayen", "Slovakia", "Sierra Leone", "Senegal", "Somalia", "Suriname", "South Sudan", "São Tomé and Príncipe", "El Salvador", "Syria", "Eswatini", "Chad", "French Southern Territories", "Togo", "Thailand", "Tajikistan", "Timor-Leste", "Turkmenistan", "Tunisia", "Tonga", "Turkey", "Trinidad and Tobago", "Taiwan", "Tanzania", "Ukraine", "Crimea", "Uganda", "Contiguous United States", "Alaska", "Southeast Alaska Power Agency", "Balancing Authority of Northern California", "CAISO", "Imperial Irrigation District", "Los Angeles Department of Water and Power", "Turlock Irrigation District", "Duke Energy Progress East", "Duke Energy Progress West", "Duke Energy Carolinas", "South Carolina Public Service Authority", "South Carolina Electric & Gas Company", "Alcoa Power Generating, Inc. Yadkin Division", "Southwestern Power Administration", "Southwest Power Pool"
        });

        JLabel tariffLabel = new JLabel("Electricity Tariff (€/kWh):");
        JTextField tariffField = new JTextField();
        tariffField.setText(String.valueOf(house.getElectricityTariff()));
        formPanel.add(regionLabel);
        formPanel.add(regionDropdown);
        formPanel.add(tariffLabel);
        formPanel.add(tariffField);

        JButton applyButton = new JButton("Apply Changes");

        applyButton.addActionListener(e -> {
            String selectedRegion = (String) regionDropdown.getSelectedItem();
            String tariffInput = tariffField.getText().trim();


            try {
                double newTariff = Double.parseDouble(tariffInput);



                System.out.println("Region set to: " + selectedRegion);

                java.lang.reflect.Field tariffFieldObj = House.class.getDeclaredField("electricityTariff");
                tariffFieldObj.setAccessible(true);
                tariffFieldObj.set(house, newTariff);

                JOptionPane.showMessageDialog(dialog, "Changes saved:\nRegion: " + selectedRegion + "\nTariff: €" + newTariff);
                dialog.dispose();
                openWhatIfWindow();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Please enter a valid number for the tariff.");
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(dialog, "Failed to update House.");
            }
        });

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(applyButton, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

}
