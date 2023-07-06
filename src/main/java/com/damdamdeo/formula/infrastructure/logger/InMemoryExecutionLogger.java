package com.damdamdeo.formula.infrastructure.logger;

import com.damdamdeo.formula.domain.Execution;
import com.damdamdeo.formula.domain.ExecutionId;
import com.damdamdeo.formula.domain.ExecutionLogger;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class InMemoryExecutionLogger implements ExecutionLogger {

    private final List<Execution> executions = new ArrayList<>();

    @Override
    public void log(final Execution execution) {
        this.executions.add(execution);
    }

    @Override
    public List<Execution> getByExecutionId(ExecutionId executionId) {
        return executions.stream()
                .filter(execution -> execution.executionId().equals(executionId))
                .collect(Collectors.toList());
    }

}
