package greenhome.household;

import java.util.List;

public class Appliance {

    // Internal attributes
    private double powerConsumption;
    private double embodiedEmissions;
    private String name;

    // Derived attributes ('/' prefix)
    private double generatedFootprint;
    private double generatedCost;

    // constructor to be deleted later
    public Appliance() {}

    public Appliance(String name, double powerConsumption, double embodiedEmissions) {
        this.name = name;
        this.powerConsumption = powerConsumption;
        this.embodiedEmissions = embodiedEmissions;
    }

    // interface getters and setters
    // setters
    // to delete later
    public void setGeneratedFootprint(double generatedFootprint) {this.generatedFootprint = generatedFootprint;}
    // to delete later
    public void setName(String name) {this.name = name;}

    // getters
    public double getGeneratedCost() {calcCost(); return generatedCost;}
    public double getGeneratedFootprint() {sumFootPrint(); return generatedFootprint;}
    public String getName() {return this.name;}
    public double getPowerConsumption() { return powerConsumption; }
    public double getEmbodiedEmissions() {return embodiedEmissions / 10.0;}


    // internal calculations
    private void sumFootPrint() {
        // to be implemented
        House house = House.getInstance();
        List<Timeframe> timeframes = house.getTimeframes();

        double totalFootprint = 0.0;
        for (Timeframe timeframe : timeframes) {
            if(timeframe.getAppliance().getName().equals(this.name)) {
                totalFootprint  += timeframe.getCarbonFootprint();
                System.out.println("total footprint: " + totalFootprint);
            }
        }
        System.out.println("finish: total footprint: " + totalFootprint);
        System.out.println("embodied emisions: " + embodiedEmissions);

        double combinedFootprint = (embodiedEmissions / 10) + totalFootprint;
        this.generatedFootprint = combinedFootprint;
    }


    private void calcCost() {
        double totalCost = 0.0;
        House house = House.getInstance();

        for (Timeframe tf : house.getTimeframes()) {
            if(tf.getAppliance().getName().equals(this.name)) {
                double usageHours = tf.getUsageDurationInHoursForAppliance();
                double elecTariff = house.getElectricityTariff();
                double watts = tf.getAppliance().powerConsumption; // Make sure this method exists

                double kWh = (watts / 1000.0) * usageHours;
                totalCost += kWh * elecTariff;
            }
        }
        this.generatedCost = totalCost;
    }

}