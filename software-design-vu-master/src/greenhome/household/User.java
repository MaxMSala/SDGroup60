package greenhome.household;

public class User {
    String name;
    int ecoScore;
    float carbonFootprint = 0;
    float cost;

    public User(String name) {
        this.name = name;
    }

    //public float getCarbonFootprint (TimeFrame[] timeFrames){
    //    for (int i = 0; i < timeFrames.length; i++) {
    //        if (timeFrames[i].User.name == this.name); {
    //            this.carbonFootprint += timeFrames[i].carbonFootprint;
    //        }
    //    }
    //}
}