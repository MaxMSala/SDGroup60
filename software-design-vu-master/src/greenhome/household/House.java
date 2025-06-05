package greenhome.household;

import greenhome.time.DateTime;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class House {

    private static volatile House instance;
    private static final Object LOCK = new Object();


    private List<User> residents;
    private List<Appliance> appliances;
    private List<Timeframe> timeframes;
    private String region;
    private double electricityTariff;
    private DateTime startDate;
    private DateTime endDate;


    private int ecoScore;
    private double footPrintGenerated;
    private double costsGenerated;


    private House(List<User> residents, List<Appliance> appliances, List<Timeframe> timeframes,
                  String region, double electricityTariff) {
        this.residents = residents != null ? residents : new ArrayList<>();
        this.appliances = appliances != null ? appliances : new ArrayList<>();
        this.timeframes = timeframes != null ? timeframes : new ArrayList<>();
        this.region = region != null ? region : "Netherlands";
        this.electricityTariff = electricityTariff;
        this.startDate = new DateTime(1, 1, 2025, 8, 0);
        this.endDate = new DateTime(31, 12, 2025, 18, 0);
        this.ecoScore = 0;
        this.footPrintGenerated = 0.0;
        this.costsGenerated = 0.0;

        System.out.println("🏠 House instance created with region: " + this.region);
    }

    public static House getInstance() {
        if (instance == null) {
            synchronized (LOCK) {
                if (instance == null) {
                    System.out.println("🔧 Auto-creating default House instance");
                    instance = new House(
                            new ArrayList<>(),
                            new ArrayList<>(),
                            new ArrayList<>(),
                            "Netherlands",
                            0.25
                    );
                }
            }
        }
        return instance;
    }

    public static synchronized House constructInstance(List<User> residents,
                                                       List<Appliance> appliances,
                                                       List<Timeframe> timeframes,
                                                       String region,
                                                       double electricityTariff) {
        if (instance == null) {
            System.out.println("🏗️ Constructing new House instance");
            instance = new House(residents, appliances, timeframes, region, electricityTariff);
        } else {
            System.out.println("🔄 Updating existing House instance");
            // Update existing instance with new data
            instance.residents = residents != null ? residents : instance.residents;
            instance.appliances = appliances != null ? appliances : instance.appliances;
            instance.timeframes = timeframes != null ? timeframes : instance.timeframes;
            instance.region = region != null ? region : instance.region;
            instance.electricityTariff = electricityTariff;
        }
        return instance;
    }

    public static void overwriteInstance(House newHouse) {
        if (newHouse != null) {
            synchronized (LOCK) {
                // 🔧 CRITICAL FIX: Ensure all lists are initialized
                if (newHouse.residents == null) newHouse.residents = new ArrayList<>();
                if (newHouse.appliances == null) newHouse.appliances = new ArrayList<>();
                if (newHouse.timeframes == null) newHouse.timeframes = new ArrayList<>();
                if (newHouse.region == null) newHouse.region = "Netherlands";
                if (newHouse.startDate == null) newHouse.startDate = new DateTime(1, 1, 2025, 8, 0);
                if (newHouse.endDate == null) newHouse.endDate = new DateTime(31, 12, 2025, 18, 0);

                instance = newHouse;
                System.out.println("✅ House instance successfully overwritten and validated");
            }
        } else {
            System.err.println("⚠️ Cannot overwrite House instance with null - creating default instead");
            getInstance(); // This will create a default instance
        }
    }


    public double getFootPrint() {
        sumFootPrint();
        return this.footPrintGenerated;
    }

    public double getCosts() {
        calcCost();
        return this.costsGenerated;
    }

    public int getEcoScore() {
        calcEcoScore();
        return this.ecoScore;
    }


    public void setRegion(String region) {
        this.region = region != null ? region : "Netherlands";
        System.out.println("🌍 Region set to: " + this.region);
    }

    public void setTariff(Double tariff) {
        this.electricityTariff = tariff != null ? tariff : 0.25;
        System.out.println("💰 Tariff set to: " + this.electricityTariff);
    }

    public void setStart(DateTime startDateTime) {
        this.startDate = startDateTime != null ? startDateTime : new DateTime(1, 1, 2025, 8, 0);
    }

    public void setEnd(DateTime endDate) {
        this.endDate = endDate != null ? endDate : new DateTime(31, 12, 2025, 18, 0);
    }

    public void addUser(User user) {
        if (user != null) {
            this.residents.add(user);
            System.out.println("👤 Added user: " + user.getName());
        }
    }

    public void addAppliance(Appliance appliance) {
        if (appliance != null) {
            this.appliances.add(appliance);
            System.out.println("🔌 Added appliance: " + appliance.getName());
        }
    }

    public void addTimeframe(Timeframe timeframe) {
        if (timeframe != null) {
            this.timeframes.add(timeframe);
            System.out.println("⏰ Added timeframe for: " + timeframe.getAppliance().getName());
        }
    }

    public List<Appliance> getAppliances() {
        if (appliances == null) appliances = new ArrayList<>();
        return appliances;
    }

    public List<Timeframe> getTimeframes() {
        if (timeframes == null) timeframes = new ArrayList<>();
        return timeframes;
    }

    public List<User> getResidents() {
        if (residents == null) residents = new ArrayList<>();
        return residents;
    }

    public double getElectricityTariff() { return electricityTariff; }
    public String getRegion() { return region != null ? region : "Netherlands"; }
    public DateTime getStart() { return startDate != null ? startDate : new DateTime(1, 1, 2025, 8, 0); }
    public DateTime getEnd() { return endDate != null ? endDate : new DateTime(31, 12, 2025, 18, 0); }
    private void calcCost() {
        double totalCost = 0.0;
        Set<String> countedNames = new HashSet<>();

        if (appliances != null && !appliances.isEmpty()) {
            for (Appliance appliance : appliances) {
                if (appliance != null) {
                    String name = appliance.getName();
                    if (!countedNames.contains(name)) {
                        totalCost += appliance.getGeneratedCost();
                        countedNames.add(name);
                    }
                }
            }
        }
        this.costsGenerated = totalCost;
    }


    private void sumFootPrint() {
        double totalFootPrint = 0.0;
        Set<String> countedNames = new HashSet<>();

        if (appliances != null && !appliances.isEmpty()) {
            for (Appliance appliance : appliances) {
                if (appliance != null) {
                    String name = appliance.getName();
                    if (!countedNames.contains(name)) {
                        totalFootPrint += appliance.getGeneratedFootprint();
                        countedNames.add(name);
                    }
                }
            }
        }
        this.footPrintGenerated = totalFootPrint;
    }

    private void calcEcoScore() {
        double totalFootPrint = 0.1;
        if (appliances != null && !appliances.isEmpty()) {
            for (Appliance appliance : appliances) {
                if (appliance != null) {
                    totalFootPrint += appliance.getGeneratedFootprint();
                }
            }
        }

        if (startDate != null && endDate != null) {
            try {
                LocalDateTime start = this.startDate.toLocalDateTime();
                LocalDateTime end = this.endDate.toLocalDateTime();
                Duration runTime = Duration.between(start, end);
                long hours = Math.max(runTime.toHours(), 1); // Prevent division by zero

                double footprintPerHour = totalFootPrint / hours;
                System.out.println("Footprint / hour: " + footprintPerHour);

                // Thresholds
                double ideal = 0.1;  // Best case: ~0.1 kg CO2/hour
                double bad = 1.0;    // Worst case: ≥ 1.0 kg CO2/hour

                // Linear scale from 100 (ideal) to 0 (bad)
                double score = 100 * (1 - ((footprintPerHour - ideal) / (bad - ideal)));
                score = Math.max(0, Math.min(100, score)); // Clamp to [0, 100]

                this.ecoScore = (int) score;
            } catch (Exception e) {
                System.err.println("⚠️ Error calculating eco score: " + e.getMessage());
                this.ecoScore = 50; // Default score on error
            }
        } else {
            this.ecoScore = 50; // Default score if dates are invalid
        }
    }
}