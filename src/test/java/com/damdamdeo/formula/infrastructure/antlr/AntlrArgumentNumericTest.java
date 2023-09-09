package com.damdamdeo.formula.infrastructure.antlr;

import com.damdamdeo.formula.domain.*;
import io.smallrye.mutiny.Uni;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

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
        final Uni<ExecutionResult> executionResult = antlrExecutor.execute(formula4Test(givenFormula), new StructuredData(),
                new NoOpExecutionWrapper());

        // Then
        assertOnExecutionResultReceived(executionResult, executionResultToAssert ->
                assertThat(((Value) executionResultToAssert.result()).isNumeric())
                        .isTrue()
        );
    }

    @Test
    public void shouldLogExecution() {
        // Given
        final String givenFormula = "0.00";
        final StructuredData givenStructuredData = new StructuredData(List.of());
        when(executedAtProvider.now())
                .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:00+01:00[Europe/Paris]")))
                .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:01+01:00[Europe/Paris]")))
                .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:02+01:00[Europe/Paris]")))
                .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:03+01:00[Europe/Paris]")));

        // When
        final Uni<ExecutionResult> executionResult = antlrExecutor.execute(formula4Test(givenFormula), givenStructuredData,
                new LoggingExecutionWrapper(executedAtProvider));

        // Then
        assertOnExecutionResultReceived(executionResult, executionResultToAssert ->
                assertThat(executionResultToAssert.elementExecutions()).containsExactly(
                        new ElementExecution(
                                Value.of("0.00"),
                                new Range(0, 3),
                                Map.of(),
                                new ExecutionProcessedIn(
                                        new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:01+01:00[Europe/Paris]")),
                                        new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:02+01:00[Europe/Paris]"))))
                )
        );
    }
}
