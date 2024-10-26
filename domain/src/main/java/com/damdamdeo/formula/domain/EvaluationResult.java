package com.damdamdeo.formula.domain;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public record EvaluationResult(Value value,
                               ParserEvaluationProcessedIn parserEvaluationProcessedIn,// can be null
                               List<IntermediateResult> intermediateResults,
                               EvaluationProcessedIn evaluationProcessedIn) {
    public EvaluationResult {
        Objects.requireNonNull(value);
        Objects.requireNonNull(intermediateResults);
        Objects.requireNonNull(evaluationProcessedIn);
    }

    public long exactProcessedInNanos() {
        return Stream.of(parserEvaluationProcessedIn, evaluationProcessedIn)
                .filter(Objects::nonNull)
                .map(ProcessedIn::in)
                .map(Duration::toNanos)
                .reduce(0L, Long::sum);
    }
}
