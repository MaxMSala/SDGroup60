package greenhome.household;

import greenhome.time.DateTime;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;


public class House {

    // Core attributes (initialized on creation)
    private static  House instance;
    private List<User> residents;
    private List<Appliance> appliances;
    private List<Timeframe> timeframes;
    private String region;
    private double electricityTariff;
    private DateTime startDate;
    private DateTime endDate;

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
        this.startDate = new DateTime(1,1,1,1,1);  // Set to null if no default
        this.endDate = new DateTime(2,2,2,2,2);;    // Set to null if no default
        this.ecoScore = 0;
        this.footPrintGenerated = 0.0;
        this.costsGenerated = 0.0;
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

    // interface methods
    // derived getters
    public double getFootPrint() { sumFootPrint(); return this.footPrintGenerated; }
    public double getCosts() {calcCost(); return this.costsGenerated;}
    public int getEcoScore() { calcEcoScore(); return this.ecoScore;}

    // setters
    public void setRegion(String region){ this.region = region;}
    public void setTariff(Double tariff){this.electricityTariff = tariff;}
    public void addUser(User user){this.residents.add(user);};
    public void addAppliance(Appliance appliance){this.appliances.add(appliance);};
    public void addTimeframe(Timeframe timeframe){this.timeframes.add(timeframe);};
    public void modUser(List<User> newRes){this.residents = newRes;};
    public void modAppliances(List<Appliance> newApps){this.appliances = newApps;};
    public void modTimeframes(List<Timeframe> newFrames){this.timeframes = newFrames;}
    public void setStart(DateTime startDateTime) {this.startDate = startDateTime;}
    public void setEnd(DateTime endDate) {this.endDate = endDate;}
    // public void setElectricityTariff(double tariff) { this.electricityTariff = tariff; }
    // public void setEcoScore(int ecoScore) { this.ecoScore = ecoScore; }
    // public void setFootPrint(double footprint) { this.footPrint = footprint; }

    //getters
    public List<Appliance> getAppliances() { return appliances;}
    public double getElectricityTariff() {return electricityTariff;}
    public List<Timeframe> getTimeframes() {return timeframes;}
    public List<User> getResidents() {return residents;}
    public String getRegion() {return region;}
    public DateTime getStart() {return startDate;}
    public DateTime getEnd() {return endDate;}


    // internal private calculations
    private void calcCost() {
        double totalCost = 0.0;

        for (Appliance appliance : appliances) {
            totalCost += appliance.getGeneratedCost();
        }
        this.costsGenerated = totalCost;
    }

    private void sumFootPrint() {
        double totalFootPrint = 0.0;

        for (Appliance appliance : appliances) {
            totalFootPrint += appliance.getGeneratedFootprint();
        }
        this.footPrintGenerated = totalFootPrint;
    }

    private void calcEcoScore()     {
        double totalFootPrint = 0.1;
        System.out.println("✅ECOSCORE START DEBUG");
        for (Appliance appliance : appliances) {
            totalFootPrint += appliance.getGeneratedFootprint();
            System.out.println(totalFootPrint);
        }

        LocalDateTime start = this.startDate.toLocalDateTime();
        LocalDateTime end = this.endDate.toLocalDateTime();

        Duration runTime = Duration.between(start, end);
        double ecoScore = 0.0;
        ecoScore = 100 / (1 + Math.exp(0.04 * ((totalFootPrint / runTime.toHours() ) - 1125)));
        System.out.println((totalFootPrint / runTime.toHours() ));
        System.out.println(Math.exp(0.04 * ((totalFootPrint / runTime.toHours() ) - 1125)));

        System.out.println("✅ECOSCORE: " + ecoScore);

        this.ecoScore = (int) ecoScore;
    }

}