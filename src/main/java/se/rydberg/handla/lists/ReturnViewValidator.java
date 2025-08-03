package se.rydberg.handla.lists;

import java.util.Arrays;

public class ReturnViewValidator {

    private static String[] whiteListReturnViews = {
            "/menu/favorites",
            "/lists",
            "/menu/history",
            "/menu/planned",
            "/menu/neveragain",
            "/menu/unplanned"};

    public static boolean validate(String returnview) {
        return returnview!=null ? Arrays.stream(whiteListReturnViews).anyMatch(returnview::equals) : false;
    }
}
