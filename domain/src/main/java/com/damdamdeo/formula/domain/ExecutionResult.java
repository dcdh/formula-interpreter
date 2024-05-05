package com.damdamdeo.formula.domain;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public record ExecutionResult(Result result,
                              ParserExecutionProcessedIn parserExecutionProcessedIn,// can be null
                              List<ElementExecution> elementExecutions,
                              ExecutionProcessedIn executionProcessedIn) {
    public ExecutionResult {
        Objects.requireNonNull(result);
        Objects.requireNonNull(elementExecutions);
        Objects.requireNonNull(executionProcessedIn);
    }

    public String value() {
        return result.value().value();
    }

    public long exactProcessedInNanos() {
        return Stream.of(parserExecutionProcessedIn, executionProcessedIn)
                .filter(Objects::nonNull)
                .map(ProcessedIn::in)
                .map(Duration::toNanos)
                .reduce(0L, Long::sum);
    }
}
