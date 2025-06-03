package greenhome.apiintegration;

import greenhome.time.DateTime;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Average {
    float averageEmissionsPerCap;
    float tonnesCO2eq;

    public void Averages(float averageEmissionsPerCap, float tonnesCO2eq) {
        this.averageEmissionsPerCap = averageEmissionsPerCap;
        this.tonnesCO2eq = tonnesCO2eq;
    }

    public double getAverageEmissionsPerCap(DateTime start, DateTime end) {
        LocalDateTime startDateTime = start.toLocalDateTime();
        LocalDateTime endDateTime = end.toLocalDateTime();

        long hoursBetween = ChronoUnit.HOURS.between(startDateTime, endDateTime);
        double hoursInYear = 365.0f * 24.0f;

        double adjusted = averageEmissionsPerCap * (hoursBetween / hoursInYear);

        return adjusted;
    }


    public void fetchAverage() {
        // Fetch average emissions per capita (mock data for now)
        this.averageEmissionsPerCap = 4.5f;
    }
}