package com.damdamdeo.formula.domain;

import java.util.List;
import java.util.concurrent.Callable;

public interface ExecutionWrapper {
    Value execute(final Callable<ContextualResult> callable);

    default List<ElementExecution> executions() {
        return List.of();
    }
}
