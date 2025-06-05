package greenhome.household;
import java.util.*;

public class User {

    private String name;

    // derived
    private int ecoScore;
    private double carbonFootprint;
    private double costsGenerated;
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof User)) return false;
        User other = (User) obj;
        return name.equals(other.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
    public User(String name) {
        this.name = name;
    }

    // interface getters
    public String getName() {return this.name;}
    public double getCostsGenerated(){ calcCost(); return costsGenerated;}
    public int getEcoScore() {calcEcoScore(); return ecoScore;}
    public double getCarbonFootprint() {sumFootPrint(); return carbonFootprint;}


    // internal calculations
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

    private void sumFootPrint() {
        double totalFootPrint = 0.0;
        House house = House.getInstance();

        for (Timeframe tf : house.getTimeframes()) {
            Set<String> uniqueUserNames = new HashSet<>();
            for (User user : tf.getUsers()) {
                uniqueUserNames.add(user.getName()); // Only names are used to ensure uniqueness
            }

            // Only count the footprint if this user is uniquely part of the timeframe
            if (uniqueUserNames.contains(this.name)) {
                totalFootPrint += (tf.getCarbonFootprint() / uniqueUserNames.size());
            }
        }

        this.carbonFootprint = totalFootPrint;
    }


    /**
     * Calculates the eco score for a user based on their contribution to the house's total carbon footprint.
     *
     * Logic:
     * - Only users who have contributed (> 0) to the footprint are considered.
     * - The user's ecoScore is calculated as: 100 - (percentage share of their emissions).
     * - The lower the user’s contribution, the higher their score (more eco-friendly).
     * - If no one emits anything, everyone gets a perfect score of 100.
     * - If single contributing user, he gets 80 by defualt
     *
     * Example:
     * Let's say there are 3 users:
     * - Max: 0 kg CO₂ → gets 100 - 0 = 100 (perfect score)
     * - Damian: 30 kg CO₂ → total = 100 → gets 100 - 30 = 70
     * - Sam: 70 kg CO₂ → total = 100 → gets 100 - 70 = 30
     */
    private void calcEcoScore() {
        House house = House.getInstance();

        // Step 1: Collect unique users by name, and sum only those with > 0 footprint
        Map<String, Double> uniqueUserFootprints = new HashMap<>();

        for (User user : house.getResidents()) {
            if (user != null) {
                String name = user.getName();
                double cf = user.getCarbonFootprint();
                if (cf > 0 && !uniqueUserFootprints.containsKey(name)) {
                    uniqueUserFootprints.put(name, cf);
                }
            }
        }

        double totalContributingFootprint = uniqueUserFootprints.values().stream().mapToDouble(Double::doubleValue).sum();
        double myFootprint = getCarbonFootprint();

        if (totalContributingFootprint == 0.0) {
            this.ecoScore = 100;
            return;
        }

        if (uniqueUserFootprints.size() == 1) {
            // Get the only contributor's name
            String soleContributor = uniqueUserFootprints.keySet().iterator().next();

            if (this.name.equals(soleContributor)) {
                this.ecoScore = 80;
            } else {
                this.ecoScore = 100;
            }
            return;
        }

        double myShare = myFootprint / totalContributingFootprint;
        double score = 100 - (myShare * 100);

        this.ecoScore = (int) Math.round(score);
    }


}