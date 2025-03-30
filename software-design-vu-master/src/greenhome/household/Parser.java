package greenhome.household;
import greenhome.time.DateTime;
import greenhome.time.Timeframe;
import greenhome.validation.*;
import java.io.*;
import org.json.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
                double embodiedEmission = Double.parseDouble(lines[++i].split(": ")[1]);
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
                    for (int j = 0; j < line.split(": ")[1].split(", Start:")[0].split(", ").length; j++) {
                        h.addUser(new User(line.split(": ")[1].split(", Start:")[0].split(", ")[j]));
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
                double embodiedEmission = Double.parseDouble(lines[++i].split(": ")[1]);
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
                    for (int j = 0; j < line.split(": ")[1].split(", Start:")[0].split(", ").length; j++) {
                        h.addUser(new User(line.split(": ")[1].split(", Start:")[0].split(", ")[j]));
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
        System.out.println("✅ New houseToString() called");

        StringBuilder sb = new StringBuilder();
        House h = House.getInstance();

        // --- House Info ---
        sb.append("HOUSE INFO\n");
        sb.append("Region: ").append(h.getRegion()).append("\n");
        sb.append("Tariff: ").append(h.getElectricityTariff()).append("\n");
        sb.append("Start DateTime: ").append(h.getStart().valsToString()).append("\n");
        sb.append("End DateTime: ").append(h.getEnd().valsToString()).append("\n\n");

        // --- Users ---
        sb.append("USERS\n");
        for (User  user : h.getResidents()) {
            sb.append("- ").append(user.getName()).append("\n");
        }
        sb.append("\n");

        // --- Appliances ---
        sb.append("APPLIANCES\n");
        int counter = 1;
        for (Appliance appliance : h.getAppliances()) {
            sb.append("Appliance ").append(counter++).append(":\n");
            sb.append("Name: ").append(appliance.getName()).append("\n");
            sb.append("Power Consumption: ").append(appliance.getPowerConsumption()).append("\n");
            sb.append("Embodied Emission: ").append(appliance.getEmbodiedEmissions()).append("\n\n");
        }

        // --- Timeframes per Appliance ---
        sb.append("TIMEFRAMES PER APPLIANCE\n");
        for (Appliance appliance : h.getAppliances()) {
            List<Timeframe> allTFs = h.getTimeframes();
            boolean hasPrinted = false;
            for (Timeframe tf : allTFs) {
                if (tf.getAppliance().getName().equals(appliance.getName())) {
                    if (!hasPrinted) {
                        sb.append("Appliance: ").append(appliance.getName()).append("\n");
                        hasPrinted = true;
                    }
                    for (User  user : tf.getUsers()) {
                        sb.append("- User: ").append(user.getName())
                                .append(", From: ").append(tf.getPeriod()[0].valsToString())
                                .append(" → ").append(tf.getPeriod()[1].valsToString()).append("\n");
                    }
                }
            }
        }

        // Debug output to check timeframes
        System.out.println("🔍 DEBUG: Timeframes for appliances:");
        for (Appliance appliance : h.getAppliances()) {
            List<Timeframe> timeframes = h.getTimeframes();
            for (Timeframe tf : timeframes) {
                if (tf.getAppliance().getName().equals(appliance.getName())) {
                    System.out.println("Appliance: " + appliance.getName() + ", Timeframe: " + tf);
                }
            }
        }

        sb.append("\n----- End of Report -----\n");
        return sb.toString();
    }


    public static void saveHouse() {
        House h = House.getInstance();
        String houseData = houseToString();

        JSONObject json = new JSONObject();
        json.put("houseData", houseData);

        try (FileWriter file = new FileWriter("json.json")) {
            file.write(json.toString(4));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadHouse() {
        File file = new File("json.json");
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