package com.damdamdeo.formula.infrastructure.antlr;

import com.damdamdeo.formula.domain.*;
import com.damdamdeo.formula.domain.provider.StateFunctionTestProvider;
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

public class AntlrStateFunctionsTest extends AbstractFunctionTest {
    @ParameterizedTest
    @MethodSource("provideValues")
    public void shouldCheck(final String givenFunction, final Value givenValue, final Value expectedValue) {
        // Given
        final String givenFormula = String.format("""
                %s("%s")
                """, givenFunction, givenValue.value());
        final List<StructuredReference> givenStructuredReferences = List.of();

        // When
        final Uni<EvaluationResult> executionResult = antlrExecutor.process(formula4Test(givenFormula),
                new PartEvaluationCallback(new NoOpPartEvaluationCallbackListener(), new NumericalContext(), givenStructuredReferences));

        // Then
        assertOnExecutionResultReceived(executionResult, executionResultToAssert ->
                assertThat(executionResultToAssert.evaluated().value())
                        .isEqualTo(expectedValue)
        );
    }

    @ParameterizedTest
    @MethodSource("provideValues")
    public void shouldCheckUsingOnStructuredReference(final String givenFunction, final Value givenValue, final Value expectedValue) {
        // Given
        final String givenFormula = String.format("%s([@[%% Commission]])", givenFunction);
        final List<StructuredReference> givenStructuredReferences = List.of(
                new StructuredReference(new Reference("% Commission"), givenValue)
        );

        // When
        final Uni<EvaluationResult> executionResult = antlrExecutor.process(formula4Test(givenFormula),
                new PartEvaluationCallback(new NoOpPartEvaluationCallbackListener(), new NumericalContext(), givenStructuredReferences));

        // Then
        assertOnExecutionResultReceived(executionResult, executionResultToAssert ->
                assertThat(executionResultToAssert.evaluated().value())
                        .isEqualTo(expectedValue)
        );
    }

    private static Stream<Arguments> provideValues() {
        return Stream.of(
                        StateFunctionTestProvider.provideIsNotAvailable()
                                .map(isNa -> Arguments.of("ISNA", isNa.get()[0], isNa.get()[1])),
                        StateFunctionTestProvider.provideIsError()
                                .map(isError -> Arguments.of("ISERROR", isError.get()[0], isError.get()[1])),
                        StateFunctionTestProvider.provideIsNumeric()
                                .map(isNumeric -> Arguments.of("ISNUM", isNumeric.get()[0], isNumeric.get()[1])),
                        StateFunctionTestProvider.provideIsText()
                                .map(isText -> Arguments.of("ISTEXT", isText.get()[0], isText.get()[1])),
                        StateFunctionTestProvider.provideIsBlank()
                                .map(isBlank -> Arguments.of("ISBLANK", isBlank.get()[0], isBlank.get()[1])),
                        StateFunctionTestProvider.provideIsLogical()
                                .map(isLogical -> Arguments.of("ISLOGICAL", isLogical.get()[0], isLogical.get()[1]))
                )
                .flatMap(Function.identity());
    }

    @Test
    public void shouldLogExecution() {
        // Given
        final String givenFormula = "ISNUM(\"123456\")";
        final List<StructuredReference> givenStructuredReferences = List.of();
        when(evaluatedAtProvider.now())
                .thenReturn(new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:00+01:00[Europe/Paris]")))
                .thenReturn(new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:01+01:00[Europe/Paris]")))
                .thenReturn(new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:02+01:00[Europe/Paris]")))
                .thenReturn(new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:03+01:00[Europe/Paris]")))
                .thenReturn(new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:04+01:00[Europe/Paris]")))
                .thenReturn(new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:05+01:00[Europe/Paris]")))
                .thenReturn(new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:06+01:00[Europe/Paris]")))
                .thenReturn(new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:07+01:00[Europe/Paris]")));

        // When
        final Uni<EvaluationResult> executionResult = antlrExecutor.process(formula4Test(givenFormula),
                new PartEvaluationCallback(new DebugPartEvaluationCallbackListener(evaluatedAtProvider), new NumericalContext(), givenStructuredReferences));

        // Then
        assertOnExecutionResultReceived(executionResult, executionResultToAssert ->
                assertThat(executionResultToAssert.intermediateResults()).containsExactly(
                        new IntermediateResult(
                                Value.of("true"),
                                new Range(0, 14),
                                List.of(
                                        new Input(new InputName("value"), Value.of("123456"), new Range(6, 13))
                                ),
                                new EvaluationProcessedIn(
                                        new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:03+01:00[Europe/Paris]")),
                                        new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:06+01:00[Europe/Paris]")))),
                        new IntermediateResult(
                                Value.of("123456"),
                                new Range(6, 13),
                                List.of(),
                                new EvaluationProcessedIn(
                                        new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:04+01:00[Europe/Paris]")),
                                        new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:05+01:00[Europe/Paris]"))))
                )
        );
    }
}
