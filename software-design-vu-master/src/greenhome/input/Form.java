package greenhome.input;

import javax.swing.*;

import com.toedter.calendar.JDateChooser;
import greenhome.household.Appliance;
import greenhome.household.House;
import greenhome.household.Parser;
import greenhome.household.User;
import greenhome.validation.Validator;

import java.awt.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import javax.swing.SpinnerDateModel;

public class Form {
    private String houseInfo = "";
    private final List<String> appliances;
    private final List<String> users;
    private final Map<String, List<String>> applianceTimeframes = new HashMap<>();

    public Form() {
        appliances = new ArrayList<>();
        users = new ArrayList<>();
    }

    public void setHouseInfo(String info) {
        this.houseInfo = info;
    }

    public void addAppliance(String applianceInfo) {
        appliances.add(applianceInfo);
    }

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



    public static void showStartupChoice() {
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
        JPanel headerPanel = new JPanel(new GridLayout(2, 1));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel welcomeLabel = new JLabel("Welcome back to GreenHome!");
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        welcomeLabel.setFont(welcomeLabel.getFont().deriveFont(Font.BOLD));


        JLabel questionLabel = new JLabel("Previous data found. What would you like to do?");
        questionLabel.setHorizontalAlignment(SwingConstants.CENTER);

        headerPanel.add(welcomeLabel);
        headerPanel.add(questionLabel);
        dialog.add(headerPanel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 15, 15));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JButton keepButton = new JButton("Continue with Previous Data");
        keepButton.setPreferredSize(new Dimension(350, 60));
        keepButton.addActionListener(e -> {
            dialog.dispose();
            continueWithData();
        });

        JButton freshButton = new JButton("Start with Fresh Data");
        freshButton.setPreferredSize(new Dimension(350, 60));
        freshButton.addActionListener(e -> {
            dialog.dispose();
            int choice = JOptionPane.showConfirmDialog(null,
                    "Delete all previous data? This cannot be undone.",
                    "Confirm", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                startFresh();
            }
        });

        buttonPanel.add(keepButton);
        buttonPanel.add(freshButton);
        dialog.add(buttonPanel, BorderLayout.CENTER);

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


    private static void continueWithData() {
        try {
            House house = House.getInstance();

            Set<String> uniqueApplianceNames = new HashSet<>();
            for (Appliance appliance : house.getAppliances()) {
                if (appliance != null) {
                    uniqueApplianceNames.add(appliance.getName());
                }
            }


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

        main(new String[]{});
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
        main(new String[]{});
    }

    private static void resetHouse() {
        try {
            java.lang.reflect.Field field = House.class.getDeclaredField("instance");
            field.setAccessible(true);
            field.set(null, null);
        } catch (Exception ignored) {
        }
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
        regionDropdown.setSelectedItem("Netherlands");

        JLabel tariffLabel = new JLabel("Electricity Tariff (€):");
        JTextField tariffField = new JTextField();
        addPlaceholder(tariffField, "e.g., 0.25 €/kWh");
        tariffField.setText("0.25");
        tariffField.setForeground(Color.BLACK);

        JLabel startDateLabel = new JLabel("Start Date:");
        JDateChooser startDateChooser = new JDateChooser();
        Calendar cal = Calendar.getInstance();
        cal.set(2025, Calendar.JUNE, 1);
        startDateChooser.setDate(cal.getTime());

        JLabel startTimeLabel = new JLabel("Start Time:");
        JSpinner startTimeSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor startTimeEditor = new JSpinner.DateEditor(startTimeSpinner, "HH:mm");
        startTimeSpinner.setEditor(startTimeEditor);
        Calendar timeCal = Calendar.getInstance();
        timeCal.set(Calendar.HOUR_OF_DAY, 0);
        timeCal.set(Calendar.MINUTE, 0);
        startTimeSpinner.setValue(timeCal.getTime());

        JLabel endDateLabel = new JLabel("End Date:");
        JDateChooser endDateChooser = new JDateChooser();
        Calendar cal2 = Calendar.getInstance();
        cal2.set(2025, Calendar.JUNE, 30);
        endDateChooser.setDate(cal2.getTime());

        JLabel endTimeLabel = new JLabel("End Time:");
        JSpinner endTimeSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor endTimeEditor = new JSpinner.DateEditor(endTimeSpinner, "HH:mm");
        endTimeSpinner.setEditor(endTimeEditor);
        Calendar timeCal2 = Calendar.getInstance();
        timeCal2.set(Calendar.HOUR_OF_DAY, 0);
        timeCal2.set(Calendar.MINUTE, 0);
        endTimeSpinner.setValue(timeCal2.getTime());

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

            frame.dispose();
            showUserInputWindow(form);
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
        userFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

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
            String user = userField.getText().trim();
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
            House house = House.getInstance();
            if (form.getUsers().isEmpty() && house.getResidents().isEmpty()) {
                JOptionPane.showMessageDialog(userFrame, "Please add at least one user before continuing.");
                return;
            }
            userFrame.dispose();
            addAppliance(form);
        });

        userFrame.add(userPanel, BorderLayout.NORTH);
        userFrame.add(scrollPane, BorderLayout.CENTER);
        userFrame.add(buttonPanel, BorderLayout.SOUTH);
        userFrame.setLocationRelativeTo(null);
        userFrame.setVisible(true);
    }

    public static void addAppliance(Form form) {
        JFrame applianceFrame = new JFrame("Add Appliance");
        applianceFrame.setSize(500, 500);
        applianceFrame.setLayout(new BorderLayout(10, 10));
        applianceFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        JPanel fieldsPanel = new JPanel(new GridLayout(6, 2, 10, 10));

        JTextField nameField = new JTextField();
        addPlaceholder(nameField, "e.g., Fridge");

        JTextField powerField = new JTextField();
        addPlaceholder(powerField, "e.g., 150 [W]");

        JTextField embodiedEmissions = new JTextField();
        addPlaceholder(embodiedEmissions, "e.g., 100 [kg CO2]");

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
                System.out.println("Timeframe added: " + newTimeframe);
                System.out.println("Appliance: " + nameField.getText());
                System.out.println("Current Users: " + form.getUsers());
            } else {
                System.out.println("Timeframe rejected or validation failed.");
            }
        });

        JPanel bottomButtons = new JPanel(new GridLayout(1, 3, 10, 10));
        JButton addButton = new JButton("Add Appliance");
        JButton confirmButton = new JButton("Confirm All Information");
        bottomButtons.add(addButton);
        bottomButtons.add(confirmButton);

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
                JOptionPane.showMessageDialog(applianceFrame, "Embodied emissions must be between 10 and 2000 kg CO₂e.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            StringBuilder applianceInfo = new StringBuilder();
            applianceInfo.append("Name: ").append(name).append("\n");
            applianceInfo.append("Power Consumption: ").append(power).append("\n");
            applianceInfo.append("Embodied Emission: ").append(emissions).append("\n");

            form.addAppliance(applianceInfo.toString());
            JOptionPane.showMessageDialog(applianceFrame, "Appliance added:\n" + applianceInfo);

            nameField.setText("");
            powerField.setText("");
            embodiedEmissions.setText("");
            timeframeArea.setText("");
            timeframes.clear();
        });

        confirmButton.addActionListener(e -> {
            JFrame confirmFrame = new JFrame("Confirmation Summary");
            confirmFrame.setSize(500, 500);
            confirmFrame.setLayout(new BorderLayout(10, 10));
            String summary = form.getFormattedInput();
            JTextArea confirmationArea = new JTextArea(summary);
            confirmationArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(confirmationArea);

            confirmFrame.add(scrollPane, BorderLayout.CENTER);

            JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            JButton finalReportButton = new JButton("Show Report");
            bottomPanel.add(finalReportButton);

            confirmFrame.add(bottomPanel, BorderLayout.SOUTH);

            finalReportButton.addActionListener(ev -> {
                form.mergeAllData();
                confirmFrame.dispose();
                applianceFrame.dispose();
                Report.main(new String[]{});
            });

            confirmFrame.setLocationRelativeTo(null);
            confirmFrame.setVisible(true);
        });

        applianceFrame.add(bottomButtons, BorderLayout.SOUTH);
        applianceFrame.setLocationRelativeTo(null);
        applianceFrame.setVisible(true);
    }

    public static String addApplianceTimeframe(JFrame parentFrame, Form form) {
        JPanel timeframePanel = new JPanel();
        timeframePanel.setLayout(new BoxLayout(timeframePanel, BoxLayout.Y_AXIS));


        Set<String> allUsers = new HashSet<>();


        allUsers.addAll(form.getUsers());


        try {
            House existingHouse = House.getInstance();
            if (existingHouse != null && existingHouse.getResidents() != null) {
                for (User user : existingHouse.getResidents()) {
                    allUsers.add(user.getName());
                }
            }
        } catch (Exception e) {
            System.out.println("Could not load existing users: " + e.getMessage());
        }

        if (allUsers.isEmpty()) {
            JOptionPane.showMessageDialog(parentFrame,
                    "No users available. Please add users first.",
                    "No Users", JOptionPane.WARNING_MESSAGE);
            return "None";
        }

        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        userPanel.add(new JLabel("User:"));

        JComboBox<String> userDropdown = new JComboBox<>(allUsers.toArray(new String[0]));
        userPanel.add(userDropdown);
        timeframePanel.add(userPanel);

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

        int result = JOptionPane.showConfirmDialog(parentFrame, timeframePanel,
                "Add Timeframe", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String selectedUser = (String) userDropdown.getSelectedItem();
            if (startDateChooser.getDate() == null || endDateChooser.getDate() == null) {
                JOptionPane.showMessageDialog(parentFrame, "All fields are required.", "Error", JOptionPane.ERROR_MESSAGE);
                return "None";
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

            String start = dateFormat.format(startDateChooser.getDate()) + " " + timeFormat.format(startTimeSpinner.getValue());
            String end = dateFormat.format(endDateChooser.getDate()) + " " + timeFormat.format(endTimeSpinner.getValue());

            String timeframe = "User: " + selectedUser + "\n Start: " + start + "\n End: " + end;


            StringBuilder usersString = new StringBuilder();
            for (String u : allUsers) {
                usersString.append("- ").append(u).append("\n");
            }


            String houseData = form.getFormattedInput().split("USERS:")[0];


            if (!Validator.validateTimeframe(timeframe, usersString.toString(), houseData)) {
                JOptionPane.showMessageDialog(parentFrame,
                        "Invalid timeframe:\n- Dates out of range\n- Start must be before End",
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
        sb.append("\nUSERS\n");


        Set<String> allUsers = new HashSet<>();


        try {
            House existingHouse = House.getInstance();
            if (existingHouse != null && existingHouse.getResidents() != null) {
                for (User user : existingHouse.getResidents()) {
                    allUsers.add(user.getName());
                }
            }
        } catch (Exception e) {
            System.out.println("No existing users found");
        }


        allUsers.addAll(users);

        for (String user : allUsers) {
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
                    String val = line.replaceAll("[^0-9]", "");
                    sb.append("Power Consumption: ").append(val).append("\n");
                } else if (line.startsWith("Embodied Emission:")) {
                    String val = line.replaceAll("[^0-9.]", "");
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

        String finalInput = sb.toString();
        System.out.println("Final submission to parser:\n" + finalInput);


        House h = House.getInstance();
        Parser.populateHouseFromForm(finalInput);
        System.out.println("Data merged with existing house");
        try {
            Parser.saveHouse("json.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}