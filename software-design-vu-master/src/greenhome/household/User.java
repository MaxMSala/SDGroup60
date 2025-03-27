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

    public double calcCost() {
        double totalCost = 0.0;
        House house = House.getInstance();

        for (Timeframe tf : house.getTimeframes()) {
            for (User user : tf.getUsers()) {
                if(user.getName().equals(this.name)) {
                    double usageHours = tf.getUsageDurationInHoursForAppliance();
                    double usageHoursAdjusted = usageHours / tf.getUsers().size();
                    double elecTariff = house.getTariff();

                    totalCost += usageHoursAdjusted * elecTariff;
                }
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