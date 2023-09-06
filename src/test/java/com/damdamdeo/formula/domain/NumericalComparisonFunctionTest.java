package com.damdamdeo.formula.domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class NumericalComparisonFunctionTest {
    @ParameterizedTest
    @MethodSource({
            "provideGreaterThan",
            "provideCommonResponses"
    })
    void shouldGreaterThanReturnExpectedValue(final Value givenLeftValue,
                                              final Value givenRightValue,
                                              final Value expectedValue) {
        // Given
        final NumericalComparisonFunction numericalComparisonFunction = NumericalComparisonFunction.ofGreaterThan();

        // When
        final Value result = numericalComparisonFunction.execute(givenLeftValue, givenRightValue, new NumericalContext());

        // Then
        assertThat(result).isEqualTo(expectedValue);
    }

    public static Stream<Arguments> provideGreaterThan() {
        return Stream.of(
                Arguments.of(new Value("660"), new Value("260"), new Value("true")),
                Arguments.of(new Value("260"), new Value("660"), new Value("false")),
                Arguments.of(new Value("260"), new Value("260"), new Value("false")));
    }

    public static Stream<Arguments> provideCommonResponses() {
        return Stream.of(
                Arguments.of(new Value("#NA!"), new Value("260"), new Value("#NA!")),
                Arguments.of(new Value("#REF!"), new Value("260"), new Value("#REF!")),
                Arguments.of(new Value("#NUM!"), new Value("260"), new Value("#NUM!")),
                Arguments.of(new Value("#DIV/0!"), new Value("260"), new Value("#DIV/0!")),
                Arguments.of(new Value("660"), new Value("#NA!"), new Value("#NA!")),
                Arguments.of(new Value("660"), new Value("#REF!"), new Value("#REF!")),
                Arguments.of(new Value("660"), new Value("#NUM!"), new Value("#NUM!")),
                Arguments.of(new Value("660"), new Value("#DIV/0!"), new Value("#DIV/0!")),
                Arguments.of(new Value("azerty"), new Value("260"), new Value("#NUM!")),
                Arguments.of(new Value("660"), new Value("azerty"), new Value("#NUM!")),
                Arguments.of(new Value("#NA!"), new Value("#NA!"), new Value("#NA!")),
                Arguments.of(new Value("#REF!"), new Value("#REF!"), new Value("#REF!")),
                Arguments.of(new Value("#NUM!"), new Value("#NUM!"), new Value("#NUM!")),
                Arguments.of(new Value("#DIV/0!"), new Value("#DIV/0!"), new Value("#DIV/0!"))
        );
    }

    @ParameterizedTest
    @MethodSource({
            "provideGreaterThanOrEqualTo",
            "provideCommonResponses"
    })
    void shouldGreaterThanOrEqualToReturnExpectedValue(final Value givenLeftValue,
                                                       final Value givenRightValue,
                                                       final Value expectedValue) {
        // Given
        final NumericalComparisonFunction numericalComparisonFunction = NumericalComparisonFunction.ofGreaterThanOrEqualTo();

        // When
        final Value result = numericalComparisonFunction.execute(givenLeftValue, givenRightValue, new NumericalContext());

        // Then
        assertThat(result).isEqualTo(expectedValue);
    }

    public static Stream<Arguments> provideGreaterThanOrEqualTo() {
        return Stream.of(
                Arguments.of(new Value("660"), new Value("260"), new Value("true")),
                Arguments.of(new Value("260"), new Value("660"), new Value("false")),
                Arguments.of(new Value("260"), new Value("260"), new Value("true")));
    }

    @ParameterizedTest
    @MethodSource({
            "provideLessThan",
            "provideCommonResponses"
    })
    void shouldLessThanReturnExpectedValue(final Value givenLeftValue,
                                           final Value givenRightValue,
                                           final Value expectedValue) {
        // Given
        final NumericalComparisonFunction numericalComparisonFunction = NumericalComparisonFunction.ofLessThan();

        // When
        final Value result = numericalComparisonFunction.execute(givenLeftValue, givenRightValue, new NumericalContext());

        // Then
        assertThat(result).isEqualTo(expectedValue);
    }

    public static Stream<Arguments> provideLessThan() {
        return Stream.of(
                Arguments.of(new Value("660"), new Value("260"), new Value("false")),
                Arguments.of(new Value("260"), new Value("660"), new Value("true")),
                Arguments.of(new Value("260"), new Value("260"), new Value("false")));
    }

    @ParameterizedTest
    @MethodSource({
            "provideLessThanOrEqualTo",
            "provideCommonResponses"
    })
    void shouldLessThanOrEqualToReturnExpectedValue(final Value givenLeftValue,
                                                    final Value givenRightValue,
                                                    final Value expectedValue) {
        // Given
        final NumericalComparisonFunction numericalComparisonFunction = NumericalComparisonFunction.ofLessThanOrEqualTo();

        // When
        final Value result = numericalComparisonFunction.execute(givenLeftValue, givenRightValue, new NumericalContext());

        // Then
        assertThat(result).isEqualTo(expectedValue);
    }

    public static Stream<Arguments> provideLessThanOrEqualTo() {
        return Stream.of(
                Arguments.of(new Value("660"), new Value("260"), new Value("false")),
                Arguments.of(new Value("260"), new Value("660"), new Value("true")),
                Arguments.of(new Value("260"), new Value("260"), new Value("true")));
    }
}