package greenhome.household;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import greenhome.time.DateTime;
import org.json.JSONArray;
import org.json.JSONObject;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class CarbonIntensity {

    private static CarbonIntensity instance;
    private DateTime[] period = new DateTime[2];


    private double carbonIntensity;

    private CarbonIntensity() {
        House house = House.getInstance();
        this.period = new DateTime[] {house.getStart(), house.getEnd()};
    }


    public static synchronized CarbonIntensity getInstance() {
        if (instance == null) {
            instance = new CarbonIntensity();
        }
        return instance;
    }

    public double getCarbonIntensity() {updateInternalCarbonIntensity(); return carbonIntensity;}


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


        CarbonHour b1 = history.get(bestStart);
        CarbonHour b2 = history.get(bestStart + 1);
        CarbonHour b3 = history.get(bestStart + 2);
        String bestStartTime = formatTime(b1.datetime);
        String bestEndTime = formatTime(b3.datetime);
        int bCI1 = b1.carbonIntensity, bCI2 = b2.carbonIntensity, bCI3 = b3.carbonIntensity;
        double bestAvg = (bCI1 + bCI2 + bCI3) / 3.0;


        CarbonHour w1 = history.get(worstStart);
        CarbonHour w2 = history.get(worstStart + 1);
        CarbonHour w3 = history.get(worstStart + 2);
        String worstStartTime = formatTime(w1.datetime);
        String worstEndTime = formatTime(w3.datetime);
        int wCI1 = w1.carbonIntensity, wCI2 = w2.carbonIntensity, wCI3 = w3.carbonIntensity;
        double worstAvg = (wCI1 + wCI2 + wCI3) / 3.0;

        return String.format("""
        Off-Peak Hours in NL:
           •  Ideal time for using high-energy appliances is between %s and %s.
           •  Carbon intensity values: [%d, %d, %d] gCO2/kWh
           •  Average carbon intensity: %.2f gCO2/kWh.

        Peak Hours in NL:
           •  Worst time to use appliance is between %s and %s.
           •  Carbon intensity values: [%d, %d, %d] gCO2/kWh
           •  Average carbon intensity: %.2f gCO2/kWh.

        """,
                bestStartTime, bestEndTime, bCI1, bCI2, bCI3, bestAvg,
                worstStartTime, worstEndTime, wCI1, wCI2, wCI3, worstAvg
        );

    }



    private void updateInternalCarbonIntensity() {
        List<CarbonHour> history = fetchCarbonIntensityHistory();

        if (history.size() != 24) {
            System.out.println(" Not enough data to fetch 24-hour carbon history.");
            return;
        }


        int startHour = this.period[0].toLocalDateTime().getHour();
        int endHour = this.period[1].toLocalDateTime().getHour();

        int startCI = history.get(startHour).carbonIntensity;
        int endCI = history.get(endHour).carbonIntensity;

        double avgCI = (startCI + endCI) / 2.0;
        this.carbonIntensity = avgCI;
    }


    private static String formatTime(String isoDatetime) {
        ZonedDateTime zdt = ZonedDateTime.parse(isoDatetime);
        return zdt.toLocalTime().withSecond(0).withNano(0).toString();
    }


    private static List<CarbonHour> fetchCarbonIntensityHistory() {
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


    private static class CarbonHour {
        public int carbonIntensity;
        public String datetime;

        public CarbonHour(int intensity, String dt) {
            this.carbonIntensity = intensity;
            this.datetime = dt;
        }
    }

}

