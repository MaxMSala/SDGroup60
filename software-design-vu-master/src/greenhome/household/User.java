package greenhome.household;

import greenhome.datavalidation.*;
import greenhome.time.*;

import java.util.Set;

public class User {
    private String name;
    private int ecoScore;
    private double carbonFootprint = 0;
    private double costsGenerated;

    public User(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public double calcCost(double electricityTariff, Set<Timeframe> timeframes) {
        double totalCost = 0.0;

        for (Timeframe tf : timeframes) {
            if(tf.getUser().getName().equals(this.name)) {
                double usageHours = tf.getUsageDurationInHoursForAppliance();
                totalCost += usageHours * electricityTariff;
            }
        }

        this.costsGenerated = totalCost;
        return totalCost;
    }

    //public float getCarbonFootprint (TimeFrame[] timeFrames){
    //    for (int i = 0; i < timeFrames.length; i++) {
    //        if (timeFrames[i].User.getName() == this.name); {
    //            this.carbonFootprint += timeFrames[i].carbonFootprint;
    //        }
    //    }
    //}
}