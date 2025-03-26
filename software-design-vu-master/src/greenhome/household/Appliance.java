package greenhome.household;

import greenhome.datavalidation.*;
import greenhome.time.Timeframe;
import java.util.Set;

public class Appliance {

    private String name;

    // Internal attributes
    private int powerConsumption;
    private double embodiedEmissions;
    private double kiloWattHours;
    private double euros;
    private double tonnesCO2eq;

    // Derived attributes ('/' prefix)
    private double generatedFootprint;
    public double generatedCost;

    public double getGeneratedFootprint() {
        return generatedFootprint;
    }

    public String getName() {
        return this.name;
    }

    // to delete later
    public void setName(String name) {
        this.name = name;
    }

    // to delete later
    public void setGeneratedFootprint(double generatedFootprint) {
        this.generatedFootprint = generatedFootprint;
    }


    // Methods
    public double sumFootPrint(Set<Timeframe> timeframes) {
        // to be implemented
        return 0.0;
    }

    public double calcCost( Set<Timeframe> timeframes) {
        // to be implemented
        double totalCost = 0.0;

        for (Timeframe tf : timeframes) {
            if(tf.getAppliance().name.equals(this.name)) {
                double usageHours = tf.getUsageDurationInHoursForAppliance();
                totalCost += usageHours * House.getInstance().getElectricityTariff();
            }
        }
        this.generatedCost = totalCost;
        return 0.0;
    }

    public double getKiloWattHours() {
        return kiloWattHours;
    }

}