package com.damdamdeo.formula.domain.evaluation.provider;

import com.damdamdeo.formula.domain.*;
import com.damdamdeo.formula.domain.evaluation.ArgumentExpression;
import com.damdamdeo.formula.domain.evaluation.ArithmeticExpression;
import com.damdamdeo.formula.domain.evaluation.Expression;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Deprecated
public record Evaluation(StructuredReferences structuredReferences,
                         Formula formula,
                         Expression expression,
                         Evaluated evaluated,
                         IntermediateResults intermediateResults,
                         EvaluationProcessedIn evaluationProcessedIn) {
    public enum Kind {
        OPERATION_ADD,
//        comment faire j'en ai plein de ADD ... si j'ai une valuer, un reference à gauche, droite...
//
        COMPOUND_ADD_MUL,
        ARGUMENT,
        LOGICAL_AND,
        LOGICAL_OR,
        COMPARISON_EQ,
        COMPARISON_NEQ,
        COMPARISON_GTE,
        COMPARISON_LTE,
        COMPARISON_GT,
        COMPARISON_LT,
        COMPARISON_IF_LEFT_WIN,
        COMPARISON_IF_RIGHT_WIN,
        COMPARISON_IF_ERROR_LEFT_WIN,
        COMPARISON_IF_ERROR_RIGHT_WIN,
        COMPARISON_IF_NA_LEFT_WIN,
        COMPARISON_IF_NA_RIGHT_WIN,
        STATE_IS_NA,
        STATE_IS_ERROR,
        STATE_IS_NUMERIC,
        STATE_IS_TEXT,
        STATE_IS_BLANK,
        STATE_IS_LOGICAL,
        BIG_ONE_IS_JOE,
        BIG_ONE_IS_NOT_JOE;

        public static boolean matchTag(final String tag) {
            for (final Kind kind : Kind.values()) {
                if (kind.name().equals(tag)) {
                    return true;
                }
            }
            return false;
        }
    }

    public static final Map<Kind, Evaluation> EVALUATIONS = new HashMap<>();

    public Evaluation {
        Objects.requireNonNull(structuredReferences);
        Objects.requireNonNull(formula);
        Objects.requireNonNull(expression);
        Objects.requireNonNull(evaluated);
        Objects.requireNonNull(intermediateResults);
        Objects.requireNonNull(evaluationProcessedIn);
    }

    static {
        EVALUATIONS.put(Kind.COMPOUND_ADD_MUL,
                new Evaluation(
                        // TODO renommer !!!! avec Given et Expected
                        new StructuredReferences(List.of()),
                        new Formula("ADD(10,MUL(20,30))"),
                        new ArithmeticExpression(ArithmeticFunction.Function.ADD,
                                new ArgumentExpression(new Value("10"), new PositionedAt(4, 5)),
                                new ArithmeticExpression(ArithmeticFunction.Function.MUL,
                                        new ArgumentExpression(new Value("20"), new PositionedAt(11, 12)),
                                        new ArgumentExpression(new Value("30"), new PositionedAt(14, 15)),
                                        new PositionedAt(7, 16)),
                                new PositionedAt(0, 17)),
                        new Evaluated(
                                new Value("610"),
                                new PositionedAt(new PositionStart(0),
                                        new PositionEnd(17))
                        ),
                        new IntermediateResults(
                                List.of(
                                        new IntermediateResult(new Value("610"), new PositionedAt(0, 17), List.of(
                                                new Input(new InputName("left"), new Value("10"), new PositionedAt(4, 5)),
                                                new Input(new InputName("right"), new Value("600"), new PositionedAt(7, 16))
                                        ), new EvaluationProcessedIn(
                                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:00+01:00[Europe/Paris]")),
                                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:09+01:00[Europe/Paris]")))),
                                        new IntermediateResult(new Value("10"), new PositionedAt(4, 5), List.of(), new EvaluationProcessedIn(
                                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:01+01:00[Europe/Paris]")),
                                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:02+01:00[Europe/Paris]")))),
                                        new IntermediateResult(new Value("600"), new PositionedAt(7, 16), List.of(
                                                new Input(new InputName("left"), new Value("20"), new PositionedAt(11, 12)),
                                                new Input(new InputName("right"), new Value("30"), new PositionedAt(14, 15))
                                        ), new EvaluationProcessedIn(
                                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:03+01:00[Europe/Paris]")),
                                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:08+01:00[Europe/Paris]")))),
                                        new IntermediateResult(new Value("20"), new PositionedAt(11, 12), List.of(), new EvaluationProcessedIn(
                                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:04+01:00[Europe/Paris]")),
                                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:05+01:00[Europe/Paris]")))),
                                        new IntermediateResult(new Value("30"), new PositionedAt(14, 15), List.of(), new EvaluationProcessedIn(
                                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:06+01:00[Europe/Paris]")),
                                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:07+01:00[Europe/Paris]"))))
                                )
                        ),
                        new EvaluationProcessedIn(
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:00+01:00[Europe/Paris]")),
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:09+01:00[Europe/Paris]"))
                        )
                ));
        EVALUATIONS.put(
                Kind.ARGUMENT,
                new Evaluation(
                        new StructuredReferences(List.of()),
                        new Formula("0.00"),
                        new ArgumentExpression(new Value("0.00"), new PositionedAt(0, 3)),
                        new Evaluated(
                                new Value("0.00"), new PositionedAt(0, 3)
                        ),
                        new IntermediateResults(
                                List.of(new IntermediateResult(
                                                Value.of("0.00"),
                                                new PositionedAt(0, 3),
                                                List.of(),
                                                new EvaluationProcessedIn(
                                                        new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:03+01:00[Europe/Paris]")),
                                                        new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:04+01:00[Europe/Paris]")))
                                        )
                                )
                        ),
                        new EvaluationProcessedIn(
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:00+01:00[Europe/Paris]")),
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:09+01:00[Europe/Paris]"))
                        )
                ));

    }
}
