package greenhome.household;

import greenhome.time.DateTime;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class House {
    // Thread-safe singleton implementation
    private static volatile House instance;
    private static final Object LOCK = new Object();

    // Core attributes
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

    // 🔧 MAIN FIX: getInstance() never returns null
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

    // 🔧 IMPROVED: constructInstance with better logic
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

    // 🔧 IMPROVED: Safe overwrite with validation and null-safety
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

    // 🆕 NEW: Method to check if instance exists
    public static boolean hasInstance() {
        return instance != null;
    }

    // 🆕 NEW: Reset for testing (keeps singleton pattern but allows reset)
    public static synchronized void resetInstance() {
        System.out.println("🔄 Resetting House instance");
        instance = null;
    }

    // 🆕 NEW: Initialize with default values
    public static House getOrCreateDefault() {
        return getInstance(); // This will create default if needed
    }

    // DERIVED GETTERS with automatic calculation
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

    // SETTERS with validation
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

    // COLLECTION MODIFIERS with null safety
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

    // COLLECTION REPLACERS with null safety
    public void modUser(List<User> newRes) {
        this.residents = newRes != null ? newRes : new ArrayList<>();
    }

    public void modAppliances(List<Appliance> newApps) {
        this.appliances = newApps != null ? newApps : new ArrayList<>();
    }

    public void modTimeframes(List<Timeframe> newFrames) {
        this.timeframes = newFrames != null ? newFrames : new ArrayList<>();
    }

    // 🔧 FIXED GETTERS - Never return null
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

    // PRIVATE CALCULATIONS (with comprehensive null safety)
    private void calcCost() {
        double totalCost = 0.0;
        if (appliances != null && !appliances.isEmpty()) {
            for (Appliance appliance : appliances) {
                if (appliance != null) {
                    totalCost += appliance.getGeneratedCost();
                }
            }
        }
        this.costsGenerated = totalCost;
        System.out.println(String.format("Damian: Costs Generated for HOUSE: %f ", this.costsGenerated));
    }

    private void sumFootPrint() {
        double totalFootPrint = 0.0;
        if (appliances != null && !appliances.isEmpty()) {
            for (Appliance appliance : appliances) {
                if (appliance != null) {
                    totalFootPrint += appliance.getGeneratedFootprint();
                }
            }
        }
        this.footPrintGenerated = totalFootPrint;
        System.out.println(String.format("Damian: CF for HOUSE: %f ", this.footPrintGenerated));
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
        System.out.println(String.format("Damian: Eco Score for HOUSE: %d ", this.ecoScore));
    }
}