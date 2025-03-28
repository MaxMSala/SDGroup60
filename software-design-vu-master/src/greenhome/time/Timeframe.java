package greenhome.time;
import greenhome.datavalidation.*;
import greenhome.apiintegration.*;
import greenhome.household.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Set;
import java.util.List;

public class Timeframe {
    public static int count;

    // Associations
    private List<User> users;
    private Appliance appliance;

    private DateTime[] period = new DateTime[2];

    private double carbonFootprint;
    private double carbonIntensity;

    public Timeframe(List<User> users, Appliance appliance, DateTime start, DateTime end) {
        this.users = users;
        this.appliance = appliance;
        this.period[0] = start;
        this.period[1] = end;

        CI ci= CI.getInstance(period);
        this.carbonIntensity = ci.getCarbonIntensity();
    }

    public Appliance getAppliance() {
        return appliance;
    }

    public double getCarbonFootprint() {
        calcFootPrint();
        return this.carbonFootprint;
    }

    public List<User> getUsers() {
        return users;
    }

    // returns number of hours the appliance was used through the timeframe
    public double getUsageDurationInHoursForAppliance() {
        if (period == null || period.length != 2) return 0.0;

        LocalDateTime start = period[0].toLocalDateTime();
        LocalDateTime end = period[1].toLocalDateTime();

        Duration duration = Duration.between(start, end);
        return duration.toMinutes() / 60.0;
    }

    private void calcFootPrint() {
        double usageHours = getUsageDurationInHoursForAppliance();
        double power = appliance.getPowerConsumption();
        double energyUsed = power * usageHours;
        this.carbonFootprint = energyUsed * carbonIntensity;
    }

    public DateTime[] getPeriod() {
        return period;
    }
}