package com.damdamdeo.formula.infrastructure.antlr;

import com.damdamdeo.formula.domain.*;
import com.damdamdeo.formula.domain.provider.LogicalBooleanFunctionTestProvider;
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

// FCK extraire parce que c'est de l'évaluation avec du mapping ...
// ceci c'est le legacy degeu ... parce que je dois parler de evaluation ...
public class AntlrLogicalBooleanFunctionsTest extends AbstractFunctionTest {
    @ParameterizedTest
    @MethodSource("provideLogicalFunctionsWithExpectedValues")
    public void shouldEvaluateLogicalFunctionsForStructuredReferenceLeftAndStructuredReferenceRight(
            final Value leftValue,
            final String givenLogicalFunction,
            final Value rightValue,
            final Value expectedValue) {
        // Given
        final String givenFormula = String.format("%s([@[North Sales Amount]],[@[South Sales Amount]])", givenLogicalFunction);
        final List<StructuredReference> givenStructuredReferences = List.of(
                new StructuredReference(new Reference("North Sales Amount"), leftValue),
                new StructuredReference(new Reference("South Sales Amount"), rightValue)
        );

        // When
        final Uni<EvaluationResult> executionResult = antlrExecutor.process(formula4Test(givenFormula),
                new PartEvaluationCallback(new NoOpPartEvaluationListener(), new NumericalContext(), givenStructuredReferences));

        // Then
        assertOnExecutionResultReceived(executionResult, executionResultToAssert ->
                assertThat(executionResultToAssert.value())
                        .isEqualTo(expectedValue)
        );
    }

    @ParameterizedTest
    @MethodSource("provideLogicalFunctionsWithExpectedValues")
    public void shouldEvaluateLogicalFunctionsForStructuredReferenceLeftAndValueRight(final Value leftValue,
                                                                                      final String givenLogicalFunction,
                                                                                      final Value rightValue,
                                                                                      final Value expectedValue) {
        // Given
        final String givenFormula;
        if (rightValue.isError() || rightValue.isText()) {
            givenFormula = String.format("""
                    %s([@[North Sales Amount]],"%s")""", givenLogicalFunction, rightValue.value());
        } else {
            givenFormula = String.format("""
                    %s([@[North Sales Amount]],%s)""", givenLogicalFunction, rightValue.value());
        }
        final List<StructuredReference> givenStructuredReferences = List.of(
                new StructuredReference(new Reference("North Sales Amount"), leftValue)
        );

        // When
        final Uni<EvaluationResult> executionResult = antlrExecutor.process(formula4Test(givenFormula),
                new PartEvaluationCallback(new NoOpPartEvaluationListener(), new NumericalContext(), givenStructuredReferences));

        // Then
        assertOnExecutionResultReceived(executionResult, executionResultToAssert ->
                assertThat(executionResultToAssert.value())
                        .isEqualTo(expectedValue)
        );
    }

    @ParameterizedTest
    @MethodSource("provideLogicalFunctionsWithExpectedValues")
    public void shouldEvaluateLogicalFunctionsForValueLeftAndStructuredReferenceRight(final Value leftValue,
                                                                                      final String givenLogicalFunction,
                                                                                      final Value rightValue,
                                                                                      final Value expectedValue) {
        // Given
        final String givenFormula;
        if (leftValue.isError() || leftValue.isText()) {
            givenFormula = String.format("""
                    %s("%s",[@[South Sales Amount]])""", givenLogicalFunction, leftValue.value());
        } else {
            givenFormula = String.format("""
                    %s(%s,[@[South Sales Amount]])""", givenLogicalFunction, leftValue.value());
        }
        final List<StructuredReference> givenStructuredReferences = List.of(
                new StructuredReference(new Reference("South Sales Amount"), rightValue)
        );

        // When
        final Uni<EvaluationResult> executionResult = antlrExecutor.process(formula4Test(givenFormula),
                new PartEvaluationCallback(new NoOpPartEvaluationListener(), new NumericalContext(), givenStructuredReferences));

        // Then
        assertOnExecutionResultReceived(executionResult, executionResultToAssert ->
                assertThat(executionResultToAssert.value())
                        .isEqualTo(expectedValue)
        );
    }

    @ParameterizedTest
    @MethodSource("provideLogicalFunctionsWithExpectedValues")
    public void shouldEvaluateLogicalFunctionsForValueLeftAndValueRight(final Value leftValue,
                                                                        final String givenLogicalFunction,
                                                                        final Value rightValue,
                                                                        final Value expectedValue) {
        // Given
        final String givenFormula;
        if ((leftValue.isError() || leftValue.isText()) && (rightValue.isError() || rightValue.isText())) {
            givenFormula = String.format("""
                    %s("%s","%s")""", givenLogicalFunction, leftValue.value(), rightValue.value());
        } else if (leftValue.isError() || leftValue.isText()) {
            givenFormula = String.format("""
                    %s("%s",%s)""", givenLogicalFunction, leftValue.value(), rightValue.value());
        } else if (rightValue.isError() || rightValue.isText()) {
            givenFormula = String.format("""
                    %s(%s,"%s")""", givenLogicalFunction, leftValue.value(), rightValue.value());
        } else {
            givenFormula = String.format("""
                    %s(%s,%s)""", givenLogicalFunction, leftValue.value(), rightValue.value());
        }
        final List<StructuredReference> givenStructuredReferences = List.of();

        // When
        final Uni<EvaluationResult> executionResult = antlrExecutor.process(formula4Test(givenFormula),
                new PartEvaluationCallback(new NoOpPartEvaluationListener(), new NumericalContext(), givenStructuredReferences));

        // Then
        assertOnExecutionResultReceived(executionResult, executionResultToAssert ->
                assertThat(executionResultToAssert.value())
                        .isEqualTo(expectedValue)
        );
    }

    doit provenir di domain !
    private static Stream<Arguments> provideLogicalFunctionsWithExpectedValues() {
        return Stream.of(
                        LogicalBooleanFunctionTestProvider.provideAnd()
                                .map(and -> Arguments.of(and.get()[0], "AND", and.get()[1], and.get()[2])),
                        LogicalBooleanFunctionTestProvider.provideCommonResponses()
                                .map(and -> Arguments.of(and.get()[0], "AND", and.get()[1], and.get()[2])),
                        LogicalBooleanFunctionTestProvider.provideOr()
                                .map(or -> Arguments.of(or.get()[0], "OR", or.get()[1], or.get()[2])),
                        LogicalBooleanFunctionTestProvider.provideCommonResponses()
                                .map(or -> Arguments.of(or.get()[0], "OR", or.get()[1], or.get()[2]))
                )
                .flatMap(Function.identity());
    }

    @ParameterizedTest
    @MethodSource("provideLogicalOperator")
    public void shouldBeUnknownWhenOneStructuredReferenceIsUnknown(final String givenLogicalFunction) {
        // Given
        final String givenFormula = String.format("%s([@[North Sales Amount]],[@[South Sales Amount]])", givenLogicalFunction);
        final List<StructuredReference> givenStructuredReferences = List.of(
                new StructuredReference(new Reference("North Sales Amount"), "660")
        );

        // When
        final Uni<EvaluationResult> executionResult = antlrExecutor.process(formula4Test(givenFormula),
                new PartEvaluationCallback(new NoOpPartEvaluationListener(), new NumericalContext(), givenStructuredReferences));

        // Then
        assertOnExecutionResultReceived(executionResult, executionResultToAssert ->
                assertThat(executionResultToAssert.value())
                        .isEqualTo(new Value("#REF!"))
        );
    }

    @ParameterizedTest
    @MethodSource("provideLogicalOperator")
    public void shouldBeNotAvailableWhenLeftStructuredReferenceIsNull(final String givenLogicalFunction) {
        // Given
        final String givenFormula = String.format("%s([@[North Sales Amount]],[@[South Sales Amount]])", givenLogicalFunction);
        final List<StructuredReference> givenStructuredReferences = List.of(
                new StructuredReference(new Reference("North Sales Amount"), (String) null),
                new StructuredReference(new Reference("South Sales Amount"), "260")
        );

        // When
        final Uni<EvaluationResult> executionResult = antlrExecutor.process(formula4Test(givenFormula),
                new PartEvaluationCallback(new NoOpPartEvaluationListener(), new NumericalContext(), givenStructuredReferences));

        // Then
        assertOnExecutionResultReceived(executionResult, executionResultToAssert ->
                assertThat(executionResultToAssert.value())
                        .isEqualTo(new Value("#NA!"))
        );
    }

    @ParameterizedTest
    @MethodSource("provideLogicalOperator")
    public void shouldBeNotAvailableWhenRightStructuredReferenceIsNull(final String givenLogicalFunction) {
        // Given
        final String givenFormula = String.format("%s([@[North Sales Amount]],[@[South Sales Amount]])", givenLogicalFunction);
        final List<StructuredReference> givenStructuredReferences = List.of(
                new StructuredReference(new Reference("North Sales Amount"), "660"),
                new StructuredReference(new Reference("South Sales Amount"), (String) null)
        );

        // When
        final Uni<EvaluationResult> executionResult = antlrExecutor.process(formula4Test(givenFormula),
                new PartEvaluationCallback(new NoOpPartEvaluationListener(), new NumericalContext(), givenStructuredReferences));

        // Then
        assertOnExecutionResultReceived(executionResult, executionResultToAssert ->
                assertThat(executionResultToAssert.value())
                        .isEqualTo(new Value("#NA!"))
        );
    }

    private static Stream<Arguments> provideLogicalOperator() {
        return Stream.of(
                Arguments.of("OR"),
                Arguments.of("AND"));
    }

    @ParameterizedTest
    @MethodSource("provideLogicalFunctionsUsingComparisonsFunction")
    public void shouldUseComparisonsFunctions(final String givenFormula) {
        // Given
        final List<StructuredReference> givenStructuredReferences = List.of(
                new StructuredReference(new Reference("North Sales Amount"), "660")
        );

        // When
        final Uni<EvaluationResult> executionResult = antlrExecutor.process(formula4Test(givenFormula),
                new PartEvaluationCallback(new NoOpPartEvaluationListener(), new NumericalContext(), givenStructuredReferences));

        // Then
        assertOnExecutionResultReceived(executionResult, executionResultToAssert ->
                assertThat(executionResultToAssert.value())
                        .isEqualTo(new Value("true"))
        );
    }

    private static Stream<Arguments> provideLogicalFunctionsUsingComparisonsFunction() {
        return Stream.of(
                Arguments.of("""
                        OR(EQ("true","true"),EQ("true","true"))"""),
                Arguments.of("""
                        AND(EQ("true","true"),EQ("true","true"))"""),
                Arguments.of("""
                        OR(EQ(true,true),EQ(true,true))"""),
                Arguments.of("""
                        AND(EQ(true,true),EQ(true,true))"""));
    }

    @Test
    public void shouldLogExecution() {
        // Given
        final String givenFormula = "AND(0,0)";
        final List<StructuredReference> givenStructuredReferences = List.of();

        // When
        final Uni<EvaluationResult> executionResult = antlrExecutor.process(formula4Test(givenFormula),
                new PartEvaluationCallback(new DebugPartEvaluationListener(evaluatedAtProvider), new NumericalContext(), givenStructuredReferences));

        // Then
        assertOnExecutionResultReceived(executionResult, executionResultToAssert ->
                assertThat(executionResultToAssert.intermediateResults()).containsExactly(
                        new IntermediateResult(
                                Value.of("false"),
                                new PositionedAt(0, 7),
                                List.of(
                                        new Input(new InputName("left"), Value.of("0"), new PositionedAt(4, 4)),
                                        new Input(new InputName("right"), Value.of("0"), new PositionedAt(6, 6))),
                                new EvaluationProcessedIn(
                                        new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:03+01:00[Europe/Paris]")),
                                        new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:08+01:00[Europe/Paris]")))),
                        new IntermediateResult(
                                Value.of("0"),
                                new PositionedAt(4, 4),
                                List.of(),
                                new EvaluationProcessedIn(
                                        new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:04+01:00[Europe/Paris]")),
                                        new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:05+01:00[Europe/Paris]")))),
                        new IntermediateResult(
                                Value.of("0"),
                                new PositionedAt(6, 6),
                                List.of(),
                                new EvaluationProcessedIn(
                                        new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:06+01:00[Europe/Paris]")),
                                        new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:07+01:00[Europe/Paris]"))))
                )
        );
    }
}
