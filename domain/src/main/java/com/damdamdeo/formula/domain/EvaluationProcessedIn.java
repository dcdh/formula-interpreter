package com.damdamdeo.formula.domain;

import java.util.Objects;

public record EvaluationProcessedIn(ProcessedAtStart processedAtStart,
                                    ProcessedAtEnd processedAtEnd) implements ProcessedIn {
    public EvaluationProcessedIn {
        Objects.requireNonNull(processedAtStart);
        Objects.requireNonNull(processedAtEnd);
    }
}
