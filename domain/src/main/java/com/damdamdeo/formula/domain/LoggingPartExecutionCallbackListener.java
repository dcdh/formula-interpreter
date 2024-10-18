package com.damdamdeo.formula.domain;

import com.damdamdeo.formula.domain.spi.ExecutedAtProvider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class LoggingPartExecutionCallbackListener implements PartExecutionCallbackListener {
    private final ExecutedAtProvider executedAtProvider;
    private final Map<ExecutionId, IntermediateResult> intermediateResultsByExecutionId;
    private final Map<ExecutionId, ExecutedAtStart> executedAtStartsByExecutionId;

    public LoggingPartExecutionCallbackListener(final ExecutedAtProvider executedAtProvider) {
        this.executedAtProvider = Objects.requireNonNull(executedAtProvider);
        this.intermediateResultsByExecutionId = new HashMap<>();
        this.executedAtStartsByExecutionId = new HashMap<>();
    }

    @Override
    public void onBeforeExecution(final ExecutionId executionId) {
        executedAtStartsByExecutionId.put(executionId, executedAtProvider.now());
    }

    @Override
    public void onAfterExecution(final ExecutionId executionId, final Result result) {
        intermediateResultsByExecutionId.put(executionId,
                IntermediateResult.Builder.newBuilder()
                        .withExecutedAtStart(executedAtStartsByExecutionId.get(executionId))
                        .withExecutedAtEnd(executedAtProvider.now())
                        .withPosition(result.range())
                        .withInputs(result.inputs())
                        .withValue(result.value())
                        .build());
    }

    @Override
    public List<IntermediateResult> intermediateResults() {
        return intermediateResultsByExecutionId
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .map(Map.Entry::getValue)
                .toList();
    }

}
