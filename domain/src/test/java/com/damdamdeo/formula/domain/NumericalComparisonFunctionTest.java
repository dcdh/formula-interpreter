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
        final NumericalComparisonFunction numericalComparisonFunction = NumericalComparisonFunction.ofGreaterThan(givenLeftValue, givenRightValue);

        // When
        final Value evaluated = numericalComparisonFunction.evaluate(new NumericalContext());

        // Then
        assertThat(evaluated).isEqualTo(expectedValue);
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
        final NumericalComparisonFunction numericalComparisonFunction = NumericalComparisonFunction.ofGreaterThanOrEqualTo(givenLeftValue, givenRightValue);

        // When
        final Value evaluated = numericalComparisonFunction.evaluate(new NumericalContext());

        // Then
        assertThat(evaluated).isEqualTo(expectedValue);
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
        final NumericalComparisonFunction numericalComparisonFunction = NumericalComparisonFunction.ofLessThan(givenLeftValue, givenRightValue);

        // When
        final Value evaluated = numericalComparisonFunction.evaluate(new NumericalContext());

        // Then
        assertThat(evaluated).isEqualTo(expectedValue);
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
        final NumericalComparisonFunction numericalComparisonFunction = NumericalComparisonFunction.ofLessThanOrEqualTo(givenLeftValue, givenRightValue);

        // When
        final Value evaluated = numericalComparisonFunction.evaluate(new NumericalContext());

        // Then
        assertThat(evaluated).isEqualTo(expectedValue);
    }
}