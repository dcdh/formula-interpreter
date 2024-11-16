package com.damdamdeo.formula.infrastructure.parser.antlr;

import com.damdamdeo.formula.domain.*;
import io.smallrye.mutiny.Uni;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ElementFunctionLoggerTest extends AbstractFunctionTest {
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
        final List<StructuredReference> givenStructuredReferences = List.of(
                new StructuredReference(new Reference("Sales Person"), "Joe"),
                new StructuredReference(new Reference("Sales Amount"), "200"),
                new StructuredReference(new Reference("% Commission"), "0.10")
        );

        // When
        final Uni<EvaluationResult> executionResult = antlrExecutor.process(formula4Test(givenFormula),
                new PartEvaluationCallback(new DebugPartEvaluationListener(evaluatedAtProvider), new NumericalContext(), givenStructuredReferences));

        // Then
        assertOnExecutionResultReceived(executionResult, executionResultToAssert ->
                assertThat(executionResultToAssert.intermediateResults()).containsExactly(
                        new IntermediateResult(
                                Value.of("40"),
                                new PositionedAt(0, 119),
                                List.of(
                                        new Input(new InputName("comparisonValue"), Value.of("true"), new PositionedAt(3, 29))),
                                new EvaluationProcessedIn(
                                        new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:03+01:00[Europe/Paris]")),
                                        new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:20+01:00[Europe/Paris]")))),
                        new IntermediateResult(
                                Value.of("true"),
                                new PositionedAt(3, 29),
                                List.of(
                                        new Input(new InputName("left"), Value.of("Joe"), new PositionedAt(6, 22)),
                                        new Input(new InputName("right"), Value.of("Joe"), new PositionedAt(24, 28))),
                                new EvaluationProcessedIn(
                                        new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:04+01:00[Europe/Paris]")),
                                        new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:09+01:00[Europe/Paris]")))),
                        new IntermediateResult(
                                Value.of("Joe"),
                                new PositionedAt(6, 22),
                                List.of(
                                        new Input(new InputName("structuredReference"), new Reference("Sales Person"), new PositionedAt(9, 20))),
                                new EvaluationProcessedIn(
                                        new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:05+01:00[Europe/Paris]")),
                                        new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:06+01:00[Europe/Paris]")))),
                        new IntermediateResult(
                                Value.of("Joe"),
                                new PositionedAt(24, 28),
                                List.of(),
                                new EvaluationProcessedIn(
                                        new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:07+01:00[Europe/Paris]")),
                                        new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:08+01:00[Europe/Paris]")))),
                        new IntermediateResult(
                                Value.of("40"),
                                new PositionedAt(31, 77),
                                List.of(
                                        new Input(new InputName("left"), Value.of("20"), new PositionedAt(35, 74)),
                                        new Input(new InputName("right"), Value.of("2"), new PositionedAt(76, 76))),
                                new EvaluationProcessedIn(
                                        new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:10+01:00[Europe/Paris]")),
                                        new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:19+01:00[Europe/Paris]")))),
                        new IntermediateResult(
                                Value.of("20"),
                                new PositionedAt(35, 74),
                                List.of(
                                        new Input(new InputName("left"), Value.of("200"), new PositionedAt(39, 55)),
                                        new Input(new InputName("right"), Value.of("0.10"), new PositionedAt(57, 73))),
                                new EvaluationProcessedIn(
                                        new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:11+01:00[Europe/Paris]")),
                                        new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:16+01:00[Europe/Paris]")))),
                        new IntermediateResult(
                                Value.of("200"),
                                new PositionedAt(39, 55),
                                List.of(
                                        new Input(new InputName("structuredReference"), new Reference("Sales Amount"), new PositionedAt(42, 53))),
                                new EvaluationProcessedIn(
                                        new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:12+01:00[Europe/Paris]")),
                                        new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:13+01:00[Europe/Paris]")))),
                        new IntermediateResult(
                                Value.of("0.10"),
                                new PositionedAt(57, 73),
                                List.of(
                                        new Input(new InputName("structuredReference"), new Reference("% Commission"), new PositionedAt(60, 71))),
                                new EvaluationProcessedIn(
                                        new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:14+01:00[Europe/Paris]")),
                                        new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:15+01:00[Europe/Paris]")))),
                        new IntermediateResult(
                                Value.of("2"),
                                new PositionedAt(76, 76),
                                List.of(),
                                new EvaluationProcessedIn(
                                        new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:17+01:00[Europe/Paris]")),
                                        new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:18+01:00[Europe/Paris]"))))
                )
        );
    }

    @Test
    public void shouldLogExecutionForRobert() {
        // Given
        final List<StructuredReference> givenStructuredReferences = List.of(
                new StructuredReference(new Reference("Sales Person"), "Robert"),
                new StructuredReference(new Reference("Sales Amount"), "200"),
                new StructuredReference(new Reference("% Commission"), "0.10")
        );

        // When
        final Uni<EvaluationResult> executionResult = antlrExecutor.process(formula4Test(givenFormula),
                new PartEvaluationCallback(new DebugPartEvaluationListener(evaluatedAtProvider), new NumericalContext(), givenStructuredReferences));

        // Then
        assertOnExecutionResultReceived(executionResult, executionResultToAssert ->
                assertThat(executionResultToAssert.intermediateResults()).containsExactly(
                        new IntermediateResult(
                                Value.of("20"),
                                new PositionedAt(0, 119),
                                List.of(
                                        new Input(new InputName("comparisonValue"), Value.of("false"), new PositionedAt(3, 29))),
                                new EvaluationProcessedIn(
                                        new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:03+01:00[Europe/Paris]")),
                                        new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:16+01:00[Europe/Paris]")))),
                        new IntermediateResult(
                                Value.of("false"),
                                new PositionedAt(3, 29),
                                List.of(
                                        new Input(new InputName("left"), Value.of("Robert"), new PositionedAt(6, 22)),
                                        new Input(new InputName("right"), Value.of("Joe"), new PositionedAt(24, 28))),
                                new EvaluationProcessedIn(
                                        new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:04+01:00[Europe/Paris]")),
                                        new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:09+01:00[Europe/Paris]")))),
                        new IntermediateResult(
                                Value.of("Robert"),
                                new PositionedAt(6, 22),
                                List.of(
                                        new Input(new InputName("structuredReference"), new Reference("Sales Person"), new PositionedAt(9, 20))),
                                new EvaluationProcessedIn(
                                        new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:05+01:00[Europe/Paris]")),
                                        new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:06+01:00[Europe/Paris]")))),
                        new IntermediateResult(
                                Value.of("Joe"),
                                new PositionedAt(24, 28),
                                List.of(),
                                new EvaluationProcessedIn(
                                        new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:07+01:00[Europe/Paris]")),
                                        new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:08+01:00[Europe/Paris]")))),
                        new IntermediateResult(
                                Value.of("20"),
                                new PositionedAt(79, 118),
                                List.of(
                                        new Input(new InputName("left"), Value.of("200"), new PositionedAt(83, 99)),
                                        new Input(new InputName("right"), Value.of("0.10"), new PositionedAt(101, 117))),
                                new EvaluationProcessedIn(
                                        new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:10+01:00[Europe/Paris]")),
                                        new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:15+01:00[Europe/Paris]")))),
                        new IntermediateResult(
                                Value.of("200"),
                                new PositionedAt(83, 99),
                                List.of(
                                        new Input(new InputName("structuredReference"), new Reference("Sales Amount"), new PositionedAt(86, 97))),
                                new EvaluationProcessedIn(
                                        new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:11+01:00[Europe/Paris]")),
                                        new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:12+01:00[Europe/Paris]")))),
                        new IntermediateResult(
                                Value.of("0.10"),
                                new PositionedAt(101, 117),
                                List.of(
                                        new Input(new InputName("structuredReference"), new Reference("% Commission"), new PositionedAt(104, 115))),
                                new EvaluationProcessedIn(
                                        new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:13+01:00[Europe/Paris]")),
                                        new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:14+01:00[Europe/Paris]"))))
                )
        );
    }
}
