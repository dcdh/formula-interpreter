package com.damdamdeo.formula.infrastructure.antlr;

import com.damdamdeo.formula.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class ExecutionLoggerTest extends AbstractExpressionTest {

    private String givenFormula;

    @BeforeEach
    public void setupFormula() {
        this.givenFormula = """
                IF(EQ([@[Sales Person]],"Joe"),MUL(MUL([@[Sales Amount]],[@[% Commission]]),2),MUL([@[Sales Amount]],[@[% Commission]]))
                """;
    }

    @Test
    public void shouldLogExecutionForJoe() throws SyntaxErrorException {
        // Given
        final StructuredData givenStructuredData = new StructuredData(List.of(
                new StructuredDatum(new Reference("Sales Person"), "Joe"),
                new StructuredDatum(new Reference("Sales Amount"), "200"),
                new StructuredDatum(new Reference("% Commission"), "0.10")
        ));
        when(executedAtProvider.now())
                .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:30+01:00[Europe/Paris]")))
                .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:31+01:00[Europe/Paris]")))
                .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:32+01:00[Europe/Paris]")))
                .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:33+01:00[Europe/Paris]")))
                .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:34+01:00[Europe/Paris]")))
                .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:35+01:00[Europe/Paris]")))
                .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:36+01:00[Europe/Paris]")))
                .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:37+01:00[Europe/Paris]")))
                .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:38+01:00[Europe/Paris]")))
        ;

        // When
        final ExecutionResult executionResult = antlrExecutor.execute(formula4Test(givenFormula), givenStructuredData);

        // Then
        assertThat(executionResult.executions()).containsExactly(
                new AntlrExecution(new ExecutionId(new UUID(0, 0)), new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:30+01:00[Europe/Paris]")), 6, 22, Map.of(
                        new InputName("structuredReference"), new Reference("Sales Person")
                ), Value.of("Joe")),
                new AntlrExecution(new ExecutionId(new UUID(0, 0)), new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:31+01:00[Europe/Paris]")), 24, 28, Map.of(), Value.of("Joe")),
                new AntlrExecution(new ExecutionId(new UUID(0, 0)), new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:32+01:00[Europe/Paris]")), 3, 29, Map.of(
                        new InputName("left"), Value.of("Joe"),
                        new InputName("right"), Value.of("Joe")
                ), Value.of("true")),
                new AntlrExecution(new ExecutionId(new UUID(0, 0)), new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:33+01:00[Europe/Paris]")), 39, 55, Map.of(
                        new InputName("structuredReference"), new Reference("Sales Amount")
                ), Value.of("200")),
                new AntlrExecution(new ExecutionId(new UUID(0, 0)), new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:34+01:00[Europe/Paris]")), 57, 73, Map.of(
                        new InputName("structuredReference"), new Reference("% Commission")
                ), Value.of("0.10")),
                new AntlrExecution(new ExecutionId(new UUID(0, 0)), new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:35+01:00[Europe/Paris]")), 35, 74, Map.of(
                        new InputName("left"), Value.of("200"),
                        new InputName("right"), Value.of("0.10")
                ), Value.of("20")),
                new AntlrExecution(new ExecutionId(new UUID(0, 0)), new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:36+01:00[Europe/Paris]")), 76, 76, Map.of(), Value.of("2")),
                new AntlrExecution(new ExecutionId(new UUID(0, 0)), new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:37+01:00[Europe/Paris]")), 31, 77, Map.of(
                        new InputName("left"), Value.of("20"),
                        new InputName("right"), Value.of("2")
                ), Value.of("40")),
                new AntlrExecution(new ExecutionId(new UUID(0, 0)), new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:38+01:00[Europe/Paris]")), 0, 119, Map.of(
                        new InputName("comparisonValue"), Value.of("true")
                ), Value.of("40"))
        );
    }

    @Test
    public void shouldLogExecutionForRobert() throws SyntaxErrorException {
        // Given
        final StructuredData givenStructuredData = new StructuredData(List.of(
                new StructuredDatum(new Reference("Sales Person"), "Robert"),
                new StructuredDatum(new Reference("Sales Amount"), "200"),
                new StructuredDatum(new Reference("% Commission"), "0.10")
        ));
        when(executedAtProvider.now())
                .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:30+01:00[Europe/Paris]")))
                .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:31+01:00[Europe/Paris]")))
                .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:32+01:00[Europe/Paris]")))
                .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:33+01:00[Europe/Paris]")))
                .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:34+01:00[Europe/Paris]")))
                .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:35+01:00[Europe/Paris]")))
                .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:36+01:00[Europe/Paris]")))
                .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:37+01:00[Europe/Paris]")))
        ;

        // When
        final ExecutionResult executionResult = antlrExecutor.execute(formula4Test(givenFormula), givenStructuredData);

        // Then
        assertThat(executionResult.executions()).containsExactly(
                new AntlrExecution(new ExecutionId(new UUID(0, 0)), new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:30+01:00[Europe/Paris]")), 6, 22, Map.of(
                        new InputName("structuredReference"), new Reference("Sales Person")
                ), Value.of("Robert")),
                new AntlrExecution(new ExecutionId(new UUID(0, 0)), new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:31+01:00[Europe/Paris]")), 24, 28, Map.of(),
                        Value.of("Joe")),
                new AntlrExecution(new ExecutionId(new UUID(0, 0)), new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:32+01:00[Europe/Paris]")), 3, 29, Map.of(
                        new InputName("left"), Value.of("Robert"),
                        new InputName("right"), Value.of("Joe")
                ), Value.of("false")),
                new AntlrExecution(new ExecutionId(new UUID(0, 0)), new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:33+01:00[Europe/Paris]")), 83, 99, Map.of(
                        new InputName("structuredReference"), new Reference("Sales Amount")
                ), Value.of("200")),
                new AntlrExecution(new ExecutionId(new UUID(0, 0)), new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:34+01:00[Europe/Paris]")), 101, 117, Map.of(
                        new InputName("structuredReference"), new Reference("% Commission")
                ), Value.of("0.10")),
                new AntlrExecution(new ExecutionId(new UUID(0, 0)), new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:35+01:00[Europe/Paris]")), 79, 118, Map.of(
                        new InputName("left"), Value.of("200"),
                        new InputName("right"), Value.of("0.10")
                ), Value.of("20")),
                new AntlrExecution(new ExecutionId(new UUID(0, 0)), new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:36+01:00[Europe/Paris]")), 0, 119, Map.of(
                        new InputName("comparisonValue"), Value.of("false")
                ), Value.of("20"))
        );
    }

}
