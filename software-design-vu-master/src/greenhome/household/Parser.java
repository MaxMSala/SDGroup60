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


    public static void populateHouseFromForm(String finalInput) {



        House house = House.getInstance();


        try {

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


            }

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

            }


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

        } catch (Exception e) {
            System.err.println("🚨 Parser: Error during population: " + e.getMessage());
            e.printStackTrace();
        }
    }



    public static void saveHouse(String filename) throws IOException {
        try {
            House house = House.getInstance();
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            try (FileWriter writer = new FileWriter(filename)) {
                gson.toJson(house, writer);

            }
        } catch (Exception e) {
            System.err.println("🚨 Error saving house: " + e.getMessage());
            throw new IOException("Failed to save house", e);
        }
    }

    public static void loadHouse(String filename) throws IOException {
        try {
            Gson gson = new Gson();
            try (FileReader reader = new FileReader(filename)) {
                House loadedHouse = gson.fromJson(reader, House.class);
                if (loadedHouse != null) {
                    House.overwriteInstance(loadedHouse);

                }
            }
        } catch (Exception e) {
            System.err.println("⚠️ Could not load house from " + filename + ": " + e.getMessage());

            House.getInstance();
        }
    }

}