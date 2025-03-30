package greenhome.input;

import com.toedter.calendar.JDateChooser;
import greenhome.household.Appliance;
import greenhome.household.House;
import greenhome.household.Parser;
import greenhome.household.User;
import greenhome.time.DateTime;
import greenhome.household.Timeframe;
import greenhome.validation.Validator;
import greenhome.input.Form;
import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

import static greenhome.input.Form.addAppliance;
import static greenhome.input.Form.addApplianceTimeframe;

public class WhatIfScenarios {
    static House house = House.getInstance();
    private static final Form whatIfForm = new Form();


    private static final Map<String, Double> simulatedChanges = new HashMap<>();
    private static final Map<String, List<String>> applianceTimeframes = new HashMap<>();

    private static String houseInfoString = null;
    private static Form form;

    public static void openWhatIfWindow() {
        JDialog whatIfFrame = new JDialog((Frame) null, "What If Scenarios", true);
        whatIfFrame.setSize(400, 250);
        whatIfFrame.setLayout(new GridLayout(4, 1, 10, 10));
        whatIfFrame.setLocationRelativeTo(null);

        JLabel instruction = new JLabel(" What-If scenarios:", SwingConstants.CENTER);
        JButton generalInfoButton = new JButton("Change  Info");
        //JButton applianceButton = new JButton("Appliance-Specific Changes");
        JButton submitAllButton = new JButton("Submit All to Parser");

        whatIfFrame.add(instruction);
        whatIfFrame.add(generalInfoButton);
        //whatIfFrame.add(applianceButton);
        whatIfFrame.add(submitAllButton);

        generalInfoButton.addActionListener(e -> {
            whatIfFrame.dispose();
            openApplianceScenarioWindow();
        });

     //  applianceButton.addActionListener(e -> {
     //      whatIfFrame.dispose();
     //      openApplianceScenarioWindow();
     //  });

        submitAllButton.addActionListener(e -> {
            submitAllChanges();
        });


        whatIfFrame.setVisible(true);
    }
      //right one !!!!!
    public static void openApplianceScenarioWindow() {
            String original = Parser.houseToString();

        JDialog dialog = new JDialog((Frame) null, "Edit Entire House (What-If)", true);
            dialog.setSize(800, 600);
            dialog.setLayout(new BorderLayout(10, 10));
            dialog.setLocationRelativeTo(null);

            JTextArea textArea = new JTextArea(original);
            JScrollPane scrollPane = new JScrollPane(textArea);

            JButton submitButton = new JButton("Submit What-If Modifications");
            JButton backButton = new JButton("Back");
        JButton addApplianceButton = new JButton("Add Appliance");

        JPanel bottomPanel = new JPanel(new FlowLayout());
        bottomPanel.add(addApplianceButton);
            bottomPanel.add(submitButton);
            bottomPanel.add(backButton);

            submitButton.addActionListener(e -> {
                String modifiedString = textArea.getText();

                try {
                    // Merge new appliance data into a string
                    whatIfForm.mergeAllData();
                    String formString = whatIfForm.getFormattedInput();

                    // Combine the text area content and form content
                    String combined = modifiedString + "\n\n" + formString;
                    JOptionPane.showMessageDialog(dialog, new JScrollPane(new JTextArea(combined)), "Final What-If String", JOptionPane.INFORMATION_MESSAGE);

                    Parser.whatifStringModHouse(combined);

                    JOptionPane.showMessageDialog(dialog, "Changes submitted via Parser.");
                    dialog.dispose();
                    whatIfForm.getUsers().clear();
                    openWhatIfWindow();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(dialog, "Failed to submit string to parser. Check format.");
                }
            });

            backButton.addActionListener(e -> {
                dialog.dispose();
                openWhatIfWindow();
            });

        addApplianceButton.addActionListener(ev -> {
            dialog.dispose(); // Close current window

            Form.addAppliance(whatIfForm); // Store appliance in the shared form

            SwingUtilities.invokeLater(() -> openApplianceScenarioWindow());
        });
            dialog.add(new JLabel("You may directly edit the house string below. Format must be valid."), BorderLayout.NORTH);
            dialog.add(scrollPane, BorderLayout.CENTER);
            dialog.add(bottomPanel, BorderLayout.SOUTH);

            dialog.setVisible(true);

        }

   // private static void openGeneralChangesWindow() {
   //     JDialog dialog = new JDialog((Frame) null, "General Info Changes", true);
   //     dialog.setSize(400, 250);
   //     dialog.setLayout(new BorderLayout(10, 10));
   //     dialog.setLocationRelativeTo(null);
   //
   //     JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10));
   //
   //     JLabel regionLabel = new JLabel("Select Region:");
   //     JComboBox<String> regionDropdown = new JComboBox<>(new String[]{
   //             "Andorra", "United Arab Emirates", "Afghanistan", "Antigua and Barbuda", "Albania", "Armenia", "Angola", "Argentina", "Austria", "Australia", "Lord Howe Island", "New South Wales", "Northern Territory", "Queensland", "South Australia", "Tasmania", "Cape Barren Island", "Flinders Island", "King Island", "Victoria", "Western Australia", "Rottnest Island", "Aruba", "Åland Islands", "Azerbaijan", "Bosnia and Herzegovina", "Barbados", "Bangladesh", "Belgium", "Burkina Faso", "Bulgaria", "Bahrain", "Burundi", "Benin", "Bermuda", "Brunei", "Bolivia", "Brazil", "Central Brazil", "North Brazil", "North-East Brazil", "South Brazil", "Bahamas", "Bhutan", "Botswana", "Belarus", "Belize", "Canada", "Alberta", "British Columbia", "Manitoba", "New Brunswick", "Newfoundland and Labrador", "Nova Scotia", "Northwest Territories", "Nunavut", "Ontario", "Prince Edward Island", "Québec", "Saskatchewan", "Yukon", "Democratic Republic of the Congo", "Central African Republic", "Congo", "Switzerland", "Ivory Coast", "Easter Island", "Sistema Eléctrico de Aysén", "Sistema Eléctrico de Magallanes", "Sistema Eléctrico Nacional", "Cameroon", "China", "Colombia", "Costa Rica", "Cuba", "Cabo Verde", "Curaçao", "Cyprus", "Czechia", "Germany", "Djibouti", "Denmark", "Bornholm", "West Denmark", "East Denmark", "Dominica", "Dominican Republic", "Algeria", "Ecuador", "Estonia", "Egypt", "Western Sahara", "Eritrea", "Spain", "Ceuta", "Fuerteventura", "Gran Canaria", "El Hierro", "Isla de la Gomera", "La Palma", "Lanzarote", "Tenerife", "Formentera", "Ibiza", "Mallorca", "Menorca", "Melilla", "Ethiopia", "Finland", "Fiji", "Falkland Islands", "Micronesia", "Faroe Islands", "Main Islands", "South Island", "France", "Corsica", "Gabon", "Great Britain", "Northern Ireland", "Orkney Islands", "Shetland Islands", "Georgia", "French Guiana", "Guernsey", "Ghana", "Gibraltar", "Greenland", "Gambia", "Guinea", "Guadeloupe", "Equatorial Guinea", "Greece", "South Georgia and the South Sandwich Islands", "Guatemala", "Guam", "Guinea-Bissau", "Guyana", "Hong Kong", "Heard Island and McDonald Islands", "Honduras", "Croatia", "Haiti", "Hungary", "Indonesia", "Ireland", "Israel", "Isle of Man", "Mainland India", "Andaman and Nicobar Islands", "Eastern India", "Himachal Pradesh", "North Eastern India", "Northern India", "Southern India", "Uttar Pradesh", "Uttarakhand", "Western India", "Iraq", "Iran", "Iceland", "Italy", "Central North Italy", "Central South Italy", "North Italy", "Sardinia", "Sicily", "South Italy", "Jersey", "Jamaica", "Jordan", "Japan", "Chūbu", "Chūgoku", "Hokkaidō", "Hokuriku", "Kansai", "Kyūshū", "Okinawa", "Shikoku", "Tōhoku", "Tōkyō", "Kenya", "Kyrgyzstan", "Cambodia", "Comoros", "North Korea", "South Korea", "Kuwait", "Cayman Islands", "Kazakhstan", "Laos", "Lebanon", "Saint Lucia", "Liechtenstein", "Sri Lanka", "Liberia", "Lesotho", "Lithuania", "Luxembourg", "Latvia", "Libya", "Morocco", "Monaco", "Moldova", "Montenegro", "Madagascar", "North Macedonia", "Mali", "Myanmar", "Mongolia", "Macao", "Martinique", "Mauritania", "Malta", "Mauritius", "Maldives", "Malawi", "Mexico", "Malaysia", "Borneo", "Peninsula", "Mozambique", "Namibia", "New Caledonia", "Niger", "Nigeria", "Nicaragua", "Netherlands", "Norway", "Southeast Norway", "Southwest Norway", "Middle Norway", "North Central Sweden", "South Central Sweden", "South Sweden", "Singapore", "Slovenia", "Svalbard and Jan Mayen", "Slovakia", "Sierra Leone", "Senegal", "Somalia", "Suriname", "South Sudan", "São Tomé and Príncipe", "El Salvador", "Syria", "Eswatini", "Chad", "French Southern Territories", "Togo", "Thailand", "Tajikistan", "Timor-Leste", "Turkmenistan", "Tunisia", "Tonga", "Turkey", "Trinidad and Tobago", "Taiwan", "Tanzania", "Ukraine", "Crimea", "Uganda", "Contiguous United States", "Alaska", "Southeast Alaska Power Agency", "Balancing Authority of Northern California", "CAISO", "Imperial Irrigation District", "Los Angeles Department of Water and Power", "Turlock Irrigation District", "Duke Energy Progress East", "Duke Energy Progress West", "Duke Energy Carolinas", "South Carolina Public Service Authority", "South Carolina Electric & Gas Company", "Alcoa Power Generating, Inc. Yadkin Division", "Southwestern Power Administration", "Southwest Power Pool"
   //
   //     });
   //     JLabel tariffLabel = new JLabel("Electricity Tariff (€/kWh):");
   //     JTextField tariffField = new JTextField();
   //
   //     // --- Load from Parser.houseToString ---
   //     String rawHouse = Parser.houseToString();
   //     String region = "Unknown";
   //     String tariff = "0.25";
   //
   //     for (String line : rawHouse.split("\n")) {
   //         if (line.startsWith("Region: ")) {
   //             region = line.split("Region: ")[1];
   //         } else if (line.startsWith("Tariff: ")) {
   //             tariff = line.split("Tariff: ")[1];
   //         }
   //     }
   //
   //     regionDropdown.setSelectedItem(region);
   //     tariffField.setText(tariff);
   //
   //     formPanel.add(regionLabel);
   //     formPanel.add(regionDropdown);
   //     formPanel.add(tariffLabel);
   //     formPanel.add(tariffField);
   //
   //     JButton applyButton = new JButton("Apply Changes");
   //
   //     applyButton.addActionListener(e -> {
   //         String selectedRegion = (String) regionDropdown.getSelectedItem();
   //         String tariffInput = tariffField.getText().trim();
   //
   //         try {
   //             if (!Validator.validateDouble(tariffInput)) {
   //                 JOptionPane.showMessageDialog(dialog, "Tariff must be a decimal number.");
   //                 return;
   //             }
   //
   //             double newTariff = Double.parseDouble(tariffInput);
   //             if (!Validator.validateElectricityTariff(newTariff)) {
   //                 JOptionPane.showMessageDialog(dialog, "Tariff must be between 0.05 and 0.50 €/kWh.");
   //                 return;
   //             }
   //
   //             // Store updated info for report
   //             houseInfoString = "Region: " + selectedRegion + "\nTariff: " + newTariff + "\n";
   //             JOptionPane.showMessageDialog(dialog, "Changes saved.");
   //             dialog.dispose();
   //             openWhatIfWindow();
   //
   //         } catch (Exception ex) {
   //             ex.printStackTrace();
   //             JOptionPane.showMessageDialog(dialog, "Failed to apply tariff update.");
   //         }
   //     });
   //
   //     dialog.add(formPanel, BorderLayout.CENTER);
   //     dialog.add(applyButton, BorderLayout.SOUTH);
   //     dialog.setVisible(true);
   // }
    public static void submitAllChanges() {
        StringBuilder sb = new StringBuilder();

        // Region and Tariff
        sb.append("Region: ").append(
                houseInfoString != null && houseInfoString.contains("Region: ")
                        ? houseInfoString.split("Region: ")[1].split("\n")[0]
                        : house.getRegion()
        ).append("\n");

        sb.append("Tariff: ").append(
                houseInfoString != null && houseInfoString.contains("Tariff: ")
                        ? houseInfoString.split("Tariff: ")[1].split("\n")[0]
                        : house.getElectricityTariff()
        ).append("\n");

        sb.append("Start DateTime: ").append(house.getStart().valsToString()).append("\n");
        sb.append("End DateTime: ").append(house.getEnd().valsToString()).append("\n\n");

        // Users
        Set<String> usersSet = new HashSet<>();
        for (Map.Entry<String, List<String>> entry : applianceTimeframes.entrySet()) {
            for (String tf : entry.getValue()) {
                try {
                    String user = tf.split("User: ")[1].split(",")[0].trim();
                    usersSet.add(user);
                } catch (Exception ignored) {}
            }
        }
        for (User u : house.getResidents()) {
            usersSet.add(u.getName());
        }
        for (String u : usersSet) {
            sb.append("- ").append(u).append("\n");
        }

        sb.append("\nAPPLIANCES\n");
        int count = 1;
        for (Appliance a : house.getAppliances()) {
            sb.append("Appliance ").append(count++).append(":\n");
            sb.append("Name: ").append(a.getName()).append("\n");
            sb.append("Power Consumption: ").append(a.getPowerConsumption()).append("\n");
            sb.append("Embodied Emission: ").append(
                    simulatedChanges.containsKey(a.getName())
                            ? simulatedChanges.get(a.getName()).intValue()
                            : (int) a.getEmbodiedEmissions()
            ).append("\n\n");
        }

        sb.append("TIMEFRAMES PER APPLIANCE\n");
        for (Appliance a : house.getAppliances()) {
            String name = a.getName();
            List<String> modified = applianceTimeframes.getOrDefault(name, new ArrayList<>());
            boolean added = false;

            if (!modified.isEmpty()) {
                sb.append("Appliance: ").append(name).append("\n");
                for (String tf : modified) {
                    sb.append("  - ").append(tf).append("\n");
                }
                added = true;
            }

            if (!added) {
                for (Timeframe tf : house.getTimeframes()) {
                    if (tf.getAppliance().getName().equals(name)) {
                        sb.append("Appliance: ").append(name).append("\n");
                        for (User u : tf.getUsers()) {
                            sb.append("  - User: ").append(u.getName())
                                    .append(", From: ").append(tf.getPeriod()[0].valsToString())
                                    .append(" → ").append(tf.getPeriod()[1].valsToString())
                                    .append("\n");
                        }
                        break;
                    }
                }
            }
        }

        sb.append("\n----- End of Report -----\n");
        String finalString = sb.toString();

        // Show preview
        JOptionPane.showMessageDialog(null, new JScrollPane(new JTextArea(finalString)),
                "Final What-If Submission", JOptionPane.INFORMATION_MESSAGE);

        System.out.println("✅ Final What-If Submission:\n" + finalString);

        // Send to parser
        Parser.whatifStringModHouse(finalString);
    }
}
