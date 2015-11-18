package net.nortlam.event.registration.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Mauricio "Maltron" Leal <maltron@gmail.com> */
public class DateUtil {
    
    public static final SimpleDateFormat PATTERN = 
                                      new SimpleDateFormat("yyyyMMdd HHmmss z");
    
    public static String toString(Calendar calendar) {
        return toString(calendar.getTime());
    }
    
    public static String toString(Date date) {
        return PATTERN.format(date);
    }
    
    public static Date toDate(String content) throws ParseException {
        return PATTERN.parse(content);
    }

}
