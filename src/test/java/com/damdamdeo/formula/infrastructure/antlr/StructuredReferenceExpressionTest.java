package com.damdamdeo.formula.infrastructure.antlr;

import com.damdamdeo.formula.domain.*;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class StructuredReferenceExpressionTest extends AbstractExpressionTest {

    @Test
    public void shouldReturnStructuredReferenceValue() {
        // Given
        final String givenFormula = "[@[% Commission]]";
        final StructuredData givenStructuredData = new StructuredData(List.of(
                new StructuredDatum(new Reference("% Commission"), "10%")
        ));

        // When
        final ExecutionResult executionResult = antlrExecutor.execute(formula4Test(givenFormula), givenStructuredData);

        // Then
        assertThat(executionResult.result()).isEqualTo(
                new Value("10%"));
    }

    @Test
    public void shouldReturnUnknownReferenceWhenStructureReferenceDoesNotExists() {
        // Given
        final String givenFormula = "[@[% Commission]]";
        final StructuredData givenStructuredData = new StructuredData();

        // When
        final ExecutionResult executionResult = antlrExecutor.execute(formula4Test(givenFormula), givenStructuredData);

        // Then
        assertThat(executionResult.result()).isEqualTo(
                new Value("#REF!"));
    }

    @Test
    public void shouldReturnNotAvailableWhenStructureReferenceValueIsNull() {
        // Given
        final String givenFormula = "[@[% Commission]]";
        final StructuredData givenStructuredData = new StructuredData(List.of(
                new StructuredDatum(new Reference("% Commission"), (String) null)
        ));

        // When
        final ExecutionResult executionResult = antlrExecutor.execute(formula4Test(givenFormula), givenStructuredData);

        // Then
        assertThat(executionResult.result()).isEqualTo(
                new Value("#NA!"));
    }

    @Test
    public void shouldLogExecution() {
        // Given
        final String givenFormula = "[@[% Commission]]";
        final StructuredData givenStructuredData = new StructuredData(List.of(
                new StructuredDatum(new Reference("% Commission"), "10%")
        ));
        when(executedAtProvider.now())
                .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:30+01:00[Europe/Paris]")))
        ;

        // When
        final ExecutionResult executionResult = antlrExecutor.execute(formula4Test(givenFormula), givenStructuredData);

        // Then
        assertThat(executionResult.executions()).containsExactly(
                new AntlrExecution(new ExecutionId(new UUID(0, 0)), new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:30+01:00[Europe/Paris]")), 0, 16, Map.of(
                        new InputName("structuredReference"), new Reference("% Commission")
                ), Value.of("10%"))
        );
    }

}
