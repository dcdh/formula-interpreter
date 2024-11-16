package com.damdamdeo.formula.domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;

public class StateFunctionTest {
    @ParameterizedTest
    @MethodSource("com.damdamdeo.formula.domain.provider.StateFunctionTestProvider#provideIsNotAvailable")
    void shouldIsNotAvailableReturnExpectedValue(final Value givenArgument,
                                                 final Value expectedArgument) {
        // Given
        final StateFunction stateFunction = StateFunction.ofIsNotAvailable(givenArgument);

        // When
        final Value evaluated = stateFunction.evaluate(new NumericalContext());

        // Then
        assertThat(evaluated).isEqualTo(expectedArgument);
    }

    @ParameterizedTest
    @MethodSource("com.damdamdeo.formula.domain.provider.StateFunctionTestProvider#provideIsError")
    void shouldIsErrorReturnExpectedValue(final Value givenArgument,
                                          final Value expectedArgument) {
        // Given
        final StateFunction stateFunction = StateFunction.ofIsError(givenArgument);

        // When
        final Value evaluated = stateFunction.evaluate(new NumericalContext());

        // Then
        assertThat(evaluated).isEqualTo(expectedArgument);
    }

    @ParameterizedTest
    @MethodSource("com.damdamdeo.formula.domain.provider.StateFunctionTestProvider#provideIsNumeric")
    void shouldIsNumericReturnExpectedValue(final Value givenArgument,
                                            final Value expectedArgument) {
        // Given
        final StateFunction stateFunction = StateFunction.ofIsNumeric(givenArgument);

        // When
        final Value evaluated = stateFunction.evaluate(new NumericalContext());

        // Then
        assertThat(evaluated).isEqualTo(expectedArgument);
    }

    @ParameterizedTest
    @MethodSource("com.damdamdeo.formula.domain.provider.StateFunctionTestProvider#provideIsText")
    void shouldIsTextReturnExpectedValue(final Value givenArgument,
                                         final Value expectedArgument) {
        // Given
        final StateFunction stateFunction = StateFunction.ofIsText(givenArgument);

        // When
        final Value evaluated = stateFunction.evaluate(new NumericalContext());

        // Then
        assertThat(evaluated).isEqualTo(expectedArgument);
    }

    @ParameterizedTest
    @MethodSource("com.damdamdeo.formula.domain.provider.StateFunctionTestProvider#provideIsBlank")
    void shouldIsBlankReturnExpectedValue(final Value givenArgument,
                                          final Value expectedArgument) {
        // Given
        final StateFunction stateFunction = StateFunction.ofIsBlank(givenArgument);

        // When
        final Value evaluated = stateFunction.evaluate(new NumericalContext());

        // Then
        assertThat(evaluated).isEqualTo(expectedArgument);
    }

    @ParameterizedTest
    @MethodSource("com.damdamdeo.formula.domain.provider.StateFunctionTestProvider#provideIsLogical")
    void shouldIsLogicalReturnExpectedValue(final Value givenArgument,
                                            final Value expectedArgument) {
        // Given
        final StateFunction stateFunction = StateFunction.ofIsLogical(givenArgument);

        // When
        final Value evaluated = stateFunction.evaluate(new NumericalContext());

        // Then
        assertThat(evaluated).isEqualTo(expectedArgument);
    }

}