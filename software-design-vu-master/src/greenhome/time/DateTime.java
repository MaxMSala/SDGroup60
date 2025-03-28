package greenhome.time;
import greenhome.datavalidation.*;
import greenhome.apiintegration.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DateTime {
    int day, month, year, hour, minute;

    public static List<Integer> stringToVals(String string) {
        List<Integer> vals = new ArrayList<>(){{add(0);add(0);add(0);add(0);add(0);}};
        vals.set(2, Integer.valueOf(string.split(": ")[1].split("-")[0]));
        vals.set(1, Integer.valueOf(string.split(": ")[1].split("-")[1]));
        vals.set(0, Integer.valueOf(string.split(": ")[1].split("-")[2].split(" ")[0]));
        vals.set(3, Integer.valueOf(string.split(": ")[1].split("-")[2].split(" ")[1].split(":")[0]));
        vals.set(4, Integer.valueOf(string.split(": ")[1].split("-")[2].split(" ")[1].split(":")[1]));
        return vals;
    }

    public DateTime(int day, int month, int year, int hour, int minute) {
        this.day = day;
        this.month = month;
        this.year = year;
        this.hour = hour;
        this.minute = minute;
    }

    public LocalDateTime toLocalDateTime() {
        return LocalDateTime.of(year, month, day, hour, minute);
    }

}