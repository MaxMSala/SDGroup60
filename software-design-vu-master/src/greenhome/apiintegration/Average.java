package greenhome.apiintegration;

public class Average {
    float averageEmissionsPerCap;
    float tonnesCO2eq;

    public void Averages(float averageEmissionsPerCap, float tonnesCO2eq) {
        this.averageEmissionsPerCap = averageEmissionsPerCap;
        this.tonnesCO2eq = tonnesCO2eq;
    }

    public float getAverageEmissionsPerCap() {
        return averageEmissionsPerCap;
    }

    public void fetchAverage() {
        // Fetch average emissions per capita (mock data for now)
        this.averageEmissionsPerCap = 4.5f;
    }
}