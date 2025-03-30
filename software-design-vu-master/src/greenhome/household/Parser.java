package greenhome.household;
import greenhome.time.DateTime;

import java.io.*;
import org.json.*;
import java.util.ArrayList;
import java.util.List;

import static greenhome.validation.Validator.validateDates;

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
                h.setRegion(line.split(": ")[line.split(": ").length-1]);
            } else if (line.startsWith("Tariff:")) {
                h.setTariff(Double.parseDouble(line.split(": ")[line.split(": ").length-1]));
            } else if (line.startsWith("Start DateTime:")) {
                DateTime start = new DateTime(DateTime.stringToVals(line).get(0),DateTime.stringToVals(line).get(1),DateTime.stringToVals(line).get(2),DateTime.stringToVals(line).get(3),DateTime.stringToVals(line).get(4));
                h.setStart(start);
            } else if (line.startsWith("End DateTime:")) {
                DateTime end = new DateTime(DateTime.stringToVals(line).get(0),DateTime.stringToVals(line).get(1),DateTime.stringToVals(line).get(2),DateTime.stringToVals(line).get(3),DateTime.stringToVals(line).get(4));
                h.setStart(end);
            } else if (line.startsWith("-")) {
               h.addUser(new User(line.split("- ")[line.split("- ").length-1]));
            } else if (line.startsWith("APPLIANCES")){
                tfFlag = false;
            } else if (line.startsWith("Appliance") && !tfFlag) {
                String name = lines[++i].split(": ")[line.split(": ").length-1];
                int powerConsumption = Integer.parseInt(lines[++i].split(": ")[line.split(": ").length]);
                double embodiedEmission = Double.parseDouble(lines[++i].split(": ")[line.split(": ").length]);
               h.addAppliance(new Appliance(name, powerConsumption, embodiedEmission));
            } else if (line.startsWith("TIMEFRAMES")){
                tfFlag = true;
            } else if (line.startsWith("Appliance ") && tfFlag) {
                Appliance chosenAppliance = new Appliance();
                for (Appliance appliance : h.getAppliances()) {
                    if (appliance.getName() == line.split(": ")[line.split(": ").length-1]) {
                        chosenAppliance = appliance;
                    }
                }

                while(line.startsWith("- User:")) {
                    List<User> timeframeUsers = new ArrayList<>();
                    for (int j = 0; j < line.split(": ")[line.split(": ").length-1].split(", Start:")[0].split(", ").length; j++) {
                        h.addUser(new User(line.split(": ")[line.split(": ").length-1].split(", Start:")[0].split(", ")[j]));
                    }
                    DateTime startTF = new DateTime(DateTime.stringToVals(line.split("End")[0]).get(0),DateTime.stringToVals(line.split("End")[0]).get(1),DateTime.stringToVals(line.split("End")[0]).get(2),DateTime.stringToVals(line.split("End")[0]).get(3),DateTime.stringToVals(line.split("End")[0]).get(4));
                    DateTime endTF = new DateTime(DateTime.stringToVals(line.split("End")[1]).get(0),DateTime.stringToVals(line.split("End")[1]).get(1),DateTime.stringToVals(line.split("End")[1]).get(2),DateTime.stringToVals(line.split("End")[1]).get(3),DateTime.stringToVals(line.split("End")[1]).get(4));
                    if(validateDates(h.getStart(), startTF) && validateDates(endTF, h.getEnd())) {
                        h.addTimeframe(new Timeframe(timeframeUsers, chosenAppliance, startTF, endTF));
                    }
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
                h.setRegion(line.split(": ")[line.split(": ").length-1]);
            } else if (line.startsWith("Tariff:")) {
                h.setTariff(Double.parseDouble(line.split(": ")[line.split(": ").length-1]));
            } else if (line.startsWith("Start DateTime:")) {
                DateTime start = new DateTime(DateTime.stringToVals(line).get(0),DateTime.stringToVals(line).get(1),DateTime.stringToVals(line).get(2),DateTime.stringToVals(line).get(3),DateTime.stringToVals(line).get(4));
                h.setStart(start);
            } else if (line.startsWith("End DateTime:")) {
                DateTime end = new DateTime(DateTime.stringToVals(line).get(0),DateTime.stringToVals(line).get(1),DateTime.stringToVals(line).get(2),DateTime.stringToVals(line).get(3),DateTime.stringToVals(line).get(4));
                h.setStart(end);
            } else if (line.startsWith("-")) {
                users.add(new User(line.split("- ")[line.split(": ").length-1]));
            } else if (line.startsWith("APPLIANCES")){
                tfFlag = false;
            } else if (line.startsWith("Appliance") && !tfFlag) {
                String name = lines[++i].split(": ")[line.split(": ").length-1];
                int powerConsumption = Integer.parseInt(lines[++i].split(": ")[line.split(": ").length-1]);
                double embodiedEmission = Double.parseDouble(lines[++i].split(": ")[line.split(": ").length-1]);
                appliances.add(new Appliance(name, powerConsumption, embodiedEmission));
            } else if (line.startsWith("TIMEFRAMES")){
                tfFlag = true;
            } else if (line.startsWith("Appliance ") && tfFlag) {
                Appliance chosenAppliance = new Appliance();
                for (Appliance appliance : h.getAppliances()) {
                    if (appliance.getName() == line.split(": ")[line.split(": ").length-1]) {
                        chosenAppliance = appliance;
                    }
                }

                while(line.startsWith("- User:")) {
                    List<User> timeframeUsers = new ArrayList<>();
                    for (int j = 0; j < line.split(": ")[line.split(": ").length-1].split(", Start:")[0].split(", ").length-1; j++) {
                        h.addUser(new User(line.split(": ")[line.split(": ").length-1].split(", Start:")[0].split(", ")[j]));
                    }
                    DateTime startTF = new DateTime(DateTime.stringToVals(line.split("End")[0]).get(0),DateTime.stringToVals(line.split("End")[0]).get(1),DateTime.stringToVals(line.split("End")[0]).get(2),DateTime.stringToVals(line.split("End")[0]).get(3),DateTime.stringToVals(line.split("End")[0]).get(4));
                    DateTime endTF = new DateTime(DateTime.stringToVals(line.split("End")[1]).get(0),DateTime.stringToVals(line.split("End")[1]).get(1),DateTime.stringToVals(line.split("End")[1]).get(2),DateTime.stringToVals(line.split("End")[1]).get(3),DateTime.stringToVals(line.split("End")[1]).get(4));
                    if(validateDates(h.getStart(), startTF) && validateDates(endTF, h.getEnd())) {
                        h.addTimeframe(new Timeframe(timeframeUsers, chosenAppliance, startTF, endTF));
                    }
                    timeframes.add(new Timeframe(timeframeUsers, chosenAppliance, startTF, endTF));
                    i++;
                }
            }
            i++;
        }
        h.modTimeframes(timeframes);
        h.modUser(users);
        h.modAppliances(appliances);
    }

    public static String houseToString() {
        StringBuilder stringB = new StringBuilder();
        House h = House.getInstance();
        stringB.append("HOUSE INFO\n");
        stringB.append("Region: ").append(h.getRegion()).append("\n");
        stringB.append("Tariff: ").append(h.getElectricityTariff()).append("\n");
        stringB.append("Start DateTime: ").append(h.getStart().valsToString()).append("\n");
        stringB.append("End DateTime: ").append(h.getEnd().valsToString()).append("\n\n");

        stringB.append("USERS\n");
        for (User user : h.getResidents()) {
            stringB.append("- ").append(user.getName()).append("\n");
        }
        stringB.append("\n");

        stringB.append("APPLIANCES:\n");
        for (Appliance appliance : h.getAppliances()) {
            stringB.append("Name: ").append(appliance.getName()).append("\n");
            stringB.append("Power Consumption: ").append(appliance.getPowerConsumption()).append("\n");
            stringB.append("Embodied Emission: ").append(appliance.getEmbodiedEmissions()).append("\n\n");
        }
        stringB.append("\n");
        stringB.append("TIMEFRAMES:\n");
        for (Appliance appliance : h.getAppliances()) {
            boolean hasTimeframes = false;
            for (Timeframe tf : h.getTimeframes()) {
                if (tf.getAppliance().getName().equals(appliance.getName())) {
                    if (!hasTimeframes) {
                        stringB.append("Appliance: ").append(appliance.getName()).append("\n");
                        hasTimeframes = true;
                    }
                    stringB.append("  - User: ");
                    for (int i = 0; i < tf.getUsers().size(); i++) {
                        stringB.append(tf.getUsers().get(i).getName());
                        if (i < tf.getUsers().size() - 1) {
                            stringB.append(", ");
                        }
                    }
                    stringB.append(", Start: ").append(tf.getPeriod()[0].valsToString());
                    stringB.append(", End: ").append(tf.getPeriod()[1].valsToString()).append("\n");
                }
            }
        }
        System.out.println("Home to String");
        System.out.println(stringB.toString());
        return stringB.toString();
    }


    public static void saveHouse() {
        House h = House.getInstance();
        String houseData = houseToString();
        System.out.println("saveHOUSE CALLED\n\n\n\n");
        JSONObject json = new JSONObject();
        json.put("houseData", houseData);

        try (FileWriter file = new FileWriter("json.json")) {
            file.write(json.toString(4));
            System.out.println("flag1");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("flag2");
        }
    }

    public static void loadHouse() {
        File file = new File("json.json");
        String path = file.getAbsolutePath();
        System.out.println(path);
        if (!file.exists()) {
            return;
        }

        StringBuilder jsonData = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader("json.json"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                jsonData.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        try {
            JSONObject json = new JSONObject(jsonData.toString());
            String houseData = json.getString("houseData");
            stringIntoHouse(houseData);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}