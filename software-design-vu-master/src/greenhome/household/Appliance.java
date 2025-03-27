package greenhome.household;

import greenhome.datavalidation.*;
import greenhome.time.Timeframe;
import java.util.Set;

public class Appliance {

    private String name;

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
        Set<Timeframe> timeframes = house.getTimeframes();


        return 0.0;
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
}