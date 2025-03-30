package greenhome.household;

public class User {

    private String name;

    // derived
    private int ecoScore;
    private double carbonFootprint;
    private double costsGenerated;

    public User(String name) {
        this.name = name;
    }

    // interface getters
    public String getName() {return this.name;}
    public double getCostsGenerated(){ calcCost(); return costsGenerated;}
    public int getEcoScore() {calcEcoScore(); return ecoScore;}
    public double getCarbonFootprint() {sumFootPrint(); return carbonFootprint;}


    // internal calculations
    private void calcCost() {
        double totalCost = 0.0;
        House house = House.getInstance();

        for (Timeframe tf : house.getTimeframes()) {
            for (User user : tf.getUsers()) {
                if(user.getName().equals(this.name)) {
                    double usageHours = tf.getUsageDurationInHoursForAppliance();
                    double usageHoursAdjusted = usageHours / tf.getUsers().size();
                    double elecTariff = house.getElectricityTariff();

                    totalCost += usageHoursAdjusted * elecTariff;
                }
            }
        }
        this.costsGenerated = totalCost;

    }

    private void sumFootPrint (){
        double totalFootPrint = 0.0;
        House house = House.getInstance();

        for (Timeframe tf : house.getTimeframes()) {
            for (User user : tf.getUsers()) {
                if(user.getName().equals(this.name)) {
                    totalFootPrint += (tf.getCarbonFootprint() / tf.getUsers().size());
                }
            }
        }
        this.carbonFootprint = totalFootPrint;
    }

    private void calcEcoScore() {
        double ecoScore = 0.0;
        House h = House.getInstance();
        double totalHours = 0.0;
        for (Timeframe tf : h.getTimeframes()) {
            for (User user : tf.getUsers()) {
                if(user.getName().equals(this.name)) {
                    double usageHours = tf.getUsageDurationInHoursForAppliance();
                    double usageHoursAdjusted = usageHours / tf.getUsers().size();

                    totalHours += usageHoursAdjusted;
                }
            }
        }
        double normalized = getCarbonFootprint() / totalHours;
        ecoScore = 100 - Math.min(100, Math.log1p(normalized) * 10);


        this.ecoScore = (int) ecoScore;

    }

}