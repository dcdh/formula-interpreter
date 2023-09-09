package com.damdamdeo.formula.infrastructure.antlr;

import com.damdamdeo.formula.domain.*;
import io.smallrye.mutiny.Uni;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class AntlrInformationFunctionsTest extends AbstractFunctionTest {
    @ParameterizedTest
    @MethodSource("provideValues")
    public void shouldCheck(final String givenFunction, final Value givenValue, final boolean expectedValue) {
        // Given
        final String givenFormula = String.format("""
                %s("%s")
                """, givenFunction, givenValue.value());
        final StructuredData givenStructuredData = new StructuredData(List.of());

        // When
        final Uni<ExecutionResult> executionResult = antlrExecutor.execute(formula4Test(givenFormula), givenStructuredData,
                new NoOpExecutionWrapper());

        // Then
        assertOnExecutionResultReceived(executionResult, executionResultToAssert ->
                assertThat(executionResultToAssert.result())
                        .isEqualTo(new Value(expectedValue))
        );
    }

    @ParameterizedTest
    @MethodSource("provideValues")
    public void shouldCheckUsingOnStructuredReference(final String givenFunction, final Value givenValue, final boolean expectedValue) {
        // Given
        final String givenFormula = String.format("%s([@[%% Commission]])", givenFunction);
        final StructuredData givenStructuredData = new StructuredData(List.of(
                new StructuredDatum(new Reference("% Commission"), givenValue.value())
        ));

        // When
        final Uni<ExecutionResult> executionResult = antlrExecutor.execute(formula4Test(givenFormula), givenStructuredData,
                new NoOpExecutionWrapper());

        // Then
        assertOnExecutionResultReceived(executionResult, executionResultToAssert ->
                assertThat(executionResultToAssert.result())
                        .isEqualTo(new Value(expectedValue))
        );
    }

    private static Stream<Arguments> provideValues() {
        return Stream.of(
                InformationFunctionTest.provideIsNotAvailable()
                        .map(isNa -> Arguments.of("ISNA", isNa.get()[0], isNa.get()[1])),
                InformationFunctionTest.provideIsError()
                        .map(isError -> Arguments.of("ISERROR", isError.get()[0], isError.get()[1])),
                InformationFunctionTest.provideIsNumeric()
                        .map(isNumeric -> Arguments.of("ISNUM", isNumeric.get()[0], isNumeric.get()[1])),
                InformationFunctionTest.provideIsText()
                        .map(isText -> Arguments.of("ISTEXT", isText.get()[0], isText.get()[1])),
                InformationFunctionTest.provideIsBlank()
                        .map(isBlank -> Arguments.of("ISBLANK", isBlank.get()[0], isBlank.get()[1])),
                InformationFunctionTest.provideIsLogical()
                        .map(isLogical -> Arguments.of("ISLOGICAL", isLogical.get()[0], isLogical.get()[1]))
        )
                .flatMap(Function.identity());
    }

    @Test
    public void shouldLogExecution() {
        // Given
        final String givenFormula = "ISNUM(\"123456\")";
        final StructuredData givenStructuredData = new StructuredData(List.of());
        when(executedAtProvider.now())
                .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:00+01:00[Europe/Paris]")))
                .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:01+01:00[Europe/Paris]")))
                .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:02+01:00[Europe/Paris]")))
                .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:03+01:00[Europe/Paris]")))
                .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:04+01:00[Europe/Paris]")))
                .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:05+01:00[Europe/Paris]")));

        // When
        final Uni<ExecutionResult> executionResult = antlrExecutor.execute(formula4Test(givenFormula), givenStructuredData,
                new LoggingExecutionWrapper(executedAtProvider));

        // Then
        assertOnExecutionResultReceived(executionResult, executionResultToAssert ->
                assertThat(executionResultToAssert.elementExecutions()).containsExactly(
                        new ElementExecution(
                                Value.of("true"),
                                new Range(0, 14),
                                Map.of(
                                        new InputName("value"), Value.of("123456")
                                ),
                                new ExecutionProcessedIn(
                                        new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:01+01:00[Europe/Paris]")),
                                        new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:04+01:00[Europe/Paris]")))),
                        new ElementExecution(
                                Value.of("123456"),
                                new Range(6, 13),
                                Map.of(),
                                new ExecutionProcessedIn(
                                        new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:02+01:00[Europe/Paris]")),
                                        new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:03+01:00[Europe/Paris]"))))
                )
        );
    }
}
