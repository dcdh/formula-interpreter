package com.damdamdeo.formula.domain.evaluation.provider;

import com.damdamdeo.formula.domain.*;
import com.damdamdeo.formula.domain.evaluation.ArgumentExpression;
import com.damdamdeo.formula.domain.evaluation.ArithmeticExpression;
import org.junit.jupiter.params.provider.Arguments;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Stream;

public class EvaluationCompoundAddMulTestProvider {

    public static Stream<Arguments> provide() {
        return Stream.of(
                Arguments.of(
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
                                        new PositionEnd(17)),
                                () -> List.of(
                                        new Input(new InputName("left"), new Value("10"), new PositionedAt(4, 5)),
                                        new Input(new InputName("right"), new Value("600"), new PositionedAt(7, 16))
                                )
                        ),
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
                        ),
                        new EvaluationProcessedIn(
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:00+01:00[Europe/Paris]")),
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:09+01:00[Europe/Paris]"))
                        ))
        );
    }

// TODO FCK faire le IF joe ... cela serait cool et du coup j'ai deux reponse differentes en fonction des inputs en entrées !
pas ici je pense pour le définir ...
}
