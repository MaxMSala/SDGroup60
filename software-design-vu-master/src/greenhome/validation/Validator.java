package greenhome.validation;

import greenhome.time.DateTime;

public class Validator {
    public static boolean validateTimeframe(String timeframe, String users, String house){
        for (int j = 0; j < timeframe.split(": ")[1].split(", Start:")[0].split(", ").length; j++) {
            for (int k = 0; k < users.split("\n").length; k++){
                if (users.split("\n")[k].replaceFirst("- ","") == (timeframe.split(": ")[1].split(", Start:")[0].split(", ")[j])) {
                    return true;
               }
            }
        }

        DateTime startHouse = new DateTime(DateTime.stringToVals(house.split("tart DateTime: ")[1].split("End")[0]).get(0),DateTime.stringToVals(house.split("tart DateTime: ")[1].split("End")[0]).get(1),DateTime.stringToVals(house.split("tart DateTime: ")[1].split("End")[0]).get(2),DateTime.stringToVals(house.split("tart DateTime: ")[1].split("End")[0]).get(3),DateTime.stringToVals(house.split("tart DateTime: ")[1].split("End")[0]).get(4));
        DateTime endHouse = new DateTime(DateTime.stringToVals(house.split("End DateTime: ")[1]).get(0),DateTime.stringToVals(house.split("End DateTime: ")[1]).get(1),DateTime.stringToVals(house.split("End DateTime: ")[1]).get(2),DateTime.stringToVals(house.split("End DateTime: ")[1]).get(3),DateTime.stringToVals(house.split("End DateTime: ")[1]).get(4));
        DateTime startTF = new DateTime(DateTime.stringToVals(timeframe.split("End")[0]).get(0),DateTime.stringToVals(timeframe.split("End")[0]).get(1),DateTime.stringToVals(timeframe.split("End")[0]).get(2),DateTime.stringToVals(timeframe.split("End")[0]).get(3),DateTime.stringToVals(timeframe.split("End")[0]).get(4));
        DateTime endTF = new DateTime(DateTime.stringToVals(timeframe.split("End")[1]).get(0),DateTime.stringToVals(timeframe.split("End")[1]).get(1),DateTime.stringToVals(timeframe.split("End")[1]).get(2),DateTime.stringToVals(timeframe.split("End")[1]).get(3),DateTime.stringToVals(timeframe.split("End")[1]).get(4));

        return(validateDates(startTF, endTF) && validateDates(startHouse, startTF) && validateDates(endTF, endHouse) && startTF != endTF);

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
        if (start.getYear() < end.getYear()) {
            return true;
        } else if (start.getYear() > end.getYear()) {
            return false;
        } else if (start.getYear() == end.getYear()) {
            if (start.getMonth() < end.getMonth()) {
                return true;
            } else if (start.getMonth() > end.getMonth()) {
                return false;
            } else if (start.getMonth() == end.getMonth()) {
                if (start.getYear() < end.getYear()) {
                    return true;
                } else if (start.getDay() > end.getDay()) {
                    return false;
                } else if (start.getDay() == end.getDay()) {
                    if (start.getHour() < end.getHour()) {
                        return true;
                    } else if (start.getHour() > end.getHour()) {
                        return false;
                    } else if (start.getHour() == end.getHour()) {
                        if (start.getMinute() <= end.getMinute()) {
                            return true;
                        }
                    }
                }
            }
        }
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