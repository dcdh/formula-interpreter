package com.damdamdeo.formula.infrastructure.evaluation.provider;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.stream.Stream;

public class DebugFormulaArgumentsProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(final ExtensionContext context) throws Exception {
        throw new IllegalStateException("TODO");
    }

    // TODO prendre fonction par fonction avec aussi expression
    // donc
    // 1. expression
    // 2. function je devrais fixer moi meme les données en entrées et reprendre uniquement le GivenFormula
    // 3. compound
    // Garder les expressions !!!
//    public static Stream<Arguments> provide() {
//        return Stream.of(
//                Arguments.of(
//                        new Given(TODO FCK reprendre ce teste côté mapping antlr expression car celui ci est bien expressif
//                                new Formula("ADD(10,MUL(20,30))"),
//                                new ArithmeticExpression(ArithmeticFunction.Function.ADD,
//                                        new ArgumentExpression(new Value("10"), new PositionedAt(4, 5)),
//                                        new ArithmeticExpression(ArithmeticFunction.Function.MUL,
//                                                new ArgumentExpression(new Value("20"), new PositionedAt(11, 12)),
//                                                new ArgumentExpression(new Value("30"), new PositionedAt(14, 15)),
//                                                new PositionedAt(7, 16)),
//                                        new PositionedAt(0, 17)),
//                                List.of()
//                        ),
//                        new ExpectedEvaluated(
//                                new Evaluated(
//                                        new Value("610"),
//                                        new PositionedAt(new PositionStart(0),
//                                                new PositionEnd(17)),
//                                        List.of(
//                                                new Input(new InputName("left"), new Value("10"), new PositionedAt(4, 5)),
//                                                new Input(new InputName("right"), new Value("600"), new PositionedAt(7, 16))
//                                        )
//                                ),
//                                List.of(
//                                        new IntermediateResult(new Value("610"), new PositionedAt(0, 17), List.of(
//                                                new Input(new InputName("left"), new Value("10"), new PositionedAt(4, 5)),
//                                                new Input(new InputName("right"), new Value("600"), new PositionedAt(7, 16))
//                                        ), new EvaluationProcessedIn(
//                                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:00+01:00[Europe/Paris]")),
//                                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:09+01:00[Europe/Paris]")))),
//                                        new IntermediateResult(new Value("10"), new PositionedAt(4, 5), List.of(), new EvaluationProcessedIn(
//                                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:01+01:00[Europe/Paris]")),
//                                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:02+01:00[Europe/Paris]")))),
//                                        new IntermediateResult(new Value("600"), new PositionedAt(7, 16), List.of(
//                                                new Input(new InputName("left"), new Value("20"), new PositionedAt(11, 12)),
//                                                new Input(new InputName("right"), new Value("30"), new PositionedAt(14, 15))
//                                        ), new EvaluationProcessedIn(
//                                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:03+01:00[Europe/Paris]")),
//                                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:08+01:00[Europe/Paris]")))),
//                                        new IntermediateResult(new Value("20"), new PositionedAt(11, 12), List.of(), new EvaluationProcessedIn(
//                                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:04+01:00[Europe/Paris]")),
//                                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:05+01:00[Europe/Paris]")))),
//                                        new IntermediateResult(new Value("30"), new PositionedAt(14, 15), List.of(), new EvaluationProcessedIn(
//                                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:06+01:00[Europe/Paris]")),
//                                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:07+01:00[Europe/Paris]"))))
//                                )
//                        )
//                ),
//                Arguments.of(
//                        joe(List.of(
//                                new StructuredReference(new Reference("Sales Person"), new Value("Joe")),
//                                new StructuredReference(new Reference("Sales Amount"), new Value("260")),
//                                new StructuredReference(new Reference("% Commission"), new Value("10"))
//                        )),
//                        new ExpectedEvaluated(
//                                new Evaluated(
//                                        new Value("52"),
//                                        new PositionedAt(new PositionStart(15), new PositionEnd(15)),
//                                        List.of(
//                                                new Input(new InputName("comparisonValue"), new Value("true"), new PositionedAt(2, 2))
//                                        )
//                                ),
//                                List.of(
//                                        new IntermediateResult(
//                                                new Value("52"),
//                                                new PositionedAt(15, 15),
//                                                List.of(
//                                                        new Input(new InputName("comparisonValue"), new Value("true"), new PositionedAt(2, 2))
//                                                ),
//                                                new EvaluationProcessedIn(
//                                                        new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:00+01:00[Europe/Paris]")),
//                                                        new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:21+01:00[Europe/Paris]")))
//                                        ),
//                                        new IntermediateResult(
//                                                new Value("true"),
//                                                new PositionedAt(2, 2),
//                                                List.of(
//                                                        new Input(new InputName("left"), new Value("Joe"), new PositionedAt(0, 0)),
//                                                        new Input(new InputName("right"), new Value("Joe"), new PositionedAt(1, 1))
//                                                ),
//                                                new EvaluationProcessedIn(
//                                                        new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:01+01:00[Europe/Paris]")),
//                                                        new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:06+01:00[Europe/Paris]")))
//                                        ),
//                                        new IntermediateResult(
//                                                new Value("Joe"),
//                                                new PositionedAt(),
//                                                List.of(
//                                                        new Input(new InputName("structuredReference"), new Reference("Sales Person"), new PositionedAt(3, -2))
//                                                ),
//                                                new EvaluationProcessedIn(
//                                                        new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:02+01:00[Europe/Paris]")),
//                                                        new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:03+01:00[Europe/Paris]")))
//                                        ),
//                                        new IntermediateResult(
//                                                new Value("Joe"),
//                                                new PositionedAt(1, 1),
//                                                List.of(),
//                                                new EvaluationProcessedIn(
//                                                        new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:04+01:00[Europe/Paris]")),
//                                                        new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:05+01:00[Europe/Paris]")))
//                                        ),
//                                        new IntermediateResult(
//                                                new Value("52"),
//                                                new PositionedAt(9, 9),
//                                                List.of(
//                                                        new Input(new InputName("left"), new Value("26"), new PositionedAt(7, 7)),
//                                                        new Input(new InputName("right"), new Value("2"), new PositionedAt(8, 8))
//                                                ),
//                                                new EvaluationProcessedIn(
//                                                        new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:07+01:00[Europe/Paris]")),
//                                                        new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:20+01:00[Europe/Paris]")))
//                                        ),
//                                        new IntermediateResult(
//                                                new Value("26"),
//                                                new PositionedAt(7, 7),
//                                                List.of(
//                                                        new Input(new InputName("left"), new Value("260"), new PositionedAt(3, 3)),
//                                                        new Input(new InputName("right"), new Value("0.1"), new PositionedAt(6, 6))
//                                                ),
//                                                new EvaluationProcessedIn(
//                                                        new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:08+01:00[Europe/Paris]")),
//                                                        new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:17+01:00[Europe/Paris]")))
//                                        ),
//                                        new IntermediateResult(
//                                                new Value("260"),
//                                                new PositionedAt(3, 3),
//                                                List.of(
//                                                        new Input(new InputName("structuredReference"), new Reference("Sales Amount"), new PositionedAt(6, 1))
//                                                ),
//                                                new EvaluationProcessedIn(
//                                                        new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:09+01:00[Europe/Paris]")),
//                                                        new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:10+01:00[Europe/Paris]")))
//                                        ),
//                                        new IntermediateResult(
//                                                new Value("0.1"),
//                                                new PositionedAt(6, 6),
//                                                List.of(
//                                                        new Input(new InputName("left"), new Value("10"), new PositionedAt(4, 4)),
//                                                        new Input(new InputName("right"), new Value("100"), new PositionedAt(5, 5))
//                                                ),
//                                                new EvaluationProcessedIn(
//                                                        new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:11+01:00[Europe/Paris]")),
//                                                        new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:16+01:00[Europe/Paris]")))
//                                        ),
//                                        new IntermediateResult(
//                                                new Value("10"),
//                                                new PositionedAt(4, 4),
//                                                List.of(
//                                                        new Input(new InputName("structuredReference"), new Reference("% Commission"), new PositionedAt(7, 2))
//                                                ),
//                                                new EvaluationProcessedIn(
//                                                        new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:12+01:00[Europe/Paris]")),
//                                                        new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:13+01:00[Europe/Paris]")))
//                                        ),
//                                        new IntermediateResult(
//                                                new Value("100"),
//                                                new PositionedAt(5, 5),
//                                                List.of(),
//                                                new EvaluationProcessedIn(
//                                                        new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:14+01:00[Europe/Paris]")),
//                                                        new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:15+01:00[Europe/Paris]")))
//                                        ),
//                                        new IntermediateResult(
//                                                new Value("2"),
//                                                new PositionedAt(8, 8),
//                                                List.of(),
//                                                new EvaluationProcessedIn(
//                                                        new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:18+01:00[Europe/Paris]")),
//                                                        new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:19+01:00[Europe/Paris]")))
//                                        )
//                                )
//                        )
//                ),
//                Arguments.of(
//                        joe(List.of(
//                                new StructuredReference(new Reference("Sales Person"), new Value("Robert")),
//                                new StructuredReference(new Reference("Sales Amount"), new Value("660")),
//                                new StructuredReference(new Reference("% Commission"), new Value("15"))
//                        )),
//                        new ExpectedEvaluated(
//                                new Evaluated(
//                                        new Value("99"),
//                                        new PositionedAt(new PositionStart(15), new PositionEnd(15)),
//                                        List.of(
//                                                new Input(new InputName("comparisonValue"), new Value("false"), new PositionedAt(2, 2))
//                                        )
//                                ),
//                                List.of(
//                                        new IntermediateResult(
//                                                new Value("99"),
//                                                new PositionedAt(15, 15),
//                                                List.of(
//                                                        new Input(new InputName("comparisonValue"), new Value("false"), new PositionedAt(2, 2))
//                                                ),
//                                                new EvaluationProcessedIn(
//                                                        new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:00+01:00[Europe/Paris]")),
//                                                        new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:17+01:00[Europe/Paris]")))
//                                        ),
//                                        new IntermediateResult(
//                                                new Value("false"),
//                                                new PositionedAt(2, 2),
//                                                List.of(
//                                                        new Input(new InputName("left"), new Value("Robert"), new PositionedAt(0, 0)),
//                                                        new Input(new InputName("right"), new Value("Joe"), new PositionedAt(1, 1))
//                                                ),
//                                                new EvaluationProcessedIn(
//                                                        new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:01+01:00[Europe/Paris]")),
//                                                        new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:06+01:00[Europe/Paris]")))
//                                        ),
//                                        new IntermediateResult(
//                                                new Value("Robert"),
//                                                new PositionedAt(0, 0),
//                                                List.of(
//                                                        new Input(new InputName("structuredReference"), new Reference("Sales Person"), new PositionedAt(3, -2))
//                                                ),
//                                                new EvaluationProcessedIn(
//                                                        new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:02+01:00[Europe/Paris]")),
//                                                        new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:03+01:00[Europe/Paris]")))
//                                        ),
//                                        new IntermediateResult(
//                                                new Value("Joe"),
//                                                new PositionedAt(1, 1),
//                                                List.of(),
//                                                new EvaluationProcessedIn(
//                                                        new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:04+01:00[Europe/Paris]")),
//                                                        new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:05+01:00[Europe/Paris]")))
//                                        ),
//                                        new IntermediateResult(
//                                                new Value("99"),
//                                                new PositionedAt(14, 14),
//                                                List.of(
//                                                        new Input(new InputName("left"), new Value("660"), new PositionedAt(10, 10)),
//                                                        new Input(new InputName("right"), new Value("0.15"), new PositionedAt(13, 13))
//                                                ),
//                                                new EvaluationProcessedIn(
//                                                        new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:07+01:00[Europe/Paris]")),
//                                                        new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:16+01:00[Europe/Paris]")))
//                                        ),
//                                        new IntermediateResult(
//                                                new Value("660"),
//                                                new PositionedAt(10, 10),
//                                                List.of(
//                                                        new Input(new InputName("structuredReference"), new Reference("Sales Amount"), new PositionedAt(13, 8))
//                                                ),
//                                                new EvaluationProcessedIn(
//                                                        new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:08+01:00[Europe/Paris]")),
//                                                        new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:09+01:00[Europe/Paris]")))
//                                        ),
//                                        new IntermediateResult(
//                                                new Value("0.15"),
//                                                new PositionedAt(13, 13),
//                                                List.of(
//                                                        new Input(new InputName("left"), new Value("15"), new PositionedAt(11, 11)),
//                                                        new Input(new InputName("right"), new Value("100"), new PositionedAt(12, 12))
//                                                ),
//                                                new EvaluationProcessedIn(
//                                                        new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:10+01:00[Europe/Paris]")),
//                                                        new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:15+01:00[Europe/Paris]")))
//                                        ),
//                                        new IntermediateResult(
//                                                new Value("15"),
//                                                new PositionedAt(11, 11),
//                                                List.of(
//                                                        new Input(new InputName("structuredReference"), new Reference("% Commission"), new PositionedAt(14, 9))
//                                                ),
//                                                new EvaluationProcessedIn(
//                                                        new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:11+01:00[Europe/Paris]")),
//                                                        new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:12+01:00[Europe/Paris]")))
//                                        ),
//                                        new IntermediateResult(
//                                                new Value("100"),
//                                                new PositionedAt(12, 12),
//                                                List.of(),
//                                                new EvaluationProcessedIn(
//                                                        new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:13+01:00[Europe/Paris]")),
//                                                        new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:14+01:00[Europe/Paris]")))
//                                        )
//
//                                )
//                        )
//                )
//        );
//    }
//
//    private static Given joe(final List<StructuredReference> structuredReferences) {
//        return new Given(
//                new Formula("IF(EQ([@[Sales Person]],\"Joe\"),MUL(MUL([@[Sales Amount]],DIV([@[% Commission]],100)),2),MUL([@[Sales Amount]],DIV([@[% Commission]],100)))"),
//                new LogicalComparisonExpression(
//                        LogicalComparisonFunction.Function.IF,
//                        new ComparisonExpression(
//                                ComparisonFunction.Comparison.EQ,
//                                new StructuredReferencesExpression(
//                                        new Reference("Sales Person"),
//                                        new PositionedAt(0, 0)
//                                ),
//                                new ArgumentExpression(new Value("Joe"), new PositionedAt(1, 1)),
//                                new PositionedAt(2, 2)
//                        ),
//                        new ArithmeticExpression(
//                                ArithmeticFunction.Function.MUL,
//                                new ArithmeticExpression(
//                                        ArithmeticFunction.Function.MUL,
//                                        new StructuredReferencesExpression(new Reference("Sales Amount"), new PositionedAt(3, 3)),
//                                        new ArithmeticExpression(
//                                                ArithmeticFunction.Function.DIV,
//                                                new StructuredReferencesExpression(new Reference("% Commission"), new PositionedAt(4, 4)),
//                                                new ArgumentExpression(new Value("100"), new PositionedAt(5, 5)),
//                                                new PositionedAt(6, 6)
//                                        ),
//                                        new PositionedAt(7, 7)
//                                ),
//                                new ArgumentExpression(new Value("2"), new PositionedAt(8, 8)),
//                                new PositionedAt(9, 9)
//                        ),
//                        new ArithmeticExpression(
//                                ArithmeticFunction.Function.MUL,
//                                new StructuredReferencesExpression(new Reference("Sales Amount"), new PositionedAt(10, 10)),
//                                new ArithmeticExpression(
//                                        ArithmeticFunction.Function.DIV,
//                                        new StructuredReferencesExpression(new Reference("% Commission"), new PositionedAt(11, 11)),
//                                        new ArgumentExpression(new Value("100"), new PositionedAt(12, 12)),
//                                        new PositionedAt(13, 13)
//                                ),
//                                new PositionedAt(14, 14)
//                        ),
//                        new PositionedAt(15, 15)
//                ),
//                structuredReferences
//        );
//    }
}