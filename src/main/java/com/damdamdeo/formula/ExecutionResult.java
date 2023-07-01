package com.damdamdeo.formula;

import java.util.List;

public record ExecutionResult(Result result, List<Execution> executions) {
    @Override
    public String toString() {
        return "ExecutionResult{" +
                "result=" + result +
                ", executions=" + executions +
                '}';
    }
}
