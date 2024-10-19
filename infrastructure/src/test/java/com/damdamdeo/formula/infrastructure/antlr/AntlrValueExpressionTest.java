package com.damdamdeo.formula.infrastructure.antlr;

import com.damdamdeo.formula.domain.*;
import io.smallrye.mutiny.Uni;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.ZonedDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class AntlrValueExpressionTest extends AbstractFunctionTest {
    @ParameterizedTest
    @CsvSource({
            "\"Hello World\",Hello World",
            "\"AZERTY\",AZERTY",
            "10,10",
            "\"-+E.09()%\",-+E.09()%"
    })
    public void shouldReturnInputValue(final String givenFormula,
                                       final String expectedResult) {
        // Given

        // When
        final Uni<EvaluationResult> executionResult = antlrExecutor.process(formula4Test(givenFormula),
                new PartEvaluationCallback(new NoOpPartEvaluationCallbackListener(), new NumericalContext(), List.of()));

        // Then
        assertOnExecutionResultReceived(executionResult, executionResultToAssert ->
                assertThat(executionResultToAssert.evaluated().value())
                        .isEqualTo(new Value(expectedResult))
        );
    }

    @Test
    public void shouldLogExecution() {
        // Given
        final String givenFormula = "\"Hello World\"";
        final List<StructuredReference> givenStructuredReferences = List.of();
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
                                Value.of("Hello World"),
                                new Range(0, 12),
                                List.of(),
                                new EvaluationProcessedIn(
                                        new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:03+01:00[Europe/Paris]")),
                                        new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:04+01:00[Europe/Paris]"))))
                )
        );
    }
}
