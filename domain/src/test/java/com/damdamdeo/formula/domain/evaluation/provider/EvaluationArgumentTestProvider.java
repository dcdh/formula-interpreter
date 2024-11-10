package com.damdamdeo.formula.domain.evaluation.provider;

import com.damdamdeo.formula.domain.*;
import com.damdamdeo.formula.domain.evaluation.ArgumentExpression;
import org.junit.jupiter.params.provider.Arguments;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Stream;

public class EvaluationArgumentTestProvider {

    public static Stream<Arguments> provideDefaultZero() {
        return Stream.of(
                Arguments.of(
                        new StructuredReferences(List.of()),
                        new Formula("0.00"),
                        new ArgumentExpression(new Value("0.00"), new PositionedAt(0, 3)),
                        new Evaluated(
                                new Value("0.00"), new PositionedAt(0, 3)
                        ),
                        List.of(new IntermediateResult(
                                        Value.of("0.00"),
                                        new PositionedAt(0, 3),
                                        List.of(),
                                        new EvaluationProcessedIn(
                                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:03+01:00[Europe/Paris]")),
                                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:04+01:00[Europe/Paris]")))
                                )
                        ),
                        new EvaluationProcessedIn(
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:00+01:00[Europe/Paris]")),
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:09+01:00[Europe/Paris]"))
                        )
                )
        );
    }
}
