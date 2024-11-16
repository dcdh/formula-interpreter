package com.damdamdeo.formula.domain;

import java.util.List;
import java.util.Objects;

public record EvaluationResult(Value value,
                               List<IntermediateResult> intermediateResults,
                               ProcessingMetrics processingMetrics) {
    public EvaluationResult {
        Objects.requireNonNull(value);
        Objects.requireNonNull(intermediateResults);
        Objects.requireNonNull(processingMetrics);
    }
}
