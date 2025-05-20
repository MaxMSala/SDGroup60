package greenhome;//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.

import greenhome.household.House;
import greenhome.household.Parser;
import greenhome.input.Form;
import greenhome.reporting.Report;

import java.io.IOException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it
                System.out.println("Welcome to GreenHome!");

                // Load the house (will create instance or fail)
                try {
                    Parser.loadHouse("json.json");
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // Fallback: if house was not created in loadHouse(), create a default one
                if (House.getInstance() == null) {
                    System.out.println("No house loaded from JSON. Creating empty instance.");
                    House.constructInstance(new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), "", 0.0);
                }

                System.out.println("House instance: " + House.getInstance());

                Form.main(new String[]{});

                System.out.println("saveHouse");
            }
}