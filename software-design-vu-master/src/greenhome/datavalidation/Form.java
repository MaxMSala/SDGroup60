package greenhome.datavalidation;

import javax.swing.*;
import com.toedter.calendar.JDateChooser;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.SpinnerDateModel;

public class Form {
    private List<String> input;
    private List<String> inputType;

    public Form() {
        input = new ArrayList<>();
        inputType = new ArrayList<>();
    }

    public void addInput(String inputValue, String type) {
        input.add(inputValue);
        inputType.add(type);
    }

    public List<String> getInput() {
        return input;
    }

    public List<String> getInputType() {
        return inputType;
    }

    public String getFormattedInput() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < input.size(); i++) {
            sb.append(inputType.get(i)).append(": ").append(input.get(i)).append("\n");
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Form::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Energy Consumption Form");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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

            String startDate = dateFormat.format(startDateChooser.getDate()) + " " + timeFormat.format((Date)startTimeSpinner.getValue());
            String endDate = dateFormat.format(endDateChooser.getDate()) + " " + timeFormat.format((Date)endTimeSpinner.getValue());

            form.addInput(regionDropdown.getSelectedItem().toString(), "Region");
            form.addInput(tariffField.getText(), "Tariff");
            form.addInput(startDate, "Start DateTime");
            form.addInput(endDate, "End DateTime");

            JOptionPane.showMessageDialog(frame, form.getFormattedInput());

            String newUser = JOptionPane.showInputDialog(frame, "Add new user:");
            if (newUser != null && !newUser.trim().isEmpty()) {
                form.addInput(newUser, "User");
                JOptionPane.showMessageDialog(frame, "New user '" + newUser + "' added successfully!");
                frame.dispose();
                showApplianceWindow(form);
            }
            String output = form.getFormattedInput();

            // Print to console to verify correctness
            System.out.println(output);

            // Display in GUI
            JOptionPane.showMessageDialog(frame, output);
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
        JButton addUserButton = new JButton("Add New User");
        JButton showReport = new JButton("show report");
        addButton.addActionListener(e -> {
            String applianceInfo = "Name: " + nameField.getText() + "\n" +
                    "Power Consumption: " + powerField.getText() + "\n" +
                    "Embodied Emmsion: " + embodiedEmission .getText();

            form.addInput(applianceInfo, "Appliance");
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

        JTextField footprintField = new JTextField();
        addPlaceholder(footprintField, "e.g., 2.4 kg CO2");

        JTextField costField = new JTextField();
        addPlaceholder(costField, "e.g., 0.36 USD/day");

        fieldsPanel.add(new JLabel("Name:"));
        fieldsPanel.add(nameField);
        fieldsPanel.add(new JLabel("Power Consumption:"));
        fieldsPanel.add(powerField);
        fieldsPanel.add(new JLabel("Footprint:"));
        fieldsPanel.add(footprintField);
        fieldsPanel.add(new JLabel("Cost:"));
        fieldsPanel.add(costField);

        JButton addTimeframeButton = new JButton("Add Timeframe");
        fieldsPanel.add(addTimeframeButton);

        applianceFrame.add(fieldsPanel, BorderLayout.NORTH);

        JTextArea timeframeArea = new JTextArea(8, 40);
        timeframeArea.setEditable(false);
        JScrollPane timeframePane = new JScrollPane(timeframeArea);
        applianceFrame.add(timeframePane, BorderLayout.CENTER);

        List<String> timeframes = new ArrayList<>();

        addTimeframeButton.addActionListener(e -> {
            String newTimeframe = addApplianceTimeframe(applianceFrame);
            if (!newTimeframe.equals("None")) {
                timeframes.add(newTimeframe);
                timeframeArea.append("Timeframe " + timeframes.size() + ": " + newTimeframe + "\n");
            }
        });

        JPanel bottomButtons = new JPanel(new GridLayout(1, 3, 10, 10));
        JButton addButton = new JButton("Add Appliance");
        JButton addUserButton = new JButton("Add New User");
        JButton showReportButton = new JButton("Show Report");
        bottomButtons.add(addButton);
        bottomButtons.add(addUserButton);
        bottomButtons.add(showReportButton);

        addButton.addActionListener(e -> {
            StringBuilder applianceInfo = new StringBuilder();
            applianceInfo.append("Name: ").append(nameField.getText()).append("\n");
            applianceInfo.append("Power Consumption: ").append(powerField.getText()).append("\n");
            applianceInfo.append("Footprint: ").append(footprintField.getText()).append("\n");
            applianceInfo.append("Cost: ").append(costField.getText()).append("\n");

            if (!timeframes.isEmpty()) {
                applianceInfo.append("Timeframes:\n");
                for (String tf : timeframes) {
                    applianceInfo.append(" - ").append(tf).append("\n");
                }
            } else {
                applianceInfo.append("Timeframes: None\n");
            }

            form.addInput(applianceInfo.toString(), "Appliance");
            JOptionPane.showMessageDialog(applianceFrame, "Appliance added:\n" + applianceInfo);

            nameField.setText("");
            powerField.setText("");
            footprintField.setText("");
            costField.setText("");
            timeframeArea.setText("");
            timeframes.clear();

            addPlaceholder(nameField, "e.g., Fridge");
            addPlaceholder(powerField, "e.g., 150W");
            addPlaceholder(footprintField, "e.g., 2.4 kg CO2");
            addPlaceholder(costField, "e.g., 0.36 USD/day");
        });

        addUserButton.addActionListener(e -> {
            String newUser = JOptionPane.showInputDialog(applianceFrame, "Add new user:");
            if (newUser != null && !newUser.trim().isEmpty()) {
                form.addInput(newUser, "User");
                JOptionPane.showMessageDialog(applianceFrame, "New user '" + newUser + "' added successfully!");
            }
        });

        showReportButton.addActionListener(e -> JOptionPane.showMessageDialog(applianceFrame, form.getFormattedInput()));

        applianceFrame.add(bottomButtons, BorderLayout.SOUTH);
        applianceFrame.setLocationRelativeTo(null);
        applianceFrame.setVisible(true);
    }
    private static String addApplianceTimeframe(JFrame parentFrame) {
        JPanel timeframePanel = new JPanel(new GridLayout(2, 2, 10, 10));

        JSpinner startSpinner = new JSpinner(new SpinnerDateModel());
        startSpinner.setEditor(new JSpinner.DateEditor(startSpinner, "HH:mm"));

        JSpinner endSpinner = new JSpinner(new SpinnerDateModel());
        endSpinner.setEditor(new JSpinner.DateEditor(endSpinner, "HH:mm"));

        timeframePanel.add(new JLabel("Start Time:"));
        timeframePanel.add(startSpinner);
        timeframePanel.add(new JLabel("End Time:"));
        timeframePanel.add(endSpinner);

        int result = JOptionPane.showConfirmDialog(parentFrame, timeframePanel,
                "Add Timeframe", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            return sdf.format(startSpinner.getValue()) + " - " + sdf.format(endSpinner.getValue());
        } else {
            return "None";
        }
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
}
