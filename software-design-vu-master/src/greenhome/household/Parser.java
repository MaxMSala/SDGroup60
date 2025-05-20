package greenhome.household;

import com.google.gson.*;
import greenhome.household.*;
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
        System.out.println("flag");
        // Extract HOUSE INFO
        Pattern houseInfoPattern = Pattern.compile(
                "Region:\\s*(.*?)\\n\\s*Tariff:\\s*(.*?)\\n\\s*Start DateTime:\\s*(.*?)\\n\\s*End DateTime:\\s*(.*?)\\n",
                Pattern.DOTALL);
        Matcher houseInfoMatcher = houseInfoPattern.matcher(finalInput);
        System.out.println(houseInfoMatcher.toString());
        if (houseInfoMatcher.find()) {
            house.setRegion(houseInfoMatcher.group(1).trim());
            house.setTariff(Double.parseDouble(houseInfoMatcher.group(2).trim()));
            house.setStart(DateTime.parseDate(houseInfoMatcher.group(3).trim()));
            house.setEnd(DateTime.parseDate(houseInfoMatcher.group(4).trim()));
        }

        // Extract USERS
        Map<String, User> userMap = new HashMap<>();
        Pattern usersPattern = Pattern.compile("USERS\\s*((?:- .*\\n)+)");
        Matcher usersMatcher = usersPattern.matcher(finalInput);
        if (usersMatcher.find()) {
            String[] userLines = usersMatcher.group(1).split("\\n");
            System.out.println(userLines);
            for (String line : userLines) {

                String name = line.replace("-", "").trim();
                System.out.println(name);
                if (!name.isEmpty()) {
                    User user = new User(name);
                    userMap.put(name, user);
                    house.addUser(user);
                }
            }
        }

        // Extract APPLIANCES
        Map<String, Appliance> applianceMap = new HashMap<>();
        Pattern applianceBlockPattern = Pattern.compile(
                "Appliance \\d+:\\s*\\n\\s*Name:\\s*(.*?)\\n\\s*Power Consumption:\\s*(.*?)\\n\\s*Embodied Emission:\\s*(.*?)(?:\\n|$)",
                Pattern.DOTALL);
        Matcher applianceMatcher = applianceBlockPattern.matcher(finalInput);
        while (applianceMatcher.find()) {
            String name = applianceMatcher.group(1).trim();
            System.out.println(name);
            double consumption = Double.parseDouble(applianceMatcher.group(2).trim());
            double emission = Double.parseDouble(applianceMatcher.group(3).trim());
            Appliance appliance = new Appliance(name, consumption, emission);
            applianceMap.put(name, appliance);
            house.addAppliance(appliance);
        }

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
                if (appliance == null) continue;

                Pattern entryPattern = Pattern.compile(
                        "-\\s*User:\\s*(.*?)\\n\\s*Start:\\s*(.*?)\\n\\s*End:\\s*(.*?)(?:\\n|$)",
                        Pattern.DOTALL);
                Matcher entryMatcher = entryPattern.matcher(usageBlock);

                while (entryMatcher.find()) {
                    System.out.println("flagging");
                    System.out.println(entryMatcher.group(0));
                    System.out.println(entryMatcher.group(1));
                    System.out.println(entryMatcher.group(2));
                    System.out.println(entryMatcher.group(3));
                    System.out.println("flaggingstyle");
                    String usersStr = entryMatcher.group(1).trim();
                    String startStr = entryMatcher.group(2).trim();
                    String endStr = entryMatcher.group(3).trim();

                    System.out.println(startStr);
                    DateTime start = DateTime.parseDate(startStr);
                    System.out.println(endStr);
                    DateTime end = DateTime.parseDate(endStr);

                    List<User> matchedUsers = new ArrayList<>();
                    for (String uname : usersStr.split(",")) {
                        uname = uname.trim();
                        matchedUsers.add(userMap.get(uname)); // No validation
                    }

                    Timeframe tf = new Timeframe(matchedUsers, appliance, start, end);
                    house.addTimeframe(tf);
                }
            }
        }
    }

    public static void whatifStringModHouse(String finalInput) {
        House house = House.getInstance();
        System.out.println("flag");
        // Extract HOUSE INFO
        Pattern houseInfoPattern = Pattern.compile(
                "Region:\\s*(.*?)\\n\\s*Tariff:\\s*(.*?)\\n\\s*Start DateTime:\\s*(.*?)\\n\\s*End DateTime:\\s*(.*?)\\n",
                Pattern.DOTALL);
        Matcher houseInfoMatcher = houseInfoPattern.matcher(finalInput);
        System.out.println(houseInfoMatcher.toString());
        if (houseInfoMatcher.find()) {
            house.setRegion(houseInfoMatcher.group(1).trim());
            house.setTariff(Double.parseDouble(houseInfoMatcher.group(2).trim()));
            house.setStart(DateTime.parseDate(houseInfoMatcher.group(3).trim()));
            house.setEnd(DateTime.parseDate(houseInfoMatcher.group(4).trim()));
        }

        // Extract USERS
        List<User> userlist = null;

        Map<String, User> userMap = new HashMap<>();
        Pattern usersPattern = Pattern.compile("USERS\\s*((?:- .*\\n)+)");
        Matcher usersMatcher = usersPattern.matcher(finalInput);
        if (usersMatcher.find()) {
            String[] userLines = usersMatcher.group(1).split("\\n");
            System.out.println(userLines);
            for (String line : userLines) {

                String name = line.replace("-", "").trim();
                System.out.println(name);
                if (!name.isEmpty()) {
                    User user = new User(name);
                    userMap.put(name, user);
                    userlist.add(user);
                }
            }
        }
        house.modUser(userlist);

        // Extract APPLIANCES
        List<Appliance> appliancelist = null;
        Map<String, Appliance> applianceMap = new HashMap<>();
        Pattern applianceBlockPattern = Pattern.compile(
                "Appliance \\d+:\\s*\\n\\s*Name:\\s*(.*?)\\n\\s*Power Consumption:\\s*(.*?)\\n\\s*Embodied Emission:\\s*(.*?)(?:\\n|$)",
                Pattern.DOTALL);
        Matcher applianceMatcher = applianceBlockPattern.matcher(finalInput);
        while (applianceMatcher.find()) {
            String name = applianceMatcher.group(1).trim();
            System.out.println(name);
            double consumption = Double.parseDouble(applianceMatcher.group(2).trim());
            double emission = Double.parseDouble(applianceMatcher.group(3).trim());
            Appliance appliance = new Appliance(name, consumption, emission);
            applianceMap.put(name, appliance);
           appliancelist.add(appliance);
        }
        house.modAppliances(appliancelist);

        // Extract TIMEFRAMES PER APPLIANCE
        List<Timeframe> timeframelist = null;
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
                    System.out.println("flagging");
                    System.out.println(entryMatcher.group(0));
                    System.out.println(entryMatcher.group(1));
                    System.out.println(entryMatcher.group(2));
                    System.out.println(entryMatcher.group(3));
                    System.out.println("flaggingstyle");
                    String usersStr = entryMatcher.group(1).trim();
                    String startStr = entryMatcher.group(2).trim();
                    String endStr = entryMatcher.group(3).trim();

                    System.out.println(startStr);
                    DateTime start = DateTime.parseDate(startStr);
                    System.out.println(endStr);
                    DateTime end = DateTime.parseDate(endStr);

                    List<User> matchedUsers = new ArrayList<>();
                    for (String uname : usersStr.split(",")) {
                        uname = uname.trim();
                        matchedUsers.add(userMap.get(uname)); // No validation
                    }

                    Timeframe tf = new Timeframe(matchedUsers, appliance, start, end);
                    timeframelist.add(tf);
                }
            }
        }

        house.modTimeframes(timeframelist);
    }

    public static void saveHouse(String filename) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter(filename)) {
            gson.toJson(House.getInstance(), writer);
        }
    }

    public static void loadHouse(String filename) throws IOException {
        Gson gson = new Gson();
        try (FileReader reader = new FileReader(filename)) {
            House loadedHouse = gson.fromJson(reader, House.class);
            House.overwriteInstance(loadedHouse); // you’ll need to implement this or a similar setter
        }
    }



    public static String houseToString() {
        House house = House.getInstance();
        StringBuilder sb = new StringBuilder();

        // HOUSE INFO
        sb.append("HOUSE INFO:\n");
        sb.append("     Region: ").append(house.getRegion()).append("\n");
        sb.append("     Tariff: ").append(house.getElectricityTariff()).append("\n");
        sb.append("     Start DateTime: ").append(house.getStart().toString()).append("\n");
        sb.append("     End DateTime: ").append(house.getEnd().toString()).append("\n\n");

        // USERS
        sb.append("     USERS:\n");
        for (User user : house.getResidents()) {
            sb.append("     - ").append(user.getName()).append("\n");
        }
        sb.append("\n");

        // APPLIANCES
        sb.append("     APPLIANCES:\n");
        for (Appliance appliance : house.getAppliances()) {
            sb.append("     Name: ").append(appliance.getName()).append("\n");
            sb.append("     Power Consumption: ").append(appliance.getPowerConsumption()).append("\n");
            sb.append("     Embodied Emission: ").append(appliance.getEmbodiedEmissions()).append("\n\n");
        }

        // TIMEFRAMES
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


