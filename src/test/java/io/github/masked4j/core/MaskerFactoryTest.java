package io.github.masked4j.core;

import io.github.masked4j.Masker;
import io.github.masked4j.annotation.MaskType;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class MaskerFactoryTest {

    @Test
    void shouldReturnSameInstanceForSameType() {
        Masker masker1 = MaskerFactory.getMasker(MaskType.EMAIL);
        Masker masker2 = MaskerFactory.getMasker(MaskType.EMAIL);

        assertThat(masker1).isSameAs(masker2);
    }

    @Test
    void shouldReturnCorrectMaskerImplementation() {
        Masker emailMasker = MaskerFactory.getMasker(MaskType.EMAIL);
        assertThat(emailMasker).isInstanceOf(EmailMasker.class);

        Masker defaultMasker = MaskerFactory.getMasker(MaskType.STRING);
        assertThat(defaultMasker).isInstanceOf(DefaultStringMasker.class);
    }
}
