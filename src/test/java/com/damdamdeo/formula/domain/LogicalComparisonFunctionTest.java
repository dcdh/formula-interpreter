package com.damdamdeo.formula.domain;

import com.damdamdeo.formula.domain.spi.ValueProvider;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class LogicalComparisonFunctionTest {
    final ValueProvider givenOnTrue = Value::ofTrue;
    final ValueProvider givenOnFalse = Value::ofFalse;

    @ParameterizedTest
    @MethodSource({
            "provideIf"
    })
    void shouldIfReturnExpectedValue(final Value givenComparison,
                                     final Value expectedResult) {
        // Given
        final LogicalComparisonFunction logicalComparisonFunction = LogicalComparisonFunction.ofIf(givenOnTrue, givenOnFalse);

        // When
        final Value result = logicalComparisonFunction.execute(givenComparison);

        // Then
        assertThat(result).isEqualTo(expectedResult);
    }

    public static Stream<Arguments> provideIf() {
        return Stream.of(
                Arguments.of(new Value("#NA!"), new Value("#NA!")),
                Arguments.of(new Value("#REF!"), new Value("#REF!")),
                Arguments.of(new Value("#NUM!"), new Value("#NUM!")),
                Arguments.of(new Value("#DIV/0!"), new Value("#DIV/0!")),
                Arguments.of(new Value("true"), new Value("true")),
                Arguments.of(new Value("false"), new Value("false")),
                Arguments.of(new Value("0"), new Value("false")),
                Arguments.of(new Value("1"), new Value("true"))
        );
    }

    @ParameterizedTest
    @MethodSource({
            "provideIfError"
    })
    void shouldIfErrorReturnExpectedValue(final Value givenComparison,
                                          final Value expectedResult) {
        // Given
        final LogicalComparisonFunction logicalComparisonFunction = LogicalComparisonFunction.ofIfError(givenOnTrue, givenOnFalse);

        // When
        final Value result = logicalComparisonFunction.execute(givenComparison);

        // Then
        assertThat(result).isEqualTo(expectedResult);
    }

    public static Stream<Arguments> provideIfError() {
        return Stream.of(
                Arguments.of(new Value("#NA!"), new Value("true")),
                Arguments.of(new Value("#REF!"), new Value("true")),
                Arguments.of(new Value("#NUM!"), new Value("true")),
                Arguments.of(new Value("#DIV/0!"), new Value("true")),
                Arguments.of(new Value("true"), new Value("false")),
                Arguments.of(new Value("false"), new Value("false")),
                Arguments.of(new Value("0"), new Value("false")),
                Arguments.of(new Value("1"), new Value("false")),
                Arguments.of(new Value("660"), new Value("false")),
                Arguments.of(new Value(""), new Value("false"))
        );
    }

    @ParameterizedTest
    @MethodSource({
            "provideIfNotAvailable"
    })
    void shouldIfNotAvailableReturnExpectedValue(final Value givenComparison,
                                                 final Value expectedResult) {
        // Given
        final LogicalComparisonFunction logicalComparisonFunction = LogicalComparisonFunction.ofIfNotAvailable(givenOnTrue, givenOnFalse);

        // When
        final Value result = logicalComparisonFunction.execute(givenComparison);

        // Then
        assertThat(result).isEqualTo(expectedResult);
    }

    public static Stream<Arguments> provideIfNotAvailable() {
        return Stream.of(
                Arguments.of(new Value("#NA!"), new Value("true")),
                Arguments.of(new Value("#REF!"), new Value("false")),
                Arguments.of(new Value("#NUM!"), new Value("false")),
                Arguments.of(new Value("#DIV/0!"), new Value("false")),
                Arguments.of(new Value("true"), new Value("false")),
                Arguments.of(new Value("false"), new Value("false")),
                Arguments.of(new Value("0"), new Value("false")),
                Arguments.of(new Value("1"), new Value("false")),
                Arguments.of(new Value("660"), new Value("false")),
                Arguments.of(new Value(""), new Value("false"))
        );
    }
}