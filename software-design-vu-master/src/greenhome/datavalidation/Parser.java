//package greenhome.datavalidation;
//
//import java.util.*;
//
//import greenhome.household.Appliance;
//import greenhome.household.House;
//import greenhome.household.User;
//import greenhome.time.DateTime;
//import greenhome.time.Timeframe;
//import org.json.JSONObject;
//import org.json.JSONArray;
//
//public class Parser {
//    public static House parseHouseData(String data) {
//        String[] lines = data.split("\n");
//        House h = null;
//        List<User> users = new ArrayList<>();
//        List<Appliance> appliances = new ArrayList<>();
//        List<Timeframe> timeframes = new ArrayList<>();
//
//        String region = "";
//        double tariff = 0;
//        DateTime startDateTime;
//        DateTime endDateTime;
//
//        int i = 0;
//        while (i < lines.length) {
//
//            String line = lines[i].trim();
//
//            if (line.startsWith("Region:")) {
//                region = line.split(": ")[1];
//            } else if (line.startsWith("Tariff:")) {
//                tariff = Double.parseDouble(line.split(": ")[1]);
//            } else if (line.startsWith("Start DateTime:")) {
//                //startDateTime(line.split(": ")[1],line.split(": ")[2],line.split(": ")[3],line.split(": ")[1];)
//
//            } else if (line.startsWith("End DateTime:")) {
//                //YEARGH
//            } else if (line.startsWith("-")) {
//                users.add(new User(line.split("- ")[1]);
//            } else if (line.startsWith("Appliance")) {
//                String name = lines[++i].split(": ")[1];
//                int powerConsumption = Integer.parseInt(lines[++i].split(": ")[1]);
//                int embodiedEmission = Integer.parseInt(lines[++i].split(": ")[1]);
//                appliances.add(new Appliance(name, powerConsumption, embodiedEmission));
//            } else if (line.startsWith("Timeframe ")) {
//                String userPart = lines[++i].split(": ")[1];
//                List<String> timeframeUsers = Arrays.asList(userPart.split(", "));
//                String start = lines[++i].split(": ")[1];
//                String end = lines[++i].split(": ")[1];
//                timeframes.add(new Timeframe(timeframeUsers, start, end));
//            }
//            i++;
//        }
//
//        DateTime[] period = new DateTime(startDateTime, endDateTime);
//
//        h = new House(region, tariff, startDateTime, endDateTime);
//        h.users.addAll(users);
//        h.appliances.addAll(appliances);
//        h.timeframes.addAll(timeframes);
//
//        return h;
//    }
//}
