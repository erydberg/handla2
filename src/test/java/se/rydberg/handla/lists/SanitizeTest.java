package se.rydberg.handla.lists;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SanitizeTest {

    @Test
    public void should_capitalize_and_trim_word(){
        assertThat(Sanitize.setCapitalFirstLetter(" erik ")).isEqualTo("Erik");
    }

    @Test
    public void should_capitalize_and_trim_sentence(){
        assertThat(Sanitize.setCapitalFirstLetter(" erik är the creator ")).isEqualTo("Erik är the creator");
    }

    @Test
    public void should_work_with_empty_word(){
        assertThat(Sanitize.setCapitalFirstLetter("")).isEmpty();
    }

    @Test
    public void should_work_with_null(){
        assertThat(Sanitize.setCapitalFirstLetter(null)).isEmpty();
    }
}
