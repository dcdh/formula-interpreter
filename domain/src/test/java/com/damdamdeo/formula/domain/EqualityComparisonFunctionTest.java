package com.damdamdeo.formula.domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;

public class EqualityComparisonFunctionTest {
    @ParameterizedTest
    @MethodSource({
            "com.damdamdeo.formula.domain.provider.EqualityComparisonFunctionTestProvider#provideEqual",
            "com.damdamdeo.formula.domain.provider.EqualityComparisonFunctionTestProvider#provideCommonResponses"
    })
    void shouldOrReturnExpectedValue(final Value givenLeftValue,
                                     final Value givenRightValue,
                                     final Value expectedValue) {
        // Given
        final EqualityComparisonFunction equalityComparisonFunction = EqualityComparisonFunction.ofEqual();

        // When
        final Value result = equalityComparisonFunction.evaluate(givenLeftValue, givenRightValue, new NumericalContext());

        // Then
        assertThat(result).isEqualTo(expectedValue);
    }

    @ParameterizedTest
    @MethodSource({
            "com.damdamdeo.formula.domain.provider.EqualityComparisonFunctionTestProvider#provideNotEqual",
            "com.damdamdeo.formula.domain.provider.EqualityComparisonFunctionTestProvider#provideCommonResponses"
    })
    void shouldAndReturnExpectedValue(final Value givenLeftValue,
                                      final Value givenRightValue,
                                      final Value expectedValue) {
        // Given
        final EqualityComparisonFunction equalityComparisonFunction = EqualityComparisonFunction.ofNotEqual();

        // When
        final Value result = equalityComparisonFunction.evaluate(givenLeftValue, givenRightValue, new NumericalContext());

        // Then
        assertThat(result).isEqualTo(expectedValue);
    }
}