package greenhome.time;
import greenhome.apiintegration.*;
import greenhome.household.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Timeframe {
    public static int count;

    // Associations
    private List<User> users = new ArrayList<>();
    private Appliance appliance;

    private DateTime[] period = new DateTime[2];

    private List<Integer> carbonIntensitiesByHour;

    public double averageCarbonIntensity;
    public double carbonFootprint;

    private double CarbonIntensity;

    public Timeframe(List<User> users, Appliance appliance, DateTime start, DateTime end) {
        this.users = users;
        this.appliance = appliance;
        this.period[0] = start;
        this.period[1] = end;
        this.averageCarbonIntensity = CI.fetchCarbonIntensity();
    }

    public Appliance getAppliance() {
        return appliance;
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

    public void calcFootPrint() {
        double usageHours = getUsageDurationInHoursForAppliance();
        double power = appliance.getPowerConsumption();
        double energyUsed = power * usageHours;
        this.carbonFootprint = energyUsed * averageCarbonIntensity;

        //this.carbonFootprint = (appliance.getPowerConsumption() * this.averageCarbonIntensity / 1000);
    }

    public DateTime[] getPeriod() {
        return period;
    }

}