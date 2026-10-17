package se.rydberg.handla.lists;

import java.util.regex.Pattern;

/**
 * Gör om dikterad text från Siri ("Köp mjölk.") till en artikeltitel ("mjölk").
 */
public class VoiceText {
    private static final Pattern TRAILING_PUNCTUATION = Pattern.compile("[.!?,]+$");
    private static final Pattern LEADING_VERB = Pattern.compile("^(lägg till|köpa|köp|buy|add)(\\s+|$)",
            Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);

    public static String toArticleTitle(String text) {
        if (text == null) {
            return "";
        }
        String title = TRAILING_PUNCTUATION.matcher(text.strip()).replaceAll("").strip();
        return LEADING_VERB.matcher(title).replaceFirst("").strip();
    }
}
