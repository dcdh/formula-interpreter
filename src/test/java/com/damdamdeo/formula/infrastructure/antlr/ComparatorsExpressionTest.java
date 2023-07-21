package com.damdamdeo.formula.infrastructure.antlr;


import com.damdamdeo.formula.domain.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class ComparatorsExpressionTest extends AbstractExpressionTest {
    @ParameterizedTest
    @MethodSource("provideComparisonsWithExpectedValues")
    public void shouldExecuteComparisonForStructuredReferenceLeftAndStructuredReferenceRight(final String leftValue,
                                                                                             final String givenComparison,
                                                                                             final String rightValue,
                                                                                             final String expectedValue) {
        // Given
        final String givenFormula = String.format("%s([@[North Sales Amount]],[@[South Sales Amount]])", givenComparison);
        final StructuredData givenStructuredData = new StructuredData(
                List.of(
                        new StructuredDatum(new Reference("North Sales Amount"), leftValue),
                        new StructuredDatum(new Reference("South Sales Amount"), rightValue)
                )
        );

        // When
        final ExecutionResult executionResult = antlrExecutor.execute(formula4Test(givenFormula), givenStructuredData);

        // Then
        assertThat(executionResult.result()).isEqualTo(
                new Value(expectedValue));
    }

    @ParameterizedTest
    @MethodSource("provideComparisonsWithExpectedValues")
    public void shouldExecuteComparisonForStructuredReferenceLeftAndValueRight(final String leftValue,
                                                                               final String givenComparison,
                                                                               final String rightValue,
                                                                               final String expectedValue) {
        // Given
        final String givenFormula = String.format("%s([@[North Sales Amount]],%s)", givenComparison, rightValue);
        final StructuredData givenStructuredData = new StructuredData(
                List.of(
                        new StructuredDatum(new Reference("North Sales Amount"), leftValue)
                )
        );

        // When
        final ExecutionResult executionResult = antlrExecutor.execute(formula4Test(givenFormula), givenStructuredData);

        // Then
        assertThat(executionResult.result()).isEqualTo(
                new Value(expectedValue));
    }

    @ParameterizedTest
    @MethodSource("provideComparisonsWithExpectedValues")
    public void shouldExecuteComparisonForValueLeftAndStructuredReferenceRight(final String leftValue,
                                                                               final String givenComparison,
                                                                               final String rightValue,
                                                                               final String expectedValue) {
        // Given
        final String givenFormula = String.format("%s(%s,[@[South Sales Amount]])", givenComparison, leftValue);
        final StructuredData givenStructuredData = new StructuredData(
                List.of(
                        new StructuredDatum(new Reference("South Sales Amount"), rightValue)
                )
        );

        // When
        final ExecutionResult executionResult = antlrExecutor.execute(formula4Test(givenFormula), givenStructuredData);

        // Then
        assertThat(executionResult.result()).isEqualTo(
                new Value(expectedValue));
    }

    @ParameterizedTest
    @MethodSource("provideComparisonsWithExpectedValues")
    public void shouldExecuteComparisonForValueLeftAndValueRight(final String leftValue,
                                                                 final String givenComparison,
                                                                 final String rightValue,
                                                                 final String expectedValue) {
        // Given
        final String givenFormula = String.format("%s(%s,%s)", givenComparison, leftValue, rightValue);
        final StructuredData givenStructuredData = new StructuredData(List.of());

        // When
        final ExecutionResult executionResult = antlrExecutor.execute(formula4Test(givenFormula), givenStructuredData);

        // Then
        assertThat(executionResult.result()).isEqualTo(
                new Value(expectedValue));
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
                Arguments.of("\"true\"", "EQ", "\"true\"", "true"),
                Arguments.of("\"true\"", "EQ", "\"false\"", "false"),
                Arguments.of("true", "EQ", "true", "true"),
                Arguments.of("true", "EQ", "false", "false"),
                Arguments.of("660", "NEQ", "260", "true"),
                Arguments.of("260", "NEQ", "660", "true"),
                Arguments.of("260", "NEQ", "260", "false"),
                Arguments.of("\"toto\"", "NEQ", "\"toto\"", "false"),
                Arguments.of("\"tata\"", "NEQ", "\"toto\"", "true"),
                Arguments.of("\"true\"", "NEQ", "\"true\"", "false"),
                Arguments.of("\"true\"", "NEQ", "\"false\"", "true"),
                Arguments.of("true", "NEQ", "true", "false"),
                Arguments.of("true", "NEQ", "false", "true"),
                Arguments.of("660", "LT", "260", "false"),
                Arguments.of("260", "LT", "660", "true"),
                Arguments.of("260", "LT", "260", "false"),
                Arguments.of("660", "LTE", "260", "false"),
                Arguments.of("260", "LTE", "660", "true"),
                Arguments.of("260", "LTE", "260", "true"));
    }

    @ParameterizedTest
    @MethodSource("provideAllComparators")
    public void shouldBeUnknownWhenOneStructuredReferenceIsUnknown(final String givenComparison) {
        // Given
        final String givenFormula = String.format("%s([@[North Sales Amount]],[@[South Sales Amount]])", givenComparison);
        final StructuredData givenStructuredData = new StructuredData(
                List.of(
                        new StructuredDatum(new Reference("North Sales Amount"), "660")
                )
        );

        // When
        final ExecutionResult executionResult = antlrExecutor.execute(formula4Test(givenFormula), givenStructuredData);

        // Then
        assertThat(executionResult.result()).isEqualTo(
                new Value("#REF!"));
    }

    @ParameterizedTest
    @MethodSource("provideAllComparators")
    public void shouldBeNotAvailableWhenLeftStructuredReferenceIsNull(final String givenComparison) {
        // Given
        final String givenFormula = String.format("%s([@[North Sales Amount]],[@[South Sales Amount]])", givenComparison);
        final StructuredData givenStructuredData = new StructuredData(
                List.of(
                        new StructuredDatum(new Reference("North Sales Amount"), (String) null),
                        new StructuredDatum(new Reference("South Sales Amount"), "260")
                )
        );

        // When
        final ExecutionResult executionResult = antlrExecutor.execute(formula4Test(givenFormula), givenStructuredData);

        // Then
        assertThat(executionResult.result()).isEqualTo(
                new Value("#NA!"));
    }

    @ParameterizedTest
    @MethodSource("provideAllComparators")
    public void shouldBeNotAvailableWhenRightStructuredReferenceIsNull(final String givenComparison) {
        // Given
        final String givenFormula = String.format("%s([@[North Sales Amount]],[@[South Sales Amount]])", givenComparison);
        final StructuredData givenStructuredData = new StructuredData(
                List.of(
                        new StructuredDatum(new Reference("North Sales Amount"), "660"),
                        new StructuredDatum(new Reference("South Sales Amount"), (String) null)
                )
        );

        // When
        final ExecutionResult executionResult = antlrExecutor.execute(formula4Test(givenFormula), givenStructuredData);

        // Then
        assertThat(executionResult.result()).isEqualTo(
                new Value("#NA!"));
    }

    private static Stream<Arguments> provideAllComparators() {
        return Stream.of(
                Arguments.of("ADD"),
                Arguments.of("SUB"),
                Arguments.of("DIV"),
                Arguments.of("MUL"),
                Arguments.of("GT"),
                Arguments.of("GTE"),
                Arguments.of("EQ"),
                Arguments.of("NEQ"),
                Arguments.of("LT"),
                Arguments.of("LTE"));
    }

    @ParameterizedTest
    @CsvSource({"ADD", "SUB", "DIV", "MUL", "GT", "GTE", "LT", "LTE"})
    public void shouldBeInErrorWhenOneStructuredReferenceIsNotANumerical(final String givenComparison) {
        // Given
        final String givenFormula = String.format("%s([@[North Sales Amount]],[@[South Sales Amount]])", givenComparison);
        final StructuredData givenStructuredData = new StructuredData(
                List.of(
                        new StructuredDatum(new Reference("North Sales Amount"), "660"),
                        new StructuredDatum(new Reference("South Sales Amount"), "boom")
                )
        );

        // When
        final ExecutionResult executionResult = antlrExecutor.execute(formula4Test(givenFormula), givenStructuredData);

        // Then
        assertThat(executionResult.result()).isEqualTo(
                new Value("#NUM!"));
    }

    @ParameterizedTest
    @MethodSource("provideComparatorFunctionsUsingArithmeticsFunction")
    public void shouldUseComparisonsFunctions(final String givenFormula) {
        // Given
        final StructuredData givenStructuredData = new StructuredData(
                List.of(
                        new StructuredDatum(new Reference("North Sales Amount"), "660")
                )
        );

        // When
        final ExecutionResult executionResult = antlrExecutor.execute(formula4Test(givenFormula), givenStructuredData);

        // Then
        assertThat(executionResult.result()).isEqualTo(
                new Value("true"));
    }

    private static Stream<Arguments> provideComparatorFunctionsUsingArithmeticsFunction() {
        return Stream.of(
                Arguments.of("LTE(ADD(100,160),ADD(100,160))"));
    }

    @Test
    public void shouldLogExecution() {
        // Given
        final String givenFormula = "GT(660,260)";
        final StructuredData givenStructuredData = new StructuredData(List.of());
        when(executedAtProvider.now())
                .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:30+01:00[Europe/Paris]")))
                .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:31+01:00[Europe/Paris]")))
                .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:32+01:00[Europe/Paris]")))
        ;

        // When
        final ExecutionResult executionResult = antlrExecutor.execute(formula4Test(givenFormula), givenStructuredData);

        // Then
        assertThat(executionResult.executions()).containsExactly(
                new AntlrExecution(new ExecutionId(new UUID(0, 0)), new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:30+01:00[Europe/Paris]")), 3, 5, Map.of(), Value.of("660")),
                new AntlrExecution(new ExecutionId(new UUID(0, 0)), new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:31+01:00[Europe/Paris]")), 7, 9, Map.of(), Value.of("260")),
                new AntlrExecution(new ExecutionId(new UUID(0, 0)), new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:32+01:00[Europe/Paris]")), 0, 10, Map.of(
                        new InputName("left"), Value.of("660"),
                        new InputName("right"), Value.of("260")
                ), Value.of("true"))
        );
    }
}
