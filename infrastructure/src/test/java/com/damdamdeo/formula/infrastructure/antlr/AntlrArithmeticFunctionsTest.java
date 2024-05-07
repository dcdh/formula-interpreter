package com.damdamdeo.formula.infrastructure.antlr;

import com.damdamdeo.formula.domain.*;
import com.damdamdeo.formula.domain.provider.ArithmeticFunctionTestProvider;
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

public class AntlrArithmeticFunctionsTest extends AbstractFunctionTest {
    @ParameterizedTest
    @MethodSource("provideOperationsWithExpectedValues")
    public void shouldExecuteOperationForStructuredReferenceLeftAndStructuredReferenceRight(final Value leftValue,
                                                                                            final String givenOperation,
                                                                                            final Value rightValue,
                                                                                            final Value expectedValue) {
        // Given
        final String givenFormula = String.format("%s([@[North Sales Amount]],[@[South Sales Amount]])", givenOperation);
        final StructuredReferences givenStructuredReferences = new StructuredReferences(
                List.of(
                        new StructuredReference(new Reference("North Sales Amount"), leftValue),
                        new StructuredReference(new Reference("South Sales Amount"), rightValue)
                )
        );

        // When
        final Uni<ExecutionResult> executionResult = antlrExecutor.execute(formula4Test(givenFormula), givenStructuredReferences,
                new NoOpExecutionWrapper());

        // Then
        assertOnExecutionResultReceived(executionResult, executionResultToAssert ->
                assertThat(executionResultToAssert.result().value())
                        .isEqualTo(expectedValue)
        );
    }

    @ParameterizedTest
    @MethodSource("provideOperationsWithExpectedValues")
    public void shouldExecuteOperationForStructuredReferenceLeftAndValueRight(final Value leftValue,
                                                                              final String givenOperation,
                                                                              final Value rightValue,
                                                                              final Value expectedValue) {
        // Given
        final String givenFormula = String.format("%s([@[North Sales Amount]],%s)", givenOperation, rightValue.value());
        final StructuredReferences givenStructuredReferences = new StructuredReferences(
                List.of(
                        new StructuredReference(new Reference("North Sales Amount"), leftValue)
                )
        );

        // When
        final Uni<ExecutionResult> executionResult = antlrExecutor.execute(formula4Test(givenFormula), givenStructuredReferences,
                new NoOpExecutionWrapper());

        // Then
        assertOnExecutionResultReceived(executionResult, executionResultToAssert ->
                assertThat(executionResultToAssert.result().value())
                        .isEqualTo(expectedValue)
        );
    }

    @ParameterizedTest
    @MethodSource("provideOperationsWithExpectedValues")
    public void shouldExecuteOperationForValueLeftAndStructuredReferenceRight(final Value leftValue,
                                                                              final String givenOperation,
                                                                              final Value rightValue,
                                                                              final Value expectedValue) {
        // Given
        final String givenFormula = String.format("%s(%s,[@[South Sales Amount]])", givenOperation, leftValue.value());
        final StructuredReferences givenStructuredReferences = new StructuredReferences(
                List.of(
                        new StructuredReference(new Reference("South Sales Amount"), rightValue)
                )
        );

        // When
        final Uni<ExecutionResult> executionResult = antlrExecutor.execute(formula4Test(givenFormula), givenStructuredReferences,
                new NoOpExecutionWrapper());

        // Then
        assertOnExecutionResultReceived(executionResult, executionResultToAssert ->
                assertThat(executionResultToAssert.result().value())
                        .isEqualTo(expectedValue)
        );
    }

    @ParameterizedTest
    @MethodSource("provideOperationsWithExpectedValues")
    public void shouldExecuteOperationForValueLeftAndValueRight(final Value leftValue,
                                                                final String givenOperation,
                                                                final Value rightValue,
                                                                final Value expectedValue) {
        // Given
        final String givenFormula = String.format("%s(%s,%s)", givenOperation, leftValue.value(), rightValue.value());
        final StructuredReferences givenStructuredReferences = new StructuredReferences(List.of());

        // When
        final Uni<ExecutionResult> executionResult = antlrExecutor.execute(formula4Test(givenFormula), givenStructuredReferences,
                new NoOpExecutionWrapper());

        // Then
        assertOnExecutionResultReceived(executionResult, executionResultToAssert ->
                assertThat(executionResultToAssert.result().value())
                        .isEqualTo(expectedValue)
        );
    }

    private static Stream<Arguments> provideOperationsWithExpectedValues() {
        return Stream.of(
                        ArithmeticFunctionTestProvider.provideAddition()
                                .map(addition -> Arguments.of(addition.get()[0], "ADD", addition.get()[1], addition.get()[2])),
                        ArithmeticFunctionTestProvider.provideSubtraction()
                                .map(addition -> Arguments.of(addition.get()[0], "SUB", addition.get()[1], addition.get()[2])),
                        ArithmeticFunctionTestProvider.provideDivision()
                                .map(addition -> Arguments.of(addition.get()[0], "DIV", addition.get()[1], addition.get()[2])),
                        ArithmeticFunctionTestProvider.provideMultiplication()
                                .map(addition -> Arguments.of(addition.get()[0], "MUL", addition.get()[1], addition.get()[2]))
                )
                .flatMap(Function.identity());
    }

    @Test
    public void shouldCompoundArithmeticFunctions() {
        // Given
        final String givenFormula = "DIV(ADD(2,MUL(2,4)),2)";
        final StructuredReferences givenStructuredReferences = new StructuredReferences(List.of());

        // When
        final Uni<ExecutionResult> executionResult = antlrExecutor.execute(formula4Test(givenFormula), givenStructuredReferences,
                new NoOpExecutionWrapper());

        // Then
        assertOnExecutionResultReceived(executionResult, executionResultToAssert ->
                assertThat(executionResultToAssert.result().value())
                        .isEqualTo(new Value("5"))
        );
    }

    @ParameterizedTest
    @MethodSource("provideOperations")
    public void shouldBeUnknownWhenOneStructuredReferenceIsUnknown(final String givenOperation) {
        // Given
        final String givenFormula = String.format("%s([@[North Sales Amount]],[@[South Sales Amount]])", givenOperation);
        final StructuredReferences givenStructuredReferences = new StructuredReferences(
                List.of(
                        new StructuredReference(new Reference("North Sales Amount"), "660")
                )
        );

        // When
        final Uni<ExecutionResult> executionResult = antlrExecutor.execute(formula4Test(givenFormula), givenStructuredReferences,
                new NoOpExecutionWrapper());

        // Then
        assertOnExecutionResultReceived(executionResult, executionResultToAssert ->
                assertThat(executionResultToAssert.result().value())
                        .isEqualTo(new Value("#REF!"))
        );
    }

    @ParameterizedTest
    @MethodSource("provideOperations")
    public void shouldBeNotAvailableWhenLeftStructuredReferenceIsNull(final String givenOperation) {
        // Given
        final String givenFormula = String.format("%s([@[North Sales Amount]],[@[South Sales Amount]])", givenOperation);
        final StructuredReferences givenStructuredReferences = new StructuredReferences(
                List.of(
                        new StructuredReference(new Reference("North Sales Amount"), (String) null),
                        new StructuredReference(new Reference("South Sales Amount"), "260")
                )
        );

        // When
        final Uni<ExecutionResult> executionResult = antlrExecutor.execute(formula4Test(givenFormula), givenStructuredReferences,
                new NoOpExecutionWrapper());

        // Then
        assertOnExecutionResultReceived(executionResult, executionResultToAssert ->
                assertThat(executionResultToAssert.result().value())
                        .isEqualTo(new Value("#NA!"))
        );
    }

    @ParameterizedTest
    @MethodSource("provideOperations")
    public void shouldBeNotAvailableWhenRightStructuredReferenceIsNull(final String givenOperation) {
        // Given
        final String givenFormula = String.format("%s([@[North Sales Amount]],[@[South Sales Amount]])", givenOperation);
        final StructuredReferences givenStructuredReferences = new StructuredReferences(
                List.of(
                        new StructuredReference(new Reference("North Sales Amount"), "660"),
                        new StructuredReference(new Reference("South Sales Amount"), (String) null)
                )
        );

        // When
        final Uni<ExecutionResult> executionResult = antlrExecutor.execute(formula4Test(givenFormula), givenStructuredReferences,
                new NoOpExecutionWrapper());

        // Then
        assertOnExecutionResultReceived(executionResult, executionResultToAssert ->
                assertThat(executionResultToAssert.result().value())
                        .isEqualTo(new Value("#NA!"))
        );
    }

    @ParameterizedTest
    @MethodSource("provideOperations")
    public void shouldBeInErrorWhenOneStructuredReferenceIsNotANumerical(final String givenOperation) {
        // Given
        final String givenFormula = String.format("%s([@[North Sales Amount]],[@[South Sales Amount]])", givenOperation);
        final StructuredReferences givenStructuredReferences = new StructuredReferences(
                List.of(
                        new StructuredReference(new Reference("North Sales Amount"), "660"),
                        new StructuredReference(new Reference("South Sales Amount"), "boom")
                )
        );

        // When
        final Uni<ExecutionResult> executionResult = antlrExecutor.execute(formula4Test(givenFormula), givenStructuredReferences,
                new NoOpExecutionWrapper());

        // Then
        assertOnExecutionResultReceived(executionResult, executionResultToAssert ->
                assertThat(executionResultToAssert.result().value())
                        .isEqualTo(new Value("#NUM!"))
        );
    }

    private static Stream<Arguments> provideOperations() {
        return Stream.of(
                Arguments.of("ADD"),
                Arguments.of("SUB"),
                Arguments.of("DIV"),
                Arguments.of("MUL")
        );
    }

    @Test
    public void shouldDivideByZeroProduceAnError() {
        // Given
        final String givenFormula = "DIV(10,0)";
        final StructuredReferences givenStructuredReferences = new StructuredReferences(List.of());

        // When
        final Uni<ExecutionResult> executionResult = antlrExecutor.execute(formula4Test(givenFormula), givenStructuredReferences,
                new NoOpExecutionWrapper());

        // Then
        assertOnExecutionResultReceived(executionResult, executionResultToAssert ->
                assertThat(executionResultToAssert.result().value())
                        .isEqualTo(new Value("#DIV/0!"))
        );
    }

    @Test
    public void shouldLogExecution() {
        // Given
        final String givenFormula = "DIV(10,0)";
        final StructuredReferences givenStructuredReferences = new StructuredReferences(List.of());
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
        final Uni<ExecutionResult> executionResult = antlrExecutor.execute(formula4Test(givenFormula), givenStructuredReferences,
                new LoggingExecutionWrapper(executedAtProvider));

        // Then
        assertOnExecutionResultReceived(executionResult, executionResultToAssert ->
                assertThat(executionResultToAssert.elementExecutions()).containsExactly(
                        new ElementExecution(
                                Value.of("#DIV/0!"),
                                new Range(0, 8),
                                List.of(
                                        new Input(new InputName("left"), Value.of("10"), new Range(4, 5)),
                                        new Input(new InputName("right"), Value.of("0"), new Range(7, 7))
                                ),
                                new ExecutionProcessedIn(
                                        new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:03+01:00[Europe/Paris]")),
                                        new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:08+01:00[Europe/Paris]")))),
                        new ElementExecution(
                                Value.of("10"),
                                new Range(4, 5),
                                List.of(),
                                new ExecutionProcessedIn(
                                        new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:04+01:00[Europe/Paris]")),
                                        new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:05+01:00[Europe/Paris]")))),
                        new ElementExecution(
                                Value.of("0"),
                                new Range(7, 7),
                                List.of(),
                                new ExecutionProcessedIn(
                                        new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:06+01:00[Europe/Paris]")),
                                        new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:07+01:00[Europe/Paris]"))))
                )
        );
    }
}
