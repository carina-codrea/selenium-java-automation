package codrea.utility;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DateUtils {
    public static String getCurrentFormattedDate(){
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("E MMM dd", Locale.ENGLISH);

        return formatter.format(currentDate);
    }
}
