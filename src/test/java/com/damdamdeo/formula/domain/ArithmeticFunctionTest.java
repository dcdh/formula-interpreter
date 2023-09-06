package com.damdamdeo.formula.domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class ArithmeticFunctionTest {
    @ParameterizedTest
    @MethodSource({
            "provideAddition",
            "provideCommonResponses"
    })
    void shouldAdditionReturnExpectedValue(final Value givenLeftValue,
                                           final Value givenRightValue,
                                           final Value expectedValue) {
        // Given
        final ArithmeticFunction arithmeticFunction = ArithmeticFunction.ofAddition();

        // When
        final Value result = arithmeticFunction.execute(givenLeftValue, givenRightValue, new NumericalContext());

        // Then
        assertThat(result).isEqualTo(expectedValue);
    }

    public static Stream<Arguments> provideAddition() {
        return Stream.of(
                Arguments.of(new Value("660"), new Value("260"), new Value("920"))
        );
    }

    public static Stream<Arguments> provideCommonResponses() {
        return Stream.of(
                Arguments.of(new Value("azerty"), new Value("260"), new Value("#NUM!")),
                Arguments.of(new Value("true"), new Value("260"), new Value("#NUM!")),
                Arguments.of(new Value("false"), new Value("260"), new Value("#NUM!")),
                Arguments.of(new Value("#NA!"), new Value("260"), new Value("#NA!")),
                Arguments.of(new Value("#REF!"), new Value("260"), new Value("#REF!")),
                Arguments.of(new Value("#NUM!"), new Value("260"), new Value("#NUM!")),
                Arguments.of(new Value("#DIV/0!"), new Value("260"), new Value("#DIV/0!")),
                Arguments.of(new Value("660"), new Value("azerty"), new Value("#NUM!")),
                Arguments.of(new Value("660"), new Value("true"), new Value("#NUM!")),
                Arguments.of(new Value("660"), new Value("false"), new Value("#NUM!")),
                Arguments.of(new Value("660"), new Value("#NA!"), new Value("#NA!")),
                Arguments.of(new Value("660"), new Value("#REF!"), new Value("#REF!")),
                Arguments.of(new Value("660"), new Value("#NUM!"), new Value("#NUM!")),
                Arguments.of(new Value("660"), new Value("#DIV/0!"), new Value("#DIV/0!"))
        );
    }

    @ParameterizedTest
    @MethodSource({
            "provideSubtraction",
            "provideCommonResponses"
    })
    void shouldSubtractionReturnExpectedValue(final Value givenLeftValue,
                                              final Value givenRightValue,
                                              final Value expectedValue) {
        // Given
        final ArithmeticFunction arithmeticFunction = ArithmeticFunction.ofSubtraction();

        // When
        final Value result = arithmeticFunction.execute(givenLeftValue, givenRightValue, new NumericalContext());

        // Then
        assertThat(result).isEqualTo(expectedValue);
    }

    public static Stream<Arguments> provideSubtraction() {
        return Stream.of(
                Arguments.of(new Value("660"), new Value("260"), new Value("400"))
        );
    }

    @ParameterizedTest
    @MethodSource({
            "provideDivision",
            "provideCommonResponses"
    })
    void shouldDivisionReturnExpectedValue(final Value givenLeftValue,
                                           final Value givenRightValue,
                                           final Value expectedValue) {
        // Given
        final ArithmeticFunction arithmeticFunction = ArithmeticFunction.ofDivision();

        // When
        final Value result = arithmeticFunction.execute(givenLeftValue, givenRightValue, new NumericalContext());

        // Then
        assertThat(result).isEqualTo(expectedValue);
    }

    public static Stream<Arguments> provideDivision() {
        return Stream.of(
                Arguments.of(new Value("660"), new Value("260"), new Value("2.538462")),
                Arguments.of(new Value("660"), new Value("0"), new Value("#DIV/0!"))
        );
    }

    @ParameterizedTest
    @MethodSource({
            "provideMultiplication",
            "provideCommonResponses"
    })
    void shouldMultiplicationReturnExpectedValue(final Value givenLeftValue,
                                                 final Value givenRightValue,
                                                 final Value expectedValue) {
        // Given
        final ArithmeticFunction arithmeticFunction = ArithmeticFunction.ofMultiplication();

        // When
        final Value result = arithmeticFunction.execute(givenLeftValue, givenRightValue, new NumericalContext());

        // Then
        assertThat(result).isEqualTo(expectedValue);
    }

    public static Stream<Arguments> provideMultiplication() {
        return Stream.of(
                Arguments.of(new Value("660"), new Value("260"), new Value("171600"))
        );
    }
}