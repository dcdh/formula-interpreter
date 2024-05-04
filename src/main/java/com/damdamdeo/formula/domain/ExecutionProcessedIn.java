package com.damdamdeo.formula.domain;

import java.util.Objects;

public record ExecutionProcessedIn(ExecutedAtStart executedAtStart,
                                   ExecutedAtEnd executedAtEnd) implements ProcessedIn {
    public ExecutionProcessedIn {
        Objects.requireNonNull(executedAtStart);
        Objects.requireNonNull(executedAtEnd);
    }
}
