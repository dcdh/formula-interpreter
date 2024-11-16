package com.damdamdeo.formula.domain;

import java.time.Duration;
import java.util.Objects;
import java.util.stream.Stream;

public record ProcessingMetrics(FormulaCacheRetrieval formulaCacheRetrieval,
                                EvaluationLoadingProcessedIn evaluationLoadingProcessedIn,
                                EvaluationProcessedIn evaluationProcessedIn) {
    public ProcessingMetrics {
        Objects.requireNonNull(formulaCacheRetrieval);
        Objects.requireNonNull(evaluationLoadingProcessedIn);
        Objects.requireNonNull(evaluationProcessedIn);
    }

    public long exactProcessedInNanos() {
        return Stream.of(evaluationLoadingProcessedIn, evaluationProcessedIn)
                .map(ProcessedIn::in)
                .map(Duration::toNanos)
                .reduce(0L, Long::sum);
    }
}
