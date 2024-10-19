package com.damdamdeo.formula.infrastructure.antlr;

import com.damdamdeo.formula.domain.*;
import com.damdamdeo.formula.domain.provider.EqualityComparisonFunctionTestProvider;
import com.damdamdeo.formula.domain.provider.NumericalComparisonFunctionTestProvider;
import io.smallrye.mutiny.Uni;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class AntlrComparisonFunctionsTest extends AbstractFunctionTest {
    @ParameterizedTest
    @MethodSource("provideComparisonsWithExpectedValues")
    public void shouldEvaluateComparisonForStructuredReferenceLeftAndStructuredReferenceRight(final Value leftValue,
                                                                                              final String givenComparison,
                                                                                              final Value rightValue,
                                                                                              final Value expectedValue) {
        // Given
        final String givenFormula = String.format("%s([@[North Sales Amount]],[@[South Sales Amount]])", givenComparison);
        final List<StructuredReference> givenStructuredReferences = List.of(
                new StructuredReference(new Reference("North Sales Amount"), leftValue),
                new StructuredReference(new Reference("South Sales Amount"), rightValue)
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

    @ParameterizedTest
    @MethodSource("provideComparisonsWithExpectedValues")
    public void shouldEvaluateComparisonForStructuredReferenceLeftAndValueRight(final Value leftValue,
                                                                                final String givenComparison,
                                                                                final Value rightValue,
                                                                                final Value expectedValue) {
        // Given
        final String givenFormula;
        if (rightValue.isError() || rightValue.isText()) {
            givenFormula = String.format("""
                    %s([@[North Sales Amount]],"%s")""", givenComparison, rightValue.value());
        } else {
            givenFormula = String.format("""
                    %s([@[North Sales Amount]],%s)""", givenComparison, rightValue.value());
        }
        final List<StructuredReference> givenStructuredReferences = List.of(
                new StructuredReference(new Reference("North Sales Amount"), leftValue)
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

    @ParameterizedTest
    @MethodSource("provideComparisonsWithExpectedValues")
    public void shouldEvaluateComparisonForValueLeftAndStructuredReferenceRight(final Value leftValue,
                                                                                final String givenComparison,
                                                                                final Value rightValue,
                                                                                final Value expectedValue) {
        // Given
        final String givenFormula;
        if (leftValue.isError() || leftValue.isText()) {
            givenFormula = String.format("""
                    %s("%s",[@[South Sales Amount]])""", givenComparison, leftValue.value());
        } else {
            givenFormula = String.format("""
                    %s(%s,[@[South Sales Amount]])""", givenComparison, leftValue.value());
        }
        final List<StructuredReference> givenStructuredReferences = List.of(
                new StructuredReference(new Reference("South Sales Amount"), rightValue)
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

    @ParameterizedTest
    @MethodSource("provideComparisonsWithExpectedValues")
    public void shouldEvaluateComparisonForValueLeftAndValueRight(final Value leftValue,
                                                                  final String givenComparison,
                                                                  final Value rightValue,
                                                                  final Value expectedValue) {
        // Given
        final String givenFormula;
        if ((leftValue.isError() || leftValue.isText()) && (rightValue.isError() || rightValue.isText())) {
            givenFormula = String.format("""
                    %s("%s","%s")""", givenComparison, leftValue.value(), rightValue.value());
        } else if (leftValue.isError() || leftValue.isText()) {
            givenFormula = String.format("""
                    %s("%s",%s)""", givenComparison, leftValue.value(), rightValue.value());
        } else if (rightValue.isError() || rightValue.isText()) {
            givenFormula = String.format("""
                    %s(%s,"%s")""", givenComparison, leftValue.value(), rightValue.value());
        } else {
            givenFormula = String.format("""
                    %s(%s,%s)""", givenComparison, leftValue.value(), rightValue.value());
        }
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

    private static Stream<Arguments> provideComparisonsWithExpectedValues() {
        return Stream.of(
                        NumericalComparisonFunctionTestProvider.provideGreaterThan()
                                .map(greaterThan -> Arguments.of(greaterThan.get()[0], "GT", greaterThan.get()[1], greaterThan.get()[2])),
                        NumericalComparisonFunctionTestProvider.provideGreaterThanOrEqualTo()
                                .map(greaterThanOrEqualTo -> Arguments.of(greaterThanOrEqualTo.get()[0], "GTE", greaterThanOrEqualTo.get()[1], greaterThanOrEqualTo.get()[2])),
                        NumericalComparisonFunctionTestProvider.provideLessThan()
                                .map(lessThan -> Arguments.of(lessThan.get()[0], "LT", lessThan.get()[1], lessThan.get()[2])),
                        NumericalComparisonFunctionTestProvider.provideLessThanOrEqualTo()
                                .map(lessThanOrEqualTo -> Arguments.of(lessThanOrEqualTo.get()[0], "LTE", lessThanOrEqualTo.get()[1], lessThanOrEqualTo.get()[2])),
                        NumericalComparisonFunctionTestProvider.provideCommonResponses()
                                .flatMap(numericalCommonResponse -> Stream.of("GT", "GTE", "LT", "LTE")
                                        .map(function -> Arguments.of(numericalCommonResponse.get()[0], function, numericalCommonResponse.get()[1], numericalCommonResponse.get()[2]))),
                        EqualityComparisonFunctionTestProvider.provideEqual()
                                .map(equal -> Arguments.of(equal.get()[0], "EQ", equal.get()[1], equal.get()[2])),
                        EqualityComparisonFunctionTestProvider.provideNotEqual()
                                .map(notEqual -> Arguments.of(notEqual.get()[0], "NEQ", notEqual.get()[1], notEqual.get()[2])),
                        EqualityComparisonFunctionTestProvider.provideCommonResponses()
                                .flatMap(comparisonCommonResponse -> Stream.of("EQ", "NEQ")
                                        .map(function -> Arguments.of(comparisonCommonResponse.get()[0], function, comparisonCommonResponse.get()[1], comparisonCommonResponse.get()[2])))
                )
                .flatMap(Function.identity());
    }

    @ParameterizedTest
    @MethodSource("provideAllComparators")
    public void shouldBeUnknownWhenOneStructuredReferenceIsUnknown(final String givenComparison) {
        // Given
        final String givenFormula = String.format("%s([@[North Sales Amount]],[@[South Sales Amount]])", givenComparison);
        final List<StructuredReference> givenStructuredReferences = List.of(
                new StructuredReference(new Reference("North Sales Amount"), "660")
        );

        // When
        final Uni<EvaluationResult> executionResult = antlrExecutor.process(formula4Test(givenFormula),
                new PartEvaluationCallback(new NoOpPartEvaluationCallbackListener(), new NumericalContext(), givenStructuredReferences));

        // Then
        assertOnExecutionResultReceived(executionResult, executionResultToAssert ->
                assertThat(executionResultToAssert.evaluated().value())
                        .isEqualTo(new Value("#REF!"))
        );
    }

    @ParameterizedTest
    @MethodSource("provideAllComparators")
    public void shouldBeNotAvailableWhenLeftStructuredReferenceIsNull(final String givenComparison) {
        // Given
        final String givenFormula = String.format("%s([@[North Sales Amount]],[@[South Sales Amount]])", givenComparison);
        final List<StructuredReference> givenStructuredReferences = List.of(
                new StructuredReference(new Reference("North Sales Amount"), (String) null),
                new StructuredReference(new Reference("South Sales Amount"), "260")
        );

        // When
        final Uni<EvaluationResult> executionResult = antlrExecutor.process(formula4Test(givenFormula),
                new PartEvaluationCallback(new NoOpPartEvaluationCallbackListener(), new NumericalContext(), givenStructuredReferences));

        // Then
        assertOnExecutionResultReceived(executionResult, executionResultToAssert ->
                assertThat(executionResultToAssert.evaluated().value())
                        .isEqualTo(new Value("#NA!"))
        );
    }

    @ParameterizedTest
    @MethodSource("provideAllComparators")
    public void shouldBeNotAvailableWhenRightStructuredReferenceIsNull(final String givenComparison) {
        // Given
        final String givenFormula = String.format("%s([@[North Sales Amount]],[@[South Sales Amount]])", givenComparison);
        final List<StructuredReference> givenStructuredReferences = List.of(
                new StructuredReference(new Reference("North Sales Amount"), "660"),
                new StructuredReference(new Reference("South Sales Amount"), (String) null)
        );

        // When
        final Uni<EvaluationResult> executionResult = antlrExecutor.process(formula4Test(givenFormula),
                new PartEvaluationCallback(new NoOpPartEvaluationCallbackListener(), new NumericalContext(), givenStructuredReferences));

        // Then
        assertOnExecutionResultReceived(executionResult, executionResultToAssert ->
                assertThat(executionResultToAssert.evaluated().value())
                        .isEqualTo(new Value("#NA!"))
        );
    }

    private static Stream<Arguments> provideAllComparators() {
        return Stream.of(
                Arguments.of("ADD"),
                Arguments.of("SUB"),
                Arguments.of("DIV"),
                Arguments.of("MUL"),
                Arguments.of("GT"),
                Arguments.of("GTE"),
                Arguments.of("EQ"),
                Arguments.of("NEQ"),
                Arguments.of("LT"),
                Arguments.of("LTE"));
    }

    @ParameterizedTest
    @CsvSource({"ADD", "SUB", "DIV", "MUL", "GT", "GTE", "LT", "LTE"})
    public void shouldBeInErrorWhenOneStructuredReferenceIsNotANumerical(final String givenComparison) {
        // Given
        final String givenFormula = String.format("%s([@[North Sales Amount]],[@[South Sales Amount]])", givenComparison);
        final List<StructuredReference> givenStructuredReferences = List.of(
                new StructuredReference(new Reference("North Sales Amount"), "660"),
                new StructuredReference(new Reference("South Sales Amount"), "boom")
        );

        // When
        final Uni<EvaluationResult> executionResult = antlrExecutor.process(formula4Test(givenFormula),
                new PartEvaluationCallback(new NoOpPartEvaluationCallbackListener(), new NumericalContext(), givenStructuredReferences));

        // Then
        assertOnExecutionResultReceived(executionResult, executionResultToAssert ->
                assertThat(executionResultToAssert.evaluated().value())
                        .isEqualTo(new Value("#NUM!"))
        );
    }

    @ParameterizedTest
    @MethodSource("provideComparatorFunctionsUsingArithmeticsFunction")
    public void shouldUseComparisonsFunctions(final String givenFormula) {
        // Given
        final List<StructuredReference> givenStructuredReferences = List.of(
                new StructuredReference(new Reference("North Sales Amount"), "660")
        );

        // When
        final Uni<EvaluationResult> executionResult = antlrExecutor.process(formula4Test(givenFormula),
                new PartEvaluationCallback(new NoOpPartEvaluationCallbackListener(), new NumericalContext(), givenStructuredReferences));

        // Then
        assertOnExecutionResultReceived(executionResult, executionResultToAssert ->
                assertThat(executionResultToAssert.evaluated().value())
                        .isEqualTo(new Value("true"))
        );
    }

    private static Stream<Arguments> provideComparatorFunctionsUsingArithmeticsFunction() {
        return Stream.of(
                Arguments.of("LTE(ADD(100,160),ADD(100,160))"));
    }

    @Test
    public void shouldLogExecution() {
        // Given
        final String givenFormula = "GT(660,260)";
        final List<StructuredReference> givenStructuredReferences = List.of();
        when(evaluatedAtProvider.now())
                .thenReturn(new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:00+01:00[Europe/Paris]")))
                .thenReturn(new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:01+01:00[Europe/Paris]")))
                .thenReturn(new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:02+01:00[Europe/Paris]")))
                .thenReturn(new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:03+01:00[Europe/Paris]")))
                .thenReturn(new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:04+01:00[Europe/Paris]")))
                .thenReturn(new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:05+01:00[Europe/Paris]")))
                .thenReturn(new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:06+01:00[Europe/Paris]")))
                .thenReturn(new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:07+01:00[Europe/Paris]")))
                .thenReturn(new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:08+01:00[Europe/Paris]")))
                .thenReturn(new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:09+01:00[Europe/Paris]")));

        // When
        final Uni<EvaluationResult> executionResult = antlrExecutor.process(formula4Test(givenFormula),
                new PartEvaluationCallback(new DebugPartEvaluationCallbackListener(evaluatedAtProvider), new NumericalContext(), givenStructuredReferences));

        // Then
        assertOnExecutionResultReceived(executionResult, executionResultToAssert ->
                assertThat(executionResultToAssert.intermediateResults()).containsExactly(
                        new IntermediateResult(
                                Value.of("true"),
                                new Range(0, 10),
                                List.of(
                                        new Input(new InputName("left"), Value.of("660"), new Range(3, 5)),
                                        new Input(new InputName("right"), Value.of("260"), new Range(7, 9))),
                                new EvaluationProcessedIn(
                                        new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:03+01:00[Europe/Paris]")),
                                        new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:08+01:00[Europe/Paris]")))),
                        new IntermediateResult(
                                Value.of("660"),
                                new Range(3, 5),
                                List.of(),
                                new EvaluationProcessedIn(
                                        new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:04+01:00[Europe/Paris]")),
                                        new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:05+01:00[Europe/Paris]")))),
                        new IntermediateResult(
                                Value.of("260"),
                                new Range(7, 9),
                                List.of(),
                                new EvaluationProcessedIn(
                                        new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:06+01:00[Europe/Paris]")),
                                        new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:07+01:00[Europe/Paris]"))))
                )
        );
    }
}
