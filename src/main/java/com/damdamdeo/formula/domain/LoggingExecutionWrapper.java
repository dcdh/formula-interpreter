package com.damdamdeo.formula.domain;

import com.damdamdeo.formula.domain.spi.ExecutedAtProvider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public final class LoggingExecutionWrapper implements ExecutionWrapper {
    private final ExecutedAtProvider executedAtProvider;
    private final Map<ExecutionId, ElementExecution> executions;
    private final AtomicInteger currentExecutionId;

    public LoggingExecutionWrapper(final ExecutedAtProvider executedAtProvider) {
        this.executedAtProvider = Objects.requireNonNull(executedAtProvider);
        this.executions = new HashMap<>();
        this.currentExecutionId = new AtomicInteger(-1);
    }

    @Override
    public Result execute(final Callable<Result> callable) {
        Objects.requireNonNull(callable);
        try {
            final ExecutionId executionId = new ExecutionId(currentExecutionId);
            final ExecutedAtStart executedAtStart = executedAtProvider.now();
            final Result result = callable.call();
            final ExecutedAtEnd executedAtEnd = executedAtProvider.now();
            executions.put(executionId,
                    ElementExecution.Builder.newBuilder()
                            .withExecutedAtStart(executedAtStart)
                            .withExecutedAtEnd(executedAtEnd)
                            .withPosition(result.range())
                            .withInputs(result.inputs())
                            .withValue(result.value())
                            .build());
            return result;
        } catch (final Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public List<ElementExecution> executions() {
        return executions
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }

}
