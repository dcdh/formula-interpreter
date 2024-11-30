package com.damdamdeo.formula.domain;

import com.damdamdeo.formula.domain.provider.*;
import org.junit.jupiter.params.provider.ArgumentsSource;

import static org.assertj.core.api.Assertions.assertThat;

class EqualityComparisonFunctionTest {

    @EqualityComparisonFunctionArgumentsProvider.EqualTest
    @ArgumentsSource(BiFunctionCommonArgumentsProvider.class)
    void shouldOrReturnExpectedValue(final GivenLeft givenLeft, final GivenRight givenRight, final Expected expected) {
        // Given
        final EqualityComparisonFunction equalityComparisonFunction = EqualityComparisonFunction.ofEqual(givenLeft.value(), givenRight.value());

        // When
        final Value evaluated = equalityComparisonFunction.evaluate(new NumericalContext());

        // Then
        assertThat(evaluated).isEqualTo(expected.value());
    }

    @EqualityComparisonFunctionArgumentsProvider.NotEqualTest
    @ArgumentsSource(BiFunctionCommonArgumentsProvider.class)
    void shouldAndReturnExpectedValue(final GivenLeft givenLeft, final GivenRight givenRight, final Expected expected) {
        // Given
        final EqualityComparisonFunction equalityComparisonFunction = EqualityComparisonFunction.ofNotEqual(givenLeft.value(), givenRight.value());

        // When
        final Value evaluated = equalityComparisonFunction.evaluate(new NumericalContext());

        // Then
        assertThat(evaluated).isEqualTo(expected.value());
    }
}