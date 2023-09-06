package com.damdamdeo.formula.domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class InformationFunctionTest {
    @ParameterizedTest
    @MethodSource({
            "provideIsNotAvailable"
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

    public static Stream<Arguments> provideIsNotAvailable() {
        return Stream.of(
                Arguments.of(new Value("#NA!"), true),
                Arguments.of(new Value("#REF!"), false),
                Arguments.of(new Value("#NUM!"), false),
                Arguments.of(new Value("#DIV/0!"), false),
                Arguments.of(new Value("true"), false),
                Arguments.of(new Value("false"), false),
                Arguments.of(new Value("1"), false),
                Arguments.of(new Value("0"), false),
                Arguments.of(new Value("660"), false),
                Arguments.of(new Value("azerty"), false),
                Arguments.of(new Value(""), false)
        );
    }

    @ParameterizedTest
    @MethodSource({
            "provideIsError"
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

    public static Stream<Arguments> provideIsError() {
        return Stream.of(
                Arguments.of(new Value("#NA!"), true),
                Arguments.of(new Value("#REF!"), true),
                Arguments.of(new Value("#NUM!"), true),
                Arguments.of(new Value("#DIV/0!"), true),
                Arguments.of(new Value("true"), false),
                Arguments.of(new Value("false"), false),
                Arguments.of(new Value("1"), false),
                Arguments.of(new Value("0"), false),
                Arguments.of(new Value("660"), false),
                Arguments.of(new Value("azerty"), false),
                Arguments.of(new Value(""), false)
        );
    }

    @ParameterizedTest
    @MethodSource({
            "provideIsNumeric"
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

    public static Stream<Arguments> provideIsNumeric() {
        return Stream.of(
                Arguments.of(new Value("#NA!"), false),
                Arguments.of(new Value("#REF!"), false),
                Arguments.of(new Value("#NUM!"), false),
                Arguments.of(new Value("#DIV/0!"), false),
                Arguments.of(new Value("true"), false),
                Arguments.of(new Value("false"), false),
                Arguments.of(new Value("1"), true),
                Arguments.of(new Value("0"), true),
                Arguments.of(new Value("660"), true),
                Arguments.of(new Value("azerty"), false),
                Arguments.of(new Value(""), false)
        );
    }

    @ParameterizedTest
    @MethodSource({
            "provideIsText"
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

    public static Stream<Arguments> provideIsText() {
        return Stream.of(
                Arguments.of(new Value("#NA!"), false),
                Arguments.of(new Value("#REF!"), false),
                Arguments.of(new Value("#NUM!"), false),
                Arguments.of(new Value("#DIV/0!"), false),
                Arguments.of(new Value("true"), false),
                Arguments.of(new Value("false"), false),
                Arguments.of(new Value("1"), false),
                Arguments.of(new Value("0"), false),
                Arguments.of(new Value("660"), false),
                Arguments.of(new Value("azerty"), true),
                Arguments.of(new Value(""), true)
        );
    }

    @ParameterizedTest
    @MethodSource({
            "provideIsBlank"
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

    public static Stream<Arguments> provideIsBlank() {
        return Stream.of(
                Arguments.of(new Value("#NA!"), false),
                Arguments.of(new Value("#REF!"), false),
                Arguments.of(new Value("#NUM!"), false),
                Arguments.of(new Value("#DIV/0!"), false),
                Arguments.of(new Value("true"), false),
                Arguments.of(new Value("false"), false),
                Arguments.of(new Value("1"), false),
                Arguments.of(new Value("0"), false),
                Arguments.of(new Value("660"), false),
                Arguments.of(new Value("azerty"), false),
                Arguments.of(new Value(""), true)
        );
    }

    @ParameterizedTest
    @MethodSource({
            "provideIsLogical"
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

    public static Stream<Arguments> provideIsLogical() {
        return Stream.of(
                Arguments.of(new Value("#NA!"), false),
                Arguments.of(new Value("#REF!"), false),
                Arguments.of(new Value("#NUM!"), false),
                Arguments.of(new Value("#DIV/0!"), false),
                Arguments.of(new Value("true"), true),
                Arguments.of(new Value("false"), true),
                Arguments.of(new Value("1"), true),
                Arguments.of(new Value("0"), true),
                Arguments.of(new Value("660"), false),
                Arguments.of(new Value("azerty"), false),
                Arguments.of(new Value(""), false)
        );
    }

}