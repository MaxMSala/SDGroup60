package greenhome;

import greenhome.household.House;
import greenhome.household.Parser;
import greenhome.input.Form;
import java.io.IOException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        System.out.println("🌱 Welcome to GreenHome!");
        System.out.println("🔧 Initializing application...");

        // 🔧 STEP 1: Try to load existing house data
        boolean houseLoaded = false;
        try {
            System.out.println("📂 Attempting to load existing house data...");
            Parser.loadHouse("json.json");

            // Check if house was actually created
            if (House.hasInstance()) {
                System.out.println("✅ House data loaded successfully from json.json");
                houseLoaded = true;
            }
        } catch (IOException e) {
            System.out.println("⚠️ Could not load existing house data: " + e.getMessage());
        }

        // 🔧 STEP 2: Ensure we have a House instance (will auto-create if needed)
        House house = House.getInstance();
        System.out.println("🏠 House instance ready: " + (house != null ? "SUCCESS" : "FAILED"));

        if (!houseLoaded) {
            System.out.println("🏗️ Using default house configuration");
        }

        // 🔧 STEP 3: Display current house status (with null safety)
        System.out.println("📊 Current House Status:");
        try {
            System.out.println("   - Region: " + house.getRegion());
            System.out.println("   - Tariff: " + house.getElectricityTariff() + " EUR/kWh");
            System.out.println("   - Users: " + house.getResidents().size());
            System.out.println("   - Appliances: " + house.getAppliances().size());
            System.out.println("   - Timeframes: " + house.getTimeframes().size());
        } catch (Exception e) {
            System.err.println("⚠️ Error displaying house status: " + e.getMessage());
            System.out.println("   - House instance exists but some data may be corrupted");
        }

        // 🔧 STEP 4: Start the Form UI
        System.out.println("🎨 Starting user interface...");
        Form.main(new String[]{});

        System.out.println("✅ Application initialized successfully!");
    }
}