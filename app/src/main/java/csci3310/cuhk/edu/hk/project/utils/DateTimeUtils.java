package csci3310.cuhk.edu.hk.project.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateTimeUtils {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

    public static String getString(Date date) {
        if (date == null) {
            date = new Date();
        }
        return dateFormat.format(date);
    }
}
