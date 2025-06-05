package greenhome.household;

import java.util.List;

public class Appliance {


    private double powerConsumption;
    private double embodiedEmissions;
    private String name;


    private double generatedFootprint;
    private double generatedCost;


    public Appliance(String name, double powerConsumption, double embodiedEmissions) {
        this.name = name;
        this.powerConsumption = powerConsumption;
        this.embodiedEmissions = embodiedEmissions;
    }


    public void setName(String name) {this.name = name;}


    public double getGeneratedCost() {calcCost(); return generatedCost;}
    public double getGeneratedFootprint() {sumFootPrint(); return generatedFootprint;}
    public String getName() {return this.name;}
    public double getPowerConsumption() { return powerConsumption; }
    public double getEmbodiedEmissions() {return embodiedEmissions ;}



    private void sumFootPrint() {

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

        double combinedFootprint = (embodiedEmissions ) + totalFootprint;
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