package greenhome.household;

import java.util.*;

public class User {

    private String name;


    private int ecoScore;
    private double carbonFootprint;

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof User)) return false;
        User other = (User) obj;
        return name.equals(other.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
    public User(String name) {
        this.name = name;
    }


    public String getName() {return this.name;}
    public int getEcoScore() {calcEcoScore(); return ecoScore;}
    public double getCarbonFootprint() {sumFootPrint(); return carbonFootprint;}


    private void sumFootPrint() {
        double totalFootPrint = 0.0;
        House house = House.getInstance();

        for (Timeframe tf : house.getTimeframes()) {
            Set<String> uniqueUserNames = new HashSet<>();
            for (User user : tf.getUsers()) {
                uniqueUserNames.add(user.getName());
            }


            if (uniqueUserNames.contains(this.name)) {
                totalFootPrint += (tf.getCarbonFootprint() / uniqueUserNames.size());
            }
        }

        this.carbonFootprint = totalFootPrint;
    }



    private void calcEcoScore() {
        House house = House.getInstance();


        Map<String, Double> uniqueUserFootprints = new HashMap<>();

        for (User user : house.getResidents()) {
            if (user != null) {
                String name = user.getName();
                double cf = user.getCarbonFootprint();
                if (cf > 0 && !uniqueUserFootprints.containsKey(name)) {
                    uniqueUserFootprints.put(name, cf);
                }
            }
        }

        double totalContributingFootprint = uniqueUserFootprints.values().stream().mapToDouble(Double::doubleValue).sum();
        double myFootprint = getCarbonFootprint();

        if (totalContributingFootprint == 0.0) {
            this.ecoScore = 100;
            return;
        }

        if (uniqueUserFootprints.size() == 1) {

            String soleContributor = uniqueUserFootprints.keySet().iterator().next();

            if (this.name.equals(soleContributor)) {
                this.ecoScore = 80;
            } else {
                this.ecoScore = 100;
            }
            return;
        }

        double myShare = myFootprint / totalContributingFootprint;
        double score = 100 - (myShare * 100);

        this.ecoScore = (int) Math.round(score);
    }


}