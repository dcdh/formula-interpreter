package com.damdamdeo.formula.domain;

import com.damdamdeo.formula.domain.provider.*;
import org.junit.jupiter.params.provider.ArgumentsSource;

import static org.assertj.core.api.Assertions.assertThat;

class ArithmeticFunctionTest {
    @ArithmeticFunctionArgumentsProvider.AddTest
    @ArgumentsSource(BiFunctionCommonArgumentsProvider.class)
    void shouldAdditionReturnExpectedValue(final GivenLeft givenLeft, final GivenRight givenRight, final Expected expected) {
        // Given
        final ArithmeticFunction arithmeticFunction = ArithmeticFunction.ofAddition(givenLeft.value(), givenRight.value());

        // When
        final Value evaluated = arithmeticFunction.evaluate(new NumericalContext());

        // Then
        assertThat(evaluated).isEqualTo(expected.value());
    }

    @ArithmeticFunctionArgumentsProvider.SubtractTest
    @ArgumentsSource(BiFunctionCommonArgumentsProvider.class)
    void shouldSubtractionReturnExpectedValue(final GivenLeft givenLeft, final GivenRight givenRight, final Expected expected) {
        // Given
        final ArithmeticFunction arithmeticFunction = ArithmeticFunction.ofSubtraction(givenLeft.value(), givenRight.value());

        // When
        final Value evaluated = arithmeticFunction.evaluate(new NumericalContext());

        // Then
        assertThat(evaluated).isEqualTo(expected.value());
    }

    @ArithmeticFunctionArgumentsProvider.DivideTest
    @ArgumentsSource(BiFunctionCommonArgumentsProvider.class)
    void shouldDivisionReturnExpectedValue(final GivenLeft givenLeft, final GivenRight givenRight, final Expected expected) {
        // Given
        final ArithmeticFunction arithmeticFunction = ArithmeticFunction.ofDivision(givenLeft.value(), givenRight.value());

        // When
        final Value evaluated = arithmeticFunction.evaluate(new NumericalContext());

        // Then
        assertThat(evaluated).isEqualTo(expected.value());
    }

    @ArithmeticFunctionArgumentsProvider.MultiplyTest
    @ArgumentsSource(BiFunctionCommonArgumentsProvider.class)
    void shouldMultiplicationReturnExpectedValue(final GivenLeft givenLeft, final GivenRight givenRight, final Expected expected) {
        // Given
        final ArithmeticFunction arithmeticFunction = ArithmeticFunction.ofMultiplication(givenLeft.value(), givenRight.value());

        // When
        final Value evaluated = arithmeticFunction.evaluate(new NumericalContext());

        // Then
        assertThat(evaluated).isEqualTo(expected.value());
    }
}