package com.damdamdeo.formula.infrastructure.antlr;

import com.damdamdeo.formula.domain.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public final class LoggingExecutionWrapper implements ExecutionWrapper {
    private final ExecutedAtProvider executedAtProvider;
    private final Map<EvalVisitor.ExecutionId, ElementExecution> executions;
    private final AtomicInteger currentExecutionId;

    public LoggingExecutionWrapper(final ExecutedAtProvider executedAtProvider) {
        this.executedAtProvider = Objects.requireNonNull(executedAtProvider);
        this.executions = new HashMap<>();
        this.currentExecutionId = new AtomicInteger(-1);
    }

    @Override
    public Value execute(final Callable<EvalVisitor.ExecutionResult> callable) {
        Objects.requireNonNull(callable);
        try {
            final EvalVisitor.ExecutionId executionId = new EvalVisitor.ExecutionId(currentExecutionId);
            final ExecutedAtStart executedAtStart = executedAtProvider.now();
            final EvalVisitor.ExecutionResult result = callable.call();
            final ExecutedAtEnd executedAtEnd = executedAtProvider.now();
            executions.put(executionId, AntlrElementExecution.Builder.newBuilder()
                    .executedAtStart(executedAtStart)
                    .executedAtEnd(executedAtEnd)
                    .withPositions(result.psoitions())
                    .withInputs(result.inputs())
                    .result(result.value())
                    .build());
            return result.value();
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
