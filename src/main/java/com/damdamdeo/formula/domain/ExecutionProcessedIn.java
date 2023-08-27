package com.damdamdeo.formula.domain;

import java.time.Duration;
import java.util.Objects;

public record ExecutionProcessedIn(ExecutedAtStart executedAtStart,
                                   ExecutedAtEnd executedAtEnd) {
    public ExecutionProcessedIn {
        Objects.requireNonNull(executedAtStart);
        Objects.requireNonNull(executedAtEnd);
    }

    public Duration in() {
        return Duration.between(
                executedAtStart.at(),
                executedAtEnd.at());
    }

}
