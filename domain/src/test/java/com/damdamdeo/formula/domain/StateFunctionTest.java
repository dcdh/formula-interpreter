package com.damdamdeo.formula.domain;

import com.damdamdeo.formula.domain.provider.Expected;
import com.damdamdeo.formula.domain.provider.GivenValue;
import com.damdamdeo.formula.domain.provider.StateFunctionArgumentsProvider;

import static org.assertj.core.api.Assertions.assertThat;

class StateFunctionTest {
    @StateFunctionArgumentsProvider.IsNotAvailableTest
    void shouldIsNotAvailableReturnExpectedValue(final GivenValue givenValue, final Expected expected) {
        // Given
        final StateFunction stateFunction = StateFunction.ofIsNotAvailable(givenValue.value());

        // When
        final Value evaluated = stateFunction.evaluate(new NumericalContext());

        // Then
        assertThat(evaluated).isEqualTo(expected.value());
    }

    @StateFunctionArgumentsProvider.IsErrorTest
    void shouldIsErrorReturnExpectedValue(final GivenValue givenValue, final Expected expected) {
        // Given
        final StateFunction stateFunction = StateFunction.ofIsError(givenValue.value());

        // When
        final Value evaluated = stateFunction.evaluate(new NumericalContext());

        // Then
        assertThat(evaluated).isEqualTo(expected.value());
    }

    @StateFunctionArgumentsProvider.IsNumericTest
    void shouldIsNumericReturnExpectedValue(final GivenValue givenValue, final Expected expected) {
        // Given
        final StateFunction stateFunction = StateFunction.ofIsNumeric(givenValue.value());

        // When
        final Value evaluated = stateFunction.evaluate(new NumericalContext());

        // Then
        assertThat(evaluated).isEqualTo(expected.value());
    }

    @StateFunctionArgumentsProvider.IsTextTest
    void shouldIsTextReturnExpectedValue(final GivenValue givenValue, final Expected expected) {
        // Given
        final StateFunction stateFunction = StateFunction.ofIsText(givenValue.value());

        // When
        final Value evaluated = stateFunction.evaluate(new NumericalContext());

        // Then
        assertThat(evaluated).isEqualTo(expected.value());
    }

    @StateFunctionArgumentsProvider.IsBlankTest
    void shouldIsBlankReturnExpectedValue(final GivenValue givenValue, final Expected expected) {
        // Given
        final StateFunction stateFunction = StateFunction.ofIsBlank(givenValue.value());

        // When
        final Value evaluated = stateFunction.evaluate(new NumericalContext());

        // Then
        assertThat(evaluated).isEqualTo(expected.value());
    }

    @StateFunctionArgumentsProvider.IsLogicalTest
    void shouldIsLogicalReturnExpectedValue(final GivenValue givenValue, final Expected expected) {
        // Given
        final StateFunction stateFunction = StateFunction.ofIsLogical(givenValue.value());

        // When
        final Value evaluated = stateFunction.evaluate(new NumericalContext());

        // Then
        assertThat(evaluated).isEqualTo(expected.value());
    }

}