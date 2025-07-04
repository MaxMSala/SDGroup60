package greenhome.household;
import greenhome.time.DateTime;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class    Timeframe {


    private List<User> users;
    private Appliance appliance;
    private DateTime[] period = new DateTime[2];
    private double carbonIntensity;


    private double carbonFootprint;

    public Timeframe(List<User> users, Appliance appliance, DateTime start, DateTime end) {
        this.users = users;
        this.appliance = appliance;
        this.period[0] = start;
        this.period[1] = end;

        CarbonIntensity ci = CarbonIntensity.getInstance();
        this.carbonIntensity = ci.getCarbonIntensity();
    }


    public double getUsageDurationInHoursForAppliance() {
        if (period == null || period.length != 2) return 0.0;

        LocalDateTime start = period[0].toLocalDateTime();
        LocalDateTime end = period[1].toLocalDateTime();

        Duration duration = Duration.between(start, end);
        return duration.toMinutes() / 60.0;
    }


    public double getCarbonFootprint() {calcFootPrint();return this.carbonFootprint;}
    public List<User> getUsers() {
        return users;
    }
    public Appliance getAppliance() {
        return appliance;
    }

    private void calcFootPrint() {
        double usageHours = getUsageDurationInHoursForAppliance();
        double power = appliance.getPowerConsumption();
        double energyUsed = (power * usageHours)/1000;
        this.carbonFootprint = (energyUsed * carbonIntensity) / 1000;
        System.out.println(String.format("Damian: Timeframe: Carbon intensity %f ", this.carbonIntensity));
        System.out.println(String.format("Damian: Timeframe: EnergyUserd %f ", energyUsed));
    }




}