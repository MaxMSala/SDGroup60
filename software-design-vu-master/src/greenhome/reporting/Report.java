package greenhome.reporting;

import greenhome.apiintegration.Average;
import greenhome.household.*;

import javax.swing.*;
import java.awt.*;
import java.util.*;

import greenhome.time.DateTime;
import greenhome.time.Timeframe;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import com.toedter.calendar.JDateChooser;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import java.text.SimpleDateFormat;
import java.util.List;

class Report {
    private static final House house = House.getInstance();

    public static void main(String[] args) {
        openReportDialog();
    }

    private static void displayReport(JPanel panel, JDialog dialog) {
        JTextArea reportArea = new JTextArea();
        reportArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(reportArea);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JButton graphButton = new JButton("Graph type");
        graphButton.addActionListener(e -> openChartWindow());
        buttonPanel.add(graphButton);

        JButton whatIfButton = new JButton("What If scenarios");
        whatIfButton.addActionListener(e -> {
            dialog.dispose();
            openWhatIfWindow();
        });
        buttonPanel.add(whatIfButton);

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dialog.dispose());
        buttonPanel.add(closeButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        String date = java.time.LocalDate.now().toString();
        injectFakeData();
        String report = generateReport(House.getInstance(), date);
        reportArea.setText(report);
    }


    private static void openReportDialog() {
        JDialog reportDialog = new JDialog((Frame) null, "Carbon Footprint Report", true);
        reportDialog.setSize(1000, 700);
        reportDialog.setLayout(new BorderLayout());

        JPanel panel = new JPanel(new BorderLayout());
        reportDialog.add(panel);

        displayReport(panel, reportDialog);

        reportDialog.setLocationRelativeTo(null);
        reportDialog.setVisible(true);
    }

    public static String generateReport(House house, String date) {
        StringBuilder report = new StringBuilder();

        report.append("Carbon Footprint Report on ").append(date).append("\n\n");
        report.append("Total CF: ").append(String.format("%.2f kg CO2", house.getFootPrint())).append("\n");
        report.append("--------------------------------\n");

        Set<Appliance> appliances = house.getAppliances();
        if (appliances != null) {
            for (Appliance appliance : appliances) {
                report.append(appliance.getName()).append(" - ")
                        .append(appliance.getGeneratedFootprint()).append(" kg CO2\n");
            }
        }

        report.append("--------------------------------\n");
        report.append("Local Ecoscore: ").append(house.getEcoScore()).append("\n");
        report.append("Total Costs: ").append(String.format("%.2f EUR", house.getElectricityTariff())).append("\n");

        Average avg = new Average();
        avg.fetchAverage();

        double perCapita = house.getTonnesCO2eq() / house.getResidents().size();
        report.append("\n--- Comparison to Global Averages ---\n");
        report.append(String.format("Your household CO2 per capita: %.2f tonnes\n", perCapita));
        report.append(String.format("Global average CO2 per capita: %.2f tonnes\n", avg.getAverageEmissionsPerCap()));

        if (perCapita > avg.getAverageEmissionsPerCap()) {
            double excess = perCapita - avg.getAverageEmissionsPerCap();
            report.append(String.format(" You are %.2f tonnes above the global average.\n", excess));
        } else {
            double savings = avg.getAverageEmissionsPerCap()     - perCapita;
            report.append(String.format(" You are %.2f tonnes below the global average. Well done!\n", savings));
        }

        report.append("--------------------------------\n");
        report.append("Recommendations: ").append("[Add logic or field for recommendations]");

        return report.toString();
    }
    private static void openChartWindow() {
        JFrame chartFrame = new JFrame("Select Chart Type");
        chartFrame.setSize(800, 600);
        chartFrame.setLayout(new BorderLayout());

        String[] chartTypes = {"Pie Chart", "Bar Chart"};
        JComboBox<String> chartSelector = new JComboBox<>(chartTypes);
        chartFrame.add(chartSelector, BorderLayout.NORTH);

        JPanel chartPanel = new JPanel(new BorderLayout());
        chartFrame.add(chartPanel, BorderLayout.CENTER);

        chartSelector.addActionListener(e -> {
            String selected = (String) chartSelector.getSelectedItem();
            chartPanel.removeAll();

            JFreeChart chart;
            if ("Pie Chart".equals(selected)) {
                chart = createPieChart();
            } else {
                chart = createBarChart();
            }

            chartPanel.add(new ChartPanel(chart), BorderLayout.CENTER);
            chartPanel.revalidate();
            chartPanel.repaint();
        });

        chartSelector.setSelectedIndex(0);
        chartFrame.setVisible(true);
    }
    private static JFreeChart createPieChart() {
        DefaultPieDataset dataset = new DefaultPieDataset();
        House house = House.getInstance();
        for (Appliance appliance : house.getAppliances()) {
            dataset.setValue(appliance.getName(), appliance.getGeneratedFootprint());
        }

        return ChartFactory.createPieChart(
                "Appliance Carbon Footprint (kg CO2)",
                dataset,
                true, true, false
        );
    }

    private static JFreeChart createBarChart() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        House house = House.getInstance();
        for (Appliance appliance : house.getAppliances()) {
            dataset.addValue(appliance.getGeneratedFootprint(), "CO2", appliance.getName());
        }

        JFreeChart chart = ChartFactory.createBarChart(
                "Carbon Footprint per Appliance",
                "Appliance",
                "kg CO2",
                dataset
        );

        org.jfree.chart.plot.CategoryPlot plot = chart.getCategoryPlot();
        org.jfree.chart.renderer.category.BarRenderer renderer = (org.jfree.chart.renderer.category.BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, Color.BLUE);

        return chart;
    }

    private static void openWhatIfWindow() {

        JDialog whatIfFrame = new JDialog((Frame) null, "What If Scenarios", true);        whatIfFrame.setSize(400, 200);
        whatIfFrame.setLayout(new GridLayout(3, 1, 10, 10));
        whatIfFrame.setLocationRelativeTo(null);

        JLabel instruction = new JLabel("Choose a What-If scenario type:", SwingConstants.CENTER);
        JButton generalInfoButton = new JButton("Change General Info");
        JButton applianceButton = new JButton("Appliance-Specific Changes");

        whatIfFrame.add(instruction);
        whatIfFrame.add(generalInfoButton);
        whatIfFrame.add(applianceButton);
        
        generalInfoButton.addActionListener(e -> {
            ;
            whatIfFrame.dispose();
            openGeneralChangesWindow();
        });

        applianceButton.addActionListener(e -> {
            whatIfFrame.dispose(); 
            openApplianceScenarioWindow(); 
        });

        whatIfFrame.setVisible(true);
    }
    private static void openApplianceScenarioWindow() {
        JFrame frame = new JFrame("What If: Appliance Changes");
        frame.setSize(750, 600);
        frame.setLayout(new BorderLayout(10, 10));
        frame.setLocationRelativeTo(null);

        House house = House.getInstance();
        Set<Appliance> appliances = house.getAppliances();

        if (appliances == null || appliances.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No appliances found.");
            return;
        }

        Map<String, Double> simulatedChanges = new HashMap<>();
        Map<String, List<String>> applianceTimeframes = new HashMap<>();
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

        // Timeframe section
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

        // Bottom preview and submit
        JTextArea previewArea = new JTextArea("Confirmed What-If Changes:\n");
        previewArea.setEditable(false);
        JScrollPane previewScroll = new JScrollPane(previewArea);
        JButton submitAll = new JButton("Submit All Changes");

        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        bottomPanel.add(previewScroll, BorderLayout.CENTER);
        bottomPanel.add(submitAll, BorderLayout.SOUTH);

        // Dropdown behavior
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
            List<String> timeframes = applianceTimeframes.getOrDefault(selected, new ArrayList<>());
            for (String tf : timeframes) {
                timeframeModel.addElement(tf);
            }
        });
        applianceDropdown.setSelectedIndex(0);


        // Add/Edit/Delete timeframe actions
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
            StringBuilder report = new StringBuilder("Final What-If Summary\n------------------------------\n");
            double total = 0.0;

            for (Appliance a : appliances) {
                String name = a.getName();
                String selected = (String) applianceDropdown.getSelectedItem();
                double emission = a.getGeneratedFootprint();
                if (selected.equals(name)) {
                    try {
                        emission = Double.parseDouble(newEmissionField.getText().trim());
                    } catch (Exception ignored) {}
                }                report.append(name).append(" - ");
                if (selected.equals(name) && !newEmissionField.getText().trim().isEmpty()) {
                    report.append(String.format("%.2f kg CO2 (what-if)", emission));
                } else {
                    report.append(String.format("%.2f kg CO2", emission));
                }
                report.append("\n");
                total += emission;

                List<String> timeframes = applianceTimeframes.get(name);
                if (timeframes != null && !timeframes.isEmpty()) {
                    for (String tf : timeframes) {
                        report.append("    - ").append(tf).append("\n");
                    }
                }
            }

            report.append("------------------------------\n");
            report.append("Simulated Total CO2: ").append(String.format("%.2f", total)).append(" kg\n");

            JTextArea area = new JTextArea(report.toString());
            area.setEditable(false);
            JOptionPane.showMessageDialog(frame, new JScrollPane(area), "Final Report", JOptionPane.INFORMATION_MESSAGE);
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
        tariffField.setText(String.valueOf(house.getTariff()));
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
                dialog.dispose();                          // ✅ 4. Close General Info dialog
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


    //dummydata
    private static void injectFakeData() {
        try {
            House house = House.getInstance();

            // Create appliance objects using no-arg constructor
            Appliance fridge = new Appliance();
            fridge.setName("Fridge");
            fridge.setGeneratedFootprint(120.5);

            Appliance tv = new Appliance();
            tv.setName("TV");
            tv.setGeneratedFootprint(80.0);

            Appliance washingMachine = new Appliance();
            washingMachine.setName("Washing Machine");
            washingMachine.setGeneratedFootprint(150.75);

            Set<Appliance> fakeAppliances = new java.util.HashSet<>();
            fakeAppliances.add(fridge);
            fakeAppliances.add(tv);
            fakeAppliances.add(washingMachine);

            // Use reflection to set private 'appliances' field
            java.lang.reflect.Field appliancesField = House.class.getDeclaredField("appliances");
            appliancesField.setAccessible(true);
            appliancesField.set(house, fakeAppliances);

            // Also set other fields (footPrint, ecoScore, tariff)
            java.lang.reflect.Field footPrintField = House.class.getDeclaredField("footPrint");
            footPrintField.setAccessible(true);
            footPrintField.set(house, 120.5 + 80 + 150.75);

            java.lang.reflect.Field ecoScoreField = House.class.getDeclaredField("ecoScore");
            ecoScoreField.setAccessible(true);
            ecoScoreField.set(house, 78);

            java.lang.reflect.Field tariffField = House.class.getDeclaredField("electricityTariff");
            tariffField.setAccessible(true);
            tariffField.set(house, 95.60);

            User alice = new User("Alice");
            User bob = new User("Bob");
            Set<User> usersAlice = new HashSet<>();
            Set<User> usersBob = new HashSet<>();
            usersAlice.add(alice);
            usersBob.add(bob);

// Create DateTime start/end
            DateTime start1 = new DateTime(2025, 3, 27, 10, 0);
            DateTime end1 = new DateTime(2025, 3, 27, 12, 0);

            DateTime start2 = new DateTime(2025, 3, 28, 14, 0);
            DateTime end2 = new DateTime(2025, 3, 28, 16, 0);

// Create timeframe objects
            Timeframe tf1 = new Timeframe(usersAlice, fridge, start1, end1, 200.0, 300.0);
            Timeframe tf2 = new Timeframe(usersBob, tv, start2, end2, 150.0, 250.0);

            Set<Timeframe> fakeTimeframes = new HashSet<>();
            fakeTimeframes.add(tf1);
            fakeTimeframes.add(tf2);

// Set the 'timeframes' field via reflection
            java.lang.reflect.Field tfField = House.class.getDeclaredField("timeframes");
            tfField.setAccessible(true);
            tfField.set(house, fakeTimeframes);

// Also set residents (optional but recommended if you use User logic elsewhere)
            Set<User> fakeUsers = new HashSet<>();
            fakeUsers.add(alice);
            fakeUsers.add(bob);
            java.lang.reflect.Field userField = House.class.getDeclaredField("residents");
            userField.setAccessible(true);
            userField.set(house, fakeUsers);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}