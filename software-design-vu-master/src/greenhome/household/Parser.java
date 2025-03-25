package greenhome.household;
import greenhome.datavalidation.*;
import greenhome.time.DateTime;

import java.util.List;
import java.util.Set;

public class Parser {

    // Attributes
    private DateTime[] period = new DateTime[2]; // Ordered, Unique — so List
    private Set<String> userList;  // Unique, Unordered — Set
    private Set<String> timeFrameList; // Unique, Unordered — Set
    private List<String> applianceList; // NonUnique, Unordered — List
    private double electricityTariff;
    private String region;

    // Association with House (1 to 1)
    private House house;

    // Constructor
    public Parser() {
        // default constructor or overload as needed
    }

    // Methods
    public void convertFromJson() {
        // to be implemented
    }

    public String convertToJson() {
        // to be implemented
        return "";
    }

    // Optional getter/setter for house if needed
    public House getHouse() {
        return house;
    }

    public void setHouse(House house) {
        this.house = house;
    }
}