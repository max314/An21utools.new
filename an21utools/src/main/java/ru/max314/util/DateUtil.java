package ru.max314.util;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * Created by max on 11.01.2015.
 */
public class DateUtil {

    /**
     * Смежение в милисекундах от UTC
     * @return
     */
    public static int getCurrentTimezoneOffset() {

        TimeZone tz = TimeZone.getDefault();
        Calendar cal = GregorianCalendar.getInstance(tz);
        int offsetInMillis = tz.getOffset(cal.getTimeInMillis());
//
//        String offset = String.format("%02d:%02d", Math.abs(offsetInMillis / 3600000), Math.abs((offsetInMillis / 60000) % 60));
//        offset = (offsetInMillis >= 0 ? "+" : "-") + offset;

        return offsetInMillis;
    }
}
