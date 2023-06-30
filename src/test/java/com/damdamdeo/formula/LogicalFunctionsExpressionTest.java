package com.damdamdeo.formula;

import com.damdamdeo.formula.structuredreference.Reference;
import com.damdamdeo.formula.structuredreference.StructuredData;
import com.damdamdeo.formula.structuredreference.StructuredDatum;
import com.damdamdeo.formula.syntax.SyntaxErrorException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class LogicalFunctionsExpressionTest extends AbstractExpressionTest {

    @ParameterizedTest
    @MethodSource("provideLogicalFunctionsWithExpectedValues")
    public void shouldExecuteLogicalFunctionsForStructuredReferenceLeftAndStructuredReferenceRight(final String leftValue,
                                                                                                   final String givenLogicalFunction,
                                                                                                   final String rightValue,
                                                                                                   final String expectedValue) throws SyntaxErrorException {
        // Given
        final String givenFormula = String.format("%s([@[North Sales Amount]],[@[South Sales Amount]])", givenLogicalFunction);
        final StructuredData givenStructuredData = new StructuredData(
                List.of(
                        new StructuredDatum(new Reference("North Sales Amount"), new Value(leftValue)),
                        new StructuredDatum(new Reference("South Sales Amount"), new Value(rightValue))
                )
        );

        // When
        final ExecutionResult executionResult = executor.execute(formula4Test(givenFormula), givenStructuredData);

        // Then
        assertThat(executionResult.result()).isEqualTo(
                new Value(expectedValue));
    }

    @ParameterizedTest
    @MethodSource("provideLogicalFunctionsWithExpectedValues")
    public void shouldExecuteLogicalFunctionsForStructuredReferenceLeftAndValueRight(final String leftValue,
                                                                                     final String givenLogicalFunction,
                                                                                     final String rightValue,
                                                                                     final String expectedValue) throws SyntaxErrorException {
        // Given
        final String givenFormula = String.format("%s([@[North Sales Amount]],%s)", givenLogicalFunction, rightValue);
        final StructuredData givenStructuredData = new StructuredData(
                List.of(
                        new StructuredDatum(new Reference("North Sales Amount"), new Value(leftValue))
                )
        );

        // When
        final ExecutionResult executionResult = executor.execute(formula4Test(givenFormula), givenStructuredData);

        // Then
        assertThat(executionResult.result()).isEqualTo(
                new Value(expectedValue));
    }

    @ParameterizedTest
    @MethodSource("provideLogicalFunctionsWithExpectedValues")
    public void shouldExecuteLogicalFunctionsForValueLeftAndStructuredReferenceRight(final String leftValue,
                                                                                     final String givenLogicalFunction,
                                                                                     final String rightValue,
                                                                                     final String expectedValue) throws SyntaxErrorException {
        // Given
        final String givenFormula = String.format("%s(%s,[@[South Sales Amount]])", givenLogicalFunction, leftValue);
        final StructuredData givenStructuredData = new StructuredData(
                List.of(
                        new StructuredDatum(new Reference("South Sales Amount"), new Value(rightValue))
                )
        );

        // When
        final ExecutionResult executionResult = executor.execute(formula4Test(givenFormula), givenStructuredData);

        // Then
        assertThat(executionResult.result()).isEqualTo(
                new Value(expectedValue));
    }

    @ParameterizedTest
    @MethodSource("provideLogicalFunctionsWithExpectedValues")
    public void shouldExecuteLogicalFunctionsForValueLeftAndValueRight(final String leftValue,
                                                                       final String givenLogicalFunction,
                                                                       final String rightValue,
                                                                       final String expectedValue) throws SyntaxErrorException {
        // Given
        final String givenFormula = String.format("%s(%s,%s)", givenLogicalFunction, leftValue, rightValue);
        final StructuredData givenStructuredData = new StructuredData(List.of());

        // When
        final ExecutionResult executionResult = executor.execute(formula4Test(givenFormula), givenStructuredData);

        // Then
        assertThat(executionResult.result()).isEqualTo(
                new Value(expectedValue));
    }

    private static Stream<Arguments> provideLogicalFunctionsWithExpectedValues() {
        return Stream.of(
                Arguments.of("0", "AND", "0", "false"),
                Arguments.of("0", "AND", "1", "false"),
                Arguments.of("1", "AND", "0", "false"),
                Arguments.of("1", "AND", "1", "true"),
                Arguments.of("1", "AND", "\"true\"", "true"),
                Arguments.of("\"true\"", "AND", "1", "true"),
                Arguments.of("\"true\"", "AND", "\"true\"", "true"),
                Arguments.of("0", "AND", "\"true\"", "false"),
                Arguments.of("\"true\"", "AND", "\"boom\"", "false"),
                Arguments.of("0", "OR", "0", "false"),
                Arguments.of("0", "OR", "1", "true"),
                Arguments.of("1", "OR", "0", "true"),
                Arguments.of("1", "OR", "1", "true"),
                Arguments.of("1", "OR", "\"true\"", "true"),
                Arguments.of("\"true\"", "OR", "1", "true"),
                Arguments.of("\"true\"", "OR", "\"true\"", "true"),
                Arguments.of("0", "OR", "\"true\"", "true"),
                Arguments.of("\"true\"", "OR", "\"boom\"", "true"));
    }

    @ParameterizedTest
    @CsvSource({"AND", "OR"})
    public void shouldBeUnknownWhenOneStructuredReferenceIsUnknown(final String givenLogicalFunction) throws SyntaxErrorException {
        // Given
        final String givenFormula = String.format("%s([@[North Sales Amount]],[@[South Sales Amount]])", givenLogicalFunction);
        final StructuredData givenStructuredData = new StructuredData(
                List.of(
                        new StructuredDatum(new Reference("North Sales Amount"), new Value("660"))
                )
        );

        // When
        final ExecutionResult executionResult = executor.execute(formula4Test(givenFormula), givenStructuredData);

        // Then
        assertThat(executionResult.result()).isEqualTo(
                new Value("#REF!"));
    }

    @ParameterizedTest
    @MethodSource("provideLogicalFunctionsUsingComparisonsFunction")
    public void shouldUseComparisonsFunctions(final String givenFormula) throws SyntaxErrorException {
        // Given
        final StructuredData givenStructuredData = new StructuredData(
                List.of(
                        new StructuredDatum(new Reference("North Sales Amount"), new Value("660"))
                )
        );

        // When
        final ExecutionResult executionResult = executor.execute(formula4Test(givenFormula), givenStructuredData);

        // Then
        assertThat(executionResult.result()).isEqualTo(
                new Value("true"));
    }

    private static Stream<Arguments> provideLogicalFunctionsUsingComparisonsFunction() {
        return Stream.of(
                Arguments.of("OR(EQ(\"true\",\"true\"),EQ(\"true\",\"true\"))"),
                Arguments.of("AND(EQ(\"true\",\"true\"),EQ(\"true\",\"true\"))"));
    }
}
