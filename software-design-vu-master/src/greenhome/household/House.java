package greenhome.household;

import greenhome.datavalidation.*;
import greenhome.time.Timeframe;
import java.util.Set;


public class House {

    private static  House instance;

    // Core attributes (initialized on creation)
    private final Set<User> residents;
    private final Set<Appliance> appliances;
    private final Set<Timeframe> timeframes;
    private final String region;
    private final double electricityTariff;

    // Derived values
    private int ecoScore;
    private double footPrintGenerated;
    private double costsGenerated;

    // Private constructor
    private House(Set<User> residents, Set<Appliance> appliances, Set<Timeframe> timeframes,
                  String region, double electricityTariff) {
        this.residents = residents;
        this.appliances = appliances;
        this.timeframes = timeframes;
        this.region = region;
        this.electricityTariff = electricityTariff;

        // Compute derived values
//        this.costsGenerated = calcCost();
//        this.footPrintGenerated = sumFootPrint();
//        this.ecoScore = calcEcoScore();
    }

    // Singleton constructor (with params on first call)
    public static synchronized House constructInstance(Set<User> residents,
                                                 Set<Appliance> appliances,
                                                 Set<Timeframe> timeframes,
                                                 String region,
                                                 double electricityTariff) {
        if (instance == null) {instance = new House(residents, appliances, timeframes, region, electricityTariff);}
        return instance;
    }

    // Singleton accessor (after the upper constructor has been already called)
    public static House getInstance() {
        if (instance == null) {throw new IllegalStateException("House not initialized yet. Use getInstance(...) first.");}
        return instance;
    }

    // === Derived calculations ===

    public double calcCost() {
        double totalCost = 0.0;

        for (Appliance appliance : appliances) {
            totalCost += appliance.calcCost();
        }

        this.costsGenerated = totalCost;
        return totalCost;
    }

    private double sumFootPrint() {
        // to be implemented
        return 0.0;
    }

    private int calcEcoScore() {
        // to be implemented
        return 0;
    }

    // Setters

   // public void setAppliances(Set<Appliance> appliances) { this.appliances = appliances; }
   // public void setElectricityTariff(double tariff) { this.electricityTariff = tariff; }
   // public void setEcoScore(int ecoScore) { this.ecoScore = ecoScore; }
   // public void setFootPrint(double footprint) { this.footPrint = footprint; }

    // Getters
    public Set<Appliance> getAppliances() { return appliances;}
    public double getFootPrint() { return footPrintGenerated; }
    public int getEcoScore() { return ecoScore; }
    public double getElectricityTariff() {return electricityTariff;}
    public Set<Timeframe> getTimeframes() {return timeframes;}
    public Set<User> getResidents() {return residents;}
}