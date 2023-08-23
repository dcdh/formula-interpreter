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

public class NumericExpressionTest extends AbstractExpressionTest {

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
        final ExecutionResult executionResult = antlrExecutor.execute(formula4Test(givenFormula), new StructuredData());

        // Then
        assertThat(((Value) executionResult.result()).isNumeric()).isTrue();
    }

    @ParameterizedTest
    @CsvSource({
            "ADD,123000000",
            "SUB,123000000",
            "DIV,-100000000000000000000",
            "MUL,-0.000151"
    })
    public void shouldExecuteOperationForValueLeftAndValueRight(final String givenOperation,
                                                                final String expectedValue) {
        // Given
        final String givenFormula = String.format("%s(12.3E+7,-1.23E-12)", givenOperation);
        final StructuredData givenStructuredData = new StructuredData(List.of());

        // When
        final ExecutionResult executionResult = antlrExecutor.execute(formula4Test(givenFormula), givenStructuredData);

        // Then
        assertThat(executionResult.result()).isEqualTo(
                new Value(expectedValue));
    }

    @Test
    public void shouldLogExecution() {
        // Given
        final String givenFormula = "ADD(10,-1.2)";
        final StructuredData givenStructuredData = new StructuredData(List.of());
        when(executedAtProvider.now())
                .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:00+01:00[Europe/Paris]")))
                .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:01+01:00[Europe/Paris]")))
                .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:02+01:00[Europe/Paris]")))
                .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:03+01:00[Europe/Paris]")))
                .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:04+01:00[Europe/Paris]")))
                .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:05+01:00[Europe/Paris]")));

        // When
        final ExecutionResult executionResult = antlrExecutor.execute(formula4Test(givenFormula), givenStructuredData);

        // Then
        assertThat(executionResult.executions()).containsExactly(
                new AntlrExecution(
                        new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:00+01:00[Europe/Paris]")),
                        new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:05+01:00[Europe/Paris]")),
                        new Position(0, 11),
                        Map.of(
                                new InputName("left"), Value.of("10"),
                                new InputName("right"), Value.of("-1.2")),
                        Value.of("8.8")),
                new AntlrExecution(
                        new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:01+01:00[Europe/Paris]")),
                        new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:02+01:00[Europe/Paris]")),
                        new Position(4, 5), Map.of(), Value.of("10")),
                new AntlrExecution(
                        new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:03+01:00[Europe/Paris]")),
                        new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:04+01:00[Europe/Paris]")),
                        new Position(7, 10), Map.of(), Value.of("-1.2"))
        );
    }
}
