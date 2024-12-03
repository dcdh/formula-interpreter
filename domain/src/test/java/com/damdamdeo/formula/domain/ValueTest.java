package com.damdamdeo.formula.domain;

import com.damdamdeo.formula.domain.provider.GivenValue;
import com.damdamdeo.formula.domain.provider.ValueArgumentsProvider;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class ValueTest {

    public static final Value SIX_SIX_ZERO = Value.ofNumeric("660");
    public static final Value TWO_SIX_ZERO = Value.ofNumeric("260");
    public static final Value AZERTY = Value.ofText("\"azerty\"");

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

    @ValueArgumentsProvider.TextTest
    void shouldBeAText(final GivenValue givenValue) {
        assertThat(givenValue.value().isText()).isTrue();
    }

    @ValueArgumentsProvider.NumericTest
    void shouldBeANumeric(final GivenValue givenValue) {
        assertThat(givenValue.value().isNumeric()).isTrue();
    }

    @ValueArgumentsProvider.BooleanTrueTest
    void shouldBeABooleanTrue(final GivenValue givenValue) {
        assertAll(
                () -> assertThat(givenValue.value().isBoolean()).isTrue(),
                () -> assertThat(givenValue.value().isTrue()).isTrue()
        );
    }

    @ValueArgumentsProvider.BooleanFalseTest
    void shouldBeABooleanFalse(final GivenValue givenValue) {
        assertAll(
                () -> assertThat(givenValue.value().isBoolean()).isTrue(),
                () -> assertThat(givenValue.value().isFalse()).isTrue()
        );
    }

    @ValueArgumentsProvider.StructuredReferenceResolvedTextTest
    void shouldResolveStructuredReferenceText(final GivenValue givenValue) {
        assertAll(
                () -> assertThat(givenValue.value().isText()).isTrue(),
                () -> assertThat(givenValue.value().value()).isEqualTo("Joe")
        );
    }

    @ValueArgumentsProvider.StructuredReferenceResolvedNumericTest
    void shouldReturnUnknownRefWhenStructuredReferenceNumeric(final GivenValue givenValue) {
        assertAll(
                () -> assertThat(givenValue.value().isNumeric()).isTrue(),
                () -> assertThat(givenValue.value().value()).isEqualTo("0.10")
        );
    }

    @ValueArgumentsProvider.StructuredReferenceResolvedBooleanTest
    void shouldReturnUnknownRefWhenStructuredReferenceBoolean(final GivenValue givenValue) {
        assertAll(
                () -> assertThat(givenValue.value().isBoolean()).isTrue(),
                () -> assertThat(givenValue.value().value()).isEqualTo("true")
        );
    }

    @ValueArgumentsProvider.StructuredReferenceUnknownTest
    void shouldReturnUnknownRefWhenStructuredReferenceIsNotPresent(final GivenValue givenValue) {
        assertThat(givenValue.value().isUnknownRef()).isTrue();
    }

    @ValueArgumentsProvider.NotAvailableTest
    void shouldBeNotAvailable(final GivenValue givenValue) {
        assertAll(
                () -> assertThat(givenValue.value().isNotAvailable()).isTrue(),
                () -> assertThat(givenValue.value().value()).isEqualTo("#NA!")
        );
    }

    @ValueArgumentsProvider.UnknownRefTest
    void shouldBeUnknownRef(final GivenValue givenValue) {
        assertAll(
                () -> assertThat(givenValue.value().isUnknownRef()).isTrue(),
                () -> assertThat(givenValue.value().value()).isEqualTo("#REF!")
        );
    }

    @ValueArgumentsProvider.NotANumericalTest
    void shouldBeNotANumerical(final GivenValue givenValue) {
        assertAll(
                () -> assertThat(givenValue.value().isNotANumericalValue()).isTrue(),
                () -> assertThat(givenValue.value().value()).isEqualTo("#NUM!")
        );
    }

    @ValueArgumentsProvider.DividedByZeroTest
    void shouldBeDividedByZero(final GivenValue givenValue) {
        assertAll(
                () -> assertThat(givenValue.value().isDivByZero()).isTrue(),
                () -> assertThat(givenValue.value().value()).isEqualTo("#DIV/0!")
        );
    }

    @ValueArgumentsProvider.NotALogicalValueTest
    void shouldBeNotALogicalValue(final GivenValue givenValue) {
        assertAll(
                () -> assertThat(givenValue.value().isNotALogicalValue()).isTrue(),
                () -> assertThat(givenValue.value().value()).isEqualTo("#LOG!")
        );
    }
}