package com.damdamdeo.formula.domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;

public class EqualityComparisonFunctionTest {
    @ParameterizedTest
    @MethodSource("com.damdamdeo.formula.domain.provider.EqualityComparisonFunctionTestProvider#provideEqual")
    void shouldOrReturnExpectedValue(final Value givenLeftValue,
                                     final Value givenRightValue,
                                     final Value expectedValue) {
        // Given
        final EqualityComparisonFunction equalityComparisonFunction = EqualityComparisonFunction.ofEqual(givenLeftValue, givenRightValue);

        // When
        final Value evaluated = equalityComparisonFunction.evaluate(new NumericalContext());

        // Then
        assertThat(evaluated).isEqualTo(expectedValue);
    }

    @ParameterizedTest
    @MethodSource("com.damdamdeo.formula.domain.provider.EqualityComparisonFunctionTestProvider#provideNotEqual")
    void shouldAndReturnExpectedValue(final Value givenLeftValue,
                                      final Value givenRightValue,
                                      final Value expectedValue) {
        // Given
        final EqualityComparisonFunction equalityComparisonFunction = EqualityComparisonFunction.ofNotEqual(givenLeftValue, givenRightValue);

        // When
        final Value evaluated = equalityComparisonFunction.evaluate(new NumericalContext());

        // Then
        assertThat(evaluated).isEqualTo(expectedValue);
    }
}