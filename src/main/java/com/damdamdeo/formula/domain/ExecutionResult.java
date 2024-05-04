package com.damdamdeo.formula.domain;

import java.time.Duration;
import java.util.List;
import java.util.Objects;

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
        final Long parserExecutionProcessedInInNanos = parserExecutionProcessedIn == null ? 0 : parserExecutionProcessedIn.in().toNanos();
        return elementExecutions.stream()
                .map(ElementExecution::executionProcessedIn)
                .map(ExecutionProcessedIn::in)
                .map(Duration::toNanos)
                .reduce(parserExecutionProcessedInInNanos, Long::sum);
    }
}
