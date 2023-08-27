package com.damdamdeo.formula.domain;

import java.util.List;
import java.util.Objects;

public record ExecutionResult(Result result,
                              List<ElementExecution> elementExecutions,
                              ExecutionProcessedIn executionProcessedIn) {
    public ExecutionResult {
        Objects.requireNonNull(result);
        Objects.requireNonNull(elementExecutions);
        Objects.requireNonNull(executionProcessedIn);
    }
}
