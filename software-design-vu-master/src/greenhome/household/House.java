package greenhome.household;

import greenhome.datavalidation.*;
import greenhome.time.DateTime;
import greenhome.time.Timeframe;

import java.util.List;
import java.util.Set;


public class House {

    private static  House instance;

    // Core attributes (initialized on creation)
    private List<User> residents;
    private List<Appliance> appliances;
    private List<Timeframe> timeframes;
    private String region;
    private double electricityTariff;
    private DateTime Start;
    private DateTime End;

    // Derived values
    private int ecoScore;
    private double footPrintGenerated;
    private double costsGenerated;

    // Private constructor
    private House(List<User> residents, List<Appliance> appliances, List<Timeframe> timeframes,
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
    public static synchronized House constructInstance(List<User> residents,
                                                 List<Appliance> appliances,
                                                 List<Timeframe> timeframes,
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
    public void setRegion(String region){ this.region = region;}
    public void setTariff(Double tariff){this.electricityTariff = tariff;}
    public void addUser(User user){this.residents.add(user);};
    public void addAppliance(Appliance appliance){this.appliances.add(appliance);};
    public void addTimeframe(Timeframe timeframe){this.timeframes.add(timeframe);};
    public void modUser(List<User> newRes){this.residents = newRes;};
    public void modAppliances(List<Appliance> newApps){this.appliances = newApps;};
    public void modTimeframes(List<Timeframe> newFrames){this.timeframes = newFrames;}

   // public void setElectricityTariff(double tariff) { this.electricityTariff = tariff; }
   // public void setEcoScore(int ecoScore) { this.ecoScore = ecoScore; }
   // public void setFootPrint(double footprint) { this.footPrint = footprint; }

    // Getters
    public List<Appliance> getAppliances() { return appliances;}
    public double getFootPrint() { return footPrintGenerated; }
    public int getEcoScore() { return ecoScore; }
    public double getElectricityTariff() {return electricityTariff;}
    public List<Timeframe> getTimeframes() {return timeframes;}
    public List<User> getResidents() {return residents;}

    public void setStart(DateTime startDateTime) {
        this.Start = startDateTime;
    }
}