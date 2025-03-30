package greenhome.household;
import greenhome.time.DateTime;

import java.io.*;
import org.json.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static greenhome.validation.Validator.validateDates;

public class Parser {


    // Constructor
    public Parser() {
        // default constructor or overload as needed
    }

    /**
     HOUSE INFO:
     Region: Andorra
     Tariff: 0.34
     Start DateTime: 2025-03-01 21:17
     End DateTime: 2025-03-31 21:17

     USERS:
     - Steve
     - Bob

     APPLIANCES:
     Name: Fridge
     Power Consumption: 99
     Embodied Emission: 99.0


     TIMEFRAMES:
     Appliance: Fridge
     - User: Steve
     Start: 2025-03-04 21:17
     End: 2025-03-06 21:17
     */
    public static void stringIntoHouse (String data) {
        System.out.println("\n\nSTRING TO HOUSE \n\n");
        System.out.println(data);
        String[] lines = data.split("\n");
        int i = 0;
        House h = House.getInstance();
        while (i < lines.length)  {
            String line = lines[i].trim();
            if (line.startsWith("Region")){
                line = line.replaceFirst("Region: ","");
                h.setRegion(line);
                System.out.println(line);
            } else if (line.startsWith("Tariff")) {
                line = line.replaceFirst("Tariff: ","");
                h.setTariff(Double.parseDouble(line));
            } else if (line.startsWith("Start DateTime")) {
                String startString = line.replaceFirst("Start DateTime: ","");
                List<Integer> startFVals = DateTime.stringToVals(startString);
                DateTime startF = new DateTime(startFVals.get(0),startFVals.get(1),startFVals.get(2),startFVals.get(3),startFVals.get(4));
                h.setStart(startF);
            } else if (line.startsWith("End DateTime")) {
                String endString = line.replaceFirst("End DateTime: ","");
                List<Integer> endFVals = DateTime.stringToVals(endString);
                DateTime endF = new DateTime(endFVals.get(0),endFVals.get(1),endFVals.get(2),endFVals.get(3),endFVals.get(4));
                h.setEnd(endF);
            } else if (line.startsWith("USERS")) {
                String newLine = lines[++i].trim();
                while (newLine.startsWith("- ")){
                    newLine = newLine.replaceFirst("- ", "");
                    User placeHUser = new User(newLine);
                    h.addUser(placeHUser);
                    newLine = lines[++i].trim();
                }
            } else if (line.startsWith("APPLIANCES")) {
                String newLine = lines[++i].trim();
                while (newLine.startsWith("Appliance")){
                    newLine = lines[++i].trim();
                    String name = newLine.replaceFirst("Name: ","");
                    newLine = lines[++i].trim();
                    String power = newLine.replaceFirst("Power Consumption: ","");
                    newLine = lines[++i].trim();
                    String emissions = newLine.replaceFirst("Embodied Emission: ","");
                    newLine = lines[++i].trim();
                    Appliance placeHAppliance = new Appliance(name, Double.parseDouble(power), Double.parseDouble(emissions)/10);
                    h.addAppliance(placeHAppliance);
                    newLine = lines[++i].trim();
                }

            } else if (line.startsWith("TIMEFRAMES PER APPLIANCE")) {
                String newLine = lines[++i].trim();
                while (newLine.startsWith("Appliance")){
                    String applianceName = newLine.replaceFirst("Appliance: ","");

                    newLine = lines[++i].trim();

                    List<String> usernameList = new ArrayList<String>(Arrays.asList(newLine.replaceFirst(" - User:","").split(", ")));
                    List<User> usersList = null;
                    for (int x = 0; x < usernameList.size(); x++){
                        usersList.add(new User(usernameList.get(x)));
                    }
                    newLine = lines[++i].trim();
                    String startString = newLine.replaceFirst(" Start: ","");
                    newLine = lines[++i].trim();
                    String endString = newLine.replaceFirst(" End: ","");

                    List<Integer> startTFVals = DateTime.stringToVals(startString);
                    List<Integer> endTFVals = DateTime.stringToVals(endString);
                    DateTime startTF = new DateTime(startTFVals.get(0),startTFVals.get(1),startTFVals.get(2),startTFVals.get(3),startTFVals.get(4));
                    DateTime endTF = new DateTime(endTFVals.get(0),endTFVals.get(1),endTFVals.get(2),endTFVals.get(3),endTFVals.get(4));

                    Appliance chosenAppliance = new Appliance();
                    for (Appliance appliance : h.getAppliances()) {
                        if (appliance.getName() == applianceName) {
                            chosenAppliance = appliance;
                        }
                    }

                    if(validateDates(h.getStart(), startTF) && validateDates(endTF, h.getEnd())) {
                        h.addTimeframe(new Timeframe(usersList, chosenAppliance, startTF, endTF));
                    }
                    newLine = lines[++i].trim();
                }
            }
            i++;
        }

    }

    public static void whatifStringModHouse(String data) {
        System.out.println("\n\nSTRING TO HOUSE \n\n");
        int l = 0;
        System.out.println("flag" + ++l);
        String[] lines = data.split("\n");
        List<User> userListToReplace = null;
        List<Appliance> applianceListToReplace = null;
        List<Timeframe> tfListToReplace = null;
        int i = 0;
        House h = House.getInstance();
        while (i < lines.length)  {
            System.out.println("looping");
            String line = lines[i].trim();
            if (line.startsWith("Region")){
                line = line.replaceFirst("Region: ","");
                h.setRegion(line);
            } else if (line.startsWith("Tariff")) {
                line = line.replaceFirst("Tariff: ","");
                h.setTariff(Double.parseDouble(line));
            } else if (line.startsWith("Start DateTime")) {
                String startString = line.replaceFirst("Start DateTime: ","");
                List<Integer> startFVals = DateTime.stringToVals(startString);
                DateTime startF = new DateTime(startFVals.get(0),startFVals.get(1),startFVals.get(2),startFVals.get(3),startFVals.get(4));
                h.setStart(startF);
            } else if (line.startsWith("End DateTime")) {
                String endString = line.replaceFirst("End DateTime: ","");
                List<Integer> endFVals = DateTime.stringToVals(endString);
                DateTime endF = new DateTime(endFVals.get(0),endFVals.get(1),endFVals.get(2),endFVals.get(3),endFVals.get(4));
                h.setEnd(endF);
            } else if (line.startsWith("USERS")) {
                String newLine = lines[++i].trim();
                while (newLine.startsWith("- ")){
                    newLine = newLine.replaceFirst("- ", "");
                    User placeHUser = new User(newLine);
                    userListToReplace.add(placeHUser);
                    newLine = lines[++i].trim();
                }
            } else if (line.startsWith("APPLIANCES")) {
                String newLine = lines[++i].trim();
                while (newLine.startsWith("Appliance")){
                    newLine = lines[++i].trim();
                    String name = newLine.replaceFirst("Name: ","");
                    newLine = lines[++i].trim();
                    String power = newLine.replaceFirst("Power Consumption: ","");
                    newLine = lines[++i].trim();
                    String emissions = newLine.replaceFirst("Embodied Emission: ","");
                    newLine = lines[++i].trim();
                    Appliance placeHAppliance = new Appliance(name, Double.parseDouble(power), Double.parseDouble(emissions)/10);
                    applianceListToReplace.add(placeHAppliance);
                    newLine = lines[++i].trim();
                }

            } else if (line.startsWith("TIMEFRAMES PER APPLIANCE")) {
                String newLine = lines[++i].trim();
                while (line.startsWith("Appliance")){
                    System.out.println("looping");
                    String applianceName = newLine.replaceFirst("Appliance: ","");

                    newLine = lines[++i].trim();

                    List<String> usernameList = new ArrayList<String>(Arrays.asList(newLine.replaceFirst(" - User:","").split(", ")));
                    List<User> usersList = null;
                    for (int x = 0; x < usernameList.size(); x++){
                        usersList.add(new User(usernameList.get(x)));
                    }
                    newLine = lines[++i].trim();
                    String startString = newLine.replaceFirst(" Start: ","");
                    newLine = lines[++i].trim();
                    String endString = newLine.replaceFirst(" End: ","");

                    List<Integer> startTFVals = DateTime.stringToVals(startString);
                    List<Integer> endTFVals = DateTime.stringToVals(endString);
                    DateTime startTF = new DateTime(startTFVals.get(0),startTFVals.get(1),startTFVals.get(2),startTFVals.get(3),startTFVals.get(4));
                    DateTime endTF = new DateTime(endTFVals.get(0),endTFVals.get(1),endTFVals.get(2),endTFVals.get(3),endTFVals.get(4));

                    Appliance chosenAppliance = new Appliance();
                    for (Appliance appliance : applianceListToReplace) {
                        if (appliance.getName() == applianceName) {
                            chosenAppliance = appliance;
                        }
                    }

                    if(validateDates(h.getStart(), startTF) && validateDates(endTF, h.getEnd())) {
                        tfListToReplace.add(new Timeframe(usersList, chosenAppliance, startTF, endTF));
                    }
                    line = lines[++i].trim();
                }
            }
            i++;
        }
        h.modTimeframes(tfListToReplace);
        h.modUser(userListToReplace);
        h.modAppliances(applianceListToReplace);

    }

    public static String houseToString() {
        System.out.println("houseToString");
        StringBuilder stringB = new StringBuilder();
        House h = House.getInstance();
        stringB.append("HOUSE INFO: \nRegion: " + h.getRegion() + "\nTariff: "+ h.getElectricityTariff()+"\nStart DateTime: "+ h.getStart().valsToString()+"\nEnd DateTime: "+ h.getEnd().valsToString()+ "\n");
        stringB.append("USERS: ");
        for (User user : h.getResidents()){
            stringB.append("\n- ");
            stringB.append(user.getName());
        }
        stringB.append("\n");
        stringB.append("\nAPPLIANCES:");
        for (Appliance appliance : h.getAppliances()){
            stringB.append("\nName: ");
            stringB.append(appliance.getName());
            stringB.append("\nPower Consumption: ");
            stringB.append(appliance.getPowerConsumption());
            stringB.append("\nEmbodied Emission: ");
            stringB.append(appliance.getEmbodiedEmissions());
            stringB.append("\n");
        }
        stringB.append("\nTIMEFRAMES:");
        for (Appliance appliance : h.getAppliances()){
            stringB.append("\nAppliance: ");
            stringB.append(appliance.getName());
            for (Timeframe timeframe : h.getTimeframes()){
                if (appliance == timeframe.getAppliance()){
                    stringB.append("\n - User: ");
                    for (User user : timeframe.getUsers()){
                        stringB.append(user.getName());
                        stringB.append(", ");
                    }
                    stringB.setLength(stringB.length() - 2);
                }
                stringB.append("\n Start:" + timeframe.getPeriod()[0].valsToString());
                stringB.append("\n End:" + timeframe.getPeriod()[1].valsToString());

            }
        }

        return stringB.toString();
    }


    // Methods
   /* public static void stringIntoHouse(String data) {
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
                double powerConsumption = Double.parseDouble(lines[++i].split(": ")[line.split(": ").length-1]);
                double embodiedEmission = Double.parseDouble(lines[++i].split(": ")[line.split(": ").length-1])/10;
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
                double powerConsumption = Double.parseDouble(lines[++i].split(": ")[line.split(": ").length-1]);
                double embodiedEmission = Double.parseDouble(lines[++i].split(": ")[line.split(": ").length-1])/10;
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
        System.out.println("HOUSE VALUES");
        System.out.println(h.getTimeframes().toString());
        System.out.println(h.getResidents().toString());
        System.out.println(h.getAppliances().toString());
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
    } */


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