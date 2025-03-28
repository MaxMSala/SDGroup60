package greenhome.apiintegration;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class CI {
    List<Integer> carbonIntensityByHour;
    float averageCarbonIntensity;
    int gramsCO2PerKiloWattHour;

    public CI(List<Integer> carbonIntensityByHour, float averageCarbonIntensity, int gramsCO2PerKiloWattHour) {
        this.carbonIntensityByHour = carbonIntensityByHour;
        this.averageCarbonIntensity = averageCarbonIntensity;
        this.gramsCO2PerKiloWattHour = gramsCO2PerKiloWattHour;
    }

    public int calcAverageCarbonIntensity(int hour) {
        if (carbonIntensityByHour.isEmpty()) {
            return 0;
        }

        int sum = 0;
        for (int intensity : carbonIntensityByHour) {
            sum += intensity;
        }

        return sum / carbonIntensityByHour.size();
    }

    public static int fetchCarbonIntensity() {
        try {
            URL url = new URL("https://api.electricitymap.org/v3/carbon-intensity/latest?zone=NL");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("auth-token", "uywCbhuQ4tOOb0fyL8NI");

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            JSONObject jsonResponse = new JSONObject(response.toString());

            return jsonResponse.getInt("carbonIntensity");
        } catch (Exception e) {
            e.printStackTrace();
            return -1; // Error case
        }
    }

    // Represents one hour of carbon intensity data
    public static class CarbonHour {
        public int carbonIntensity;
        public String datetime;

        public CarbonHour(int intensity, String dt) {
            this.carbonIntensity = intensity;
            this.datetime = dt;
        }
    }

    // Fetches the full 24-hour carbon intensity history
    public static List<CarbonHour> fetchCarbonIntensityHistory() {
        List<CarbonHour> history = new ArrayList<>();
        try {
            URL url = new URL("https://api.electricitymap.org/v3/carbon-intensity/history?zone=NL");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("auth-token", "uywCbhuQ4tOOb0fyL8NI");

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            JSONObject json = new JSONObject(response.toString());
            JSONArray historyArr = json.getJSONArray("history");


            for (int i = 0; i < historyArr.length(); i++) {
                JSONObject item = historyArr.getJSONObject(i);
                int ci = item.getInt("carbonIntensity");
                String dt = item.getString("datetime");
                history.add(new CarbonHour(ci, dt));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return history;
    }

    public static String findBestLowCarbonTimeRange() {
        List<CarbonHour> history = fetchCarbonIntensityHistory();
        if (history.size() < 3) return "Not enough data";

        int minSum = Integer.MAX_VALUE;
        int maxSum = Integer.MIN_VALUE;
        int bestStart = 0;
        int worstStart = 0;

        for (int i = 0; i <= history.size() - 3; i++) {
            int sum = history.get(i).carbonIntensity +
                    history.get(i + 1).carbonIntensity +
                    history.get(i + 2).carbonIntensity;

            if (sum < minSum) {
                minSum = sum;
                bestStart = i;
            }

            if (sum > maxSum) {
                maxSum = sum;
                worstStart = i;
            }
        }

        // BEST
        CarbonHour b1 = history.get(bestStart);
        CarbonHour b2 = history.get(bestStart + 1);
        CarbonHour b3 = history.get(bestStart + 2);
        String bestStartTime = formatTime(b1.datetime);
        String bestEndTime = formatTime(b3.datetime);
        int bCI1 = b1.carbonIntensity, bCI2 = b2.carbonIntensity, bCI3 = b3.carbonIntensity;
        double bestAvg = (bCI1 + bCI2 + bCI3) / 3.0;

        // WORST
        CarbonHour w1 = history.get(worstStart);
        CarbonHour w2 = history.get(worstStart + 1);
        CarbonHour w3 = history.get(worstStart + 2);
        String worstStartTime = formatTime(w1.datetime);
        String worstEndTime = formatTime(w3.datetime);
        int wCI1 = w1.carbonIntensity, wCI2 = w2.carbonIntensity, wCI3 = w3.carbonIntensity;
        double worstAvg = (wCI1 + wCI2 + wCI3) / 3.0;

        return String.format(
                "\uD83D\uDCC9 Off-Peak Hours in \uD83C\uDDF3\uD83C\uDDF1:\n" +
                "   •  Ideal time for using high-energy appliances is between %s and %s.\n" +
                        "   •  Carbon intensity values: [%d, %d, %d] gCO2/kWh\n" +
                        "   •  Average carbon intensity: %.2f gCO2/kWh.\n\n" +

                        "\uD83D\uDCC8 Peak Hours in \uD83C\uDDF3\uD83C\uDDF1:\n" +
                        "   •  Worst time to use appliance is between %s and %s.\n" +
                        "   •  Carbon intensity values: [%d, %d, %d] gCO2/kWh\n" +
                        "   •  Average carbon intensity: %.2f gCO2/kWh.\n\n",

                bestStartTime, bestEndTime, bCI1, bCI2, bCI3, bestAvg,
                worstStartTime, worstEndTime, wCI1, wCI2, wCI3, worstAvg
        );
    }



    // Format ISO time to readable local hour:minute
    private static String formatTime(String isoDatetime) {
        ZonedDateTime zdt = ZonedDateTime.parse(isoDatetime);
        return zdt.toLocalTime().withSecond(0).withNano(0).toString();
    }

}

