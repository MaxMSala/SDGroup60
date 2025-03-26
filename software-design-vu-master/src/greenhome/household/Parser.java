package greenhome.household;
import greenhome.datavalidation.*;
import greenhome.time.DateTime;
import greenhome.time.Timeframe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class Parser {

    // Attributes
    private DateTime[] period = new DateTime[2]; // Ordered, Unique — so List
    private Set<String> userList;  // Unique, Unordered — Set
    private Set<String> timeFrameList; // Unique, Unordered — Set
    private List<String> applianceList; // NonUnique, Unordered — List
    private double electricityTariff;
    private String region;

    // Association with House (1 to 1)
    private House house;

    // Constructor
    public Parser() {
        // default constructor or overload as needed
    }

    // Methods
    public static House parseHouseData(String data) {
        String[] lines = data.split("\n");
        House h = null;
        List<User> users = new ArrayList<>();
        List<Appliance> appliances = new ArrayList<>();
        List<Timeframe> timeframes = new ArrayList<>();

        String region = "";
        double tariff = 0;
        DateTime startDateTime;
        DateTime endDateTime;

        int i = 0;
        while (i < lines.length) {

            String line = lines[i].trim();

            if (line.startsWith("Region:")) {
                region = line.split(": ")[1];
            } else if (line.startsWith("Tariff:")) {
                tariff = Double.parseDouble(line.split(": ")[1]);
            } else if (line.startsWith("Start DateTime:")) {
                //startDateTime(line.split(": ")[1],line.split(": ")[2],line.split(": ")[3],line.split(": ")[1];)

            } else if (line.startsWith("End DateTime:")) {
                //YEARGH
            } else if (line.startsWith("-")) {
               // users.add(new User(line.split("- ")[1]);
            } else if (line.startsWith("Appliance")) {
                String name = lines[++i].split(": ")[1];
                int powerConsumption = Integer.parseInt(lines[++i].split(": ")[1]);
                int embodiedEmission = Integer.parseInt(lines[++i].split(": ")[1]);
               // appliances.add(new Appliance(name, powerConsumption, embodiedEmission));
            } else if (line.startsWith("Timeframe ")) {
                String userPart = lines[++i].split(": ")[1];
                List<String> timeframeUsers = Arrays.asList(userPart.split(", "));
                String start = lines[++i].split(": ")[1];
                String end = lines[++i].split(": ")[1];
              //  timeframes.add(new Timeframe(timeframeUsers, start, end));
            }
            i++;
        }

      //  DateTime[] period = new DateTime(startDateTime, endDateTime);

       // h = new House(region, tariff, startDateTime, endDateTime);
       // h.users.addAll(users);
      //  h.appliances.addAll(appliances);
       // h.timeframes.addAll(timeframes);

        return h;
    }


    public String convertToJson() {
        // to be implemented
        return "";
    }

    // Optional getter/setter for house if needed
    public House getHouse() {
        return house;
    }

    public void setHouse(House house) {
        this.house = house;
    }
}