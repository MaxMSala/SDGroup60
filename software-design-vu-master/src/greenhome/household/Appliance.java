package greenhome.household;

import greenhome.datavalidation.*;
import greenhome.time.Timeframe;

import java.util.List;
import java.util.Set;

public class Appliance {

    private String name;

    // constructor to be deleted later
    public Appliance() {}

    public Appliance(String name, double powerConsumption, double embodiedEmissions) {
        this.name = name;
        this.powerConsumption = powerConsumption;
        this.embodiedEmissions = embodiedEmissions;
    }

    // Internal attributes
    private double powerConsumption;
    private double embodiedEmissions;

    // Derived attributes ('/' prefix)
    private double generatedFootprint;
    public double generatedCost;

    // Methods
    public double sumFootPrint() {
        // to be implemented
        House house = House.getInstance();
        List<Timeframe> timeframes = house.getTimeframes();

        double sum = 0.0;

        for (Timeframe timeframe : timeframes) {
            if(timeframe.getAppliance().equals(this)) {
                timeframe.calcFootPrint();
                sum += timeframe.carbonFootprint;
            }
        }

        double combinedFootprint = embodiedEmissions + sum;
        generatedFootprint = combinedFootprint;
        return combinedFootprint;
    }

    public double calcCost() {
        double totalCost = 0.0;
        House house = House.getInstance();

        for (Timeframe tf : house.getTimeframes()) {
            if(tf.getAppliance().getName().equals(this.name)) {
                double usageHours = tf.getUsageDurationInHoursForAppliance();
                double elecTariff = house.getElectricityTariff();

                totalCost += usageHours * elecTariff;
            }
        }
        this.generatedCost = totalCost;
        return totalCost;
    }

    // setters
    // to delete later
    public void setGeneratedFootprint(double generatedFootprint) {this.generatedFootprint = generatedFootprint;}
    // to delete later
    public void setName(String name) {this.name = name;}

    // getters
    public double getGeneratedFootprint() {return generatedFootprint;}
    public String getName() {return this.name;}
    public double getPowerConsumption() { return powerConsumption; }
    public double getEmbodiedEmissions() {return embodiedEmissions;}
}