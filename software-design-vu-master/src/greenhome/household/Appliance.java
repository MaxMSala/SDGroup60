package greenhome.household;

public class Appliance {
    String name;
    int powerConsumption;
    int embodiedEmission;

    public Appliance(String name, int powerConsumption, int embodiedEmission) {
        this.name = name;
        this.powerConsumption = powerConsumption;
        this.embodiedEmission = embodiedEmission;
    }
}