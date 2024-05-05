package com.damdamdeo.formula.domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;

public class InformationFunctionTest {
    @ParameterizedTest
    @MethodSource({
            "com.damdamdeo.formula.domain.provider.InformationFunctionTestProvider#provideIsNotAvailable"
    })
    void shouldIsNotAvailableReturnExpectedValue(final Value givenArgument,
                                                 final boolean expectedArgument) {
        // Given
        final InformationFunction informationFunction = InformationFunction.ofIsNotAvailable();

        // When
        final boolean result = informationFunction.execute(givenArgument);

        // Then
        assertThat(result).isEqualTo(expectedArgument);
    }

    @ParameterizedTest
    @MethodSource({
            "com.damdamdeo.formula.domain.provider.InformationFunctionTestProvider#provideIsError"
    })
    void shouldIsErrorReturnExpectedValue(final Value givenArgument,
                                          final boolean expectedArgument) {
        // Given
        final InformationFunction informationFunction = InformationFunction.ofIsError();

        // When
        final boolean result = informationFunction.execute(givenArgument);

        // Then
        assertThat(result).isEqualTo(expectedArgument);
    }

    @ParameterizedTest
    @MethodSource({
            "com.damdamdeo.formula.domain.provider.InformationFunctionTestProvider#provideIsNumeric"
    })
    void shouldIsNumericReturnExpectedValue(final Value givenArgument,
                                            final boolean expectedArgument) {
        // Given
        final InformationFunction informationFunction = InformationFunction.ofIsNumeric();

        // When
        final boolean result = informationFunction.execute(givenArgument);

        // Then
        assertThat(result).isEqualTo(expectedArgument);
    }

    @ParameterizedTest
    @MethodSource({
            "com.damdamdeo.formula.domain.provider.InformationFunctionTestProvider#provideIsText"
    })
    void shouldIsTextReturnExpectedValue(final Value givenArgument,
                                         final boolean expectedArgument) {
        // Given
        final InformationFunction informationFunction = InformationFunction.ofIsText();

        // When
        final boolean result = informationFunction.execute(givenArgument);

        // Then
        assertThat(result).isEqualTo(expectedArgument);
    }

    @ParameterizedTest
    @MethodSource({
            "com.damdamdeo.formula.domain.provider.InformationFunctionTestProvider#provideIsBlank"
    })
    void shouldIsBlankReturnExpectedValue(final Value givenArgument,
                                          final boolean expectedArgument) {
        // Given
        final InformationFunction informationFunction = InformationFunction.ofIsBlank();

        // When
        final boolean result = informationFunction.execute(givenArgument);

        // Then
        assertThat(result).isEqualTo(expectedArgument);
    }

    @ParameterizedTest
    @MethodSource({
            "com.damdamdeo.formula.domain.provider.InformationFunctionTestProvider#provideIsLogical"
    })
    void shouldIsLogicalReturnExpectedValue(final Value givenArgument,
                                            final boolean expectedArgument) {
        // Given
        final InformationFunction informationFunction = InformationFunction.ofIsLogical();

        // When
        final boolean result = informationFunction.execute(givenArgument);

        // Then
        assertThat(result).isEqualTo(expectedArgument);
    }

}