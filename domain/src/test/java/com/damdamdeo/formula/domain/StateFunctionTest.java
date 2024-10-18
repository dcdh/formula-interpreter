package com.damdamdeo.formula.domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;

public class StateFunctionTest {
    @ParameterizedTest
    @MethodSource({
            "com.damdamdeo.formula.domain.provider.StateFunctionTestProvider#provideIsNotAvailable"
    })
    void shouldIsNotAvailableReturnExpectedValue(final Value givenArgument,
                                                 final boolean expectedArgument) {
        // Given
        final StateFunction stateFunction = StateFunction.ofIsNotAvailable();

        // When
        final boolean result = stateFunction.evaluate(givenArgument);

        // Then
        assertThat(result).isEqualTo(expectedArgument);
    }

    @ParameterizedTest
    @MethodSource({
            "com.damdamdeo.formula.domain.provider.StateFunctionTestProvider#provideIsError"
    })
    void shouldIsErrorReturnExpectedValue(final Value givenArgument,
                                          final boolean expectedArgument) {
        // Given
        final StateFunction stateFunction = StateFunction.ofIsError();

        // When
        final boolean result = stateFunction.evaluate(givenArgument);

        // Then
        assertThat(result).isEqualTo(expectedArgument);
    }

    @ParameterizedTest
    @MethodSource({
            "com.damdamdeo.formula.domain.provider.StateFunctionTestProvider#provideIsNumeric"
    })
    void shouldIsNumericReturnExpectedValue(final Value givenArgument,
                                            final boolean expectedArgument) {
        // Given
        final StateFunction stateFunction = StateFunction.ofIsNumeric();

        // When
        final boolean result = stateFunction.evaluate(givenArgument);

        // Then
        assertThat(result).isEqualTo(expectedArgument);
    }

    @ParameterizedTest
    @MethodSource({
            "com.damdamdeo.formula.domain.provider.StateFunctionTestProvider#provideIsText"
    })
    void shouldIsTextReturnExpectedValue(final Value givenArgument,
                                         final boolean expectedArgument) {
        // Given
        final StateFunction stateFunction = StateFunction.ofIsText();

        // When
        final boolean result = stateFunction.evaluate(givenArgument);

        // Then
        assertThat(result).isEqualTo(expectedArgument);
    }

    @ParameterizedTest
    @MethodSource({
            "com.damdamdeo.formula.domain.provider.StateFunctionTestProvider#provideIsBlank"
    })
    void shouldIsBlankReturnExpectedValue(final Value givenArgument,
                                          final boolean expectedArgument) {
        // Given
        final StateFunction stateFunction = StateFunction.ofIsBlank();

        // When
        final boolean result = stateFunction.evaluate(givenArgument);

        // Then
        assertThat(result).isEqualTo(expectedArgument);
    }

    @ParameterizedTest
    @MethodSource({
            "com.damdamdeo.formula.domain.provider.StateFunctionTestProvider#provideIsLogical"
    })
    void shouldIsLogicalReturnExpectedValue(final Value givenArgument,
                                            final boolean expectedArgument) {
        // Given
        final StateFunction stateFunction = StateFunction.ofIsLogical();

        // When
        final boolean result = stateFunction.evaluate(givenArgument);

        // Then
        assertThat(result).isEqualTo(expectedArgument);
    }

}