package greenhome.household;

import greenhome.datavalidation.*;
import greenhome.time.Timeframe;
import java.util.Set;

public class Appliance {

    public String name;

    // Internal attributes
    private int powerConsumption;
    private double embodiedEmissions;
    private double kiloWattHours;
    private double euros;
    private double tonnesCO2eq;

    // Derived attributes ('/' prefix)
    public double generatedFootprint;
    public double generatedCost;

    // Methods
    public double sumFootPrint(Set<Timeframe> timeframes) {
        // to be implemented
        return 0.0;
    }

    public double calcCost(double electricityTariff, Set<Timeframe> timeframes) {
        // to be implemented
        return 0.0;
    }

    public double getKiloWattHours() {
        return kiloWattHours;
    }

}