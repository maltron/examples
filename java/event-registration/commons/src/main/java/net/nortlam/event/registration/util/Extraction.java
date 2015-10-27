package net.nortlam.event.registration.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Mauricio "Maltron" Leal <maltron@gmail.com> */
public class Extraction {

    private static final Pattern REGEX_DESIGNATION = Pattern.compile("[A-Za-z]+");
    private static final Pattern REGEX_EDITION = Pattern.compile("\\d+");

    public static String extractDesignation(String value) {
        if(value == null) return null;
        
        Matcher matcher = REGEX_DESIGNATION.matcher(value);
        return matcher.find() ? matcher.group() : null;
    }
    
    public static int extractEdition(String value) {
        if(value == null) return 0;
        
        Matcher matcher = REGEX_EDITION.matcher(value);
        return matcher.find() ? Integer.parseInt(matcher.group()) : 0;
    }
}
