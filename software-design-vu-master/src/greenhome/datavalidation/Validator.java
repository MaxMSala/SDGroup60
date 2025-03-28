package greenhome.datavalidation;

import greenhome.household.User;

class Validator {
    public static boolean validateTimeframe(String timeframe, String users){
        for (int j = 0; j < timeframe.split(": ")[1].split(", Start:")[0].split(", ").length; j++) {
            for (int k = 0; k < users.split(", ").length; k++){
                if (users.split(", ")[k] == (timeframe.split(": ")[1].split(", Start:")[0].split(", ")[j])) {
                    return true;
               }
            }
        }
        return false;
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