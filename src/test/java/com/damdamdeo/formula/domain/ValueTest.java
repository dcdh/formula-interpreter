package com.damdamdeo.formula.domain;

import com.damdamdeo.formula.domain.Value;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class ValueTest {
    @Nested
    class IsNumeric {

        @ParameterizedTest
        @CsvSource({
                "0",
                "0.00",
                "123",
                "-123",
                "1.23E3",
                "1.23E+3",
                "12.3E+7",
                "12.0",
                "12.3",
                "0.00123",
                "-1.23E-12",
                "1234.5E-4",
                "0E+7",
                "-0"
        })
        public void shouldBeANumeric(final String givenValue) {
            assertThat(new Value(givenValue).isNumeric()).isTrue();
        }

        @ParameterizedTest
        @CsvSource({
                "#VALUE!",
                "#REF!",
                "TRUE",
                "FALSE",
                "AZERTY",
                "Hello World"
        })
        public void shouldNotBeANumeric(final String givenValue) {
            assertThat(new Value(givenValue).isNumeric()).isFalse();
        }
    }

}