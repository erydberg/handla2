package se.rydberg.handla.lists;

import org.junit.jupiter.api.Test;
import org.owasp.encoder.Encode;

import static org.assertj.core.api.Assertions.assertThat;

class ReturnViewValidatorTest {

    @Test
    public void shouldValidateCorrectReturnView(){
        assertThat(ReturnViewValidator.validate("/lists")).isTrue();
    }

    @Test
    public void shouldValidateFalseReturnView(){
        assertThat(ReturnViewValidator.validate("/evilUrl")).isFalse();
    }

    @Test
    public void shouldValidateEmptyReturnView(){
        assertThat(ReturnViewValidator.validate("")).isFalse();
    }

    @Test
    public void shouldValidateNullReturnView(){
        assertThat(ReturnViewValidator.validate(null)).isFalse();
    }
}