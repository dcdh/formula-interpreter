package com.damdamdeo.formula.domain;

import com.damdamdeo.formula.domain.provider.*;
import org.junit.jupiter.params.provider.ArgumentsSource;

import static org.assertj.core.api.Assertions.assertThat;

class NumericalComparisonFunctionTest {

    @NumericalComparisonFunctionArgumentsProvider.GreaterThanTest
    @ArgumentsSource(BiFunctionCommonArgumentsProvider.class)
    void shouldGreaterThanReturnExpectedValue(final GivenLeft givenLeft, final GivenRight givenRight, final Expected expected) {
        // Given
        final NumericalComparisonFunction numericalComparisonFunction = NumericalComparisonFunction.ofGreaterThan(givenLeft.value(), givenRight.value());

        // When
        final Value evaluated = numericalComparisonFunction.evaluate(new NumericalContext());

        // Then
        assertThat(evaluated).isEqualTo(expected.value());
    }

    @NumericalComparisonFunctionArgumentsProvider.GreaterThanOrEqualToTest
    @ArgumentsSource(BiFunctionCommonArgumentsProvider.class)
    void shouldGreaterThanOrEqualToReturnExpectedValue(final GivenLeft givenLeft, final GivenRight givenRight, final Expected expected) {
        // Given
        final NumericalComparisonFunction numericalComparisonFunction = NumericalComparisonFunction.ofGreaterThanOrEqualTo(givenLeft.value(), givenRight.value());

        // When
        final Value evaluated = numericalComparisonFunction.evaluate(new NumericalContext());

        // Then
        assertThat(evaluated).isEqualTo(expected.value());
    }

    @NumericalComparisonFunctionArgumentsProvider.LessThanTest
    @ArgumentsSource(BiFunctionCommonArgumentsProvider.class)
    void shouldLessThanReturnExpectedValue(final GivenLeft givenLeft, final GivenRight givenRight, final Expected expected) {
        // Given
        final NumericalComparisonFunction numericalComparisonFunction = NumericalComparisonFunction.ofLessThan(givenLeft.value(), givenRight.value());

        // When
        final Value evaluated = numericalComparisonFunction.evaluate(new NumericalContext());

        // Then
        assertThat(evaluated).isEqualTo(expected.value());
    }

    @NumericalComparisonFunctionArgumentsProvider.LessThanOrEqualToTest
    @ArgumentsSource(BiFunctionCommonArgumentsProvider.class)
    void shouldLessThanOrEqualToReturnExpectedValue(final GivenLeft givenLeft, final GivenRight givenRight, final Expected expected) {
        // Given
        final NumericalComparisonFunction numericalComparisonFunction = NumericalComparisonFunction.ofLessThanOrEqualTo(givenLeft.value(), givenRight.value());

        // When
        final Value evaluated = numericalComparisonFunction.evaluate(new NumericalContext());

        // Then
        assertThat(evaluated).isEqualTo(expected.value());
    }
}