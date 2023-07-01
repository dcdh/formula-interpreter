package com.damdamdeo.formula;

import com.damdamdeo.formula.structuredreference.Reference;
import com.damdamdeo.formula.structuredreference.StructuredData;
import com.damdamdeo.formula.structuredreference.StructuredDatum;
import com.damdamdeo.formula.syntax.SyntaxErrorException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

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

        // When
        final ExecutionResult executionResult = executor.execute(formula4Test(givenFormula), givenStructuredData);

        // Then
        assertThat(executionResult.executions()).containsExactly(
                new AntlrExecution(new ExecutionId(new UUID(0, 0)), 6, 22, Map.of(
                        new InputName("structuredReference"), new Reference("Sales Person")
                ), Value.of("Joe")),
                new AntlrExecution(new ExecutionId(new UUID(0, 0)), 24, 28, Map.of(), Value.of("Joe")),
                new AntlrExecution(new ExecutionId(new UUID(0, 0)), 3, 29, Map.of(
                        new InputName("left"), Value.of("Joe"),
                        new InputName("right"), Value.of("Joe")
                ), Value.of("true")),
                new AntlrExecution(new ExecutionId(new UUID(0, 0)), 39, 55, Map.of(
                        new InputName("structuredReference"), new Reference("Sales Amount")
                ), Value.of("200")),
                new AntlrExecution(new ExecutionId(new UUID(0, 0)), 57, 73, Map.of(
                        new InputName("structuredReference"), new Reference("% Commission")
                ), Value.of("0.10")),
                new AntlrExecution(new ExecutionId(new UUID(0, 0)), 35, 74, Map.of(
                        new InputName("left"), Value.of("200"),
                        new InputName("right"), Value.of("0.10")
                ), Value.of("20")),
                new AntlrExecution(new ExecutionId(new UUID(0, 0)), 76, 76, Map.of(), Value.of("2")),
                new AntlrExecution(new ExecutionId(new UUID(0, 0)), 31, 77, Map.of(
                        new InputName("left"), Value.of("20"),
                        new InputName("right"), Value.of("2")
                ), Value.of("40")),
                new AntlrExecution(new ExecutionId(new UUID(0, 0)), 0, 119, Map.of(
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

        // When
        final ExecutionResult executionResult = executor.execute(formula4Test(givenFormula), givenStructuredData);

        // Then
        assertThat(executionResult.executions()).containsExactly(
                new AntlrExecution(new ExecutionId(new UUID(0, 0)), 6, 22, Map.of(
                        new InputName("structuredReference"), new Reference("Sales Person")
                ), Value.of("Robert")),
                new AntlrExecution(new ExecutionId(new UUID(0, 0)), 24, 28, Map.of(), Value.of("Joe")),
                new AntlrExecution(new ExecutionId(new UUID(0, 0)), 3, 29, Map.of(
                        new InputName("left"), Value.of("Robert"),
                        new InputName("right"), Value.of("Joe")
                ), Value.of("false")),
                new AntlrExecution(new ExecutionId(new UUID(0, 0)), 83, 99, Map.of(
                        new InputName("structuredReference"), new Reference("Sales Amount")
                ), Value.of("200")),
                new AntlrExecution(new ExecutionId(new UUID(0, 0)), 101, 117, Map.of(
                        new InputName("structuredReference"), new Reference("% Commission")
                ), Value.of("0.10")),
                new AntlrExecution(new ExecutionId(new UUID(0, 0)), 79, 118, Map.of(
                        new InputName("left"), Value.of("200"),
                        new InputName("right"), Value.of("0.10")
                ), Value.of("20")),
                new AntlrExecution(new ExecutionId(new UUID(0, 0)), 0, 119, Map.of(
                        new InputName("comparisonValue"), Value.of("false")
                ), Value.of("20"))
        );
    }

}
