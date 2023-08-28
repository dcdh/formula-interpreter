package com.damdamdeo.formula.infrastructure.antlr;

import com.damdamdeo.formula.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class ElementExecutionLoggerTest extends AbstractExpressionTest {

    private String givenFormula;

    @BeforeEach
    public void setupFormula() {
        this.givenFormula = """
                IF(EQ([@[Sales Person]],"Joe"),MUL(MUL([@[Sales Amount]],[@[% Commission]]),2),MUL([@[Sales Amount]],[@[% Commission]]))
                """;
    }

    @Test
    public void shouldLogExecutionForJoe() {
        // Given
        final StructuredData givenStructuredData = new StructuredData(List.of(
                new StructuredDatum(new Reference("Sales Person"), "Joe"),
                new StructuredDatum(new Reference("Sales Amount"), "200"),
                new StructuredDatum(new Reference("% Commission"), "0.10")
        ));
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
                .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:09+01:00[Europe/Paris]")))
                .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:10+01:00[Europe/Paris]")))
                .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:11+01:00[Europe/Paris]")))
                .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:12+01:00[Europe/Paris]")))
                .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:13+01:00[Europe/Paris]")))
                .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:14+01:00[Europe/Paris]")))
                .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:15+01:00[Europe/Paris]")))
                .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:16+01:00[Europe/Paris]")))
                .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:17+01:00[Europe/Paris]")))
                .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:18+01:00[Europe/Paris]")))
                .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:19+01:00[Europe/Paris]")));

        // When
        final ExecutionResult executionResult = antlrExecutor.execute(formula4Test(givenFormula), givenStructuredData,
                DebugFeature.ACTIVE);

        // Then
        assertThat(executionResult.elementExecutions()).containsExactly(
                new AntlrElementExecution(
                        new Position(0, 119),
                        Map.of(
                                new InputName("comparisonValue"), Value.of("true")),
                        Value.of("40"),
                        new ExecutionProcessedIn(
                                new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:01+01:00[Europe/Paris]")),
                                new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:18+01:00[Europe/Paris]")))),
                new AntlrElementExecution(
                        new Position(3, 29),
                        Map.of(
                                new InputName("left"), Value.of("Joe"),
                                new InputName("right"), Value.of("Joe")),
                        Value.of("true"),
                        new ExecutionProcessedIn(
                                new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:02+01:00[Europe/Paris]")),
                                new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:07+01:00[Europe/Paris]")))),
                new AntlrElementExecution(
                        new Position(6, 22),
                        Map.of(
                                new InputName("structuredReference"), new Reference("Sales Person")),
                        Value.of("Joe"),
                        new ExecutionProcessedIn(
                                new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:03+01:00[Europe/Paris]")),
                                new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:04+01:00[Europe/Paris]")))),
                new AntlrElementExecution(
                        new Position(24, 28), Map.of(), Value.of("Joe"),
                        new ExecutionProcessedIn(
                                new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:05+01:00[Europe/Paris]")),
                                new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:06+01:00[Europe/Paris]")))),
                new AntlrElementExecution(
                        new Position(31, 77),
                        Map.of(
                                new InputName("left"), Value.of("20"),
                                new InputName("right"), Value.of("2")),
                        Value.of("40"),
                        new ExecutionProcessedIn(
                                new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:08+01:00[Europe/Paris]")),
                                new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:17+01:00[Europe/Paris]")))),
                new AntlrElementExecution(
                        new Position(35, 74),
                        Map.of(
                                new InputName("left"), Value.of("200"),
                                new InputName("right"), Value.of("0.10")),
                        Value.of("20"),
                        new ExecutionProcessedIn(
                                new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:09+01:00[Europe/Paris]")),
                                new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:14+01:00[Europe/Paris]")))),
                new AntlrElementExecution(
                        new Position(39, 55),
                        Map.of(
                                new InputName("structuredReference"), new Reference("Sales Amount")),
                        Value.of("200"),
                        new ExecutionProcessedIn(
                                new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:10+01:00[Europe/Paris]")),
                                new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:11+01:00[Europe/Paris]")))),
                new AntlrElementExecution(
                        new Position(57, 73),
                        Map.of(
                                new InputName("structuredReference"), new Reference("% Commission")),
                        Value.of("0.10"),
                        new ExecutionProcessedIn(
                                new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:12+01:00[Europe/Paris]")),
                                new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:13+01:00[Europe/Paris]")))),
                new AntlrElementExecution(
                        new Position(76, 76), Map.of(), Value.of("2"),
                        new ExecutionProcessedIn(
                                new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:15+01:00[Europe/Paris]")),
                                new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:16+01:00[Europe/Paris]"))))
        );
    }

    @Test
    public void shouldLogExecutionForRobert() {
        // Given
        final StructuredData givenStructuredData = new StructuredData(List.of(
                new StructuredDatum(new Reference("Sales Person"), "Robert"),
                new StructuredDatum(new Reference("Sales Amount"), "200"),
                new StructuredDatum(new Reference("% Commission"), "0.10")
        ));
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
                .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:09+01:00[Europe/Paris]")))
                .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:10+01:00[Europe/Paris]")))
                .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:11+01:00[Europe/Paris]")))
                .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:12+01:00[Europe/Paris]")))
                .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:13+01:00[Europe/Paris]")))
                .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:14+01:00[Europe/Paris]")))
                .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:15+01:00[Europe/Paris]")));

        // When
        final ExecutionResult executionResult = antlrExecutor.execute(formula4Test(givenFormula), givenStructuredData,
                DebugFeature.ACTIVE);

        // Then
        assertThat(executionResult.elementExecutions()).containsExactly(
                new AntlrElementExecution(
                        new Position(0, 119),
                        Map.of(
                                new InputName("comparisonValue"), Value.of("false")),
                        Value.of("20"),
                        new ExecutionProcessedIn(
                                new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:01+01:00[Europe/Paris]")),
                                new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:14+01:00[Europe/Paris]")))),
                new AntlrElementExecution(
                        new Position(3, 29),
                        Map.of(
                                new InputName("left"), Value.of("Robert"),
                                new InputName("right"), Value.of("Joe")),
                        Value.of("false"),
                        new ExecutionProcessedIn(
                                new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:02+01:00[Europe/Paris]")),
                                new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:07+01:00[Europe/Paris]")))),
                new AntlrElementExecution(
                        new Position(6, 22),
                        Map.of(
                                new InputName("structuredReference"), new Reference("Sales Person")),
                        Value.of("Robert"),
                        new ExecutionProcessedIn(
                                new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:03+01:00[Europe/Paris]")),
                                new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:04+01:00[Europe/Paris]")))),
                new AntlrElementExecution(
                        new Position(24, 28), Map.of(), Value.of("Joe"),
                        new ExecutionProcessedIn(
                                new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:05+01:00[Europe/Paris]")),
                                new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:06+01:00[Europe/Paris]")))),
                new AntlrElementExecution(
                        new Position(79, 118),
                        Map.of(
                                new InputName("left"), Value.of("200"),
                                new InputName("right"), Value.of("0.10")),
                        Value.of("20"),
                        new ExecutionProcessedIn(
                                new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:08+01:00[Europe/Paris]")),
                                new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:13+01:00[Europe/Paris]")))),
                new AntlrElementExecution(
                        new Position(83, 99),
                        Map.of(
                                new InputName("structuredReference"), new Reference("Sales Amount")),
                        Value.of("200"),
                        new ExecutionProcessedIn(
                                new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:09+01:00[Europe/Paris]")),
                                new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:10+01:00[Europe/Paris]")))),
                new AntlrElementExecution(
                        new Position(101, 117),
                        Map.of(
                                new InputName("structuredReference"), new Reference("% Commission")),
                        Value.of("0.10"),
                        new ExecutionProcessedIn(
                                new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:11+01:00[Europe/Paris]")),
                                new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:12+01:00[Europe/Paris]"))))
        );
    }

}
