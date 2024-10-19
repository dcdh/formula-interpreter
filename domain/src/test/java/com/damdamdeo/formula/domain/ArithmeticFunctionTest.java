package com.damdamdeo.formula.domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;

class ArithmeticFunctionTest {
    @ParameterizedTest
    @MethodSource({
            "com.damdamdeo.formula.domain.provider.ArithmeticFunctionTestProvider#provideAddition",
            "com.damdamdeo.formula.domain.provider.ArithmeticFunctionTestProvider#provideCommonResponses"
    })
    void shouldAdditionReturnExpectedValue(final Value givenLeftValue,
                                           final Value givenRightValue,
                                           final Value expectedValue) {
        // Given
        final ArithmeticFunction arithmeticFunction = ArithmeticFunction.ofAddition(givenLeftValue, givenRightValue);

        // When
        final Value evaluated = arithmeticFunction.evaluate(new NumericalContext());

        // Then
        assertThat(evaluated).isEqualTo(expectedValue);
    }

    @ParameterizedTest
    @MethodSource({
            "com.damdamdeo.formula.domain.provider.ArithmeticFunctionTestProvider#provideSubtraction",
            "com.damdamdeo.formula.domain.provider.ArithmeticFunctionTestProvider#provideCommonResponses"
    })
    void shouldSubtractionReturnExpectedValue(final Value givenLeftValue,
                                              final Value givenRightValue,
                                              final Value expectedValue) {
        // Given
        final ArithmeticFunction arithmeticFunction = ArithmeticFunction.ofSubtraction(givenLeftValue, givenRightValue);

        // When
        final Value evaluated = arithmeticFunction.evaluate(new NumericalContext());

        // Then
        assertThat(evaluated).isEqualTo(expectedValue);
    }

    @ParameterizedTest
    @MethodSource({
            "com.damdamdeo.formula.domain.provider.ArithmeticFunctionTestProvider#provideDivision",
            "com.damdamdeo.formula.domain.provider.ArithmeticFunctionTestProvider#provideCommonResponses"
    })
    void shouldDivisionReturnExpectedValue(final Value givenLeftValue,
                                           final Value givenRightValue,
                                           final Value expectedValue) {
        // Given
        final ArithmeticFunction arithmeticFunction = ArithmeticFunction.ofDivision(givenLeftValue, givenRightValue);

        // When
        final Value evaluated = arithmeticFunction.evaluate(new NumericalContext());

        // Then
        assertThat(evaluated).isEqualTo(expectedValue);
    }

    @ParameterizedTest
    @MethodSource({
            "com.damdamdeo.formula.domain.provider.ArithmeticFunctionTestProvider#provideMultiplication",
            "com.damdamdeo.formula.domain.provider.ArithmeticFunctionTestProvider#provideCommonResponses"
    })
    void shouldMultiplicationReturnExpectedValue(final Value givenLeftValue,
                                                 final Value givenRightValue,
                                                 final Value expectedValue) {
        // Given
        final ArithmeticFunction arithmeticFunction = ArithmeticFunction.ofMultiplication(givenLeftValue, givenRightValue);

        // When
        final Value evaluated = arithmeticFunction.evaluate(new NumericalContext());

        // Then
        assertThat(evaluated).isEqualTo(expectedValue);
    }
}