package se.rydberg.handla.lists;

import org.thymeleaf.util.StringUtils;

public class Sanitize {

    public static String setCapitalFirstLetter(String message) {
        if (message != null) {
            return StringUtils.capitalize(message.trim());
        } else {
            return "";
        }
    }
}
