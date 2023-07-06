package com.damdamdeo.formula.domain;

import java.util.List;
import java.util.Objects;

public record ExecutionResult(Result result, List<Execution> executions) {
    public ExecutionResult {
        Objects.requireNonNull(result);
        Objects.requireNonNull(executions);
    }
}
