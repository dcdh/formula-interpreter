package com.damdamdeo.formula.domain.evaluation.provider;

import com.damdamdeo.formula.domain.*;
import com.damdamdeo.formula.domain.evaluation.ArgumentExpression;
import com.damdamdeo.formula.domain.evaluation.LogicalBooleanExpression;
import com.damdamdeo.formula.domain.evaluation.StructuredReferencesExpression;
import com.damdamdeo.formula.domain.provider.LogicalBooleanFunctionTestProvider;
import org.junit.jupiter.params.provider.Arguments;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class LogicalBooleanExpressionTestProvider {

    private static Map<LogicalBooleanFunction.Function, Supplier<Stream<Arguments>>> ARGUMENTS_BY_LOGICAL_BOOLEAN_FUNC = Map.of(
            LogicalBooleanFunction.Function.AND, LogicalBooleanFunctionTestProvider::provideAnd,
            LogicalBooleanFunction.Function.OR, LogicalBooleanFunctionTestProvider::provideOr
    );

    public static Stream<Arguments> provideOperationForStructuredReferenceLeftAndStructuredReferenceRight() {
        return ARGUMENTS_BY_LOGICAL_BOOLEAN_FUNC.entrySet()
                .stream()
                .flatMap(entry -> {
                    final LogicalBooleanFunction.Function function = entry.getKey();
                    final Formula givenFormula = new Formula(String.format("%s([@[North Sales Amount]],[@[South Sales Amount]])", function.name()));
                    return entry.getValue().get().map(arguments -> {
                        final Value leftValue = (Value) arguments.get()[0];
                        final Value rightValue = (Value) arguments.get()[1];
                        final Value expected = (Value) arguments.get()[2];
                        return Arguments.of(
                                new Given(
                                        givenFormula,
                                        new LogicalBooleanExpression(function,
                                                new StructuredReferencesExpression(new Reference("North Sales Amount"), new PositionedAt(6, 7)),
                                                new StructuredReferencesExpression(new Reference("South Sales Amount"), new PositionedAt(8, 9)),
                                                new PositionedAt(3, 3)),
                                        List.of(
                                                new StructuredReference(new Reference("North Sales Amount"), leftValue),
                                                new StructuredReference(new Reference("South Sales Amount"), rightValue)
                                        )),
                                new ExpectedEvaluation(
                                        new Evaluated(
                                                expected, new PositionedAt(3, 3),
                                                List.of(
                                                        new Input(new InputName("left"), leftValue, new PositionedAt(6, 7)),
                                                        new Input(new InputName("right"), rightValue, new PositionedAt(8, 9))
                                                )
                                        ),
                                        List.of(new IntermediateResult(
                                                        expected,
                                                        new PositionedAt(3, 3),
                                                        List.of(
                                                                new Input(new InputName("left"), leftValue, new PositionedAt(6, 7)),
                                                                new Input(new InputName("right"), rightValue, new PositionedAt(8, 9))
                                                        ),
                                                        new EvaluationProcessedIn(
                                                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:00+01:00[Europe/Paris]")),
                                                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:05+01:00[Europe/Paris]")))
                                                ),
                                                new IntermediateResult(
                                                        leftValue,
                                                        new PositionedAt(6, 7),
                                                        List.of(
                                                                new Input(new InputName("structuredReference"), new Reference("North Sales Amount"), new PositionedAt(9, 5))
                                                        ),
                                                        new EvaluationProcessedIn(
                                                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:01+01:00[Europe/Paris]")),
                                                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:02+01:00[Europe/Paris]")))
                                                ),
                                                new IntermediateResult(
                                                        rightValue,
                                                        new PositionedAt(8, 9),
                                                        List.of(
                                                                new Input(new InputName("structuredReference"), new Reference("South Sales Amount"), new PositionedAt(11, 7))
                                                        ),
                                                        new EvaluationProcessedIn(
                                                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:03+01:00[Europe/Paris]")),
                                                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:04+01:00[Europe/Paris]")))
                                                )
                                        )
                                )
                        );
                    });
                });
    }

    public static Stream<Arguments> provideOperationForStructuredReferenceLeftAndValueRight() {
        return ARGUMENTS_BY_LOGICAL_BOOLEAN_FUNC.entrySet()
                .stream()
                .flatMap(entry -> {
                    final LogicalBooleanFunction.Function function = entry.getKey();
                    return entry.getValue().get().map(arguments -> {
                        final Value leftValue = (Value) arguments.get()[0];
                        final Value rightValue = (Value) arguments.get()[1];
                        final Value expected = (Value) arguments.get()[2];
                        final Formula givenFormula = new Formula(String.format("%s([@[North Sales Amount]],%s)", function.name(), rightValue.value()));
                        return Arguments.of(
                                new Given(
                                        givenFormula,
                                        new LogicalBooleanExpression(function,
                                                new StructuredReferencesExpression(new Reference("North Sales Amount"), new PositionedAt(6, 7)),
                                                new ArgumentExpression(rightValue, new PositionedAt(0, 1)),
                                                new PositionedAt(3, 3)),
                                        List.of(
                                                new StructuredReference(new Reference("North Sales Amount"), leftValue)
                                        )),
                                new ExpectedEvaluation(
                                        new Evaluated(
                                                expected, new PositionedAt(3, 3),
                                                List.of(
                                                        new Input(new InputName("left"), leftValue, new PositionedAt(6, 7)),
                                                        new Input(new InputName("right"), rightValue, new PositionedAt(0, 1))
                                                )
                                        ),
                                        List.of(new IntermediateResult(
                                                        expected,
                                                        new PositionedAt(3, 3),
                                                        List.of(
                                                                new Input(new InputName("left"), leftValue, new PositionedAt(6, 7)),
                                                                new Input(new InputName("right"), rightValue, new PositionedAt(0, 1))
                                                        ),
                                                        new EvaluationProcessedIn(
                                                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:00+01:00[Europe/Paris]")),
                                                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:05+01:00[Europe/Paris]")))
                                                ),
                                                new IntermediateResult(
                                                        leftValue,
                                                        new PositionedAt(6, 7),
                                                        List.of(
                                                                new Input(new InputName("structuredReference"), new Reference("North Sales Amount"), new PositionedAt(9, 5))
                                                        ),
                                                        new EvaluationProcessedIn(
                                                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:01+01:00[Europe/Paris]")),
                                                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:02+01:00[Europe/Paris]")))
                                                ),
                                                new IntermediateResult(
                                                        rightValue,
                                                        new PositionedAt(0, 1),
                                                        List.of(),
                                                        new EvaluationProcessedIn(
                                                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:03+01:00[Europe/Paris]")),
                                                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:04+01:00[Europe/Paris]")))
                                                )
                                        )
                                )
                        );
                    });
                });
    }

    public static Stream<Arguments> provideOperationForValueLeftAndStructuredReferenceRight() {
        return ARGUMENTS_BY_LOGICAL_BOOLEAN_FUNC.entrySet()
                .stream()
                .flatMap(entry -> {
                    final LogicalBooleanFunction.Function function = entry.getKey();
                    return entry.getValue().get().map(arguments -> {
                        final Value leftValue = (Value) arguments.get()[0];
                        final Value rightValue = (Value) arguments.get()[1];
                        final Value expected = (Value) arguments.get()[2];
                        final Formula givenFormula = new Formula(String.format("%s(%s,[@[South Sales Amount]])", function.name(), leftValue.value()));
                        return Arguments.of(
                                new Given(
                                        givenFormula,
                                        new LogicalBooleanExpression(function,
                                                new ArgumentExpression(leftValue, new PositionedAt(0, 1)),
                                                new StructuredReferencesExpression(new Reference("South Sales Amount"), new PositionedAt(6, 7)),
                                                new PositionedAt(3, 3)),
                                        List.of(
                                                new StructuredReference(new Reference("South Sales Amount"), rightValue)
                                        )),
                                new ExpectedEvaluation(
                                        new Evaluated(
                                                expected, new PositionedAt(3, 3),
                                                List.of(
                                                        new Input(new InputName("left"), leftValue, new PositionedAt(0, 1)),
                                                        new Input(new InputName("right"), rightValue, new PositionedAt(6, 7))
                                                )
                                        ),
                                        List.of(new IntermediateResult(
                                                        expected,
                                                        new PositionedAt(3, 3),
                                                        List.of(
                                                                new Input(new InputName("left"), leftValue, new PositionedAt(0, 1)),
                                                                new Input(new InputName("right"), rightValue, new PositionedAt(6, 7))
                                                        ),
                                                        new EvaluationProcessedIn(
                                                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:00+01:00[Europe/Paris]")),
                                                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:05+01:00[Europe/Paris]")))
                                                ),
                                                new IntermediateResult(
                                                        leftValue,
                                                        new PositionedAt(0, 1),
                                                        List.of(),
                                                        new EvaluationProcessedIn(
                                                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:01+01:00[Europe/Paris]")),
                                                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:02+01:00[Europe/Paris]")))
                                                ),
                                                new IntermediateResult(
                                                        rightValue,
                                                        new PositionedAt(6, 7),
                                                        List.of(
                                                                new Input(new InputName("structuredReference"), new Reference("South Sales Amount"), new PositionedAt(9, 5))
                                                        ),
                                                        new EvaluationProcessedIn(
                                                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:03+01:00[Europe/Paris]")),
                                                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:04+01:00[Europe/Paris]")))
                                                )
                                        )
                                )
                        );
                    });
                });
    }

    public static Stream<Arguments> provideOperationForValueLeftAndValueRight() {
        return ARGUMENTS_BY_LOGICAL_BOOLEAN_FUNC.entrySet()
                .stream()
                .flatMap(entry -> {
                    final LogicalBooleanFunction.Function function = entry.getKey();
                    return entry.getValue().get().map(arguments -> {
                        final Value leftValue = (Value) arguments.get()[0];
                        final Value rightValue = (Value) arguments.get()[1];
                        final Value expected = (Value) arguments.get()[2];
                        final Formula givenFormula = new Formula(String.format("%s(%s,%s)", function.name(), leftValue.value(), rightValue.value()));
                        return Arguments.of(
                                new Given(
                                        givenFormula,
                                        new LogicalBooleanExpression(function,
                                                new ArgumentExpression(leftValue, new PositionedAt(0, 1)),
                                                new ArgumentExpression(rightValue, new PositionedAt(6, 7)),
                                                new PositionedAt(3, 3)),
                                        List.of()),
                                new ExpectedEvaluation(
                                        new Evaluated(
                                                expected, new PositionedAt(3, 3),
                                                List.of(
                                                        new Input(new InputName("left"), leftValue, new PositionedAt(0, 1)),
                                                        new Input(new InputName("right"), rightValue, new PositionedAt(6, 7))
                                                )
                                        ),
                                        List.of(new IntermediateResult(
                                                        expected,
                                                        new PositionedAt(3, 3),
                                                        List.of(
                                                                new Input(new InputName("left"), leftValue, new PositionedAt(0, 1)),
                                                                new Input(new InputName("right"), rightValue, new PositionedAt(6, 7))
                                                        ),
                                                        new EvaluationProcessedIn(
                                                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:00+01:00[Europe/Paris]")),
                                                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:05+01:00[Europe/Paris]")))
                                                ),
                                                new IntermediateResult(
                                                        leftValue,
                                                        new PositionedAt(0, 1),
                                                        List.of(),
                                                        new EvaluationProcessedIn(
                                                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:01+01:00[Europe/Paris]")),
                                                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:02+01:00[Europe/Paris]")))
                                                ),
                                                new IntermediateResult(
                                                        rightValue,
                                                        new PositionedAt(6, 7),
                                                        List.of(),
                                                        new EvaluationProcessedIn(
                                                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:03+01:00[Europe/Paris]")),
                                                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:04+01:00[Europe/Paris]")))
                                                )
                                        )
                                )
                        );
                    });
                });
    }

}
