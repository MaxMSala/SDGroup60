import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;
import java.util.List;

class CI {
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
            System.out.println(jsonResponse);
            return jsonResponse.getInt("carbonIntensity");
        } catch (Exception e) {
            e.printStackTrace();
            return -1; // Error case
        }
    }
}
