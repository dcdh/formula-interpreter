package com.damdamdeo.formula.infrastructure.antlr;

import com.damdamdeo.formula.domain.*;
import io.smallrye.mutiny.Uni;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class StructuredReferenceExpressionTest extends AbstractExecutionTest {

    @Test
    public void shouldReturnStructuredReferenceValue() {
        // Given
        final String givenFormula = "[@[% Commission]]";
        final StructuredData givenStructuredData = new StructuredData(List.of(
                new StructuredDatum(new Reference("% Commission"), "10%")
        ));

        // When
        final Uni<ExecutionResult> executionResult = antlrExecutor.execute(formula4Test(givenFormula), givenStructuredData,
                DebugFeature.ACTIVE);

        // Then
        assertOnExecutionResultReceived(executionResult, executionResultToAssert ->
                assertThat(executionResultToAssert.result())
                        .isEqualTo(new Value("10%"))
        );
    }

    @Test
    public void shouldReturnUnknownReferenceWhenStructureReferenceDoesNotExists() {
        // Given
        final String givenFormula = "[@[% Commission]]";
        final StructuredData givenStructuredData = new StructuredData();

        // When
        final Uni<ExecutionResult> executionResult = antlrExecutor.execute(formula4Test(givenFormula), givenStructuredData,
                DebugFeature.ACTIVE);

        // Then
        assertOnExecutionResultReceived(executionResult, executionResultToAssert ->
                assertThat(executionResultToAssert.result())
                        .isEqualTo(new Value("#REF!"))
        );
    }

    @Test
    public void shouldReturnNotAvailableWhenStructureReferenceValueIsNull() {
        // Given
        final String givenFormula = "[@[% Commission]]";
        final StructuredData givenStructuredData = new StructuredData(List.of(
                new StructuredDatum(new Reference("% Commission"), (String) null)
        ));

        // When
        final Uni<ExecutionResult> executionResult = antlrExecutor.execute(formula4Test(givenFormula), givenStructuredData,
                DebugFeature.ACTIVE);

        // Then
        assertOnExecutionResultReceived(executionResult, executionResultToAssert ->
                assertThat(executionResultToAssert.result())
                        .isEqualTo(new Value("#NA!"))
        );
    }

    @Test
    public void shouldLogExecution() {
        // Given
        final String givenFormula = "[@[% Commission]]";
        final StructuredData givenStructuredData = new StructuredData(List.of(
                new StructuredDatum(new Reference("% Commission"), "10%")
        ));
        when(executedAtProvider.now())
                .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:00+01:00[Europe/Paris]")))
                .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:01+01:00[Europe/Paris]")))
                .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:02+01:00[Europe/Paris]")))
                .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:03+01:00[Europe/Paris]")));

        // When
        final Uni<ExecutionResult> executionResult = antlrExecutor.execute(formula4Test(givenFormula), givenStructuredData,
                DebugFeature.ACTIVE);

        // Then
        assertOnExecutionResultReceived(executionResult, executionResultToAssert ->
                assertThat(executionResultToAssert.elementExecutions()).containsExactly(
                        new AntlrElementExecution(
                                new Position(0, 16),
                                Map.of(
                                        new InputName("structuredReference"), new Reference("% Commission")),
                                Value.of("10%"),
                                new ExecutionProcessedIn(
                                        new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:01+01:00[Europe/Paris]")),
                                        new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:02+01:00[Europe/Paris]"))))
                )
        );
    }

}
