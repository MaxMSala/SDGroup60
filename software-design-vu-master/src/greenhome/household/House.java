package greenhome.household;

import greenhome.time.DateTime;
import greenhome.time.Timeframe;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;


public class House {

    private static  House instance;

    // Core attributes (initialized on creation)
    private List<User> residents;
    private List<Appliance> appliances;
    private List<Timeframe> timeframes;
    private String region;
    private double electricityTariff;
    private DateTime start;
    private DateTime end;

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

    private void calcCost() {
        double totalCost = 0.0;

        for (Appliance appliance : appliances) {
            totalCost += appliance.getGeneratedCost();
        }
        this.costsGenerated = totalCost;
    }

    public double getCostsGenerated() {
        calcCost();
        return this.costsGenerated;
    }

    private void sumFootPrint() {
        double totalFootPrint = 0.0;

        for (Appliance appliance : appliances) {
            totalFootPrint += appliance.getFootprint();
        }
        this.footPrintGenerated = totalFootPrint;
    }

    public double getFootPrintGenerated() {
        sumFootPrint();
        return this.footPrintGenerated;
    }

    private void calcEcoScore() {
        double totalFootPrint = 0.1;

        for (Appliance appliance : appliances) {
            totalFootPrint += appliance.getFootprint();
        }
        LocalDateTime start = this.start.toLocalDateTime();
        LocalDateTime end = this.end.toLocalDateTime();

        Duration runTime = Duration.between(start, end);
        double ecoScore = 0.0;
        ecoScore = 100 / (1 + Math.exp(0.04 * ((totalFootPrint / runTime.toHours() ) - 1125)));
        this.ecoScore = (int) ecoScore;

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
    public String getRegion() {return region;}
    public DateTime getStart() {return start;}
    public DateTime getEnd() {return end;}

    public void setStart(DateTime startDateTime) {
        this.start = startDateTime;
    }
}