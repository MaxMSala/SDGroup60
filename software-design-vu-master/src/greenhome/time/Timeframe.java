package greenhome.time;
import greenhome.datavalidation.*;
import greenhome.apiintegration.*;
import greenhome.household.*;

import java.time.Duration;
import java.time.LocalDateTime;

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

    public Appliance getAppliance() {
        return appliance;
    }

    public User getUser() {
        return user;
    }

    // returns number of hours the appliance was used through the timeframe
    public double getUsageDurationInHoursForAppliance() {
        if (period == null || period.length != 2) return 0.0;

        LocalDateTime start = period[0].toLocalDateTime();
        LocalDateTime end = period[1].toLocalDateTime();

        Duration duration = Duration.between(start, end);
        return duration.toMinutes() / 60.0;
    }

    public void calcFootPrint() {
        this.carbonFootprint = (appliance.getKiloWattHours() * this.gramsCO2PerKiloWattHour / 1000);
    }
}