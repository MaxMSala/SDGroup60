package greenhome.household;

import greenhome.time.*;

public class User {

    private String name;
    private int ecoScore;
    private double carbonFootprint = 0;
    private double costsGenerated;

    public User(String name) {
        this.name = name;
    }

    private void calcCost() {
        double totalCost = 0.0;
        House house = House.getInstance();

        for (Timeframe tf : house.getTimeframes()) {
            for (User user : tf.getUsers()) {
                if(user.getName().equals(this.name)) {
                    double usageHours = tf.getUsageDurationInHoursForAppliance();
                    double usageHoursAdjusted = usageHours / tf.getUsers().size();
                    double elecTariff = house.getElectricityTariff();

                    totalCost += usageHoursAdjusted * elecTariff;
                }
            }
        }
        this.costsGenerated = totalCost;

    }

    public double getCostsGenerated(){
        calcCost();
        return costsGenerated;
    }

    private void sumFootPrint (){
        double totalFootPrint = 0.0;
        House house = House.getInstance();

        for (Timeframe tf : house.getTimeframes()) {
            for (User user : tf.getUsers()) {
                if(user.getName().equals(this.name)) {
                    totalFootPrint += (tf.getCarbonFootprint() / tf.getUsers().size());
                }
            }
        }
        this.carbonFootprint = totalFootPrint;
    }

    public double getCarbonFootprint() {
        sumFootPrint();
        return carbonFootprint;
    }

    // getters
    public String getName() {return this.name;}
}