package greenhome.household;

import greenhome.datavalidation.*;
import greenhome.time.Timeframe;
import java.util.Set;


public class House {

    // Aggregation (1..*) with User
    private Set<User> residents;

    private double electricityTariff;

    // Composition (1..*) with Appliance
    private Set<Appliance> appliances;

    private Set<Timeframe> timeframes;

    // derived attributes ("/" prefix)
    private int ecoScore;
    private double footPrint;

    // Internal attributes
    private double euroPerKiloWattHour;
    private double tonnesCO2eq;

    // Methods
    private int calcEcoScore() {
        // to be implemented
        return 0;
    }

    private double sumFootPrint() {
        // to be implemented
        return 0.0;
    }

    // Getters & Setters can be added later as needed
}