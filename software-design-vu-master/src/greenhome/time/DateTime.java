package greenhome.time;
import greenhome.datavalidation.*;
import greenhome.apiintegration.*;
import java.time.LocalDateTime;

public class DateTime {
    int day, month, year, hour, minute;

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