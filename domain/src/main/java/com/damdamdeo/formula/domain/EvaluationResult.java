package com.damdamdeo.formula.domain;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public record EvaluationResult(Result result,
                               ParserEvaluationProcessedIn parserEvaluationProcessedIn,// can be null
                               List<IntermediateResult> intermediateResults,
                               EvaluationProcessedIn evaluationProcessedIn) {
    public EvaluationResult {
        Objects.requireNonNull(result);
        Objects.requireNonNull(intermediateResults);
        Objects.requireNonNull(evaluationProcessedIn);
    }

    public String value() {
        return result.value().value();
    }

    public long exactProcessedInNanos() {
        return Stream.of(parserEvaluationProcessedIn, evaluationProcessedIn)
                .filter(Objects::nonNull)
                .map(ProcessedIn::in)
                .map(Duration::toNanos)
                .reduce(0L, Long::sum);
    }
}
