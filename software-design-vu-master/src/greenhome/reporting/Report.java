package greenhome.reporting;

import greenhome.household.*;

import javax.swing.*;
import java.awt.*;
import java.util.Set;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.category.DefaultCategoryDataset;

class Report {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Carbon Footprint Report");
        frame.setSize(1000, 700);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout());
        frame.add(panel);
        displayReport(panel);

        frame.setVisible(true);
    }

    private static void displayReport(JPanel panel) {
        JTextArea reportArea = new JTextArea();
        reportArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(reportArea);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton graphButton = new JButton("Graph type");
        JButton whatIfButton = new JButton("What If scenarios");
        buttonPanel.add(graphButton);
        graphButton.addActionListener(e -> openChartWindow());
        buttonPanel.add(whatIfButton);
        whatIfButton.addActionListener(e -> openWhatIfWindow());

        panel.add(buttonPanel, BorderLayout.SOUTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        House house = House.getInstance();
        injectFakeData();
        String date = java.time.LocalDate.now().toString();
        String report = generateReport(house, date);
        reportArea.setText(report);
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

        return ChartFactory.createBarChart(
                "Carbon Footprint per Appliance",
                "Appliance",
                "kg CO2",
                dataset
        );
    }

    private static void openWhatIfWindow() {
        JFrame whatIfFrame = new JFrame("What If Scenarios");
        whatIfFrame.setSize(400, 200);
        whatIfFrame.setLayout(new GridLayout(3, 1, 10, 10));
        whatIfFrame.setLocationRelativeTo(null);

        JLabel instruction = new JLabel("Choose a What-If scenario type:", SwingConstants.CENTER);
        JButton generalInfoButton = new JButton("Change General Info");
        JButton applianceButton = new JButton("Appliance-Specific Changes");

        whatIfFrame.add(instruction);
        whatIfFrame.add(generalInfoButton);
        whatIfFrame.add(applianceButton);

        // Placeholder actions
        generalInfoButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(whatIfFrame, "soon");
        });

        applianceButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(whatIfFrame, "soon");
        });

        whatIfFrame.setVisible(true);
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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}