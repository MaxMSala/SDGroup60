class Timeframe {
    int count;
    User user;
    Appliance appliance;
    Datetime[] period = new Datetime[2];
    float averageCarbonIntensity;
    float carbonFootprint;
    float gramsCO2PerKiloWattHour;
    float tonnesCO2eq;

    public Timeframe(User user, Appliance appliance, Datetime start, Datetime end, float averageCarbonIntensity, float gramsCO2PerKiloWattHour) {
        this.user = user;
        this.appliance = appliance;
        this.period[0] = start;
        this.period[1] = end;
        this.averageCarbonIntensity = averageCarbonIntensity;
        this.gramsCO2PerKiloWattHour = gramsCO2PerKiloWattHour;
    }

    public void calcFootPrint() {
        this.carbonFootprint = appliance.embodiedEmissions + (appliance.kiloWattHours * this.gramsCO2PerKiloWattHour / 1000);
    }
}