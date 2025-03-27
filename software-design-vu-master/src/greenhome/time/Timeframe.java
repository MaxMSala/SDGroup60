package greenhome.time;
import greenhome.datavalidation.*;
import greenhome.apiintegration.*;
import greenhome.household.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.List;

public class Timeframe {
    public static int count;

    // Associations
    private Set<User> users;
    private Appliance appliance;

    private DateTime[] period = new DateTime[2];

    public double averageCarbonIntensity;
    public double carbonFootprint;

    private double CarbonIntensity;
    private double tonnesCO2eq;

    public Timeframe(Set<User> users, Appliance appliance, DateTime start, DateTime end, List<Integer> averageCarbonIntensity, double CarbonIntensity) {
        this.users = users;
        this.appliance = appliance;
        this.period[0] = start;
        this.period[1] = end;
        this.averageCarbonIntensity = averageCarbonIntensity;
        this.CarbonIntensity = CI.fetchCarbonIntensity();
    }

    public Appliance getAppliance() {
        return appliance;
    }

    public Set<User> getUsers() {
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

    public void calcFootPrint() {
        this.carbonFootprint = (appliance.getPowerConsumption() * this.averageCarbonIntensity / 1000);
    }

    public DateTime[] getPeriod() {
        return period;
    }
}