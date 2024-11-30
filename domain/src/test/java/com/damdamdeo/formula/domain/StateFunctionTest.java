package com.damdamdeo.formula.domain;

import com.damdamdeo.formula.domain.provider.Expected;
import com.damdamdeo.formula.domain.provider.GivenArgument;
import com.damdamdeo.formula.domain.provider.StateFunctionArgumentsProvider;

import static org.assertj.core.api.Assertions.assertThat;

class StateFunctionTest {
    @StateFunctionArgumentsProvider.IsNotAvailableTest
    void shouldIsNotAvailableReturnExpectedValue(final GivenArgument givenArgument, final Expected expected) {
        // Given
        final StateFunction stateFunction = StateFunction.ofIsNotAvailable(givenArgument.value());

        // When
        final Value evaluated = stateFunction.evaluate(new NumericalContext());

        // Then
        assertThat(evaluated).isEqualTo(expected.value());
    }

    @StateFunctionArgumentsProvider.IsErrorTest
    void shouldIsErrorReturnExpectedValue(final GivenArgument givenArgument, final Expected expected) {
        // Given
        final StateFunction stateFunction = StateFunction.ofIsError(givenArgument.value());

        // When
        final Value evaluated = stateFunction.evaluate(new NumericalContext());

        // Then
        assertThat(evaluated).isEqualTo(expected.value());
    }

    @StateFunctionArgumentsProvider.IsNumericTest
    void shouldIsNumericReturnExpectedValue(final GivenArgument givenArgument, final Expected expected) {
        // Given
        final StateFunction stateFunction = StateFunction.ofIsNumeric(givenArgument.value());

        // When
        final Value evaluated = stateFunction.evaluate(new NumericalContext());

        // Then
        assertThat(evaluated).isEqualTo(expected.value());
    }

    @StateFunctionArgumentsProvider.IsTextTest
    void shouldIsTextReturnExpectedValue(final GivenArgument givenArgument, final Expected expected) {
        // Given
        final StateFunction stateFunction = StateFunction.ofIsText(givenArgument.value());

        // When
        final Value evaluated = stateFunction.evaluate(new NumericalContext());

        // Then
        assertThat(evaluated).isEqualTo(expected.value());
    }

    @StateFunctionArgumentsProvider.IsBlankTest
    void shouldIsBlankReturnExpectedValue(final GivenArgument givenArgument, final Expected expected) {
        // Given
        final StateFunction stateFunction = StateFunction.ofIsBlank(givenArgument.value());

        // When
        final Value evaluated = stateFunction.evaluate(new NumericalContext());

        // Then
        assertThat(evaluated).isEqualTo(expected.value());
    }

    @StateFunctionArgumentsProvider.IsLogicalTest
    void shouldIsLogicalReturnExpectedValue(final GivenArgument givenArgument, final Expected expected) {
        // Given
        final StateFunction stateFunction = StateFunction.ofIsLogical(givenArgument.value());

        // When
        final Value evaluated = stateFunction.evaluate(new NumericalContext());

        // Then
        assertThat(evaluated).isEqualTo(expected.value());
    }

}