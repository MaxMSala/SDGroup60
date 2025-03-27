package greenhome.household;

import greenhome.datavalidation.*;
import greenhome.time.Timeframe;
import java.util.Set;


public class House {


    // Aggregation (1..*) with User
    private Set<User> residents;

    private double electricityTariff;

    // Composition (1..*) with Appliance
    private Set<Appliance> appliances;

    private Set<Timeframe> timeframes;

    // derived attributes ("/" prefix)
    private int ecoScore;
    private double footPrint;
   // private double costsGenerated = calcCost();

    // Internal attributes
    private double euroPerKiloWattHour;
    private double tonnesCO2eq;

    // Methods
    private int calcEcoScore() {
        // to be implemented
        return 0;
    }

  //  public double calcCost() {
  //      double totalCost = 0.0;
  //
  //      for (Appliance appliance : appliances) {
  //          totalCost += appliance.calcCost(this.electricityTariff, this.timeframes);
  //      }
  //
  //      this.costsGenerated = totalCost;
  //      return totalCost;
  //  }
  //
    private double sumFootPrint() {
        // to be implemented
        return 0.0;
    }

    // Getters & Setters can be added later as needed
   // public void setAppliances(Set<Appliance> appliances) {
   //     this.appliances = appliances;
   // }
   //
   // public void setElectricityTariff(double tariff) {
   //     this.electricityTariff = tariff;
   // }
   //
   // public void setEcoScore(int ecoScore) {
   //     this.ecoScore = ecoScore;
   // }
   //
   // public void setFootPrint(double footprint) {
   //     this.footPrint = footprint;
   // }
    public double getFootPrint() {
        return footPrint;
    }

    public Set<Appliance> getAppliances() {
        return appliances;
    }

    public int getEcoScore() {
        return ecoScore;
    }

    public double getElectricityTariff() {
        return electricityTariff;
    }
    public double getTariff(){
        return euroPerKiloWattHour;
    }
    public Set<Timeframe> getTimeframes() {
        return timeframes;
    }
    private static final House instance = new House();

    public static House getInstance() {
        return instance;
    }
    public double getTonnesCO2eq() {
        return tonnesCO2eq;
    }

    public Set<User> getResidents() {
        return residents;
    }
}