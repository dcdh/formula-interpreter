package com.damdamdeo.formula.domain;

import java.util.Objects;

public record ParserEvaluationProcessedIn(EvaluatedAtStart evaluatedAtStart,
                                          EvaluatedAtEnd evaluatedAtEnd) implements ProcessedIn {
    public ParserEvaluationProcessedIn {
        Objects.requireNonNull(evaluatedAtStart);
        Objects.requireNonNull(evaluatedAtEnd);
    }
}
