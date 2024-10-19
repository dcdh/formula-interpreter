package com.damdamdeo.formula.infrastructure.antlr;

import com.damdamdeo.formula.domain.*;
import io.smallrye.mutiny.Uni;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class AntlrStructuredReferenceExpressionTest extends AbstractFunctionTest {
    @Test
    public void shouldReturnStructuredReferenceValue() {
        // Given
        final String givenFormula = "[@[% Commission]]";
        final List<StructuredReference> givenStructuredReferences = List.of(
                new StructuredReference(new Reference("% Commission"), "10%")
        );

        // When
        final Uni<EvaluationResult> executionResult = antlrExecutor.process(formula4Test(givenFormula),
                new PartEvaluationCallback(new NoOpPartEvaluationCallbackListener(), new NumericalContext(), givenStructuredReferences));

        // Then
        assertOnExecutionResultReceived(executionResult, executionResultToAssert ->
                assertThat(executionResultToAssert.evaluated().value())
                        .isEqualTo(new Value("10%"))
        );
    }

    @Test
    public void shouldReturnUnknownReferenceWhenStructureReferenceDoesNotExists() {
        // Given
        final String givenFormula = "[@[% Commission]]";
        final List<StructuredReference> givenStructuredReferences = List.of();

        // When
        final Uni<EvaluationResult> executionResult = antlrExecutor.process(formula4Test(givenFormula),
                new PartEvaluationCallback(new NoOpPartEvaluationCallbackListener(), new NumericalContext(), givenStructuredReferences));

        // Then
        assertOnExecutionResultReceived(executionResult, executionResultToAssert ->
                assertThat(executionResultToAssert.evaluated().value())
                        .isEqualTo(new Value("#REF!"))
        );
    }

    @Test
    public void shouldReturnNotAvailableWhenStructureReferenceValueIsNull() {
        // Given
        final String givenFormula = "[@[% Commission]]";
        final List<StructuredReference> givenStructuredReferences = List.of(
                new StructuredReference(new Reference("% Commission"), (String) null)
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

    @Test
    public void shouldLogExecution() {
        // Given
        final String givenFormula = "[@[% Commission]]";
        final List<StructuredReference> givenStructuredReferences = List.of(
                new StructuredReference(new Reference("% Commission"), "10%")
        );
        when(evaluatedAtProvider.now())
                .thenReturn(new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:00+01:00[Europe/Paris]")))
                .thenReturn(new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:01+01:00[Europe/Paris]")))
                .thenReturn(new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:02+01:00[Europe/Paris]")))
                .thenReturn(new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:03+01:00[Europe/Paris]")))
                .thenReturn(new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:04+01:00[Europe/Paris]")))
                .thenReturn(new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:05+01:00[Europe/Paris]")));

        // When
        final Uni<EvaluationResult> executionResult = antlrExecutor.process(formula4Test(givenFormula),
                new PartEvaluationCallback(new DebugPartEvaluationCallbackListener(evaluatedAtProvider), new NumericalContext(), givenStructuredReferences));

        // Then
        assertOnExecutionResultReceived(executionResult, executionResultToAssert ->
                assertThat(executionResultToAssert.intermediateResults()).containsExactly(
                        new IntermediateResult(
                                Value.of("10%"),
                                new Range(0, 16),
                                List.of(
                                        new Input(new InputName("structuredReference"), new Reference("% Commission"), new Range(3, 14))),
                                new EvaluationProcessedIn(
                                        new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:03+01:00[Europe/Paris]")),
                                        new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:04+01:00[Europe/Paris]"))))
                )
        );
    }

}
