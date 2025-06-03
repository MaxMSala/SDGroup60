package greenhome.reporting;
import java.util.*;

import greenhome.apiintegration.CarbonIntensity;
import greenhome.household.*;
import greenhome.household.Timeframe;
import greenhome.time.DateTime;


public class Recommendations {

    public static String generate() {
        StringBuilder recs = new StringBuilder();


        String section1 = " Household Eco-Score  ";
        int totalWidth = 100;
        int section1Length = section1.length();
        int section1Padding = (totalWidth - section1Length) / 2;
        String centeredSection1 = "-".repeat(section1Padding) + section1 + "-".repeat(98 - section1Padding - section1Length);
        recs.append(centeredSection1).append("\n\n");

        House house = House.getInstance();

        // EcoScore feedback
        System.out.println("House eco score: " + house.getEcoScore());
        if (house.getEcoScore() >= 75) {
            String formatted = String.format("STRONG (%d)! Keep optimizing your energy habits. Look below at recommendations!\n", house.getEcoScore());
            recs.append(formatted);

        } else if (house.getEcoScore() >= 50) {
            String formatted = String.format("DECENT (%d)! Keep optimizing your energy habits. Look below at recommendations!\n", house.getEcoScore());
            recs.append(formatted);
        } else {
            String formatted = String.format("WEAK (%d)! Keep optimizing your energy habits. Look below at recommendations!\n", house.getEcoScore());
            recs.append(formatted);
        }
        recs.append("\n");

        //  Individual User Eco-Scores (sorted)
        String section2 = " User Eco-Scores ";
        int section2Length = section2.length();
        int section2Padding = (totalWidth - section2Length) / 2;
        String centeredSection2 = "-".repeat(section2Padding) + section2 + "-".repeat(98 - section2Padding - section2Length);
        recs.append(centeredSection2).append("\n\n");

        Set<String> seenNames = new HashSet<>();

        house.getResidents().stream()
                .filter(user -> seenNames.add(user.getName())) // only pass if name is not already in the set
                .sorted((u1, u2) -> Integer.compare(u2.getEcoScore(), u1.getEcoScore())) // descending
                .forEach(user -> recs.append(
                        String.format("   • %s – Eco-Score: %d\n", user.getName(), user.getEcoScore())
                ));




        recs.append("\n");
        // Recommend user-specific behavior change based on highest carbon footprint appliance
        String section3 = " Household Recommendations: ";
        int section3Length = section3.length();
        int section3Padding = (totalWidth - section3Length) / 2;
        String centeredSection3 = "-".repeat(section3Padding) + section3 + "-".repeat(98 - section3Padding - section3Length);
        recs.append(centeredSection3).append("\n\n");

        Appliance worstAppliance = house.getAppliances().stream()
                .max(Comparator.comparingDouble(Appliance::getGeneratedFootprint))
                .orElse(null);

        if (worstAppliance != null) {
            // Map to keep track of total usage hours per user
            Map<String, Double> userUsageMap = new HashMap<>();

            for (Timeframe tf : house.getTimeframes()) {
                if (tf.getAppliance().getName().equals(worstAppliance.getName())) {
                    double totalHours = tf.getUsageDurationInHoursForAppliance();
                    int userCount = tf.getUsers().size();
                    double perUserHours = totalHours / userCount;

                    for (User user : tf.getUsers()) {
                        userUsageMap.merge(user.getName(), perUserHours, Double::sum);
                    }
                }
            }

            // Find the user with the most total usage hours
            String worstUser = null;
            double maxHours = 0;

            for (Map.Entry<String, Double> entry : userUsageMap.entrySet()) {
                if (entry.getValue() > maxHours) {
                    maxHours = entry.getValue();
                    worstUser = entry.getKey();
                }
            }

            if (worstUser != null) {
                recs.append(
                        "User-Specific Tip:\n"

                );
                recs.append(String.format(
                        "   • Tell %s to ease up on the '%s' — they’ve used it for a total of %.1f hours, and it's currently the top CO₂ emitter!\n\n",
                        worstUser, worstAppliance.getName(), maxHours
                ));

            }
        }

        // Top carbon footprint appliances
        recs.append("Appliances generating the most carbon footprint:\n");

        Set<String> seenNames2 = new HashSet<>();

        List<Appliance> topCarbon = house.getAppliances().stream()
                .sorted((a1, a2) -> Double.compare(a2.getGeneratedFootprint(), a1.getGeneratedFootprint()))
                .filter(a -> seenNames2.add(a.getName())) // ensures uniqueness
                .limit(3)
                .toList();

        for (Appliance appliance : topCarbon) {
            recs.append(String.format("   • %s – %.2f gCO₂ emitted\n", appliance.getName(), appliance.getGeneratedFootprint()));
        }

        recs.append(" Tip: Use these appliances during off-peak low-carbon hours shown below.\n\n");
        recs.append(CarbonIntensity.findBestLowCarbonTimeRange());



        // Suggest replacing inefficient appliances
        recs.append("Appliance Efficiency Tips:\n");

        Set<String> seenNames3 = new HashSet<>();

        house.getAppliances().stream()
                .filter(a -> a.getPowerConsumption() > 2.0)
                .filter(a -> seenNames3.add(a.getName())) // ensure each name is used only once
                .forEach(a -> recs.append(String.format(
                        "   • '%s' consumes %.2f kWh. Consider replacing it with a more efficient model.\n\n",
                        a.getName(), a.getPowerConsumption()
                )));


        // Most costly appliance
        Set<String> seenNames4 = new HashSet<>();

        Appliance mostCostly = house.getAppliances().stream()
                .filter(a -> seenNames4.add(a.getName())) // keep only the first occurrence of each name
                .max(Comparator.comparingDouble(Appliance::getGeneratedCost))
                .orElse(null);

        if (mostCostly != null) {
            recs.append("Cost-Saving Insight:\n");
            recs.append(String.format(
                    "   • '%s' generates the highest electricity cost. Reduce usage or shift to more energy efficient model.\n\n",
                    mostCostly.getName()
            ));
        }


        return recs.toString();
    }
//
//    public static void main(String[] args) {
//        try {
//            // === Create Mock Data ===
//
//            User user1 = new User("Alice");
//            User user2 = new User("Bob");
//            // List with only Alice
//            List<User> onlyAlice = new ArrayList<>(List.of(user1));
//
//            // List with only Bob
//            List<User> onlyBob = new ArrayList<>(List.of(user2));
//
//            // List with both
//            List<User> bothUsers = new ArrayList<>(List.of(user1, user2));
//
//            Appliance fridge = new Appliance("Fridge", 10, 10.0);
//            Appliance heater = new Appliance("Heater", 2.5, 50.0);
//            List<Appliance> appliances = new ArrayList<>(List.of(fridge, heater));
//            fridge.setGeneratedFootprint(20);
//            heater.setGeneratedFootprint(7);
//
//            // Create manual DateTime instances
//            DateTime start1 = new DateTime(27, 3, 2025, 8, 0);
//            DateTime end1 = new DateTime(30, 3, 2025, 10, 0);
//            DateTime start2 = new DateTime(27, 3, 2025, 14, 0);
//            DateTime end2 = new DateTime(31, 3, 2025, 16, 0);
//
//            List<Integer> carbonIntensities = Arrays.asList(200, 220, 180);
//
//            Timeframe tf1 = new Timeframe(onlyBob, heater, start1, end1);
//            Timeframe tf2 = new Timeframe(onlyAlice, fridge, start2, end2);
//            List<Timeframe> timeframes = new ArrayList<>(List.of(tf1, tf2));
//
//            DateTime startDate = new DateTime(27, 3, 2025, 8, 0);
//            DateTime endDate = new DateTime(30, 3, 2025, 8, 0);
//
//            House mockHouse = House.constructInstance(bothUsers, appliances, timeframes, "NL", 0.25);
//            mockHouse.setStart(startDate);
//            mockHouse.setEnd(endDate);
//
//            System.out.println("Alice CF: " + user1.getCarbonFootprint());
//            System.out.println("Bob CF: " + user2.getCarbonFootprint());
//            // === Generate & print recommendations ===
//            String output = generate(mockHouse);
//            System.out.println(output);
//
//        } catch (Exception e) {
//            System.out.println("⚠️ Error during mock execution: " + e.getMessage());
//            e.printStackTrace();
//        }
//    }
}
