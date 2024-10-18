package com.damdamdeo.formula.domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;

public class NumericalComparisonFunctionTest {
    @ParameterizedTest
    @MethodSource({
            "com.damdamdeo.formula.domain.provider.NumericalComparisonFunctionTestProvider#provideGreaterThan",
            "com.damdamdeo.formula.domain.provider.NumericalComparisonFunctionTestProvider#provideCommonResponses"
    })
    void shouldGreaterThanReturnExpectedValue(final Value givenLeftValue,
                                              final Value givenRightValue,
                                              final Value expectedValue) {
        // Given
        final NumericalComparisonFunction numericalComparisonFunction = NumericalComparisonFunction.ofGreaterThan();

        // When
        final Value result = numericalComparisonFunction.evaluate(givenLeftValue, givenRightValue, new NumericalContext());

        // Then
        assertThat(result).isEqualTo(expectedValue);
    }

    @ParameterizedTest
    @MethodSource({
            "com.damdamdeo.formula.domain.provider.NumericalComparisonFunctionTestProvider#provideGreaterThanOrEqualTo",
            "com.damdamdeo.formula.domain.provider.NumericalComparisonFunctionTestProvider#provideCommonResponses"
    })
    void shouldGreaterThanOrEqualToReturnExpectedValue(final Value givenLeftValue,
                                                       final Value givenRightValue,
                                                       final Value expectedValue) {
        // Given
        final NumericalComparisonFunction numericalComparisonFunction = NumericalComparisonFunction.ofGreaterThanOrEqualTo();

        // When
        final Value result = numericalComparisonFunction.evaluate(givenLeftValue, givenRightValue, new NumericalContext());

        // Then
        assertThat(result).isEqualTo(expectedValue);
    }

    @ParameterizedTest
    @MethodSource({
            "com.damdamdeo.formula.domain.provider.NumericalComparisonFunctionTestProvider#provideLessThan",
            "com.damdamdeo.formula.domain.provider.NumericalComparisonFunctionTestProvider#provideCommonResponses"
    })
    void shouldLessThanReturnExpectedValue(final Value givenLeftValue,
                                           final Value givenRightValue,
                                           final Value expectedValue) {
        // Given
        final NumericalComparisonFunction numericalComparisonFunction = NumericalComparisonFunction.ofLessThan();

        // When
        final Value result = numericalComparisonFunction.evaluate(givenLeftValue, givenRightValue, new NumericalContext());

        // Then
        assertThat(result).isEqualTo(expectedValue);
    }

    @ParameterizedTest
    @MethodSource({
            "com.damdamdeo.formula.domain.provider.NumericalComparisonFunctionTestProvider#provideLessThanOrEqualTo",
            "com.damdamdeo.formula.domain.provider.NumericalComparisonFunctionTestProvider#provideCommonResponses"
    })
    void shouldLessThanOrEqualToReturnExpectedValue(final Value givenLeftValue,
                                                    final Value givenRightValue,
                                                    final Value expectedValue) {
        // Given
        final NumericalComparisonFunction numericalComparisonFunction = NumericalComparisonFunction.ofLessThanOrEqualTo();

        // When
        final Value result = numericalComparisonFunction.evaluate(givenLeftValue, givenRightValue, new NumericalContext());

        // Then
        assertThat(result).isEqualTo(expectedValue);
    }
}