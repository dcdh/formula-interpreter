package com.damdamdeo.formula.domain;

import com.damdamdeo.formula.domain.provider.Expected;
import com.damdamdeo.formula.domain.provider.GivenLeft;
import com.damdamdeo.formula.domain.provider.GivenRight;
import com.damdamdeo.formula.domain.provider.LogicalBooleanFunctionArgumentsProvider;

import static org.assertj.core.api.Assertions.assertThat;

class LogicalBooleanFunctionTest {
    @LogicalBooleanFunctionArgumentsProvider.OrTest
    void shouldOrReturnExpectedValue(final GivenLeft givenLeft, final GivenRight givenRight, final Expected expected) {
        // Given
        final LogicalBooleanFunction logicalBooleanFunction = LogicalBooleanFunction.ofOr(givenLeft.value(), givenRight.value());

        // When
        final Value evaluated = logicalBooleanFunction.evaluate(new NumericalContext());

        // Then
        assertThat(evaluated).isEqualTo(expected.value());
    }

    @LogicalBooleanFunctionArgumentsProvider.AndTest
    void shouldAndReturnExpectedValue(final GivenLeft givenLeft, final GivenRight givenRight, final Expected expected) {
        // Given
        final LogicalBooleanFunction logicalBooleanFunction = LogicalBooleanFunction.ofAnd(givenLeft.value(), givenRight.value());

        // When
        final Value evaluated = logicalBooleanFunction.evaluate(new NumericalContext());

        // Then
        assertThat(evaluated).isEqualTo(expected.value());
    }
}