package com.damdamdeo.formula.domain;

import java.util.List;
import java.util.concurrent.Callable;

public interface ExecutionWrapper {
    Result execute(final Callable<Result> callable);

    default List<ElementExecution> executions() {
        return List.of();
    }
}
