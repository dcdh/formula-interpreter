package com.damdamdeo.formula.domain;

import java.time.Duration;

public interface ProcessedIn {
    ExecutedAtStart executedAtStart();

    ExecutedAtEnd executedAtEnd();

    public default Duration in() {
        return Duration.between(
                executedAtStart().at(),
                executedAtEnd().at());
    }
}
