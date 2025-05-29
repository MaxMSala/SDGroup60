package greenhome.household;

import com.google.gson.*;
import greenhome.time.DateTime;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

    // 🔧 MAIN FIX: Always get a valid House instance
    public static void populateHouseFromForm(String finalInput) {
        System.out.println("🔧 Parser: Starting to populate house from form data");

        // Get or create House instance - NEVER null
        House house = House.getInstance();
        System.out.println("✅ Parser: Got House instance: " + house);

        try {
            // Extract HOUSE INFO
            Pattern houseInfoPattern = Pattern.compile(
                    "Region:\\s*(.*?)\\n\\s*Tariff:\\s*(.*?)\\n\\s*Start DateTime:\\s*(.*?)\\n\\s*End DateTime:\\s*(.*?)\\n",
                    Pattern.DOTALL);
            Matcher houseInfoMatcher = houseInfoPattern.matcher(finalInput);

            if (houseInfoMatcher.find()) {
                String region = houseInfoMatcher.group(1).trim();
                String tariffStr = houseInfoMatcher.group(2).trim();
                String startStr = houseInfoMatcher.group(3).trim();
                String endStr = houseInfoMatcher.group(4).trim();

                house.setRegion(region);
                house.setTariff(Double.parseDouble(tariffStr));
                house.setStart(DateTime.parseDate(startStr));
                house.setEnd(DateTime.parseDate(endStr));

                System.out.println("✅ House info updated: " + region + ", " + tariffStr);
            } else {
                System.out.println("⚠️ No house info found in input, keeping defaults");
            }

            // Extract USERS
            Map<String, User> userMap = new HashMap<>();
            Pattern usersPattern = Pattern.compile("USERS\\s*((?:- .*\\n)+)");
            Matcher usersMatcher = usersPattern.matcher(finalInput);

            if (usersMatcher.find()) {
                String[] userLines = usersMatcher.group(1).split("\\n");
                for (String line : userLines) {
                    String name = line.replace("-", "").trim();
                    if (!name.isEmpty()) {
                        User user = new User(name);
                        userMap.put(name, user);
                        house.addUser(user);
                    }
                }
                System.out.println("✅ Added " + userMap.size() + " users");
            }

            // Extract APPLIANCES
            Map<String, Appliance> applianceMap = new HashMap<>();
            Pattern applianceBlockPattern = Pattern.compile(
                    "Appliance \\d+:\\s*\\n\\s*Name:\\s*(.*?)\\n\\s*Power Consumption:\\s*(.*?)\\n\\s*Embodied Emission:\\s*(.*?)(?:\\n|$)",
                    Pattern.DOTALL);
            Matcher applianceMatcher = applianceBlockPattern.matcher(finalInput);

            while (applianceMatcher.find()) {
                try {
                    String name = applianceMatcher.group(1).trim();
                    double consumption = Double.parseDouble(applianceMatcher.group(2).trim());
                    double emission = Double.parseDouble(applianceMatcher.group(3).trim());

                    Appliance appliance = new Appliance(name, consumption, emission);
                    applianceMap.put(name, appliance);
                    house.addAppliance(appliance);
                } catch (NumberFormatException e) {
                    System.err.println("⚠️ Error parsing appliance data: " + e.getMessage());
                }
            }
            System.out.println("✅ Added " + applianceMap.size() + " appliances");

            // Extract TIMEFRAMES PER APPLIANCE
            Pattern timeframeSectionPattern = Pattern.compile(
                    "TIMEFRAMES PER APPLIANCE(.*)",
                    Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
            Matcher tfSectionMatcher = timeframeSectionPattern.matcher(finalInput);

            if (tfSectionMatcher.find()) {
                String timeframesBlock = tfSectionMatcher.group(1).trim();
                Pattern applianceTfPattern = Pattern.compile(
                        "Appliance:\\s*(.*?)\\n((?:\\s*-\\s*User:.*?\\n\\s*Start:.*?\\n\\s*End:.*?(?:\\n|$))+)",
                        Pattern.DOTALL);
                Matcher applianceTfMatcher = applianceTfPattern.matcher(timeframesBlock);

                while (applianceTfMatcher.find()) {
                    String applianceName = applianceTfMatcher.group(1).trim();
                    String usageBlock = applianceTfMatcher.group(2);

                    Appliance appliance = applianceMap.get(applianceName);
                    if (appliance == null) {
                        System.err.println("⚠️ Appliance not found: " + applianceName);
                        continue;
                    }

                    Pattern entryPattern = Pattern.compile(
                            "-\\s*User:\\s*(.*?)\\n\\s*Start:\\s*(.*?)\\n\\s*End:\\s*(.*?)(?:\\n|$)",
                            Pattern.DOTALL);
                    Matcher entryMatcher = entryPattern.matcher(usageBlock);

                    while (entryMatcher.find()) {
                        try {
                            String usersStr = entryMatcher.group(1).trim();
                            String startStr = entryMatcher.group(2).trim();
                            String endStr = entryMatcher.group(3).trim();

                            DateTime start = DateTime.parseDate(startStr);
                            DateTime end = DateTime.parseDate(endStr);

                            List<User> matchedUsers = new ArrayList<>();
                            for (String uname : usersStr.split(",")) {
                                uname = uname.trim();
                                User user = userMap.get(uname);
                                if (user != null) {
                                    matchedUsers.add(user);
                                } else {
                                    System.err.println("⚠️ User not found: " + uname);
                                }
                            }

                            if (!matchedUsers.isEmpty()) {
                                Timeframe tf = new Timeframe(matchedUsers, appliance, start, end);
                                house.addTimeframe(tf);
                            }
                        } catch (Exception e) {
                            System.err.println("⚠️ Error parsing timeframe: " + e.getMessage());
                        }
                    }
                }
            }

            System.out.println("✅ Parser: Successfully populated house with all data");

        } catch (Exception e) {
            System.err.println("🚨 Parser: Error during population: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // 🔧 IMPROVED: whatif with better error handling
    public static void whatifStringModHouse(String finalInput) {
        System.out.println("🔄 Parser: Starting what-if scenario modification");

        House house = House.getInstance();

        try {
            // Extract HOUSE INFO
            Pattern houseInfoPattern = Pattern.compile(
                    "Region:\\s*(.*?)\\n\\s*Tariff:\\s*(.*?)\\n\\s*Start DateTime:\\s*(.*?)\\n\\s*End DateTime:\\s*(.*?)\\n",
                    Pattern.DOTALL);
            Matcher houseInfoMatcher = houseInfoPattern.matcher(finalInput);

            if (houseInfoMatcher.find()) {
                house.setRegion(houseInfoMatcher.group(1).trim());
                house.setTariff(Double.parseDouble(houseInfoMatcher.group(2).trim()));
                house.setStart(DateTime.parseDate(houseInfoMatcher.group(3).trim()));
                house.setEnd(DateTime.parseDate(houseInfoMatcher.group(4).trim()));
            }

            // Extract USERS
            List<User> userList = new ArrayList<>();
            Map<String, User> userMap = new HashMap<>();
            Pattern usersPattern = Pattern.compile("USERS\\s*((?:- .*\\n)+)");
            Matcher usersMatcher = usersPattern.matcher(finalInput);

            if (usersMatcher.find()) {
                String[] userLines = usersMatcher.group(1).split("\\n");
                for (String line : userLines) {
                    String name = line.replace("-", "").trim();
                    if (!name.isEmpty()) {
                        User user = new User(name);
                        userMap.put(name, user);
                        userList.add(user);
                    }
                }
            }
            house.modUser(userList);

            // Extract APPLIANCES
            List<Appliance> applianceList = new ArrayList<>();
            Map<String, Appliance> applianceMap = new HashMap<>();
            Pattern applianceBlockPattern = Pattern.compile(
                    "Appliance \\d+:\\s*\\n\\s*Name:\\s*(.*?)\\n\\s*Power Consumption:\\s*(.*?)\\n\\s*Embodied Emission:\\s*(.*?)(?:\\n|$)",
                    Pattern.DOTALL);
            Matcher applianceMatcher = applianceBlockPattern.matcher(finalInput);

            while (applianceMatcher.find()) {
                try {
                    String name = applianceMatcher.group(1).trim();
                    double consumption = Double.parseDouble(applianceMatcher.group(2).trim());
                    double emission = Double.parseDouble(applianceMatcher.group(3).trim());

                    Appliance appliance = new Appliance(name, consumption, emission);
                    applianceMap.put(name, appliance);
                    applianceList.add(appliance);
                } catch (NumberFormatException e) {
                    System.err.println("⚠️ Error parsing appliance in what-if: " + e.getMessage());
                }
            }
            house.modAppliances(applianceList);

            // Extract TIMEFRAMES PER APPLIANCE
            List<Timeframe> timeframeList = new ArrayList<>();
            Pattern timeframeSectionPattern = Pattern.compile(
                    "TIMEFRAMES PER APPLIANCE(.*)",
                    Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
            Matcher tfSectionMatcher = timeframeSectionPattern.matcher(finalInput);

            if (tfSectionMatcher.find()) {
                String timeframesBlock = tfSectionMatcher.group(1).trim();
                Pattern applianceTfPattern = Pattern.compile(
                        "Appliance:\\s*(.*?)\\n((?:\\s*-\\s*User:.*?\\n\\s*Start:.*?\\n\\s*End:.*?(?:\\n|$))+)",
                        Pattern.DOTALL);
                Matcher applianceTfMatcher = applianceTfPattern.matcher(timeframesBlock);

                while (applianceTfMatcher.find()) {
                    String applianceName = applianceTfMatcher.group(1).trim();
                    String usageBlock = applianceTfMatcher.group(2);

                    Appliance appliance = applianceMap.get(applianceName);
                    if (appliance == null) continue;

                    Pattern entryPattern = Pattern.compile(
                            "-\\s*User:\\s*(.*?)\\n\\s*Start:\\s*(.*?)\\n\\s*End:\\s*(.*?)(?:\\n|$)",
                            Pattern.DOTALL);
                    Matcher entryMatcher = entryPattern.matcher(usageBlock);

                    while (entryMatcher.find()) {
                        try {
                            String usersStr = entryMatcher.group(1).trim();
                            String startStr = entryMatcher.group(2).trim();
                            String endStr = entryMatcher.group(3).trim();

                            DateTime start = DateTime.parseDate(startStr);
                            DateTime end = DateTime.parseDate(endStr);

                            List<User> matchedUsers = new ArrayList<>();
                            for (String uname : usersStr.split(",")) {
                                uname = uname.trim();
                                User user = userMap.get(uname);
                                if (user != null) {
                                    matchedUsers.add(user);
                                }
                            }

                            if (!matchedUsers.isEmpty()) {
                                Timeframe tf = new Timeframe(matchedUsers, appliance, start, end);
                                timeframeList.add(tf);
                            }
                        } catch (Exception e) {
                            System.err.println("⚠️ Error parsing timeframe in what-if: " + e.getMessage());
                        }
                    }
                }
            }
            house.modTimeframes(timeframeList);

            System.out.println("✅ What-if scenario applied successfully");

        } catch (Exception e) {
            System.err.println("🚨 Parser: Error during what-if modification: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // 🔧 IMPROVED: Save with error handling
    public static void saveHouse(String filename) throws IOException {
        try {
            House house = House.getInstance();
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            try (FileWriter writer = new FileWriter(filename)) {
                gson.toJson(house, writer);
                System.out.println("✅ House saved to: " + filename);
            }
        } catch (Exception e) {
            System.err.println("🚨 Error saving house: " + e.getMessage());
            throw new IOException("Failed to save house", e);
        }
    }

    // 🔧 IMPROVED: Load with better error handling
    public static void loadHouse(String filename) throws IOException {
        try {
            Gson gson = new Gson();
            try (FileReader reader = new FileReader(filename)) {
                House loadedHouse = gson.fromJson(reader, House.class);
                if (loadedHouse != null) {
                    House.overwriteInstance(loadedHouse);
                    System.out.println("✅ House loaded from: " + filename);
                } else {
                    System.out.println("⚠️ Loaded house was null, keeping current instance");
                }
            }
        } catch (Exception e) {
            System.err.println("⚠️ Could not load house from " + filename + ": " + e.getMessage());
            // Don't throw exception - let the system continue with default house
            House.getInstance(); // Ensure we have a default house
        }
    }

    // UNCHANGED: houseToString method
    public static String houseToString() {
        House house = House.getInstance();
        StringBuilder sb = new StringBuilder();

        sb.append("HOUSE INFO:\n");
        sb.append("     Region: ").append(house.getRegion()).append("\n");
        sb.append("     Tariff: ").append(house.getElectricityTariff()).append("\n");
        sb.append("     Start DateTime: ").append(house.getStart().toString()).append("\n");
        sb.append("     End DateTime: ").append(house.getEnd().toString()).append("\n\n");

        sb.append("     USERS:\n");
        for (User user : house.getResidents()) {
            sb.append("     - ").append(user.getName()).append("\n");
        }
        sb.append("\n");

        sb.append("     APPLIANCES:\n");
        for (Appliance appliance : house.getAppliances()) {
            sb.append("     Name: ").append(appliance.getName()).append("\n");
            sb.append("     Power Consumption: ").append(appliance.getPowerConsumption()).append("\n");
            sb.append("     Embodied Emission: ").append(appliance.getEmbodiedEmissions()).append("\n\n");
        }

        sb.append("     TIMEFRAMES:\n");
        for (Timeframe tf : house.getTimeframes()) {
            sb.append("     Appliance: ").append(tf.getAppliance().getName()).append("\n");
            sb.append("     - User: ");
            List<User> users = tf.getUsers();
            for (int i = 0; i < users.size(); i++) {
                sb.append(users.get(i).getName());
                if (i < users.size() - 1) sb.append(", ");
            }
            sb.append("\n");
            sb.append("     Start: ").append(tf.getStart().toString()).append("\n");
            sb.append("     End: ").append(tf.getEnd().toString()).append("\n\n");
        }

        return sb.toString();
    }
}