package com.damdamdeo.formula.domain;

import com.damdamdeo.formula.domain.spi.ValueProvider;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;

public class LogicalComparisonFunctionTest {
    final ValueProvider givenOnTrue = Value::ofTrue;
    final ValueProvider givenOnFalse = Value::ofFalse;

    @ParameterizedTest
    @MethodSource({
            "com.damdamdeo.formula.domain.provider.LogicalComparisonFunctionTestProvider#provideIf"
    })
    void shouldIfReturnExpectedValue(final Value givenComparison,
                                     final Value expectedResult) {
        // Given
        final LogicalComparisonFunction logicalComparisonFunction = LogicalComparisonFunction.ofIf(givenOnTrue, givenOnFalse);

        // When
        final Value result = logicalComparisonFunction.execute(givenComparison);

        // Then
        assertThat(result).isEqualTo(expectedResult);
    }

    @ParameterizedTest
    @MethodSource({
            "com.damdamdeo.formula.domain.provider.LogicalComparisonFunctionTestProvider#provideIfError"
    })
    void shouldIfErrorReturnExpectedValue(final Value givenComparison,
                                          final Value expectedResult) {
        // Given
        final LogicalComparisonFunction logicalComparisonFunction = LogicalComparisonFunction.ofIfError(givenOnTrue, givenOnFalse);

        // When
        final Value result = logicalComparisonFunction.execute(givenComparison);

        // Then
        assertThat(result).isEqualTo(expectedResult);
    }

    @ParameterizedTest
    @MethodSource({
            "com.damdamdeo.formula.domain.provider.LogicalComparisonFunctionTestProvider#provideIfNotAvailable"
    })
    void shouldIfNotAvailableReturnExpectedValue(final Value givenComparison,
                                                 final Value expectedResult) {
        // Given
        final LogicalComparisonFunction logicalComparisonFunction = LogicalComparisonFunction.ofIfNotAvailable(givenOnTrue, givenOnFalse);

        // When
        final Value result = logicalComparisonFunction.execute(givenComparison);

        // Then
        assertThat(result).isEqualTo(expectedResult);
    }
}