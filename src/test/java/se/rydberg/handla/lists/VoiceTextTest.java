package se.rydberg.handla.lists;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class VoiceTextTest {

    @Test
    public void should_strip_swedish_verb_and_punctuation() {
        assertThat(VoiceText.toArticleTitle("Köp mjölk.")).isEqualTo("mjölk");
    }

    @Test
    public void should_strip_english_verb() {
        assertThat(VoiceText.toArticleTitle("Buy milk")).isEqualTo("milk");
    }

    @Test
    public void should_strip_lagg_till() {
        assertThat(VoiceText.toArticleTitle("Lägg till ägg!")).isEqualTo("ägg");
    }

    @Test
    public void should_keep_multi_word_items() {
        assertThat(VoiceText.toArticleTitle("köpa röd lök")).isEqualTo("röd lök");
    }

    @Test
    public void should_not_strip_verb_inside_word() {
        assertThat(VoiceText.toArticleTitle("Köttfärs")).isEqualTo("Köttfärs");
    }

    @Test
    public void should_give_empty_for_only_verb_or_null() {
        assertThat(VoiceText.toArticleTitle("köpa")).isEmpty();
        assertThat(VoiceText.toArticleTitle(null)).isEmpty();
    }
}
