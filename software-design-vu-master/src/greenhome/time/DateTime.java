package greenhome.time;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class DateTime {
    int day, month, year, hour, minute;

    public static List<Integer> stringToVals(String string) {

        string = string.split(": ")[string.split(": ").length-1];
        List<Integer> vals = new ArrayList<>(){{add(0);add(0);add(0);add(0);add(0);}};

        vals.set(2, Integer.valueOf(string.split("-")[0]));
        vals.set(1, Integer.valueOf(string.split("-")[1]));

        vals.set(0, Integer.valueOf(string.split("-")[2].split(" ")[0]));

        vals.set(3, Integer.valueOf(string.split("-")[2].split(" ")[1].split(":")[0]));

        vals.set(4, Integer.valueOf(string.split("-")[2].split(" ")[1].split(":")[1].split("\n")[0].replace(",","")));

        return vals;
    }

    public static DateTime parseDate(String string) {

        String string1 = new String(string.split(" ")[0]);
        String string2 = new String(string.split(" ")[1]);

        DateTime date = new DateTime(Integer.valueOf(string1.split("-")[2]),Integer.valueOf(string1.split("-")[1]),Integer.valueOf(string1.split("-")[0]),Integer.valueOf(string2.split(":")[0]),Integer.valueOf(string2.split(":")[1]));
        return date;
    }

    public DateTime(int day, int month, int year, int hour, int minute) {
        this.day = day;
        this.month = month;
        this.year = year;
        this.hour = hour;
        this.minute = minute;
    }

    public static int daysBetween(DateTime start, DateTime end) {
        LocalDateTime startDateTime = start.toLocalDateTime();
        LocalDateTime endDateTime = end.toLocalDateTime();
        return (int) ChronoUnit.DAYS.between(startDateTime, endDateTime) + 1;
    }


    public LocalDateTime toLocalDateTime() {
        return LocalDateTime.of(year, month, day, hour, minute);
    }


    public String toSlashString() {
        return String.format("%02d/%02d/%04d %02d:%02d", day, month, year, hour, minute);
    }


    public int getYear(){return this.year;}
    public int getMonth(){return this.month;}
    public int getDay(){return this.day;}
    public int getHour(){return this.hour;}
    public int getMinute(){return this.minute;}

}