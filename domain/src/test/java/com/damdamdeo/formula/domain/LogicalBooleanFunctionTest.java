package com.damdamdeo.formula.domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;

public class LogicalBooleanFunctionTest {
    @ParameterizedTest
    @MethodSource("com.damdamdeo.formula.domain.provider.LogicalBooleanFunctionTestProvider#provideOr")
    void shouldOrReturnExpectedValue(final Value givenLeftValue,
                                     final Value givenRightValue,
                                     final Value expectedValue) {
        // Given
        final LogicalBooleanFunction logicalBooleanFunction = LogicalBooleanFunction.ofOr(givenLeftValue, givenRightValue);

        // When
        final Value evaluated = logicalBooleanFunction.evaluate(new NumericalContext());

        // Then
        assertThat(evaluated).isEqualTo(expectedValue);
    }

    @ParameterizedTest
    @MethodSource("com.damdamdeo.formula.domain.provider.LogicalBooleanFunctionTestProvider#provideAnd")
    void shouldAndReturnExpectedValue(final Value givenLeftValue,
                                      final Value givenRightValue,
                                      final Value expectedValue) {
        // Given
        final LogicalBooleanFunction logicalBooleanFunction = LogicalBooleanFunction.ofAnd(givenLeftValue, givenRightValue);

        // When
        final Value evaluated = logicalBooleanFunction.evaluate(new NumericalContext());

        // Then
        assertThat(evaluated).isEqualTo(expectedValue);
    }
}