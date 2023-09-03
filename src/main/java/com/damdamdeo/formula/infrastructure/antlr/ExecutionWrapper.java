package com.damdamdeo.formula.infrastructure.antlr;

import com.damdamdeo.formula.domain.ElementExecution;
import com.damdamdeo.formula.domain.Value;

import java.util.List;
import java.util.concurrent.Callable;

public interface ExecutionWrapper {

    Value execute(final Callable<EvalVisitor.ExecutionResult> callable);

    default List<ElementExecution> executions() {
        return List.of();
    }
}
