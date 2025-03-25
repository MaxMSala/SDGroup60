//package greenhome.time;
//import greenhome.household.User;
//import greenhome.household.Appliance;
//
//public class Timeframe {
//    int count;
//    User user;
//    Appliance appliance;
//    DateTime[] period = new DateTime[2];
//    double averageCarbonIntensity;
//    double carbonFootprint;
//    double gramsCO2PerKiloWattHour;
//    double tonnesCO2eq;
//
//    public Timeframe(User user, Appliance appliance, DateTime start, DateTime end, double averageCarbonIntensity, double gramsCO2PerKiloWattHour) {
//        this.user = user;
//        this.appliance = appliance;
//        this.period[0] = start;
//        this.period[1] = end;
//        this.averageCarbonIntensity = averageCarbonIntensity;
//        this.gramsCO2PerKiloWattHour = gramsCO2PerKiloWattHour;
//    }
//
//    public void calcFootPrint() {
//        this.carbonFootprint = (appliance.kiloWattHours * this.gramsCO2PerKiloWattHour / 1000);
//    }
//}