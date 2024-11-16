package com.damdamdeo.formula.domain;

import java.util.Objects;

public record EvaluationLoadingProcessedIn(ProcessedAtStart processedAtStart,
                                           ProcessedAtEnd processedAtEnd) implements ProcessedIn {
    public EvaluationLoadingProcessedIn {
        Objects.requireNonNull(processedAtStart);
        Objects.requireNonNull(processedAtEnd);
    }
}
