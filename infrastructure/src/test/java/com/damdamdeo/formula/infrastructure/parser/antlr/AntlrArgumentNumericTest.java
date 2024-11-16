package com.damdamdeo.formula.infrastructure.parser.antlr;

import com.damdamdeo.formula.domain.*;
import io.smallrye.mutiny.Uni;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.ZonedDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class AntlrArgumentNumericTest extends AbstractFunctionTest {
    @ParameterizedTest
    @CsvSource({
            "0",
            "0.00",
            "123",
            "-123",
            "1.23E3",
            "1.23E+3",
            "12.3E+7",
            "12.0",
            "12.3",
            "0.00123",
            "-1.23E-12",
            "1234.5E-4",
            "0E+7",
            "-0"
    })
    public void shouldBeANumeric(final String givenFormula) {
        // Given

        // When
        final Uni<EvaluationResult> executionResult = antlrExecutor.process(formula4Test(givenFormula),
                new PartEvaluationCallback(new NoOpPartEvaluationListener(), new NumericalContext(), List.of()));

        // Then
        assertOnExecutionResultReceived(executionResult, executionResultToAssert ->
                assertThat(executionResultToAssert.value().isNumeric())
                        .isTrue()
        );
    }

    @Test
    public void shouldLogExecution() {
        // Given
        final String givenFormula = "0.00";
        final List<StructuredReference> givenStructuredReferences = List.of();

        // When
        final Uni<EvaluationResult> executionResult = antlrExecutor.process(formula4Test(givenFormula),
                new PartEvaluationCallback(new DebugPartEvaluationListener(evaluatedAtProvider), new NumericalContext(), givenStructuredReferences));

        // Then
        assertOnExecutionResultReceived(executionResult, executionResultToAssert ->
                assertThat(executionResultToAssert.intermediateResults()).containsExactly(
                        new IntermediateResult(
                                Value.of("0.00"),
                                new PositionedAt(0, 3),
                                List.of(),
                                new EvaluationProcessedIn(
                                        new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:03+01:00[Europe/Paris]")),
                                        new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:04+01:00[Europe/Paris]"))))
                )
        );
    }
}
