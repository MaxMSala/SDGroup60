package greenhome.household;
import greenhome.time.DateTime;
import greenhome.time.Timeframe;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Parser {


    // Constructor
    public Parser() {
        // default constructor or overload as needed
    }

    // Methods
    public static void stringIntoHouse(String data) {
        String[] lines = data.split("\n");
        House h = House.getInstance();
        boolean tfFlag = false;
        int i = 0;
        while (i < lines.length) {

            String line = lines[i].trim();

            if (line.startsWith("Region:")) {
                h.setRegion(line.split(": ")[1]);
            } else if (line.startsWith("Tariff:")) {
                h.setTariff(Double.parseDouble(line.split(": ")[1]));
            } else if (line.startsWith("Start DateTime:")) {
                DateTime start = new DateTime(DateTime.stringToVals(line).get(0),DateTime.stringToVals(line).get(1),DateTime.stringToVals(line).get(2),DateTime.stringToVals(line).get(3),DateTime.stringToVals(line).get(4));
                h.setStart(start);
            } else if (line.startsWith("End DateTime:")) {
                DateTime end = new DateTime(DateTime.stringToVals(line).get(0),DateTime.stringToVals(line).get(1),DateTime.stringToVals(line).get(2),DateTime.stringToVals(line).get(3),DateTime.stringToVals(line).get(4));
                h.setStart(end);
            } else if (line.startsWith("-")) {
               h.addUser(new User(line.split("- ")[1]));
            } else if (line.startsWith("APPLIANCES")){
                tfFlag = false;
            } else if (line.startsWith("Appliance") && !tfFlag) {
                String name = lines[++i].split(": ")[1];
                int powerConsumption = Integer.parseInt(lines[++i].split(": ")[1]);
                int embodiedEmission = Integer.parseInt(lines[++i].split(": ")[1]);
               h.addAppliance(new Appliance(name, powerConsumption, embodiedEmission));
            } else if (line.startsWith("TIMEFRAMES")){
                tfFlag = true;
            } else if (line.startsWith("Appliance ") && tfFlag) {
                Appliance chosenAppliance = new Appliance();
                for (Appliance appliance : h.getAppliances()) {
                    if (appliance.getName() == line.split(": ")[1]) {
                        chosenAppliance = appliance;
                    }
                }

                while(line.startsWith("- User:")) {
                    List<User> timeframeUsers = new ArrayList<>();
                    for (int j = 0; j < line.split(": ")[1].split(", Start:")[0].split(", ").length; i++) {
                        h.addUser(new User(line.split(": ")[1].split(", Start:")[0].split(", ")[j]));
                    }
                    DateTime startTF = new DateTime(DateTime.stringToVals(line.split("End")[0]).get(0),DateTime.stringToVals(line).get(1),DateTime.stringToVals(line).get(2),DateTime.stringToVals(line).get(3),DateTime.stringToVals(line).get(4));
                    DateTime endTF = new DateTime(DateTime.stringToVals(line.split("End")[1]).get(0),DateTime.stringToVals(line).get(1),DateTime.stringToVals(line).get(2),DateTime.stringToVals(line).get(3),DateTime.stringToVals(line).get(4));

                    //h.addTimeframe(new Timeframe(timeframeUsers, chosenAppliance, startTF, endTF, 200.0/*placeholders till timeframe is fixed*/, 300/*placeholders till timeframe is fixed*/));
                i++;
                }
            }
            i++;
        }

    }

    public static void whatifStringModHouse(String data) {
        String[] lines = data.split("\n");
        House h = House.getInstance();
        List<User> users = new ArrayList<>();
        List<Appliance> appliances = new ArrayList<>();
        List<Timeframe> timeframes = new ArrayList<>();
        boolean tfFlag = false;
        int i = 0;
        while (i < lines.length) {

            String line = lines[i].trim();

            if (line.startsWith("Region:")) {
                h.setRegion(line.split(": ")[1]);
            } else if (line.startsWith("Tariff:")) {
                h.setTariff(Double.parseDouble(line.split(": ")[1]));
            } else if (line.startsWith("Start DateTime:")) {
                DateTime start = new DateTime(DateTime.stringToVals(line).get(0),DateTime.stringToVals(line).get(1),DateTime.stringToVals(line).get(2),DateTime.stringToVals(line).get(3),DateTime.stringToVals(line).get(4));
                h.setStart(start);
            } else if (line.startsWith("End DateTime:")) {
                DateTime end = new DateTime(DateTime.stringToVals(line).get(0),DateTime.stringToVals(line).get(1),DateTime.stringToVals(line).get(2),DateTime.stringToVals(line).get(3),DateTime.stringToVals(line).get(4));
                h.setStart(end);
            } else if (line.startsWith("-")) {
                users.add(new User(line.split("- ")[1]));
            } else if (line.startsWith("APPLIANCES")){
                tfFlag = false;
            } else if (line.startsWith("Appliance") && !tfFlag) {
                String name = lines[++i].split(": ")[1];
                int powerConsumption = Integer.parseInt(lines[++i].split(": ")[1]);
                int embodiedEmission = Integer.parseInt(lines[++i].split(": ")[1]);
                appliances.add(new Appliance(name, powerConsumption, embodiedEmission));
            } else if (line.startsWith("TIMEFRAMES")){
                tfFlag = true;
            } else if (line.startsWith("Appliance ") && tfFlag) {
                Appliance chosenAppliance = new Appliance();
                for (Appliance appliance : h.getAppliances()) {
                    if (appliance.getName() == line.split(": ")[1]) {
                        chosenAppliance = appliance;
                    }
                }

                while(line.startsWith("- User:")) {
                    List<User> timeframeUsers = new ArrayList<>();
                    for (int j = 0; j < line.split(": ")[1].split(", Start:")[0].split(", ").length; i++) {
                        h.addUser(new User(line.split(": ")[1].split(", Start:")[0].split(", ")[j]));
                    }
                    DateTime startTF = new DateTime(DateTime.stringToVals(line.split("End")[0]).get(0),DateTime.stringToVals(line).get(1),DateTime.stringToVals(line).get(2),DateTime.stringToVals(line).get(3),DateTime.stringToVals(line).get(4));
                    DateTime endTF = new DateTime(DateTime.stringToVals(line.split("End")[1]).get(0),DateTime.stringToVals(line).get(1),DateTime.stringToVals(line).get(2),DateTime.stringToVals(line).get(3),DateTime.stringToVals(line).get(4));

                   // timeframes.add(new Timeframe(timeframeUsers, chosenAppliance, startTF, endTF, 200.0/*placeholders till timeframe is fixed*/, 300/*placeholders till timeframe is fixed*/));
                    i++;
                }
            }
            i++;
        }
        h.modTimeframes(timeframes);
        h.modUser(users);
        h.modAppliances(appliances);
    }

    public static void houseToString(){

    }

}