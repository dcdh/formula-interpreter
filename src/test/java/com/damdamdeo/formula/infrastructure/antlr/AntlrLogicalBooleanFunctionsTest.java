package com.damdamdeo.formula.infrastructure.antlr;

import com.damdamdeo.formula.domain.*;
import io.smallrye.mutiny.Uni;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class AntlrLogicalBooleanFunctionsTest extends AbstractFunctionTest {
    @ParameterizedTest
    @MethodSource("provideLogicalFunctionsWithExpectedValues")
    public void shouldExecuteLogicalFunctionsForStructuredReferenceLeftAndStructuredReferenceRight(
            final Value leftValue,
            final String givenLogicalFunction,
            final Value rightValue,
            final Value expectedValue) {
        // Given
        final String givenFormula = String.format("%s([@[North Sales Amount]],[@[South Sales Amount]])", givenLogicalFunction);
        final StructuredData givenStructuredData = new StructuredData(
                List.of(
                        new StructuredDatum(new Reference("North Sales Amount"), leftValue.value()),
                        new StructuredDatum(new Reference("South Sales Amount"), rightValue.value())
                )
        );

        // When
        final Uni<ExecutionResult> executionResult = antlrExecutor.execute(formula4Test(givenFormula), givenStructuredData,
                new NoOpExecutionWrapper());

        // Then
        assertOnExecutionResultReceived(executionResult, executionResultToAssert ->
                assertThat(executionResultToAssert.result().value())
                        .isEqualTo(expectedValue)
        );
    }

    @ParameterizedTest
    @MethodSource("provideLogicalFunctionsWithExpectedValues")
    public void shouldExecuteLogicalFunctionsForStructuredReferenceLeftAndValueRight(final Value leftValue,
                                                                                     final String givenLogicalFunction,
                                                                                     final Value rightValue,
                                                                                     final Value expectedValue) {
        // Given
        final String givenFormula;
        if (rightValue.isError() || rightValue.isText()) {
            givenFormula = String.format("""
                    %s([@[North Sales Amount]],"%s")""", givenLogicalFunction, rightValue.value());
        } else {
            givenFormula = String.format("""
                    %s([@[North Sales Amount]],%s)""", givenLogicalFunction, rightValue.value());
        }
        final StructuredData givenStructuredData = new StructuredData(
                List.of(
                        new StructuredDatum(new Reference("North Sales Amount"), leftValue.value())
                )
        );

        // When
        final Uni<ExecutionResult> executionResult = antlrExecutor.execute(formula4Test(givenFormula), givenStructuredData,
                new NoOpExecutionWrapper());

        // Then
        assertOnExecutionResultReceived(executionResult, executionResultToAssert ->
                assertThat(executionResultToAssert.result().value())
                        .isEqualTo(expectedValue)
        );
    }

    @ParameterizedTest
    @MethodSource("provideLogicalFunctionsWithExpectedValues")
    public void shouldExecuteLogicalFunctionsForValueLeftAndStructuredReferenceRight(final Value leftValue,
                                                                                     final String givenLogicalFunction,
                                                                                     final Value rightValue,
                                                                                     final Value expectedValue) {
        // Given
        final String givenFormula;
        if (leftValue.isError() || leftValue.isText()) {
            givenFormula = String.format("""
                    %s("%s",[@[South Sales Amount]])""", givenLogicalFunction, leftValue.value());
        } else {
            givenFormula = String.format("""
                    %s(%s,[@[South Sales Amount]])""", givenLogicalFunction, leftValue.value());
        }
        final StructuredData givenStructuredData = new StructuredData(
                List.of(
                        new StructuredDatum(new Reference("South Sales Amount"), rightValue.value())
                )
        );

        // When
        final Uni<ExecutionResult> executionResult = antlrExecutor.execute(formula4Test(givenFormula), givenStructuredData,
                new NoOpExecutionWrapper());

        // Then
        assertOnExecutionResultReceived(executionResult, executionResultToAssert ->
                assertThat(executionResultToAssert.result().value())
                        .isEqualTo(expectedValue)
        );
    }

    @ParameterizedTest
    @MethodSource("provideLogicalFunctionsWithExpectedValues")
    public void shouldExecuteLogicalFunctionsForValueLeftAndValueRight(final Value leftValue,
                                                                       final String givenLogicalFunction,
                                                                       final Value rightValue,
                                                                       final Value expectedValue) {
        // Given
        final String givenFormula;
        if ((leftValue.isError() || leftValue.isText()) && (rightValue.isError() || rightValue.isText())) {
            givenFormula = String.format("""
                    %s("%s","%s")""", givenLogicalFunction, leftValue.value(), rightValue.value());
        } else if (leftValue.isError() || leftValue.isText()) {
            givenFormula = String.format("""
                    %s("%s",%s)""", givenLogicalFunction, leftValue.value(), rightValue.value());
        } else if (rightValue.isError() || rightValue.isText()) {
            givenFormula = String.format("""
                    %s(%s,"%s")""", givenLogicalFunction, leftValue.value(), rightValue.value());
        } else {
            givenFormula = String.format("""
                    %s(%s,%s)""", givenLogicalFunction, leftValue.value(), rightValue.value());
        }
        final StructuredData givenStructuredData = new StructuredData(List.of());

        // When
        final Uni<ExecutionResult> executionResult = antlrExecutor.execute(formula4Test(givenFormula), givenStructuredData,
                new NoOpExecutionWrapper());

        // Then
        assertOnExecutionResultReceived(executionResult, executionResultToAssert ->
                assertThat(executionResultToAssert.result().value())
                        .isEqualTo(expectedValue)
        );
    }

    private static Stream<Arguments> provideLogicalFunctionsWithExpectedValues() {
        return Stream.of(
                        LogicalBooleanFunctionTest.provideAnd()
                                .map(and -> Arguments.of(and.get()[0], "AND", and.get()[1], and.get()[2])),
                        LogicalBooleanFunctionTest.provideCommonResponses()
                                .map(and -> Arguments.of(and.get()[0], "AND", and.get()[1], and.get()[2])),
                        LogicalBooleanFunctionTest.provideOr()
                                .map(or -> Arguments.of(or.get()[0], "OR", or.get()[1], or.get()[2])),
                        LogicalBooleanFunctionTest.provideCommonResponses()
                                .map(or -> Arguments.of(or.get()[0], "OR", or.get()[1], or.get()[2]))
                )
                .flatMap(Function.identity());
    }

    @ParameterizedTest
    @MethodSource("provideLogicalOperator")
    public void shouldBeUnknownWhenOneStructuredReferenceIsUnknown(final String givenLogicalFunction) {
        // Given
        final String givenFormula = String.format("%s([@[North Sales Amount]],[@[South Sales Amount]])", givenLogicalFunction);
        final StructuredData givenStructuredData = new StructuredData(
                List.of(
                        new StructuredDatum(new Reference("North Sales Amount"), "660")
                )
        );

        // When
        final Uni<ExecutionResult> executionResult = antlrExecutor.execute(formula4Test(givenFormula), givenStructuredData,
                new NoOpExecutionWrapper());

        // Then
        assertOnExecutionResultReceived(executionResult, executionResultToAssert ->
                assertThat(executionResultToAssert.result().value())
                        .isEqualTo(new Value("#REF!"))
        );
    }

    @ParameterizedTest
    @MethodSource("provideLogicalOperator")
    public void shouldBeNotAvailableWhenLeftStructuredReferenceIsNull(final String givenLogicalFunction) {
        // Given
        final String givenFormula = String.format("%s([@[North Sales Amount]],[@[South Sales Amount]])", givenLogicalFunction);
        final StructuredData givenStructuredData = new StructuredData(
                List.of(
                        new StructuredDatum(new Reference("North Sales Amount"), (String) null),
                        new StructuredDatum(new Reference("South Sales Amount"), "260")

                )
        );

        // When
        final Uni<ExecutionResult> executionResult = antlrExecutor.execute(formula4Test(givenFormula), givenStructuredData,
                new NoOpExecutionWrapper());

        // Then
        assertOnExecutionResultReceived(executionResult, executionResultToAssert ->
                assertThat(executionResultToAssert.result().value())
                        .isEqualTo(new Value("#NA!"))
        );
    }

    @ParameterizedTest
    @MethodSource("provideLogicalOperator")
    public void shouldBeNotAvailableWhenRightStructuredReferenceIsNull(final String givenLogicalFunction) {
        // Given
        final String givenFormula = String.format("%s([@[North Sales Amount]],[@[South Sales Amount]])", givenLogicalFunction);
        final StructuredData givenStructuredData = new StructuredData(
                List.of(
                        new StructuredDatum(new Reference("North Sales Amount"), "660"),
                        new StructuredDatum(new Reference("South Sales Amount"), (String) null)
                )
        );

        // When
        final Uni<ExecutionResult> executionResult = antlrExecutor.execute(formula4Test(givenFormula), givenStructuredData,
                new NoOpExecutionWrapper());

        // Then
        assertOnExecutionResultReceived(executionResult, executionResultToAssert ->
                assertThat(executionResultToAssert.result().value())
                        .isEqualTo(new Value("#NA!"))
        );
    }

    private static Stream<Arguments> provideLogicalOperator() {
        return Stream.of(
                Arguments.of("OR"),
                Arguments.of("AND"));
    }

    @ParameterizedTest
    @MethodSource("provideLogicalFunctionsUsingComparisonsFunction")
    public void shouldUseComparisonsFunctions(final String givenFormula) {
        // Given
        final StructuredData givenStructuredData = new StructuredData(
                List.of(
                        new StructuredDatum(new Reference("North Sales Amount"), "660")
                )
        );

        // When
        final Uni<ExecutionResult> executionResult = antlrExecutor.execute(formula4Test(givenFormula), givenStructuredData,
                new NoOpExecutionWrapper());

        // Then
        assertOnExecutionResultReceived(executionResult, executionResultToAssert ->
                assertThat(executionResultToAssert.result().value())
                        .isEqualTo(new Value("true"))
        );
    }

    private static Stream<Arguments> provideLogicalFunctionsUsingComparisonsFunction() {
        return Stream.of(
                Arguments.of("""
                        OR(EQ("true","true"),EQ("true","true"))"""),
                Arguments.of("""
                        AND(EQ("true","true"),EQ("true","true"))"""),
                Arguments.of("""
                        OR(EQ(true,true),EQ(true,true))"""),
                Arguments.of("""
                        AND(EQ(true,true),EQ(true,true))"""));
    }

    @Test
    public void shouldLogExecution() {
        // Given
        final String givenFormula = "AND(0,0)";
        final StructuredData givenStructuredData = new StructuredData(List.of());
        when(executedAtProvider.now())
                .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:00+01:00[Europe/Paris]")))
                .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:01+01:00[Europe/Paris]")))
                .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:02+01:00[Europe/Paris]")))
                .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:03+01:00[Europe/Paris]")))
                .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:04+01:00[Europe/Paris]")))
                .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:05+01:00[Europe/Paris]")))
                .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:06+01:00[Europe/Paris]")))
                .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:07+01:00[Europe/Paris]")))
                .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:08+01:00[Europe/Paris]")))
                .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:09+01:00[Europe/Paris]")));

        // When
        final Uni<ExecutionResult> executionResult = antlrExecutor.execute(formula4Test(givenFormula), givenStructuredData,
                new LoggingExecutionWrapper(executedAtProvider));

        // Then
        assertOnExecutionResultReceived(executionResult, executionResultToAssert ->
                assertThat(executionResultToAssert.elementExecutions()).containsExactly(
                        new ElementExecution(
                                Value.of("false"),
                                new Range(0, 7),
                                List.of(
                                        new Input(new InputName("left"), Value.of("0"), new Range(4, 4)),
                                        new Input(new InputName("right"), Value.of("0"), new Range(6, 6))),
                                new ExecutionProcessedIn(
                                        new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:03+01:00[Europe/Paris]")),
                                        new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:08+01:00[Europe/Paris]")))),
                        new ElementExecution(
                                Value.of("0"),
                                new Range(4, 4),
                                List.of(),
                                new ExecutionProcessedIn(
                                        new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:04+01:00[Europe/Paris]")),
                                        new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:05+01:00[Europe/Paris]")))),
                        new ElementExecution(
                                Value.of("0"),
                                new Range(6, 6),
                                List.of(),
                                new ExecutionProcessedIn(
                                        new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:06+01:00[Europe/Paris]")),
                                        new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:07+01:00[Europe/Paris]"))))
                )
        );
    }
}
