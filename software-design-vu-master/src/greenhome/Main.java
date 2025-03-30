package greenhome;//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.

import greenhome.household.House;
import greenhome.household.Parser;
import greenhome.input.Form;
import greenhome.reporting.Report;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        System.out.println("Welcome to GreenHome!");
        House.constructInstance(new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), "", 0.0);
        //Parser.loadHouse();
        Form.main(new String[]{});
        System.out.println("saveHouse");
        //System.out.println(CI.fetchCarbonIntensity());
    }
}