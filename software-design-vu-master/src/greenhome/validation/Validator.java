package greenhome.validation;

import greenhome.time.DateTime;

import java.util.Arrays;

public class Validator {
    public static boolean validateTimeframe(String timeframe, String users, String house){
        boolean foundFlag = false;
        int foundNum = 0;
        //System.out.println("flag:StartValidate\n");
        //System.out.println(Arrays.toString(timeframe.split(": ")[1].split(", Start:")[0].split(", ")));
        //System.out.println(timeframe.split(": ")[1].split(", Start:")[0].split(", ").length);
        for (int j = 0; j < timeframe.split("\n")[1].replaceFirst("- User: ","").split(", ").length; j++) {
            //System.out.println("flag:ForLoop1\n");
            for (int k = 0; k < users.split("\n").length; k++){
                //System.out.println("flag:ForLoop2\n");
                //System.out.println(users.split("\n")[k].replaceFirst("- ",""));
                //System.out.println(timeframe.split(": ")[1]);
                //System.out.println(timeframe.split(": ")[1].split(", Start")[0]);
                System.out.println(j);
                System.out.println(timeframe.split("\n")[1].replaceFirst("- User: ","").split(", ")[j]);
                if (users.split("\n")[k].replaceFirst("- ", "").equals(timeframe.split("\n")[1].replaceFirst("- User: ","").split(", ")[j])) {
                    //System.out.println("match");
                    foundNum++;
               }
                //System.out.println("names dont match");
                if (foundNum == timeframe.split(": ")[1].split(", Start:")[0].split(", ").length-1){
                    foundFlag = true;
                }
            }
        }
        //System.out.println("flag:PastLoops\n");
        DateTime startHouse = new DateTime(DateTime.stringToVals(house.split("tart DateTime: ")[1].split("End")[0]).get(0),DateTime.stringToVals(house.split("tart DateTime: ")[1].split("End")[0]).get(1),DateTime.stringToVals(house.split("tart DateTime: ")[1].split("End")[0]).get(2),DateTime.stringToVals(house.split("tart DateTime: ")[1].split("End")[0]).get(3),DateTime.stringToVals(house.split("tart DateTime: ")[1].split("End")[0]).get(4));
        DateTime endHouse = new DateTime(DateTime.stringToVals(house.split("End DateTime: ")[1]).get(0),DateTime.stringToVals(house.split("End DateTime: ")[1]).get(1),DateTime.stringToVals(house.split("End DateTime: ")[1]).get(2),DateTime.stringToVals(house.split("End DateTime: ")[1]).get(3),DateTime.stringToVals(house.split("End DateTime: ")[1]).get(4));
        DateTime startTF = new DateTime(DateTime.stringToVals(timeframe.split("End")[0]).get(0),DateTime.stringToVals(timeframe.split("End")[0]).get(1),DateTime.stringToVals(timeframe.split("End")[0]).get(2),DateTime.stringToVals(timeframe.split("End")[0]).get(3),DateTime.stringToVals(timeframe.split("End")[0]).get(4));
        DateTime endTF = new DateTime(DateTime.stringToVals(timeframe.split("End")[1]).get(0),DateTime.stringToVals(timeframe.split("End")[1]).get(1),DateTime.stringToVals(timeframe.split("End")[1]).get(2),DateTime.stringToVals(timeframe.split("End")[1]).get(3),DateTime.stringToVals(timeframe.split("End")[1]).get(4));
        //System.out.println("flag:ReturnTime\n");
        //System.out.println(validateDates(startTF, endTF));
        //System.out.println(validateDates(startHouse, startTF));
        //System.out.println(validateDates(endTF, endHouse));
        //System.out.println(startTF != endTF);
        //System.out.println(foundFlag);
        return(validateDates(startTF, endTF) && validateDates(startHouse, startTF) && validateDates(endTF, endHouse) && startTF != endTF && foundFlag);

    }

    public static boolean validateUser(String user) {
        if (user.contains(",")) {
            System.out.println("Validation failed: Usernames cannot contain commas.");
            return false;
        }
        return true;
    }

    public static boolean validateInteger(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            System.out.println("Validation failed: Expected an integer value.");
            return false;
        }
    }

    public static boolean validateDouble(String input) {
        try {
            Double.parseDouble(input);
            return true;
        } catch (NumberFormatException e) {
            System.out.println("Validation failed: Expected a decimal number.");
            return false;
        }
    }

    public static boolean validateDates(DateTime start, DateTime end) {
        //System.out.println(start.valsToString());
        //System.out.println(end.valsToString());
        if (start.getYear() < end.getYear()) {
            return true;
        } else if (start.getYear() > end.getYear()) {
            return false;
        } else if (start.getYear() == end.getYear()) {
            //System.out.println("years equal");
            if (start.getMonth() < end.getMonth()) {
                return true;
            } else if (start.getMonth() > end.getMonth()) {
                return false;
            } else if (start.getMonth() == end.getMonth()) {
                //System.out.println("Months equal");
                if (start.getDay() < end.getDay()) {
                    return true;
                } else if (start.getDay() > end.getDay()) {
                    return false;
                } else if (start.getDay() == end.getDay()) {
                    //System.out.println("Days equal");
                    if (start.getHour() < end.getHour()) {
                        return true;
                    } else if (start.getHour() > end.getHour()) {
                        return false;
                    } else if (start.getHour() == end.getHour()) {
                        if (start.getMinute() <= end.getMinute()) {
                            return true;
                        } else if (start.getMinute() > end.getMinute()){
                            //System.out.println("minutes issue");
                            return false;
                        }
                    }
                }
            }
        }
        //System.out.println("HUH???");
        return false;
    }

    public static boolean validateEmbodiedEmissions(double value) {
        if (value < 10 || value > 500) {
            System.out.println("Validation failed: Embodied emissions " + value + " kg CO₂e is out of range (10 - 500 kg CO₂e).");
            return false;
        }
        return true;
    }

    public static boolean validatePowerConsumption(int value) {
        if (value < 10 || value > 5000) {
            System.out.println("Validation failed: Power consumption " + value + " W is out of range (10 - 5000 W).");
            return false;
        }
        return true;
    }

    public static boolean validateElectricityTariff(double value) {
        if (value < 0.05 || value > 0.50) {
            System.out.println("Validation failed: Electricity tariff " + value + " EUR/kWh is out of range (0.05 - 0.50 EUR/kWh).");
            return false;
        }
        return true;
    }

}