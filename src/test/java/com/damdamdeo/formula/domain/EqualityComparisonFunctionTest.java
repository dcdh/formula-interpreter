package com.damdamdeo.formula.domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class EqualityComparisonFunctionTest {
    @ParameterizedTest
    @MethodSource({
            "provideEqual",
            "provideCommonResponses"
    })
    void shouldOrReturnExpectedValue(final Value givenLeftValue,
                                     final Value givenRightValue,
                                     final Value expectedValue) {
        // Given
        final EqualityComparisonFunction equalityComparisonFunction = EqualityComparisonFunction.ofEqual();

        // When
        final Value result = equalityComparisonFunction.execute(givenLeftValue, givenRightValue, new NumericalContext());

        // Then
        assertThat(result).isEqualTo(expectedValue);
    }

    public static Stream<Arguments> provideEqual() {
        return Stream.of(
                Arguments.of(new Value("660"), new Value("260"), new Value("false")),
                Arguments.of(new Value("260"), new Value("660"), new Value("false")),
                Arguments.of(new Value("260"), new Value("260"), new Value("true")),
                Arguments.of(new Value("toto"), new Value("toto"), new Value("true")),
                Arguments.of(new Value("tata"), new Value("toto"), new Value("false")),
                Arguments.of(new Value("true"), new Value("true"), new Value("true")),
                Arguments.of(new Value("true"), new Value("false"), new Value("false")),
                Arguments.of(new Value("true"), new Value("true"), new Value("true")),
                Arguments.of(new Value("true"), new Value("false"), new Value("false"))
        );
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
                Arguments.of(new Value("#NA!"), new Value("#NA!"), new Value("#NA!")),
                Arguments.of(new Value("#REF!"), new Value("#REF!"), new Value("#REF!")),
                Arguments.of(new Value("#NUM!"), new Value("#NUM!"), new Value("#NUM!")),
                Arguments.of(new Value("#DIV/0!"), new Value("#DIV/0!"), new Value("#DIV/0!"))
        );
    }

    @ParameterizedTest
    @MethodSource({
            "provideNotEqual",
            "provideCommonResponses"
    })
    void shouldAndReturnExpectedValue(final Value givenLeftValue,
                                      final Value givenRightValue,
                                      final Value expectedValue) {
        // Given
        final EqualityComparisonFunction equalityComparisonFunction = EqualityComparisonFunction.ofNotEqual();

        // When
        final Value result = equalityComparisonFunction.execute(givenLeftValue, givenRightValue, new NumericalContext());

        // Then
        assertThat(result).isEqualTo(expectedValue);
    }

    public static Stream<Arguments> provideNotEqual() {
        return Stream.of(
                Arguments.of(new Value("660"), new Value("260"), new Value("true")),
                Arguments.of(new Value("260"), new Value("660"), new Value("true")),
                Arguments.of(new Value("260"), new Value("260"), new Value("false")),
                Arguments.of(new Value("toto"), new Value("toto"), new Value("false")),
                Arguments.of(new Value("tata"), new Value("toto"), new Value("true")),
                Arguments.of(new Value("true"), new Value("true"), new Value("false")),
                Arguments.of(new Value("true"), new Value("false"), new Value("true")),
                Arguments.of(new Value("true"), new Value("true"), new Value("false")),
                Arguments.of(new Value("true"), new Value("false"), new Value("true"))
        );
    }
}