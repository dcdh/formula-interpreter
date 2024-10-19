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
    public void shouldEvaluateOperationForStructuredReferenceLeftAndStructuredReferenceRight(final Value leftValue,
                                                                                             final String givenOperation,
                                                                                             final Value rightValue,
                                                                                             final Value expectedValue) {
        // Given
        final String givenFormula = String.format("%s([@[North Sales Amount]],[@[South Sales Amount]])", givenOperation);
        final List<StructuredReference> givenStructuredReferences = List.of(
                new StructuredReference(new Reference("North Sales Amount"), leftValue),
                new StructuredReference(new Reference("South Sales Amount"), rightValue)
        );

        // When
        final Uni<EvaluationResult> executionResult = antlrExecutor.process(formula4Test(givenFormula),
                new PartEvaluationCallback(new NoOpPartEvaluationCallbackListener(), new NumericalContext(), givenStructuredReferences));

        // Then
        assertOnExecutionResultReceived(executionResult, executionResultToAssert ->
                assertThat(executionResultToAssert.evaluated().value())
                        .isEqualTo(expectedValue)
        );
    }

    @ParameterizedTest
    @MethodSource("provideOperationsWithExpectedValues")
    public void shouldEvaluateOperationForStructuredReferenceLeftAndValueRight(final Value leftValue,
                                                                               final String givenOperation,
                                                                               final Value rightValue,
                                                                               final Value expectedValue) {
        // Given
        final String givenFormula = String.format("%s([@[North Sales Amount]],%s)", givenOperation, rightValue.value());
        final List<StructuredReference> givenStructuredReferences = List.of(
                new StructuredReference(new Reference("North Sales Amount"), leftValue)
        );

        // When
        final Uni<EvaluationResult> executionResult = antlrExecutor.process(formula4Test(givenFormula),
                new PartEvaluationCallback(new NoOpPartEvaluationCallbackListener(), new NumericalContext(), givenStructuredReferences));

        // Then
        assertOnExecutionResultReceived(executionResult, executionResultToAssert ->
                assertThat(executionResultToAssert.evaluated().value())
                        .isEqualTo(expectedValue)
        );
    }

    @ParameterizedTest
    @MethodSource("provideOperationsWithExpectedValues")
    public void shouldEvaluateOperationForValueLeftAndStructuredReferenceRight(final Value leftValue,
                                                                               final String givenOperation,
                                                                               final Value rightValue,
                                                                               final Value expectedValue) {
        // Given
        final String givenFormula = String.format("%s(%s,[@[South Sales Amount]])", givenOperation, leftValue.value());
        final List<StructuredReference> givenStructuredReferences = List.of(
                new StructuredReference(new Reference("South Sales Amount"), rightValue)
        );

        // When
        final Uni<EvaluationResult> executionResult = antlrExecutor.process(formula4Test(givenFormula),
                new PartEvaluationCallback(new NoOpPartEvaluationCallbackListener(), new NumericalContext(), givenStructuredReferences));

        // Then
        assertOnExecutionResultReceived(executionResult, executionResultToAssert ->
                assertThat(executionResultToAssert.evaluated().value())
                        .isEqualTo(expectedValue)
        );
    }

    @ParameterizedTest
    @MethodSource("provideOperationsWithExpectedValues")
    public void shouldEvaluateOperationForValueLeftAndValueRight(final Value leftValue,
                                                                 final String givenOperation,
                                                                 final Value rightValue,
                                                                 final Value expectedValue) {
        // Given
        final String givenFormula = String.format("%s(%s,%s)", givenOperation, leftValue.value(), rightValue.value());
        final List<StructuredReference> givenStructuredReferences = List.of();

        // When
        final Uni<EvaluationResult> executionResult = antlrExecutor.process(formula4Test(givenFormula),
                new PartEvaluationCallback(new NoOpPartEvaluationCallbackListener(), new NumericalContext(), givenStructuredReferences));

        // Then
        assertOnExecutionResultReceived(executionResult, executionResultToAssert ->
                assertThat(executionResultToAssert.evaluated().value())
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
        final List<StructuredReference> givenStructuredReferences = List.of();

        // When
        final Uni<EvaluationResult> executionResult = antlrExecutor.process(formula4Test(givenFormula),
                new PartEvaluationCallback(new NoOpPartEvaluationCallbackListener(), new NumericalContext(), givenStructuredReferences));

        // Then
        assertOnExecutionResultReceived(executionResult, executionResultToAssert ->
                assertThat(executionResultToAssert.evaluated().value())
                        .isEqualTo(new Value("5"))
        );
    }

    @ParameterizedTest
    @MethodSource("provideOperations")
    public void shouldBeUnknownWhenOneStructuredReferenceIsUnknown(final String givenOperation) {
        // Given
        final String givenFormula = String.format("%s([@[North Sales Amount]],[@[South Sales Amount]])", givenOperation);
        final List<StructuredReference> givenStructuredReferences = List.of(
                new StructuredReference(new Reference("North Sales Amount"), "660")
        );

        // When
        final Uni<EvaluationResult> executionResult = antlrExecutor.process(formula4Test(givenFormula),
                new PartEvaluationCallback(new NoOpPartEvaluationCallbackListener(), new NumericalContext(), givenStructuredReferences));

        // Then
        assertOnExecutionResultReceived(executionResult, executionResultToAssert ->
                assertThat(executionResultToAssert.evaluated().value())
                        .isEqualTo(new Value("#REF!"))
        );
    }

    @ParameterizedTest
    @MethodSource("provideOperations")
    public void shouldBeNotAvailableWhenLeftStructuredReferenceIsNull(final String givenOperation) {
        // Given
        final String givenFormula = String.format("%s([@[North Sales Amount]],[@[South Sales Amount]])", givenOperation);
        final List<StructuredReference> givenStructuredReferences = List.of(
                new StructuredReference(new Reference("North Sales Amount"), (String) null),
                new StructuredReference(new Reference("South Sales Amount"), "260")
        );

        // When
        final Uni<EvaluationResult> executionResult = antlrExecutor.process(formula4Test(givenFormula),
                new PartEvaluationCallback(new NoOpPartEvaluationCallbackListener(), new NumericalContext(), givenStructuredReferences));

        // Then
        assertOnExecutionResultReceived(executionResult, executionResultToAssert ->
                assertThat(executionResultToAssert.evaluated().value())
                        .isEqualTo(new Value("#NA!"))
        );
    }

    @ParameterizedTest
    @MethodSource("provideOperations")
    public void shouldBeNotAvailableWhenRightStructuredReferenceIsNull(final String givenOperation) {
        // Given
        final String givenFormula = String.format("%s([@[North Sales Amount]],[@[South Sales Amount]])", givenOperation);
        final List<StructuredReference> givenStructuredReferences = List.of(
                new StructuredReference(new Reference("North Sales Amount"), "660"),
                new StructuredReference(new Reference("South Sales Amount"), (String) null)
        );

        // When
        final Uni<EvaluationResult> executionResult = antlrExecutor.process(formula4Test(givenFormula),
                new PartEvaluationCallback(new NoOpPartEvaluationCallbackListener(), new NumericalContext(), givenStructuredReferences));

        // Then
        assertOnExecutionResultReceived(executionResult, executionResultToAssert ->
                assertThat(executionResultToAssert.evaluated().value())
                        .isEqualTo(new Value("#NA!"))
        );
    }

    @ParameterizedTest
    @MethodSource("provideOperations")
    public void shouldBeInErrorWhenOneStructuredReferenceIsNotANumerical(final String givenOperation) {
        // Given
        final String givenFormula = String.format("%s([@[North Sales Amount]],[@[South Sales Amount]])", givenOperation);
        final List<StructuredReference> givenStructuredReferences = List.of(
                new StructuredReference(new Reference("North Sales Amount"), "660"),
                new StructuredReference(new Reference("South Sales Amount"), "boom")
        );

        // When
        final Uni<EvaluationResult> executionResult = antlrExecutor.process(formula4Test(givenFormula),
                new PartEvaluationCallback(new NoOpPartEvaluationCallbackListener(), new NumericalContext(), givenStructuredReferences));

        // Then
        assertOnExecutionResultReceived(executionResult, executionResultToAssert ->
                assertThat(executionResultToAssert.evaluated().value())
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
        final List<StructuredReference> givenStructuredReferences = List.of();

        // When
        final Uni<EvaluationResult> executionResult = antlrExecutor.process(formula4Test(givenFormula),
                new PartEvaluationCallback(new NoOpPartEvaluationCallbackListener(), new NumericalContext(), givenStructuredReferences));

        // Then
        assertOnExecutionResultReceived(executionResult, executionResultToAssert ->
                assertThat(executionResultToAssert.evaluated().value())
                        .isEqualTo(new Value("#DIV/0!"))
        );
    }

    @Test
    public void shouldLogExecution() {
        // Given
        final String givenFormula = "DIV(10,0)";
        final List<StructuredReference> givenStructuredReferences = List.of();
        when(evaluatedAtProvider.now())
                .thenReturn(new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:00+01:00[Europe/Paris]")))
                .thenReturn(new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:01+01:00[Europe/Paris]")))
                .thenReturn(new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:02+01:00[Europe/Paris]")))
                .thenReturn(new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:03+01:00[Europe/Paris]")))
                .thenReturn(new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:04+01:00[Europe/Paris]")))
                .thenReturn(new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:05+01:00[Europe/Paris]")))
                .thenReturn(new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:06+01:00[Europe/Paris]")))
                .thenReturn(new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:07+01:00[Europe/Paris]")))
                .thenReturn(new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:08+01:00[Europe/Paris]")))
                .thenReturn(new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:09+01:00[Europe/Paris]")));

        // When
        final Uni<EvaluationResult> executionResult = antlrExecutor.process(formula4Test(givenFormula),
                new PartEvaluationCallback(new DebugPartEvaluationCallbackListener(evaluatedAtProvider), new NumericalContext(), givenStructuredReferences));

        // Then
        assertOnExecutionResultReceived(executionResult, executionResultToAssert ->
                assertThat(executionResultToAssert.intermediateResults()).containsExactly(
                        new IntermediateResult(
                                Value.of("#DIV/0!"),
                                new Range(0, 8),
                                List.of(
                                        new Input(new InputName("left"), Value.of("10"), new Range(4, 5)),
                                        new Input(new InputName("right"), Value.of("0"), new Range(7, 7))
                                ),
                                new EvaluationProcessedIn(
                                        new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:03+01:00[Europe/Paris]")),
                                        new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:08+01:00[Europe/Paris]")))),
                        new IntermediateResult(
                                Value.of("10"),
                                new Range(4, 5),
                                List.of(),
                                new EvaluationProcessedIn(
                                        new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:04+01:00[Europe/Paris]")),
                                        new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:05+01:00[Europe/Paris]")))),
                        new IntermediateResult(
                                Value.of("0"),
                                new Range(7, 7),
                                List.of(),
                                new EvaluationProcessedIn(
                                        new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:06+01:00[Europe/Paris]")),
                                        new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:07+01:00[Europe/Paris]"))))
                )
        );
    }
}
