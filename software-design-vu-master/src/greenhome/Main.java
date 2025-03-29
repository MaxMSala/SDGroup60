package greenhome;//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.

import greenhome.household.House;
import greenhome.household.Parser;
import greenhome.input.Form;
import greenhome.reporting.Report;

public class Main {
    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        System.out.println("Welcome to GreenHome!");
        Form.main(new String[]{});
        Parser.loadHouse();
        Report.main(new String[]{});
        //System.out.println(CI.fetchCarbonIntensity());


    }
}