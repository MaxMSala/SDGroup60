package greenhome.time;
import greenhome.datavalidation.*;
import greenhome.apiintegration.*;
import greenhome.household.*;

public class Timeframe {
    public static int count;

    // Associations
    private User user;
    private Appliance appliance;

    private DateTime[] period = new DateTime[2];

    public double averageCarbonIntensity;
    public double carbonFootprint;

    private double gramsCO2PerKiloWattHour;
    private double tonnesCO2eq;

    public Timeframe(User user, Appliance appliance, DateTime start, DateTime end, double averageCarbonIntensity, double gramsCO2PerKiloWattHour) {
        this.user = user;
        this.appliance = appliance;
        this.period[0] = start;
        this.period[1] = end;
        this.averageCarbonIntensity = averageCarbonIntensity;
        this.gramsCO2PerKiloWattHour = gramsCO2PerKiloWattHour;
    }

    public void calcFootPrint() {
        this.carbonFootprint = (appliance.getKiloWattHours() * this.gramsCO2PerKiloWattHour / 1000);
    }
}