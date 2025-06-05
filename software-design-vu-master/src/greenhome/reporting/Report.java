package greenhome.reporting;

import greenhome.apiintegration.Average;
import greenhome.household.*;
import greenhome.input.WhatIfScenarios;
import greenhome.time.DateTime;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;


public class Report {

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

        graphButton.addActionListener(e -> {
            dialog.setVisible(false);
            openChartWindow(() -> dialog.setVisible(true));
        });
        buttonPanel.add(graphButton);
        JButton whatIfButton = new JButton("What If scenarios");

        whatIfButton.addActionListener(e -> {
            dialog.dispose();
            WhatIfScenarios.openWhatIfWindow();
        });
        buttonPanel.add(whatIfButton);
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dialog.dispose());
        buttonPanel.add(closeButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        String date = java.time.LocalDate.now().toString();
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

        report.append("Carbon Footprint Report on ").append(date).append("\n");
        report.append(String.format("Generated for period: %s -- %s\n", house.getStart().toSlashString(), house.getEnd().toSlashString()));
        report.append("\n");
        String title = " REPORT ";
        int totalWidth = 100;
        int titleLength = title.length();
        int titlePadding = (totalWidth - titleLength) / 2;
        String centeredTitle = "#".repeat(titlePadding) + title + "#".repeat(98 - titlePadding - titleLength);
        report.append(centeredTitle).append("\n\n");

        report.append("Total Carbon Footprint: ").append(String.format("%.2f kg CO2", house.getFootPrint())).append("\n");
        report.append("Total Costs: ").append(String.format("%.2f EUR", house.getCosts())).append(String.format(" with %.2f EUR per kW", house.getElectricityTariff())).append("\n");


        report.append("\n");
        String section1 = " Appliances Ranked by Carbon Footprint (Descending) ";
        int section1Length = section1.length();
        int section1Padding = (totalWidth - section1Length) / 2;

        String centeredSection1 = "-".repeat(section1Padding) + section1 + "-".repeat(totalWidth - section1Padding - section1Length);
        report.append(centeredSection1).append("\n\n");

        List<Appliance> appliances_byCF = house.getAppliances();
        if (appliances_byCF != null) {
            appliances_byCF.sort((a1, a2) -> Double.compare(a2.getGeneratedFootprint(), a1.getGeneratedFootprint()));

            Set<String> printedNames = new HashSet<>();

            for (Appliance appliance : appliances_byCF) {
                String name = appliance.getName();
                if (!printedNames.contains(name)) {
                    report.append(name).append(" - ")
                            .append(String.format("%.2f", appliance.getGeneratedFootprint()))
                            .append(" kg CO2\n");
                    printedNames.add(name);
                }
            }
        }


        report.append("\n");
        String section2 = " Appliances Ranked by Costs Generated (Descending) ";
        int section2Length = section2.length();
        int section2Padding = (totalWidth - section2Length) / 2;

        String centeredSection2 = "-".repeat(section2Padding) + section2 + "-".repeat(totalWidth - section2Padding - section2Length);
        report.append(centeredSection2).append("\n\n");

        List<Appliance> appliances_byCosts = house.getAppliances();
        if (appliances_byCosts != null) {

            appliances_byCosts.sort((a1, a2) -> Double.compare(a2.getGeneratedCost(), a1.getGeneratedCost()));

            Set<String> printedNames = new HashSet<>();

            for (Appliance appliance : appliances_byCosts) {
                String name = appliance.getName();
                if (!printedNames.contains(name)) {
                    report.append(name).append(" - ")
                            .append(String.format("%.2f", appliance.getGeneratedCost()))
                            .append(" EUR\n");
                    printedNames.add(name);
                }
            }
        }


        report.append("\n");
        String section3 = " Household Compared to International Average  ";
        int section3Lentgth = section3.length();
        int section3Padding = (totalWidth - section3Lentgth) / 2;

        String centeredSection3 = "-".repeat(section3Padding) + section3 + "-".repeat(totalWidth - section3Padding - section3Lentgth);
        report.append(centeredSection3).append("\n\n");
        Average avg = new Average();
        avg.fetchAverage();
        double perCapita = house.getFootPrint() / house.getResidents().size() / 1000;
        report.append(String.format("Your household CO2 per capita: %.2f tonnes\n", perCapita));
        report.append(String.format("Global average CO2 per capita for the interval of %d days: %.2f tonnes\n", DateTime.daysBetween(house.getStart(),house.getEnd()), avg.getAverageEmissionsPerCap(house.getStart(),house.getEnd())));

        if (perCapita > avg.getAverageEmissionsPerCap(house.getStart(),house.getEnd())) {
            double excess = perCapita - avg.getAverageEmissionsPerCap(house.getStart(),house.getEnd());
            report.append(String.format("You are %.2f tonnes above the global average.\n", excess));
        } else {
            double savings = avg.getAverageEmissionsPerCap(house.getStart(),house.getEnd()) - perCapita;
            report.append(String.format("You are %.2f tonnes below the global average. Well done!\n", savings));
        }


        report.append("\n\n");
        String title2 = " RECOMMENDATIONS ";
        int title2Length = title2.length();
        int title2Padding = (totalWidth - title2Length) / 2;
        String centeredTitile2 = "#".repeat(title2Padding) + title2 + "#".repeat(98 - title2Padding - title2Length);
        report.append(centeredTitile2).append("\n\n");
        report.append(Recommendations.generate());

        return report.toString();
    }

    private static void openChartWindow(Runnable onClose) {
        JFrame chartFrame = new JFrame("Chart View");
        chartFrame.setSize(800, 600);
        chartFrame.setLayout(new BorderLayout());
        chartFrame.setLocationRelativeTo(null);

        chartFrame.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
                onClose.run();
            }
        });

        String[] chartTypes = {"Pie Chart", "Bar Chart"};
        JComboBox<String> chartSelector = new JComboBox<>(chartTypes);
        chartFrame.add(chartSelector, BorderLayout.NORTH);
        JPanel chartPanel = new JPanel(new BorderLayout());
        chartFrame.add(chartPanel, BorderLayout.CENTER);
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> {
            chartFrame.dispose();
            onClose.run();
        });
        bottomPanel.add(closeButton);
        chartFrame.add(bottomPanel, BorderLayout.SOUTH);

        chartSelector.addActionListener(e -> {
            String selected = (String) chartSelector.getSelectedItem();
            chartPanel.removeAll();
            JFreeChart chart = "Pie Chart".equals(selected) ? createPieChart() : createBarChart();
            chartPanel.add(new ChartPanel(chart), BorderLayout.CENTER);
            chartPanel.revalidate();
            chartPanel.repaint();
        });
        chartSelector.setSelectedIndex(0);
        chartFrame.setVisible(true);
    }

    private static JFreeChart createPieChart() {
        DefaultPieDataset dataset = new DefaultPieDataset();
        for (Appliance appliance : house.getAppliances()) {
            dataset.setValue(appliance.getName(), appliance.getGeneratedFootprint());
        }
        return ChartFactory.createPieChart("Appliance Carbon Footprint (kg CO2)", dataset, true, true, false);
    }

    private static JFreeChart createBarChart() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
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
}
