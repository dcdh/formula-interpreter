package com.damdamdeo.formula.domain;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public record EvaluationResult(Value value,
                               List<IntermediateResult> intermediateResults,
                               FormulaCacheRetrieval formulaCacheRetrieval,
                               EvaluationLoadingProcessedIn evaluationLoadingProcessedIn,
                               EvaluationProcessedIn evaluationProcessedIn) {
    public EvaluationResult {
        Objects.requireNonNull(value);
        Objects.requireNonNull(intermediateResults);
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
