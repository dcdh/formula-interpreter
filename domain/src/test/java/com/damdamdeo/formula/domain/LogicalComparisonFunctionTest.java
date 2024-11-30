package com.damdamdeo.formula.domain;

import com.damdamdeo.formula.domain.provider.Expected;
import com.damdamdeo.formula.domain.provider.GivenComparison;
import com.damdamdeo.formula.domain.provider.LogicalComparisonFunctionArgumentsProviders;
import com.damdamdeo.formula.domain.spi.ValueProvider;

import static org.assertj.core.api.Assertions.assertThat;

public class LogicalComparisonFunctionTest {
    final ValueProvider givenOnTrue = Value::ofTrue;
    final ValueProvider givenOnFalse = Value::ofFalse;

    @LogicalComparisonFunctionArgumentsProviders.IfTest
    void shouldIfReturnExpectedValue(final GivenComparison givenComparison, final Expected expected) {
        // Given
        final LogicalComparisonFunction logicalComparisonFunction = LogicalComparisonFunction.ofIf(givenComparison.value(), givenOnTrue, givenOnFalse);

        // When
        final Value evaluated = logicalComparisonFunction.evaluate(new NumericalContext());

        // Then
        assertThat(evaluated).isEqualTo(expected.value());
    }

    @LogicalComparisonFunctionArgumentsProviders.IfErrorTest
    void shouldIfErrorReturnExpectedValue(final GivenComparison givenComparison, final Expected expected) {
        // Given
        final LogicalComparisonFunction logicalComparisonFunction = LogicalComparisonFunction.ofIfError(givenComparison.value(), givenOnTrue, givenOnFalse);

        // When
        final Value evaluated = logicalComparisonFunction.evaluate(new NumericalContext());

        // Then
        assertThat(evaluated).isEqualTo(expected.value());
    }

    @LogicalComparisonFunctionArgumentsProviders.IfNotAvailableTest
    void shouldIfNotAvailableReturnExpectedValue(final GivenComparison givenComparison, final Expected expected) {
        // Given
        final LogicalComparisonFunction logicalComparisonFunction = LogicalComparisonFunction.ofIfNotAvailable(givenComparison.value(), givenOnTrue, givenOnFalse);

        // When
        final Value evaluated = logicalComparisonFunction.evaluate(new NumericalContext());

        // Then
        assertThat(evaluated).isEqualTo(expected.value());
    }
}