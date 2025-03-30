package greenhome.input;

import javax.swing.*;
import com.toedter.calendar.JDateChooser;
import greenhome.household.Parser;
import greenhome.reporting.Report;
import greenhome.validation.Validator;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import javax.swing.SpinnerDateModel;

public class Form {
    private String houseInfo = "";
    private final List<String> appliances;
    private final List<String> users;
    private Map<String, List<String>> applianceTimeframes = new HashMap<>();

    public Form() {
        appliances = new ArrayList<>();
        users = new ArrayList<>();
    }

    // House info
    public void setHouseInfo(String info) {
        this.houseInfo = info;
    }





    // Appliances
    public void addAppliance(String applianceInfo) {
        appliances.add(applianceInfo);
    }




    // Users
    public void addUser(String user) {
        if (!users.contains(user)) {
            users.add(user);
        }
    }

    public List<String> getUsers() {
        return users;
    }

    public void addTimeframe(String applianceName, String timeframe) {
        applianceTimeframes.putIfAbsent(applianceName, new ArrayList<>());
        applianceTimeframes.get(applianceName).add(timeframe);
    }

    public String getFormattedInput() {
        StringBuilder sb = new StringBuilder();

        sb.append("HOUSE INFO:\n").append(houseInfo).append("\n\n");

        sb.append("USERS:\n");
        if (users.isEmpty()) {
            sb.append(" (None)\n");
        } else {
            for (String user : users) {
                sb.append(" - ").append(user).append("\n");
            }
        }

        sb.append("\n APPLIANCES:\n");
        if (appliances.isEmpty()) {
            sb.append(" (None)\n");
        } else {
            for (String appliance : appliances) {
                sb.append(appliance).append("\n");
            }
        }

        sb.append("\n TIMEFRAMES:\n");
        if (applianceTimeframes.isEmpty()) {
            sb.append(" (None)\n");
        } else {
            for (Map.Entry<String, List<String>> entry : applianceTimeframes.entrySet()) {
                sb.append("Appliance: ").append(entry.getKey()).append("\n");
                for (String tf : entry.getValue()) {
                    sb.append("  - ").append(tf).append("\n");
                }
            }
        }

        return sb.toString();
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(Form::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Energy Consumption Form");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(500, 350);

        Form form = new Form();

        JPanel panel = new JPanel(new GridLayout(7, 2, 10, 10));

        JLabel regionLabel = new JLabel("Region:");
        JComboBox<String> regionDropdown = new JComboBox<>(new String[]{
                "Andorra", "United Arab Emirates", "Afghanistan", "Antigua and Barbuda", "Albania", "Armenia", "Angola", "Argentina", "Austria", "Australia", "Lord Howe Island", "New South Wales", "Northern Territory", "Queensland", "South Australia", "Tasmania", "Cape Barren Island", "Flinders Island", "King Island", "Victoria", "Western Australia", "Rottnest Island", "Aruba", "Åland Islands", "Azerbaijan", "Bosnia and Herzegovina", "Barbados", "Bangladesh", "Belgium", "Burkina Faso", "Bulgaria", "Bahrain", "Burundi", "Benin", "Bermuda", "Brunei", "Bolivia", "Brazil", "Central Brazil", "North Brazil", "North-East Brazil", "South Brazil", "Bahamas", "Bhutan", "Botswana", "Belarus", "Belize", "Canada", "Alberta", "British Columbia", "Manitoba", "New Brunswick", "Newfoundland and Labrador", "Nova Scotia", "Northwest Territories", "Nunavut", "Ontario", "Prince Edward Island", "Québec", "Saskatchewan", "Yukon", "Democratic Republic of the Congo", "Central African Republic", "Congo", "Switzerland", "Ivory Coast", "Easter Island", "Sistema Eléctrico de Aysén", "Sistema Eléctrico de Magallanes", "Sistema Eléctrico Nacional", "Cameroon", "China", "Colombia", "Costa Rica", "Cuba", "Cabo Verde", "Curaçao", "Cyprus", "Czechia", "Germany", "Djibouti", "Denmark", "Bornholm", "West Denmark", "East Denmark", "Dominica", "Dominican Republic", "Algeria", "Ecuador", "Estonia", "Egypt", "Western Sahara", "Eritrea", "Spain", "Ceuta", "Fuerteventura", "Gran Canaria", "El Hierro", "Isla de la Gomera", "La Palma", "Lanzarote", "Tenerife", "Formentera", "Ibiza", "Mallorca", "Menorca", "Melilla", "Ethiopia", "Finland", "Fiji", "Falkland Islands", "Micronesia", "Faroe Islands", "Main Islands", "South Island", "France", "Corsica", "Gabon", "Great Britain", "Northern Ireland", "Orkney Islands", "Shetland Islands", "Georgia", "French Guiana", "Guernsey", "Ghana", "Gibraltar", "Greenland", "Gambia", "Guinea", "Guadeloupe", "Equatorial Guinea", "Greece", "South Georgia and the South Sandwich Islands", "Guatemala", "Guam", "Guinea-Bissau", "Guyana", "Hong Kong", "Heard Island and McDonald Islands", "Honduras", "Croatia", "Haiti", "Hungary", "Indonesia", "Ireland", "Israel", "Isle of Man", "Mainland India", "Andaman and Nicobar Islands", "Eastern India", "Himachal Pradesh", "North Eastern India", "Northern India", "Southern India", "Uttar Pradesh", "Uttarakhand", "Western India", "Iraq", "Iran", "Iceland", "Italy", "Central North Italy", "Central South Italy", "North Italy", "Sardinia", "Sicily", "South Italy", "Jersey", "Jamaica", "Jordan", "Japan", "Chūbu", "Chūgoku", "Hokkaidō", "Hokuriku", "Kansai", "Kyūshū", "Okinawa", "Shikoku", "Tōhoku", "Tōkyō", "Kenya", "Kyrgyzstan", "Cambodia", "Comoros", "North Korea", "South Korea", "Kuwait", "Cayman Islands", "Kazakhstan", "Laos", "Lebanon", "Saint Lucia", "Liechtenstein", "Sri Lanka", "Liberia", "Lesotho", "Lithuania", "Luxembourg", "Latvia", "Libya", "Morocco", "Monaco", "Moldova", "Montenegro", "Madagascar", "North Macedonia", "Mali", "Myanmar", "Mongolia", "Macao", "Martinique", "Mauritania", "Malta", "Mauritius", "Maldives", "Malawi", "Mexico", "Malaysia", "Borneo", "Peninsula", "Mozambique", "Namibia", "New Caledonia", "Niger", "Nigeria", "Nicaragua", "Netherlands", "Norway", "Southeast Norway", "Southwest Norway", "Middle Norway", "North Central Sweden", "South Central Sweden", "South Sweden", "Singapore", "Slovenia", "Svalbard and Jan Mayen", "Slovakia", "Sierra Leone", "Senegal", "Somalia", "Suriname", "South Sudan", "São Tomé and Príncipe", "El Salvador", "Syria", "Eswatini", "Chad", "French Southern Territories", "Togo", "Thailand", "Tajikistan", "Timor-Leste", "Turkmenistan", "Tunisia", "Tonga", "Turkey", "Trinidad and Tobago", "Taiwan", "Tanzania", "Ukraine", "Crimea", "Uganda", "Contiguous United States", "Alaska", "Southeast Alaska Power Agency", "Balancing Authority of Northern California", "CAISO", "Imperial Irrigation District", "Los Angeles Department of Water and Power", "Turlock Irrigation District", "Duke Energy Progress East", "Duke Energy Progress West", "Duke Energy Carolinas", "South Carolina Public Service Authority", "South Carolina Electric & Gas Company", "Alcoa Power Generating, Inc. Yadkin Division", "Southwestern Power Administration", "Southwest Power Pool"

        });

        JLabel tariffLabel = new JLabel("Tariff:");
        JTextField tariffField = new JTextField();
        addPlaceholder(tariffField, "e.g., 0.25 USD/kWh");

        JLabel startDateLabel = new JLabel("Start Date:");
        JDateChooser startDateChooser = new JDateChooser();

        JLabel startTimeLabel = new JLabel("Start Time:");
        JSpinner startTimeSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor startTimeEditor = new JSpinner.DateEditor(startTimeSpinner, "HH:mm");
        startTimeSpinner.setEditor(startTimeEditor);

        JLabel endDateLabel = new JLabel("End Date:");
        JDateChooser endDateChooser = new JDateChooser();

        JLabel endTimeLabel = new JLabel("End Time:");
        JSpinner endTimeSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor endTimeEditor = new JSpinner.DateEditor(endTimeSpinner, "HH:mm");
        endTimeSpinner.setEditor(endTimeEditor);

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

            String startDate = dateFormat.format(startDateChooser.getDate()) + " " + timeFormat.format((Date) startTimeSpinner.getValue());
            String endDate = dateFormat.format(endDateChooser.getDate()) + " " + timeFormat.format((Date) endTimeSpinner.getValue());
            String tariffText = tariffField.getText().trim();
            double tariff;
            try {
                tariff = Double.parseDouble(tariffText);
                if (!Validator.validateElectricityTariff(tariff)) {
                    JOptionPane.showMessageDialog(frame, "Tariff must be between 0.05 and 0.50 EUR/kWh.");
                    return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Tariff must be a valid decimal number.");
                return;
            }
            StringBuilder houseDetails = new StringBuilder();
            houseDetails.append("Region: ").append(regionDropdown.getSelectedItem().toString()).append("\n");
            houseDetails.append("Tariff: ").append(tariffField.getText()).append("\n");
            houseDetails.append("Start DateTime: ").append(startDate).append("\n");
            houseDetails.append("End DateTime: ").append(endDate);
            form.setHouseInfo(houseDetails.toString());

            JOptionPane.showMessageDialog(frame, form.getFormattedInput());

            frame.dispose();
            showUserInputWindow(form);

            String output = form.getFormattedInput();

            // Print to console to verify correctness
          //  System.out.println(output);

            //JOptionPane.showMessageDialog(frame, output);
        });

        panel.add(regionLabel);
        panel.add(regionDropdown);
        panel.add(tariffLabel);
        panel.add(tariffField);
        panel.add(startDateLabel);
        panel.add(startDateChooser);
        panel.add(startTimeLabel);
        panel.add(startTimeSpinner);
        panel.add(endDateLabel);
        panel.add(endDateChooser);
        panel.add(endTimeLabel);
        panel.add(endTimeSpinner);
        panel.add(new JLabel());
        panel.add(submitButton);

        frame.add(panel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    private static void showUserInputWindow(Form form) {
        JFrame userFrame = new JFrame("Add Users");
        userFrame.setSize(400, 300);
        userFrame.setLayout(new BorderLayout(10, 10));

        JPanel userPanel = new JPanel(new BorderLayout(10, 10));
        JTextField userField = new JTextField();
        addPlaceholder(userField, "Enter user name");
        userPanel.add(userField, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        JButton addUserButton = new JButton("Add User");
        JButton continueButton = new JButton("Continue");
        buttonPanel.add(addUserButton);
        buttonPanel.add(continueButton);

        JTextArea userListArea = new JTextArea();
        userListArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(userListArea);



        addUserButton.addActionListener(e -> {
            String  user = userField.getText().trim();
            if (!user.isEmpty()) {
                if (!Validator.validateUser(user)) {
                    JOptionPane.showMessageDialog(userFrame, "Usernames cannot contain commas.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                form.addUser(user);
                userListArea.append(user + "\n");
                userField.setText("");
            }
        });

        continueButton.addActionListener(e -> {
            userFrame.dispose();
            showApplianceWindow(form);
        });

        userFrame.add(userPanel, BorderLayout.NORTH);
        userFrame.add(scrollPane, BorderLayout.CENTER);
        userFrame.add(buttonPanel, BorderLayout.SOUTH);
        userFrame.setLocationRelativeTo(null);
        userFrame.setVisible(true);
    }
    private static void showApplianceWindow(Form form) {
        JFrame applianceFrame = new JFrame("Add Appliance");
        applianceFrame.setSize(400, 300);
        applianceFrame.setLayout(new GridLayout(6, 2, 10, 10));

        JTextField nameField = new JTextField();
        addPlaceholder(nameField, "e.g., Fridge");
        JTextField powerField = new JTextField();
        addPlaceholder(powerField, "e.g., 150W");
        JTextField embodiedEmission = new JTextField();
        addPlaceholder(embodiedEmission, "e.g., Fridge");

        JButton addButton = new JButton("Add Appliance");
        JButton showReport = new JButton("show report"); // here for the future report button
        addButton.addActionListener(e -> {
            String applianceInfo = "Name: " + nameField.getText() + "\n" +
                    "Power Consumption: " + powerField.getText() + "\n" +
                    "Embodied Emmsion: " + embodiedEmission.getText();

            form.addAppliance(applianceInfo);
            JOptionPane.showMessageDialog(applianceFrame, "Appliance added:\n" + applianceInfo);

        });

        addAppliance(form);
    }

    private static void addAppliance(Form form) {
        JFrame applianceFrame = new JFrame("Add Appliance");
        applianceFrame.setSize(500, 500);
        applianceFrame.setLayout(new BorderLayout(10, 10));

        JPanel fieldsPanel = new JPanel(new GridLayout(6, 2, 10, 10));

        JTextField nameField = new JTextField();
        addPlaceholder(nameField, "e.g., Fridge");

        JTextField powerField = new JTextField();
        addPlaceholder(powerField, "e.g., 150W");

        JTextField embodiedEmissions = new JTextField();
        addPlaceholder(embodiedEmissions, "e.g., 2.4 kg CO2");


        fieldsPanel.add(new JLabel("Name:"));
        fieldsPanel.add(nameField);
        fieldsPanel.add(new JLabel("Power Consumption:"));
        fieldsPanel.add(powerField);
        fieldsPanel.add(new JLabel("Embodied Emissions:"));
        fieldsPanel.add(embodiedEmissions);


        JButton addTimeframeButton = new JButton("Add Timeframe");
        fieldsPanel.add(addTimeframeButton);

        applianceFrame.add(fieldsPanel, BorderLayout.NORTH);

        JTextArea timeframeArea = new JTextArea(8, 40);
        timeframeArea.setEditable(false);
        JScrollPane timeframePane = new JScrollPane(timeframeArea);
        applianceFrame.add(timeframePane, BorderLayout.CENTER);

        List<String> timeframes = new ArrayList<>();

        addTimeframeButton.addActionListener(e -> {
            String newTimeframe = addApplianceTimeframe(applianceFrame, form);
            if (!newTimeframe.equals("None")) {
                form.addTimeframe(nameField.getText(), newTimeframe);
                timeframeArea.append("Timeframe " + timeframes.size() + ": " + newTimeframe + "\n");

                // ✅ Debug output to terminal
                System.out.println("✅ Timeframe added: " + newTimeframe);
                System.out.println("📦 Appliance: " + nameField.getText());
                System.out.println("👥 Current Users: " + form.getUsers());
                System.out.println("🏠 House Info:\n" + form.getFormattedInput().split("USERS:")[0]);
            } else {
                System.out.println("❌ Timeframe rejected or validation failed.");
            }
        });

        JPanel bottomButtons = new JPanel(new GridLayout(1, 3, 10, 10));
        JButton addButton = new JButton("Add Appliance");
        JButton confirmButton = new JButton("Confirm All Information");
        //JButton showReportButton = new JButton("Show Report");
        bottomButtons.add(addButton);
        bottomButtons.add(confirmButton);
        // bottomButtons.add(showReportButton);

        addButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String powerStr = powerField.getText().trim().replaceAll("[^0-9]", "");
            String emissionsStr = embodiedEmissions.getText().trim().replaceAll("[^0-9]", "");

            if (name.isEmpty() || powerStr.isEmpty() || emissionsStr.isEmpty()) {
                JOptionPane.showMessageDialog(applianceFrame, "All fields must be filled.");
                return;
            }
            if (!Validator.validateInteger(powerStr)) {
                JOptionPane.showMessageDialog(applianceFrame, "Power consumption must be a valid integer.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int power = Integer.parseInt(powerStr);
            if (!Validator.validateDouble(emissionsStr)) {
                JOptionPane.showMessageDialog(applianceFrame, "Embodied emissions must be a valid number.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            double emissions = Double.parseDouble(emissionsStr);

            if (!Validator.validatePowerConsumption(power)) {
                JOptionPane.showMessageDialog(applianceFrame, "Power consumption must be between 10 and 5000 W.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!Validator.validateEmbodiedEmissions(emissions)) {
                JOptionPane.showMessageDialog(applianceFrame, "Embodied emissions must be between 10 and 500 kg CO₂e.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }


            StringBuilder applianceInfo = new StringBuilder();
            applianceInfo.append("Name: ").append(name).append("\n");
            applianceInfo.append("Power Consumption: ").append(power).append("\n");
            applianceInfo.append("Embodied Emission: ").append(emissions).append("\n");

          //  if (!timeframes.isEmpty()) {
          //      applianceInfo.append("Timeframes:\n");
          //      for (String tf : timeframes) {
          //          applianceInfo.append(" - ").append(tf).append("\n");
          //      }
          //  } else {
          //      applianceInfo.append("Timeframes: None\n");
          //  }

            form.addAppliance(applianceInfo.toString());
            JOptionPane.showMessageDialog(applianceFrame, "Appliance added:\n" + applianceInfo);

            nameField.setText("");
            powerField.setText("");
            embodiedEmissions.setText("");
            timeframeArea.setText("");
            timeframes.clear();
        });


        //addUserButton.addActionListener(e -> {
       //    String newUser = JOptionPane.showInputDialog(applianceFrame, "Add new user:");
       //    if (newUser != null && !newUser.trim().isEmpty()) {
       //        form.addUser(newUser);
       //        JOptionPane.showMessageDialog(applianceFrame, "New user '" + newUser + "' added successfully!");
       //    }
       //});
        confirmButton.addActionListener(e -> {
            // Create a new confirmation window
            JFrame confirmFrame = new JFrame("Confirmation Summary");
            confirmFrame.setSize(500, 500);
            confirmFrame.setLayout(new BorderLayout(10, 10));
            String summary = form.getFormattedInput();
            JTextArea confirmationArea = new JTextArea(summary);
            confirmationArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(confirmationArea);

            confirmFrame.add(scrollPane, BorderLayout.CENTER);

            // Bottom panel with "Show Report" only
            JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            JButton finalReportButton = new JButton("Show Report");
            //JButton whatIfButton = new JButton("What If scenarios");
            bottomPanel.add(finalReportButton);
            //bottomPanel.add(whatIfButton);

            confirmFrame.add(bottomPanel, BorderLayout.SOUTH);

            finalReportButton.addActionListener(ev -> {
                form.mergeAllData();
                JOptionPane.showMessageDialog(confirmFrame, summary, "Final Report", JOptionPane.INFORMATION_MESSAGE);
                Report.main(new String[]{});
            });

            confirmFrame.setLocationRelativeTo(null);
            confirmFrame.setVisible(true);
        });

        // showReportButton.addActionListener(e -> JOptionPane.showMessageDialog(applianceFrame, form.getFormattedInput()));

        applianceFrame.add(bottomButtons, BorderLayout.SOUTH);
        applianceFrame.setLocationRelativeTo(null);
        applianceFrame.setVisible(true);
    }

    private static String addApplianceTimeframe(JFrame parentFrame, Form form) {
        JPanel timeframePanel = new JPanel();
        timeframePanel.setLayout(new BoxLayout(timeframePanel, BoxLayout.Y_AXIS));

        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        userPanel.add(new JLabel("User:"));
        JTextField userField = new JTextField();
        userPanel.add(userField);
        timeframePanel.add(userPanel);
        addPlaceholder(userField, "e.g., John Doe");

        JDateChooser startDateChooser = new JDateChooser();
        JSpinner startTimeSpinner = new JSpinner(new SpinnerDateModel());
        startTimeSpinner.setEditor(new JSpinner.DateEditor(startTimeSpinner, "HH:mm"));
        JPanel startPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        startPanel.add(new JLabel("Start Date:"));
        startPanel.add(startDateChooser);
        startPanel.add(new JLabel("Start Time:"));
        startPanel.add(startTimeSpinner);
        timeframePanel.add(startPanel);

        JDateChooser endDateChooser = new JDateChooser();
        JSpinner endTimeSpinner = new JSpinner(new SpinnerDateModel());
        endTimeSpinner.setEditor(new JSpinner.DateEditor(endTimeSpinner, "HH:mm"));
        JPanel endPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        endPanel.add(new JLabel("End Date:"));
        endPanel.add(endDateChooser);
        endPanel.add(new JLabel("End Time:"));
        endPanel.add(endTimeSpinner);
        timeframePanel.add(endPanel);

      //  timeframePanel.add(new JLabel("User:"));
      //  timeframePanel.add(userField);
      //  timeframePanel.add(new JLabel("Start Date:"));
      //  timeframePanel.add(startDateChooser);
      //  timeframePanel.add(new JLabel("Start Time:"));
      //  timeframePanel.add(startTimeSpinner);
      //  timeframePanel.add(new JLabel("End Date:"));
      //  timeframePanel.add(endDateChooser);
      //  timeframePanel.add(new JLabel("End Time:"));
      //  timeframePanel.add(endTimeSpinner);

        int result = JOptionPane.showConfirmDialog(parentFrame, timeframePanel,
                "Add Timeframe", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String user = userField.getText().trim();
            if (user.isEmpty() || startDateChooser.getDate() == null || endDateChooser.getDate() == null) {
                JOptionPane.showMessageDialog(parentFrame, "All fields are required.", "Error", JOptionPane.ERROR_MESSAGE);
                return "None";
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

            String start = dateFormat.format(startDateChooser.getDate()) + " " + timeFormat.format(startTimeSpinner.getValue());
            String end = dateFormat.format(endDateChooser.getDate()) + " " + timeFormat.format(endTimeSpinner.getValue());

            String timeframe = "User: " + user + ", Start: " + start + ", End: " + end;

            // Format user list into string (e.g., "- Alice\n- Bob\n")
            StringBuilder usersString = new StringBuilder();
            for (String u : form.getUsers()) {
                usersString.append("- ").append(u).append("\n");
            }

            // Extract just the house block from formatted input
            String houseData = form.getFormattedInput().split("USERS:")[0];

            // ✅ VALIDATE THE TIMEFRAME!
            if (!Validator.validateTimeframe(timeframe, usersString.toString(), houseData)) {
                JOptionPane.showMessageDialog(parentFrame,
                        "⛔ Invalid timeframe:\n- User not found in the list\n- Dates out of range\n- Start must be before End",
                        "Validation Error", JOptionPane.ERROR_MESSAGE);
                return "None";
            }

            return timeframe;
        }
        return "None";
        }



        private static void addPlaceholder(JTextField textField, String placeholder) {
        textField.setForeground(Color.GRAY);
        textField.setText(placeholder);

        textField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (textField.getText().equals(placeholder)) {
                    textField.setText("");
                    textField.setForeground(Color.BLACK);
                }
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                if (textField.getText().isEmpty()) {
                    textField.setForeground(Color.GRAY);
                    textField.setText(placeholder);
                }
            }
        });
    }

    public void mergeAllData() {
        StringBuilder sb = new StringBuilder();

        if (!houseInfo.isEmpty()) {
            sb.append(houseInfo).append("\n\n");
        } else {
            sb.append("Region: Unknown\n");
            sb.append("Tariff: 0.0\n");
            sb.append("Start DateTime: 0000-00-00 00:00\n");
            sb.append("End DateTime: 0000-00-00 00:00\n\n");
        }

        for (String user : users) {
            sb.append("- ").append(user).append("\n");
        }

        sb.append("\nAPPLIANCES\n");

        int applianceCount = 1;
        for (String applianceRaw : appliances) {
            sb.append("Appliance ").append(applianceCount++).append(":\n");

            String[] lines = applianceRaw.split("\n");
            for (String line : lines) {
                if (line.startsWith("Name:")) {
                    sb.append(line).append("\n");
                } else if (line.startsWith("Power Consumption:")) {
                    String val = line.replaceAll("[^0-9]", ""); // Strip units
                    sb.append("Power Consumption: ").append(val).append("\n");
                } else if (line.startsWith("Embodied Emission:")) {
                    String val = line.replaceAll("[^0-9]", ""); // Strip units
                    sb.append("Embodied Emission: ").append(val).append("\n");
                }
            }
        }

        sb.append("\nTIMEFRAMES PER APPLIANCE\n");

        for (Map.Entry<String, List<String>> entry : applianceTimeframes.entrySet()) {
            sb.append("Appliance: ").append(entry.getKey()).append("\n");
            for (String tf : entry.getValue()) {
                sb.append("  - ").append(tf).append("\n");
            }
        }

        sb.append("\n----- End of Report -----");

        String finalInput = sb.toString();
        System.out.println("✅ Final submission to parser:\n" + finalInput);
        Parser.stringIntoHouse(finalInput);
        Parser.saveHouse();}
}
