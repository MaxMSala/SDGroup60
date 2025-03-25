package greenhome.household;
//import greenhome.time.Timeframe;

import java.util.*;

public class House {
    String region;
    double tariff;
    String startDateTime;
    String endDateTime;
    public List<User> users = new ArrayList<>();
    public List<Appliance> appliances = new ArrayList<>();
//    public List<Timeframe> timeframes = new ArrayList<>();

    public House(String region, double tariff, String startDateTime, String endDateTime) {
        this.region = region;
        this.tariff = tariff;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }
}