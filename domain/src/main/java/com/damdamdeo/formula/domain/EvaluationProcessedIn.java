package com.damdamdeo.formula.domain;

import java.util.Objects;

public record EvaluationProcessedIn(EvaluatedAtStart evaluatedAtStart,
                                    EvaluatedAtEnd evaluatedAtEnd) implements ProcessedIn {
    public EvaluationProcessedIn {
        Objects.requireNonNull(evaluatedAtStart);
        Objects.requireNonNull(evaluatedAtEnd);
    }
}
