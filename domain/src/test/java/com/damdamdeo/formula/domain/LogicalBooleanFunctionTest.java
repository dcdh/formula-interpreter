package com.damdamdeo.formula.domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;

public class LogicalBooleanFunctionTest {
    @ParameterizedTest
    @MethodSource({
            "com.damdamdeo.formula.domain.provider.LogicalBooleanFunctionTestProvider#provideOr",
            "com.damdamdeo.formula.domain.provider.LogicalBooleanFunctionTestProvider#provideCommonResponses"
    })
    void shouldOrReturnExpectedValue(final Value givenLeftValue,
                                     final Value givenRightValue,
                                     final Value expectedValue) {
        // Given
        final LogicalBooleanFunction logicalBooleanFunction = LogicalBooleanFunction.ofOr();

        // When
        final Value result = logicalBooleanFunction.execute(givenLeftValue, givenRightValue);

        // Then
        assertThat(result).isEqualTo(expectedValue);
    }

    @ParameterizedTest
    @MethodSource({
            "com.damdamdeo.formula.domain.provider.LogicalBooleanFunctionTestProvider#provideAnd",
            "com.damdamdeo.formula.domain.provider.LogicalBooleanFunctionTestProvider#provideCommonResponses"
    })
    void shouldAndReturnExpectedValue(final Value givenLeftValue,
                                      final Value givenRightValue,
                                      final Value expectedValue) {
        // Given
        final LogicalBooleanFunction logicalBooleanFunction = LogicalBooleanFunction.ofAnd();

        // When
        final Value result = logicalBooleanFunction.execute(givenLeftValue, givenRightValue);

        // Then
        assertThat(result).isEqualTo(expectedValue);
    }
}