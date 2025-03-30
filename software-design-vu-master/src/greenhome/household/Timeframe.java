package greenhome.household;
import greenhome.apiintegration.*;
import greenhome.time.DateTime;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class Timeframe {
    public static int count;

    private List<User> users;
    private Appliance appliance;
    private DateTime[] period = new DateTime[2];
    private double carbonIntensity;

    // derived
    private double carbonFootprint;

    // public interface
    public Timeframe(List<User> users, Appliance appliance, DateTime start, DateTime end) {
        this.users = users;
        this.appliance = appliance;
        this.period[0] = start;
        this.period[1] = end;

        CarbonIntensity ci = CarbonIntensity.getInstance(period);
        this.carbonIntensity = ci.getCarbonIntensity();
    }

    // returns number of hours the appliance was used through the timeframe
    public double getUsageDurationInHoursForAppliance() {
        if (period == null || period.length != 2) return 0.0;

        LocalDateTime start = period[0].toLocalDateTime();
        LocalDateTime end = period[1].toLocalDateTime();

        Duration duration = Duration.between(start, end);
        return duration.toMinutes() / 60.0;
    }

    // getters
    public double getCarbonFootprint() {calcFootPrint();return this.carbonFootprint;}
    public List<User> getUsers() {
        return users;
    }
    public Appliance getAppliance() {
        return appliance;
    }
    public DateTime[] getPeriod() {
        return period;
    }


    // private calculations
    private void calcFootPrint() {
        double usageHours = getUsageDurationInHoursForAppliance();
        double power = appliance.getPowerConsumption();
        double energyUsed = power * usageHours;
        this.carbonFootprint = energyUsed * carbonIntensity;
    }




}