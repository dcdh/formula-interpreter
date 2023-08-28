package com.damdamdeo.formula.infrastructure.antlr;

import com.damdamdeo.formula.domain.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class ValueExpressionTest extends AbstractExpressionTest {

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
        final ExecutionResult executionResult = antlrExecutor.execute(formula4Test(givenFormula), new StructuredData(),
                DebugFeature.ACTIVE);

        // Then
        assertThat(executionResult.result()).isEqualTo(
                new Value(expectedResult));
    }

    @Test
    public void shouldLogExecution() {
        // Given
        final String givenFormula = "\"Hello World\"";
        final StructuredData givenStructuredData = new StructuredData(List.of());
        when(executedAtProvider.now())
                .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:00+01:00[Europe/Paris]")))
                .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:01+01:00[Europe/Paris]")))
                .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:02+01:00[Europe/Paris]")))
                .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:03+01:00[Europe/Paris]")));

        // When
        final ExecutionResult executionResult = antlrExecutor.execute(formula4Test(givenFormula), givenStructuredData,
                DebugFeature.ACTIVE);

        // Then
        assertThat(executionResult.elementExecutions()).containsExactly(
                new AntlrElementExecution(
                        new Position(0, 12),
                        Map.of(),
                        Value.of("Hello World"),
                        new ExecutionProcessedIn(
                                new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:01+01:00[Europe/Paris]")),
                                new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:02+01:00[Europe/Paris]"))))
        );
    }
}
