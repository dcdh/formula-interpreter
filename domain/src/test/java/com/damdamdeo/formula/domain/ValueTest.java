package com.damdamdeo.formula.domain;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class ValueTest {

    @Test
    void shouldVerifyEqualsVerifier() {
        EqualsVerifier.forClass(Value.class).verify();
    }

    @Test
    void shouldOfNotAvailableReturnExpectedRepresentation() {
        assertThat(Value.ofNotAvailable()).isEqualTo(new Value("#NA!"));
    }

    @Test
    void shouldOfUnknownRefReturnExpectedRepresentation() {
        assertThat(Value.ofUnknownRef()).isEqualTo(new Value("#REF!"));
    }

    @Test
    void shouldOfNotANumericalValueReturnRepresentation() {
        assertThat(Value.ofNotANumericalValue()).isEqualTo(new Value("#NUM!"));
    }

    @Test
    void shouldOfDividedByZeroReturnExpectedRepresentation() {
        assertThat(Value.ofDividedByZero()).isEqualTo(new Value("#DIV/0!"));
    }

    @Test
    void shouldOfTrueReturnExpectedRepresentation() {
        assertThat(Value.ofTrue()).isEqualTo(new Value("true"));
    }

    @Test
    void shouldOfZeroReturnExpectedRepresentation() {
        assertThat(Value.ofZero()).isEqualTo(new Value("0"));
    }

    @Test
    void shouldOfFalseReturnExpectedRepresentation() {
        assertThat(Value.ofFalse()).isEqualTo(new Value("false"));
    }

    @Test
    void shouldOfOneReturnExpectedRepresentation() {
        assertThat(Value.ofOne()).isEqualTo(new Value("1"));
    }

    @Test
    void shouldOfEmptyReturnExpectedRepresentation() {
        assertThat(Value.ofEmpty()).isEqualTo(new Value(""));
    }

    @Test
    void shouldOfNotALogicalValueReturnRepresentation() {
        assertThat(Value.ofNotALogicalValue()).isEqualTo(new Value("#LOG!"));
    }

    // TODO should use a provider
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