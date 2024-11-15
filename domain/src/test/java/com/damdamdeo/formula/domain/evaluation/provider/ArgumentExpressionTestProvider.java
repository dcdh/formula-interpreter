package com.damdamdeo.formula.domain.evaluation.provider;

import com.damdamdeo.formula.domain.*;
import com.damdamdeo.formula.domain.evaluation.ArgumentExpression;
import org.junit.jupiter.params.provider.Arguments;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Stream;

public class ArgumentExpressionTestProvider {

    public static Stream<Arguments> provide() {
        return Stream.of(
                Arguments.of(
                        new Given(
                                new Formula("0.00"),
                                new ArgumentExpression(new Value("0.00"), new PositionedAt(0, 3)),
                                List.of()),
                        new ExpectedEvaluation(
                                new Evaluated(
                                        new Value("0.00"), new PositionedAt(0, 3)
                                ),
                                List.of(new IntermediateResult(
                                                Value.of("0.00"),
                                                new PositionedAt(0, 3),
                                                List.of(),
                                                new EvaluationProcessedIn(
                                                        new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:00+01:00[Europe/Paris]")),
                                                        new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:01+01:00[Europe/Paris]")))
                                        )
                                )
                        )
                )
        );
    }
}
