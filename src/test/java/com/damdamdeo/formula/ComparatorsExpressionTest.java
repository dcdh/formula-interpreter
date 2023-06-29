package com.damdamdeo.formula;


import com.damdamdeo.formula.result.ErrorResult;
import com.damdamdeo.formula.result.ExecutionResult;
import com.damdamdeo.formula.result.UnknownReferenceResult;
import com.damdamdeo.formula.result.ValueResult;
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

public class ComparatorsExpressionTest extends AbstractExpressionTest {
    @ParameterizedTest
    @MethodSource("provideComparisonsWithExpectedValues")
    public void shouldExecuteComparisonForStructuredReferenceLeftAndStructuredReferenceRight(final String leftValue,
                                                                                             final String givenComparison,
                                                                                             final String rightValue,
                                                                                             final String expectedValue) throws SyntaxErrorException {
        // Given
        final String givenFormula = String.format("%s([@[North Sales Amount]],[@[South Sales Amount]])", givenComparison);
        final StructuredData givenStructuredData = new StructuredData(
                List.of(
                        new StructuredDatum(new Reference("North Sales Amount"), new Value(leftValue)),
                        new StructuredDatum(new Reference("South Sales Amount"), new Value(rightValue))
                )
        );

        // When
        final ExecutionResult executionResult = executor.execute(formula4Test(givenFormula), givenStructuredData);

        // Then
        assertThat(executionResult).isEqualTo(
                new ExecutionResult(
                        new ValueResult(expectedValue)));
    }

    @ParameterizedTest
    @MethodSource("provideComparisonsWithExpectedValues")
    public void shouldExecuteComparisonForStructuredReferenceLeftAndValueRight(final String leftValue,
                                                                               final String givenComparison,
                                                                               final String rightValue,
                                                                               final String expectedValue) throws SyntaxErrorException {
        // Given
        final String givenFormula = String.format("%s([@[North Sales Amount]],%s)", givenComparison, rightValue);
        final StructuredData givenStructuredData = new StructuredData(
                List.of(
                        new StructuredDatum(new Reference("North Sales Amount"), new Value(leftValue))
                )
        );

        // When
        final ExecutionResult executionResult = executor.execute(formula4Test(givenFormula), givenStructuredData);

        // Then
        assertThat(executionResult).isEqualTo(
                new ExecutionResult(
                        new ValueResult(expectedValue)));
    }

    @ParameterizedTest
    @MethodSource("provideComparisonsWithExpectedValues")
    public void shouldExecuteComparisonForValueLeftAndStructuredReferenceRight(final String leftValue,
                                                                               final String givenComparison,
                                                                               final String rightValue,
                                                                               final String expectedValue) throws SyntaxErrorException {
        // Given
        final String givenFormula = String.format("%s(%s,[@[South Sales Amount]])", givenComparison, leftValue);
        final StructuredData givenStructuredData = new StructuredData(
                List.of(
                        new StructuredDatum(new Reference("South Sales Amount"), new Value(rightValue))
                )
        );

        // When
        final ExecutionResult executionResult = executor.execute(formula4Test(givenFormula), givenStructuredData);

        // Then
        assertThat(executionResult).isEqualTo(
                new ExecutionResult(
                        new ValueResult(expectedValue)));
    }

    @ParameterizedTest
    @MethodSource("provideComparisonsWithExpectedValues")
    public void shouldExecuteComparisonForValueLeftAndValueRight(final String leftValue,
                                                                 final String givenComparison,
                                                                 final String rightValue,
                                                                 final String expectedValue) throws SyntaxErrorException {
        // Given
        final String givenFormula = String.format("%s(%s,%s)", givenComparison, leftValue, rightValue);
        final StructuredData givenStructuredData = new StructuredData(List.of());

        // When
        final ExecutionResult executionResult = executor.execute(formula4Test(givenFormula), givenStructuredData);

        // Then
        assertThat(executionResult).isEqualTo(
                new ExecutionResult(
                        new ValueResult(expectedValue)));
    }

    private static Stream<Arguments> provideComparisonsWithExpectedValues() {
        return Stream.of(
                Arguments.of("660", "GT", "260", "true"),
                Arguments.of("260", "GT", "660", "false"),
                Arguments.of("260", "GT", "260", "false"),
                Arguments.of("660", "GTE", "260", "true"),
                Arguments.of("260", "GTE", "660", "false"),
                Arguments.of("260", "GTE", "260", "true"),
                Arguments.of("660", "EQ", "260", "false"),
                Arguments.of("260", "EQ", "660", "false"),
                Arguments.of("260", "EQ", "260", "true"),
                Arguments.of("\"toto\"", "EQ", "\"toto\"", "true"),
                Arguments.of("\"tata\"", "EQ", "\"toto\"", "false"),
                Arguments.of("660", "NEQ", "260", "true"),
                Arguments.of("260", "NEQ", "660", "true"),
                Arguments.of("260", "NEQ", "260", "false"),
                Arguments.of("\"toto\"", "NEQ", "\"toto\"", "false"),
                Arguments.of("\"tata\"", "NEQ", "\"toto\"", "true"),
                Arguments.of("660", "LT", "260", "false"),
                Arguments.of("260", "LT", "660", "true"),
                Arguments.of("260", "LT", "260", "false"),
                Arguments.of("660", "LTE", "260", "false"),
                Arguments.of("260", "LTE", "660", "true"),
                Arguments.of("260", "LTE", "260", "true")
        );
    }

    @ParameterizedTest
    @CsvSource({"ADD", "SUB", "DIV", "MUL", "GT", "GTE", "EQ", "NEQ", "LT", "LTE"})
    public void shouldBeUnknownWhenOneStructuredReferenceIsUnknown(final String givenComparison) throws SyntaxErrorException {
        // Given
        final String givenFormula = String.format("%s([@[North Sales Amount]],[@[South Sales Amount]])", givenComparison);
        final StructuredData givenStructuredData = new StructuredData(
                List.of(
                        new StructuredDatum(new Reference("North Sales Amount"), new Value("660"))
                )
        );

        // When
        final ExecutionResult executionResult = executor.execute(formula4Test(givenFormula), givenStructuredData);

        // Then
        assertThat(executionResult).isEqualTo(
                new ExecutionResult(
                        new UnknownReferenceResult()));
    }

    @ParameterizedTest
    @CsvSource({"ADD", "SUB", "DIV", "MUL", "GT", "GTE", "LT", "LTE"})
    public void shouldBeInErrorWhenOneStructuredReferenceIsNotANumerical(final String givenComparison) throws SyntaxErrorException {
        // Given
        final String givenFormula = String.format("%s([@[North Sales Amount]],[@[South Sales Amount]])", givenComparison);
        final StructuredData givenStructuredData = new StructuredData(
                List.of(
                        new StructuredDatum(new Reference("North Sales Amount"), new Value("660")),
                        new StructuredDatum(new Reference("South Sales Amount"), new Value("boom"))
                )
        );

        // When
        final ExecutionResult executionResult = executor.execute(formula4Test(givenFormula), givenStructuredData);

        // Then
        assertThat(executionResult).isEqualTo(
                new ExecutionResult(
                        new ErrorResult()));
    }

}
