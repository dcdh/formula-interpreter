package com.damdamdeo.formula.domain;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

public class ValueTest {

    public static final Value SIX_SIX_ZERO = Value.ofNumeric("660");
    public static final Value TWO_SIX_ZERO = Value.ofNumeric("260");
    public static final Value AZERTY = Value.ofText("azerty");

    @Test
    void shouldVerifyEqualsVerifier() {
        EqualsVerifier.forClass(Value.class).verify();
    }

    @Test
    void shouldOfNotAvailableReturnExpectedRepresentation() {
        assertThat(Value.ofNotAvailable().value()).isEqualTo("#NA!");
    }

    @Test
    void shouldOfUnknownRefReturnExpectedRepresentation() {
        assertThat(Value.ofUnknownRef().value()).isEqualTo("#REF!");
    }

    @Test
    void shouldOfNotANumericalValueReturnRepresentation() {
        assertThat(Value.ofNotANumericalValue().value()).isEqualTo("#NUM!");
    }

    @Test
    void shouldOfDividedByZeroReturnExpectedRepresentation() {
        assertThat(Value.ofDividedByZero().value()).isEqualTo("#DIV/0!");
    }

    @Test
    void shouldOfTrueReturnExpectedRepresentation() {
        assertThat(Value.ofTrue().value()).isEqualTo("true");
    }

    @Test
    void shouldOfZeroReturnExpectedRepresentation() {
        assertThat(Value.ofZero().value()).isEqualTo("0");
    }

    @Test
    void shouldOfFalseReturnExpectedRepresentation() {
        assertThat(Value.ofFalse().value()).isEqualTo("false");
    }

    @Test
    void shouldOfOneReturnExpectedRepresentation() {
        assertThat(Value.ofOne().value()).isEqualTo("1");
    }

    @Test
    void shouldOfEmptyReturnExpectedRepresentation() {
        assertThat(Value.ofEmpty().value()).isEqualTo("");
    }

    @Test
    void shouldOfNotALogicalValueReturnRepresentation() {
        assertThat(Value.ofNotALogicalValue().value()).isEqualTo("#LOG!");
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
            assertThat(Value.ofAny(givenValue).isNumeric()).isTrue();
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
            assertThat(Value.ofAny(givenValue).isNumeric()).isFalse();
        }
    }

}