package com.damdamdeo.formula.domain;

import java.time.Duration;

public interface ProcessedIn {
    EvaluatedAtStart evaluatedAtStart();

    EvaluatedAtEnd evaluatedAtEnd();

    default Duration in() {
        return Duration.between(
                evaluatedAtStart().at(),
                evaluatedAtEnd().at());
    }
}
