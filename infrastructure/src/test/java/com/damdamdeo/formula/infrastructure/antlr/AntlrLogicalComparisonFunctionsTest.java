package com.damdamdeo.formula.infrastructure.antlr;

import com.damdamdeo.formula.domain.*;
import com.damdamdeo.formula.domain.provider.LogicalComparisonFunctionTestProvider;
import io.smallrye.mutiny.Uni;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class AntlrLogicalComparisonFunctionsTest extends AbstractFunctionTest {
    @ParameterizedTest
    @MethodSource("provideLogicalComparisonFunctions")
    public void shouldComputeIf(final String givenIfFormula, final Value expectedValue) {
        // Given
        final StructuredReferences givenStructuredReferences = new StructuredReferences(List.of());

        // When
        final Uni<ExecutionResult> executionResult = antlrExecutor.execute(formula4Test(givenIfFormula),
                new PartExecutionCallback(new NoOpPartExecutionCallbackListener(), new NumericalContext(), givenStructuredReferences));

        // Then
        assertOnExecutionResultReceived(executionResult, executionResultToAssert ->
                assertThat(executionResultToAssert.result().value())
                        .isEqualTo(expectedValue)
        );
    }

    private static Stream<Arguments> provideLogicalComparisonFunctions() {
        return Stream.of(
                        LogicalComparisonFunctionTestProvider.provideIf()
                                .map(ifF -> Arguments.of(String.format("""
                                        IF("%s",true,false)""", ((Value) ifF.get()[0]).value()), ifF.get()[1])),
                        LogicalComparisonFunctionTestProvider.provideIfError()
                                .map(ifError -> Arguments.of(String.format("""
                                        IFERROR("%s",true,false)""", ((Value) ifError.get()[0]).value()), ifError.get()[1])),
                        LogicalComparisonFunctionTestProvider.provideIfNotAvailable()
                                .map(ifNotAvailable -> Arguments.of(String.format("""
                                        IFNA("%s",true,false)""", ((Value) ifNotAvailable.get()[0]).value()), ifNotAvailable.get()[1])),
                        Stream.of(Arguments.of("""
                                        IF("false",ADD(1,1),ADD(2,2))""", "4"),
                                Arguments.of("""
                                        IFERROR("false",ADD(1,1),ADD(2,2))""", "4"),
                                Arguments.of("""
                                        IFNA("false",ADD(1,1),ADD(2,2))""", "4"))
                )
                .flatMap(Function.identity());
    }

    @Test
    public void shouldLogExecution() {
        // Given
        final String givenFormula = "IF(\"true\",\"true\",\"false\")";
        final StructuredReferences givenStructuredReferences = new StructuredReferences(List.of());
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
                .thenReturn(new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:09+01:00[Europe/Paris]")));

        // When
        final Uni<ExecutionResult> executionResult = antlrExecutor.execute(formula4Test(givenFormula),
                new PartExecutionCallback(new LoggingPartExecutionCallbackListener(executedAtProvider), new NumericalContext(), givenStructuredReferences));

        // Then
        assertOnExecutionResultReceived(executionResult, executionResultToAssert ->
                assertThat(executionResultToAssert.intermediateResults()).containsExactly(
                        new IntermediateResult(
                                Value.of("true"),
                                new Range(0, 24),
                                List.of(
                                        new Input(new InputName("comparisonValue"), Value.of("true"), new Range(3, 8))),
                                new ExecutionProcessedIn(
                                        new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:03+01:00[Europe/Paris]")),
                                        new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:08+01:00[Europe/Paris]")))),
                        new IntermediateResult(
                                Value.of("true"),
                                new Range(3, 8),
                                List.of(),
                                new ExecutionProcessedIn(
                                        new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:04+01:00[Europe/Paris]")),
                                        new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:05+01:00[Europe/Paris]")))),
                        new IntermediateResult(
                                Value.of("true"),
                                new Range(10, 15),
                                List.of(),
                                new ExecutionProcessedIn(
                                        new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:06+01:00[Europe/Paris]")),
                                        new ExecutedAt(ZonedDateTime.parse("2023-12-25T10:15:07+01:00[Europe/Paris]"))))
                )
        );
    }
}
